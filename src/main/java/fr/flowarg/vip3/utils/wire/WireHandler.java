/**
 * MIT License
 *
 * Copyright (c) 2022 Space Walker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fr.flowarg.vip3.utils.wire;

import fr.flowarg.vip3.utils.CalledAtRuntime;
import fr.flowarg.vip3.utils.extensions.IBlockState;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * This class handles power changes for redstone wire. The algorithm was
 * designed with the following goals in mind:
 * <br>
 * 1. Minimize the number of times a wire checks its surroundings to determine
 * its power level.
 * <br>
 * 2. Minimize the number of block and shape updates emitted.
 * <br>
 * 3. Emit block and shape updates in a deterministic, non-locational order,
 * fixing bug MC-11193.
 * 
 * <p>
 * In Vanilla redstone wire is laggy because it fails on points 1 and 2.
 * 
 * <p>
 * Redstone wire updates recursively and each wire calculates its power level in
 * isolation rather than in the context of the network it is a part of. This
 * means a wire in a grid can change its power level over half a dozen times
 * before settling on its final value. This problem used to be worse in 1.13 and
 * below, where a wire would only decrease its power level by 1 at a time.
 * 
 * <p>
 * In addition to this, a wire emits 42 block updates and up to 22 shape updates
 * each time it changes its power level.
 * 
 * <p>
 * Of those 42 block updates, 6 are to itself, which are thus not only
 * redundant, but a big source of lag, since those cause the wire to
 * unnecessarily re-calculate its power level. A block only has 24 neighbors
 * within a Manhattan distance of 2, meaning 12 of the remaining 36 block
 * updates are duplicates and thus also redundant.
 * 
 * <p>
 * Of the 22 shape updates, only 6 are strictly necessary. The other 16 are sent
 * to blocks diagonally above and below. These are necessary if a wire changes
 * its connections, but not when it changes its power level.
 * 
 * <p>
 * Redstone wire in Vanilla also fails on point 3, though this is more of a
 * quality-of-life issue than a lag issue. The recursive nature in which it
 * updates, combined with the location-dependent order in which each wire
 * updates its neighbors, makes the order in which neighbors of a wire network
 * are updated incredibly inconsistent and seemingly random.
 * 
 * <p>
 * Alternate Current fixes each of these problems as follows.
 * 
 * <p>
 * 1. To make sure a wire calculates its power level as little as possible, we
 * remove the recursive nature in which redstone wire updates in Vanilla.
 * Instead, we build a network of connected wires, find those wires that receive
 * redstone power from "outside" the network, and spread the power from there.
 * This has a few advantages:
 * <br>
 * - Each wire checks for power from non-wire components just once, and from
 * nearby wires just twice.
 * <br>
 * - Each wire only sets its power level in the world once. This is important,
 * because calls to Level.setBlock are even more expensive than calls to
 * Level.getBlockState.
 * 
 * <p>
 * 2. There are 2 obvious ways in which we can reduce the number of block and
 * shape updates.
 * <br>
 * - Get rid of the 18 redundant block updates and 16 redundant shape updates,
 * so each wire only emits 24 block updates and 6 shape updates whenever it
 * changes its power level.
 * <br>
 * - Only emit block updates and shape updates once a wire reaches its final
 * power level, rather than at each intermediary stage.
 * <br>
 * For an individual wire, these two optimizations are the best you can do, but
 * for an entire grid, you can do better!
 * 
 * <p>
 * Since we calculate the power of the entire network, sending block and shape
 * updates to the wires in it is redundant. Removing those updates can reduce
 * the number of block and shape updates by up to 20%.
 * 
 * <p>
 * 3. To make the order of block updates to neighbors of a network
 * deterministic, the first thing we must do is to replace the location-
 * dependent order in which a wire updates its neighbors. Instead, we base it on
 * the direction of power flow. This part of the algorithm was heavily inspired
 * by theosib's 'RedstoneWireTurbo', which you can read more about in theosib's
 * comment on Mojira <a href="https://bugs.mojang.com/browse/MC-81098?focusedCommentId=420777&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#comment-420777">here</a>
 * or by checking out its implementation in carpet mod <a href="https://github.com/gnembon/fabric-carpet/blob/master/src/main/java/carpet/helpers/RedstoneWireTurbo.java">here</a>.
 * 
 * <p>
 * The idea is to determine the direction of power flow through a wire based on
 * the power it receives from neighboring wires. For example, if the only power
 * a wire receives is from a neighboring wire to its west, it can be said that
 * the direction of power flow through the wire is east.
 * 
 * <p>
 * We make the order of block updates to neighbors of a wire depend on what is
 * determined to be the direction of power flow. This not only removes
 * locationality entirely, it even removes directionality in a large number of
 * cases. Unlike in 'RedstoneWireTurbo', however, I have decided to keep a
 * directional element in ambiguous cases, rather than to introduce randomness,
 * though this is trivial to change.
 * 
 * <p>
 * While this change fixes the block update order of individual wires, we must
 * still address the overall block update order of a network. This turns out to
 * be a simple fix, because of a change we made earlier: we search through the
 * network for wires that receive power from outside it, and spread the power
 * from there. If we make each wire transmit its power to neighboring wires in
 * an order dependent on the direction of power flow, we end up with a
 * non-locational and largely non-directional wire update order.
 * 
 * @author Space Walker
 */
public class WireHandler {

	public static class Directions {

		public static final Direction[] ALL        = { Direction.WEST, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.DOWN, Direction.UP };
		public static final Direction[] HORIZONTAL = { Direction.WEST, Direction.NORTH, Direction.EAST, Direction.SOUTH };

		// Indices for the arrays above.
		// The cardinal directions are ordered clockwise. This allows
		// for conversion between relative and absolute directions
		// ('left' 'right' vs 'east' 'west') with simple arithmetic:
		// If some Direction index 'iDir' is considered 'forward', then
		// '(iDir + 1) & 0b11' is 'right', '(iDir + 2) & 0b11' is 'backward', etc.
		public static final int WEST  = 0b000; // 0
		public static final int NORTH = 0b001; // 1
		public static final int EAST  = 0b010; // 2
		public static final int SOUTH = 0b011; // 3
		public static final int DOWN  = 0b100; // 4
		public static final int UP    = 0b101; // 5

		public static int iOpposite(int iDir) {
			return iDir ^ (0b10 >>> (iDir >>> 2));
		}

		/**
		 * Each array is placed at the index that encodes the direction that is missing
		 * from the array.
		 */
		private static final int[][] I_EXCEPT = {
			{       NORTH, EAST, SOUTH, DOWN, UP },
			{ WEST,        EAST, SOUTH, DOWN, UP },
			{ WEST, NORTH,       SOUTH, DOWN, UP },
			{ WEST, NORTH, EAST,        DOWN, UP },
			{ WEST, NORTH, EAST, SOUTH,       UP },
			{ WEST, NORTH, EAST, SOUTH, DOWN     }
		};
	}

	/**
	 * This conversion table takes in information about incoming flow, and outputs
	 * the determined outgoing flow.
	 * 
	 * <p>
	 * The input is a 4 bit number that encodes the incoming flow. Each bit
	 * represents a cardinal direction, and when it is 'on', there is flow in that
	 * direction.
	 * 
	 * <p>
	 * The output is a single Direction index, or -1 for ambiguous cases.
	 * 
	 * <p>
	 * The outgoing flow is determined as follows:
	 * 
	 * <p>
	 * If there is just 1 direction of incoming flow, that direction will be the
	 * direction of outgoing flow.
	 * 
	 * <p>
	 * If there are 2 directions of incoming flow, and these directions are not each
	 * other's opposites, the direction that is 'more clockwise' will be the
	 * direction of outgoing flow. More precisely, the direction that is 1 clockwise
	 * turn from the other is picked.
	 * 
	 * <p>
	 * If there are 3 directions of incoming flow, the two opposing directions
	 * cancel each other out, and the remaining direction will be the direction of
	 * outgoing flow.
	 * 
	 * <p>
	 * In all other cases, the flow is completely ambiguous.
	 */
	static final int[] FLOW_IN_TO_FLOW_OUT = {
		-1,               // 0b0000: -                     -> x
		Directions.WEST,  // 0b0001: west                  -> west
		Directions.NORTH, // 0b0010: north                 -> north
		Directions.NORTH, // 0b0011: west/north            -> north
		Directions.EAST,  // 0b0100: east                  -> east
		-1,               // 0b0101: west/east             -> x
		Directions.EAST,  // 0b0110: north/east            -> east
		Directions.NORTH, // 0b0111: west/north/east       -> north
		Directions.SOUTH, // 0b1000: south                 -> south
		Directions.WEST,  // 0b1001: west/south            -> west
		-1,               // 0b1010: north/south           -> x
		Directions.WEST,  // 0b1011: west/north/south      -> west
		Directions.SOUTH, // 0b1100: east/south            -> south
		Directions.SOUTH, // 0b1101: west/east/south       -> south
		Directions.EAST,  // 0b1110: north/east/south      -> east
		-1,               // 0b1111: west/north/east/south -> x
	};
	/**
	 * Update order of cardinal directions. Given that the index encodes the
	 * direction that is to be considered 'forward', the resulting update order is {
	 * front, back, right, left }.
	 */
	static final int[][] CARDINAL_UPDATE_ORDERS = {
		{ Directions.WEST , Directions.EAST , Directions.NORTH, Directions.SOUTH },
		{ Directions.NORTH, Directions.SOUTH, Directions.EAST , Directions.WEST  },
		{ Directions.EAST , Directions.WEST , Directions.SOUTH, Directions.NORTH },
		{ Directions.SOUTH, Directions.NORTH, Directions.WEST , Directions.EAST  }
	};
	/**
	 * The default update order of all cardinal directions.
	 */
	static final int[] DEFAULT_CARDINAL_UPDATE_ORDER = CARDINAL_UPDATE_ORDERS[0];
	/**
	 * The default update order of all directions. It is equivalent to the order of
	 * shape updates in vanilla Minecraft.
	 */
	static final int[] DEFAULT_FULL_UPDATE_ORDER = {
		Directions.WEST,
		Directions.EAST,
		Directions.NORTH,
		Directions.SOUTH,
		Directions.DOWN,
		Directions.UP
	};

	private static final int POWER_MAX = Redstone.SIGNAL_MAX;
	private static final int POWER_MIN = Redstone.SIGNAL_MIN;
	private static final int POWER_STEP = 1;

	// If Vanilla will ever multi-thread the ticking of levels, there should
	// be only one WireHandler per level, in case redstone updates in multiple
	// levels at the same time. There are already mods that add multi-threading
	// as well.
	private final ServerLevel level;

	/** All the wires in the network. */
	private final List<WireNode> network;
	/** Map of wires and neighboring blocks. */
	private final Long2ObjectMap<Node> nodes;
	/** The queue for updating wires and neighboring nodes. */
	private final Queue<Node> updates;

	private int rootCount;
	// Rather than creating new nodes every time a network is updated we keep
	// a cache of nodes that can be re-used.
	private Node[] nodeCache;
	private int nodeCount;

	/** Is this WireHandler currently working through the update queue? */
	private boolean updating;

	public WireHandler(ServerLevel level) {
		this.level = level;

		this.network = new ArrayList<>();
		this.nodes = new Long2ObjectOpenHashMap<>();
		this.updates = new UpdateQueue();

		this.nodeCache = new Node[16];
		this.fillNodeCache(0, 16);
	}

	/**
	 * Retrieve the {@link Node Node} that represents the
	 * block at the given position in the level.
	 */
	private Node getOrAddNode(BlockPos pos) {
		return nodes.compute(pos.asLong(), (key, node) -> {
			if (node == null) {
				// If there is not yet a node at this position, retrieve and
				// update one from the cache.
				return getNextNode(pos);
			}
			if (node.invalid) {
				return revalidateNode(node);
			}

			return node;
		});
	}

	/**
	 * Return a {@link Node Node} that represents the block
	 * at the given position.
	 */
	private Node getNextNode(BlockPos pos) {
		return getNextNode(pos, level.getBlockState(pos));
	}

	/**
	 * Return a node that represents the given position and block state. If it is a
	 * wire, then create a new {@link WireNode WireNode}.
	 * Otherwise, grab the next {@link Node Node} from the
	 * cache and update it.
	 */
	private Node getNextNode(BlockPos pos, BlockState state) {
		return state.is(Blocks.REDSTONE_WIRE) ? new WireNode(level, pos, state) : getNextNode().update(pos, state, true);
	}

	/**
	 * Grab the first unused Node from the cache. If all of the cache is already in
	 * use, increase it in size first.
	 */
	private Node getNextNode() {
		if (nodeCount == nodeCache.length) {
			increaseNodeCache();
		}

		return nodeCache[nodeCount++];
	}

	private void increaseNodeCache() {
		Node[] oldCache = nodeCache;
		nodeCache = new Node[oldCache.length << 1];

		System.arraycopy(oldCache, 0, nodeCache, 0, oldCache.length);

		fillNodeCache(oldCache.length, nodeCache.length);
	}

	private void fillNodeCache(int start, int end) {
		for (int index = start; index < end; index++) {
			nodeCache[index] = new Node(level);
		}
	}

	private Node removeNode(BlockPos pos) {
		return nodes.remove(pos.asLong());
	}

	/**
	 * Try to revalidate the given node by looking at the block state that is
	 * occupying its position. If the given node is a wire but the block state is
	 * not, a new node must be created/grabbed from the cache. Otherwise, the node
	 * can be quickly revalidated with the new block state;
	 */
	private Node revalidateNode(Node node) {
		BlockPos pos = node.pos;
		BlockState state = level.getBlockState(pos);

		boolean wasWire = node.isWire();
		boolean isWire = state.is(Blocks.REDSTONE_WIRE);

		if (wasWire != isWire) {
			return getNextNode(pos, state);
		}

		node.invalid = false;

		if (isWire) {
			// No need to update the block state of this wire - it will grab
			// the current block state just before setting power anyway.
			WireNode wire = node.asWire();

			wire.prepared = false;
			wire.inNetwork = false;
		} else {
			node.update(pos, state, false);
		}

		return node;
	}

	/**
	 * Retrieve the neighbor of a node in the given direction and create a link
	 * between the two nodes if they are not yet linked.
	 */
	private Node getNeighbor(Node node, int iDir) {
		Node neighbor = node.neighbors[iDir];

		if (neighbor == null || neighbor.invalid) {
			Direction dir = Directions.ALL[iDir];
			BlockPos pos = node.pos.relative(dir);

			Node oldNeighbor = neighbor;
			neighbor = getOrAddNode(pos);

			if (neighbor != oldNeighbor) {
				int iOpp = Directions.iOpposite(iDir);

				node.neighbors[iDir] = neighbor;
				neighbor.neighbors[iOpp] = node;
			}
		}

		return neighbor;
	}

	/**
	 * Iterate over all neighboring nodes of the given wire. The iteration order is
	 * designed to be an extension of the default block update order, and is
	 * determined as follows:
	 * <br>
	 * 1. The direction of power flow through the wire is to be considered
	 * 'forward'. The iteration order depends on the neighbors' relative positions
	 * to the wire.
	 * <br>
	 * 2. Each neighbor is identified by the step(s) you must take, starting at the
	 * wire, to reach it. Each step is 1 block, thus the position of a neighbor is
	 * encoded by the direction(s) of the step(s), e.g. (right), (down), (up, left),
	 * etc.
	 * <br>
	 * 3. Neighbors are iterated over in pairs that lie on opposite sides of the
	 * wire.
	 * <br>
	 * 4. Neighbors are iterated over in order of their distance from the wire. This
	 * means they are iterated over in 3 groups: direct neighbors first, then
	 * diagonal neighbors, and last are the far neighbors that are 2 blocks directly
	 * out.
	 * <br>
	 * 5. The order within each group is determined using the following basic order:
	 * { front, back, right, left, down, up }. This order was chosen because it
	 * converts to the following order of absolute directions when west is said to
	 * be 'forward': { west, east, north, south, down, up } - this is the order of
	 * shape updates.
	 */
	private void forEachNeighbor(WireNode wire, Consumer<Node> consumer) {
		int forward   = wire.iFlowDir;
		int rightward = (forward + 1) & 0b11;
		int backward  = (forward + 2) & 0b11;
		int leftward  = (forward + 3) & 0b11;
		int downward  = Directions.DOWN;
		int upward    = Directions.UP;

		Node front = getNeighbor(wire, forward);
		Node right = getNeighbor(wire, rightward);
		Node back  = getNeighbor(wire, backward);
		Node left  = getNeighbor(wire, leftward);
		Node below = getNeighbor(wire, downward);
		Node above = getNeighbor(wire, upward);

		// direct neighbors (6)
		consumer.accept(front);
		consumer.accept(back);
		consumer.accept(right);
		consumer.accept(left);
		consumer.accept(below);
		consumer.accept(above);

		// diagonal neighbors (12)
		consumer.accept(getNeighbor(front, rightward));
		consumer.accept(getNeighbor(back, leftward));
		consumer.accept(getNeighbor(front, leftward));
		consumer.accept(getNeighbor(back, rightward));
		consumer.accept(getNeighbor(front, downward));
		consumer.accept(getNeighbor(back, upward));
		consumer.accept(getNeighbor(front, upward));
		consumer.accept(getNeighbor(back, downward));
		consumer.accept(getNeighbor(right, downward));
		consumer.accept(getNeighbor(left, upward));
		consumer.accept(getNeighbor(right, upward));
		consumer.accept(getNeighbor(left, downward));

		// far neighbors (6)
		consumer.accept(getNeighbor(front, forward));
		consumer.accept(getNeighbor(back, backward));
		consumer.accept(getNeighbor(right, rightward));
		consumer.accept(getNeighbor(left, leftward));
		consumer.accept(getNeighbor(below, downward));
		consumer.accept(getNeighbor(above, upward));
	}

	/**
	 * This method should be called whenever a wire receives a block update.
	 */
	@CalledAtRuntime
	public void onWireUpdated(BlockPos pos) {
		invalidateNodes();
		findRoots(pos);
		tryUpdatePower();
	}

	/**
	 * This method should be called whenever a wire is placed.
	 */
	@CalledAtRuntime
	public void onWireAdded(BlockPos pos) {
		Node node = getOrAddNode(pos);

		if (!node.isWire()) {
			return; // we should never get here
		}

		WireNode wire = node.asWire();
		wire.added = true;

		invalidateNodes();
		tryAddRoot(wire);
		tryUpdatePower();
	}

	/**
	 * This method should be called whenever a wire is removed.
	 */
	@CalledAtRuntime
	public void onWireRemoved(BlockPos pos, BlockState state) {
		Node node = removeNode(pos);
		WireNode wire;

		if (node == null || !node.isWire()) {
			wire = new WireNode(level, pos, state);
		} else {
			wire = node.asWire();
		}

		wire.invalid = true;
		wire.removed = true;

		// If these fields are set to 'true', the removal of this wire was part of
		// already ongoing power changes, so we can exit early here.
		if (updating && wire.shouldBreak) {
			return;
		}

		invalidateNodes();
		tryAddRoot(wire);
		tryUpdatePower();
	}

	/**
	 * The nodes map is a snapshot of the state of the world. It becomes invalid
	 * when power changes are carried out, since the block and shape updates can
	 * lead to block changes. If these block changes cause the network to be updated
	 * again every node must be invalidated, and revalidated before it is used
	 * again. This ensures the power calculations of the network are accurate.
	 */
	private void invalidateNodes() {
		if (updating && !nodes.isEmpty()) {
			Iterator<Entry<Node>> it = Long2ObjectMaps.fastIterator(nodes);

			while (it.hasNext()) {
				Entry<Node> entry = it.next();
				Node node = entry.getValue();

				node.invalid = true;
			}
		}
	}

	/**
	 * Look for wires at and around the given position that are in an invalid state
	 * and require power changes. These wires are called 'roots' because it is only
	 * when these wires change power level that neighboring wires must adjust as
	 * well.
	 * 
	 * <p>
	 * While it is strictly only necessary to check the wire at the given position,
	 * if that wire is part of a network, it is beneficial to check its surroundings
	 * for other wires that require power changes. This is because a network can
	 * receive power at multiple points. Consider the following setup:
	 * 
	 * <p>
	 * (top-down view, W = wire, L = lever, _ = air/other)
	 * <br> {@code _ _ W _ _ }
	 * <br> {@code _ W W W _ }
	 * <br> {@code W W L W W }
	 * <br> {@code _ W W W _ }
	 * <br> {@code _ _ W _ _ }
	 * 
	 * <p>
	 * The lever powers four wires in the network at once. If this is identified
	 * correctly, the entire network can (un)power at once. While it is not
	 * practical to cover every possible situation where a network is (un)powered
	 * from multiple points at once, checking for common cases like the one
	 * described above is relatively straight-forward.
	 * 
	 * <p>
	 * While these extra checks can provide significant performance gains in some
	 * cases, in the majority of cases they will have little to no effect, but do
	 * require extra code modifications to all redstone power emitters. Removing
	 * these optimizations would limit code modifications to the RedStoneWireBlock
	 * and ServerLevel classes while leaving the performance mostly intact.
	 */
	private void findRoots(BlockPos pos) {
		Node node = getOrAddNode(pos);

		if (!node.isWire()) {
			return; // we should never get here
		}

		WireNode wire = node.asWire();
		tryAddRoot(wire);

		// If the wire at the given position is not in an invalid state or is not
		// part of a larger network, we can exit early.
		if (!wire.inNetwork || wire.connections.total == 0) {
			return;
		}

		for (int iDir : DEFAULT_FULL_UPDATE_ORDER) {
			Node neighbor = getNeighbor(wire, iDir);

			if (neighbor.isConductor()) {
				// Redstone components can power multiple wires through solid
				// blocks.
				findSignalSourcesAround(neighbor, Directions.iOpposite(iDir));
			} else if (((IBlockState)neighbor.state).isSignalSourceTo(level, neighbor.pos, Directions.ALL[iDir])) {
				// Redstone components can also power multiple wires directly.
				findRootsAroundSignalSource(neighbor, Directions.iOpposite(iDir));
			}
		}
	}

	/**
	 * Find signal sources around the given node that can provide direct signals to
	 * that node, and then search for wires that require power changes around those
	 * signal sources.
	 */
	private void findSignalSourcesAround(Node node, int except) {
		for (int iDir : Directions.I_EXCEPT[except]) {
			Node neighbor = getNeighbor(node, iDir);

			if (((IBlockState)neighbor.state).isDirectSignalSourceTo(level, neighbor.pos, Directions.ALL[iDir])) {
				findRootsAroundSignalSource(neighbor, iDir);
			}
		}
	}

	/**
	 * Find wires around the given signal source that require power changes.
	 */
	private void findRootsAroundSignalSource(Node node, int except) {
		for (int iDir : Directions.I_EXCEPT[except]) {
			// Directions are backwards for redstone related methods, so we must
			// check for power emitted in the opposite direction that we are
			// interested in.
			int iOpp = Directions.iOpposite(iDir);
			Direction opp = Directions.ALL[iOpp];

			boolean signal = ((IBlockState)node.state).isSignalSourceTo(level, node.pos, opp);
			boolean directSignal = ((IBlockState)node.state).isDirectSignalSourceTo(level, node.pos, opp);

			// If the signal source does not emit any power in this direction,
			// move on to the next direction.
			if (!signal && !directSignal) {
				continue;
			}

			Node neighbor = getNeighbor(node, iDir);

			if (signal && neighbor.isWire()) {
				tryAddRoot(neighbor.asWire());
			} else if (directSignal && neighbor.isConductor()) {
				findRootsAround(neighbor, iOpp);
			}
		}
	}

	/**
	 * Look for wires around the given node that require power changes.
	 */
	private void findRootsAround(Node node, int except) {
		for (int iDir : Directions.I_EXCEPT[except]) {
			Node neighbor = getNeighbor(node, iDir);

			if (neighbor.isWire()) {
				tryAddRoot(neighbor.asWire());
			}
		}
	}

	/**
	 * Check if the given wire is in an illegal state and needs power changes.
	 */
	private void tryAddRoot(WireNode wire) {
		// Each potential root needs to be checked only once.
		if (wire.prepared) {
			return;
		}

		prepare(wire);
		findPower(wire, false);

		if (needsPowerChange(wire)) {
			addRoot(wire);
		}
	}

	/**
	 * Add the given wire to the network as a root.
	 */
	private void addRoot(WireNode wire) {
		network.add(wire);
		rootCount++;

		wire.inNetwork = true;

		if (wire.connections.iFlowDir >= 0) {
			wire.iFlowDir = wire.connections.iFlowDir;
		}
	}

	/**
	 * Before a wire can be added to the network, it must be properly prepared.
	 * This method
	 * <br>
	 * - checks if this wire should break. Rather than break the wire right away,
	 * its effects are integrated into the power calculations.
	 * <br>
	 * - determines the 'external power' this wire receives (power from non-wire
	 * components).
	 * <br>
	 * - finds connections this wire has to neighboring wires.
	 */
	private void prepare(WireNode wire) {
		// Each wire only needs to be prepared once.
		if (wire.prepared) {
			return;
		}

		wire.prepared = true;
		wire.inNetwork = false;

		if (!wire.removed && !wire.shouldBreak && !wire.state.canSurvive(level, wire.pos)) {
			wire.shouldBreak = true;
		}

		wire.virtualPower = wire.externalPower = getInitialPower(wire);
		wire.connections.set(this::getNeighbor);
	}

	private int getInitialPower(WireNode wire) {
		return (wire.removed || wire.shouldBreak) ? POWER_MIN : getExternalPower(wire);
	}

	private int getExternalPower(WireNode wire) {
		int power = POWER_MIN;

		for (int iDir = 0; iDir < Directions.ALL.length; iDir++) {
			Node neighbor = getNeighbor(wire, iDir);

			// Power from wires is handled separately.
			if (neighbor.isWire()) {
				continue;
			}

			// Since 1.16 there is a block that is both a conductor and a signal
			// source: the target block!
			if (neighbor.isConductor()) {
				power = Math.max(power, getDirectSignalTo(wire, neighbor, Directions.iOpposite(iDir)));
			}
			if (neighbor.isSignalSource()) {
				power = Math.max(power, neighbor.state.getSignal(level, neighbor.pos, Directions.ALL[iDir]));
			}

			if (power >= POWER_MAX) {
				return POWER_MAX;
			}
		}

		return power;
	}

	/**
	 * Determine the direct signal the given wire receives from neighboring blocks
	 * through the given conductor node.
	 */
	private int getDirectSignalTo(WireNode ignored, Node node, int except) {
		int power = POWER_MIN;

		for (int iDir : Directions.I_EXCEPT[except]) {
			Node neighbor = getNeighbor(node, iDir);

			if (neighbor.isSignalSource()) {
				power = Math.max(power, neighbor.state.getDirectSignal(level, neighbor.pos, Directions.ALL[iDir]));

				if (power >= POWER_MAX) {
					return POWER_MAX;
				}
			}
		}

		return power;
	}

	/**
	 * Determine the power level the given wire receives from the surrounding blocks.
	 * Power from non-wire components has already been determined, so only power
	 * received from other wires needs to be checked. There are a few exceptions:
	 * <br>
	 * - If the wire is removed or going to break, its power level should always be
	 * the minimum value. This is because it (effectively) no longer exists, so
	 * cannot provide any power to neighboring wires.
	 * <br>
	 * - Power received from neighboring wires will never exceed {@code POWER_MAX - 
	 * POWER_STEP}, so if the external power is already larger than or equal to
	 * that, there is no need to check for power from neighboring wires.
	 */
	private void findPower(WireNode wire, boolean ignoreNetwork) {
		if (wire.removed || wire.shouldBreak || wire.externalPower >= (POWER_MAX - POWER_STEP)) {
			return;
		}

		// The virtual power is reset to the external power, so the flow information
		// must be reset as well.
		wire.virtualPower = wire.externalPower;
		wire.flowIn = 0;

		findWirePower(wire, ignoreNetwork);
	}

	/**
	 * Determine the power level the given wire receives from connected wires.
	 */
	private void findWirePower(WireNode wire, boolean ignoreNetwork) {
		wire.connections.forEach(connection -> {
			if (!connection.accept) {
				return;
			}

			WireNode neighbor = connection.wire;

			if (!ignoreNetwork || !neighbor.inNetwork) {
				int power = Math.max(POWER_MIN, neighbor.virtualPower - POWER_STEP);
				int iOpp = Directions.iOpposite(connection.iDir);

				wire.offerPower(power, iOpp);
			}
		});
	}

	private boolean needsPowerChange(WireNode wire) {
		return wire.removed || wire.shouldBreak || wire.virtualPower != wire.currentPower;
	}

	private void tryUpdatePower() {
		if (rootCount > 0) {
			updatePower();
		}
		if (!updating) {
			nodes.clear();
			nodeCount = 0;
		}
	}

	/**
	 * Propagate power changes through the network and notify neighboring blocks of
	 * these changes.
	 * 
	 * <p>
	 * Power changes are done in the following 3 steps.
	 * 
	 * <p>
	 * <b>1. Build up the network</b>
	 * <br>
	 * Collect all the wires around the roots that need to change their power
	 * levels.
	 * 
	 * <p>
	 * <b>2. Find powered wires</b>
	 * <br>
	 * Find those wires in the network that receive power from outside the network.
	 * This can come in 2 forms:
	 * <br>
	 * - Power from non-wire components (repeaters, torches, etc.).
	 * <br>
	 * - Power from wires that are not in the network.
	 * <br>
	 * These powered wires will then queue their power changes.
	 * 
	 * <p>
	 * <b>3. Let power flow</b>
	 * <br>
	 * Work through the queue of power changes. After each wire's power change, emit
	 * shape and block updates to neighboring blocks, then queue power changes for
	 * connected wires.
	 */
	private void updatePower() {
		// Build a network of wires that need power changes. This includes the roots
		// as well as any wires that will be affected by power changes to those roots.
		buildNetwork();

		// Find those wires in the network that receive power from outside it.
		// Remember that the power changes for those wires are already queued here!
		findPoweredWires();

		// Once the powered wires have been found, the network is no longer needed. In
		// fact, it should be cleared before block and shape updates are emitted, in
		// case a different network is updated that needs power changes.
		network.clear();
		rootCount = 0;

		// Carry out the power changes and emit shape and block updates.
		try {
			letPowerFlow();
		} catch (Throwable t) {
			// If anything goes wrong while carrying out power changes, this field must
			// be reset to 'false', or the wire handler will be locked out of carrying
			// out power changes until the world is reloaded.
			updating = false;
			throw t;
		}
	}

	/**
	 * Build up a network of wires that need power changes. This includes the roots
	 * that were already added and any wires powered by those roots that will need
	 * power changes as a result of power changes to the roots.
	 */
	private void buildNetwork() {
		for (int i = 0; i < network.size(); i++)
		{
			final WireNode wire = network.get(i);
			// The order in which wires are added to the network can influence the
			// order in which they update their power levels.
			wire.connections.forEach(connection -> {
				if (!connection.offer)
				{
					return;
				}

				WireNode neighbor = connection.wire;

				if (neighbor.inNetwork)
				{
					return;
				}

				prepare(neighbor);
				findPower(neighbor, false);

				if (needsPowerChange(neighbor))
				{
					addToNetwork(neighbor, connection.iDir);
				}
			}, wire.iFlowDir);
		}
	}

	/**
	 * Add the given wire to the network and set its outgoing flow to some backup
	 * value. This avoids directionality in redstone grids.
	 */
	private void addToNetwork(WireNode wire, int iBackupFlowDir) {
		network.add(wire);

		wire.inNetwork = true;
		// Normally the flow is not set until the power level is updated. However,
		// in networks with multiple power sources the update order between them
		// depends on which was discovered first. To make this less prone to
		// directionality, each wire node is given a 'backup' flow. For roots, this
		// is the determined flow of their connections. For non-roots this is the
		// direction from which they were discovered.
		wire.iFlowDir = iBackupFlowDir;
	}

	/**
	 * Find those wires in the network that receive power from outside it, either
	 * from non-wire components or from wires that are not in the network, and queue
	 * the power changes for those wires.
	 */
	private void findPoweredWires() {
		for (int index = 0; index < network.size(); index++) {
			WireNode wire = network.get(index);
			findPower(wire, true);

			if (index < rootCount || wire.removed || wire.shouldBreak || wire.virtualPower > POWER_MIN) {
				queueWire(wire);
			} else {
				// Wires that do not receive any power do not queue power changes
				// until they are offered power from a neighboring wire. To ensure
				// that they accept any power from neighboring wires and thus queue
				// their power changes, their virtual power is set to below the
				// minimum.
				wire.virtualPower--;
			}
		}
	}

	/**
	 * Carry out power changes, setting the new power of each wire in the world,
	 * notifying neighbors of the power change, then queueing power changes of
	 * connected wires.
	 */
	private void letPowerFlow() {
		// If an instantaneous update chain causes updates to another network
		// (or the same network in another place), new power changes will be
		// integrated into the already ongoing power queue, so we can exit early
		// here.
		if (updating) {
			return;
		}

		updating = true;

		while (!updates.isEmpty()) {
			Node node = updates.poll();

			if (node.isWire()) {
				WireNode wire = node.asWire();

				if (!needsPowerChange(wire)) {
					continue;
				}

				findPowerFlow(wire);
				transmitPower(wire);

				if (wire.setPower()) {
					queueNeighbors(wire);

					// If the wire was newly placed or removed, shape updates have
					// already been emitted.
					if (!wire.added && !wire.shouldBreak) {
						updateNeighborShapes(wire);
					}
				}
			} else {
				WireNode neighborWire = node.neighborWire;

				if (neighborWire != null) {
					BlockPos neighborPos = neighborWire.pos;
					Block neighborBlock = neighborWire.state.getBlock();

					updateNeighbor(node, neighborPos, neighborBlock);
				}
			}
		}

		updating = false;
	}

	/**
	 * Use the information of incoming power flow to determine the direction of
	 * power flow through this wire. If that flow is ambiguous, try to use a flow
	 * direction based on connections to neighboring wires. If that is also
	 * ambiguous, use the backup value that was set when the wire was first added to
	 * the network.
	 */
	private void findPowerFlow(WireNode wire) {
		int flow = FLOW_IN_TO_FLOW_OUT[wire.flowIn];

		if (flow >= 0) {
			wire.iFlowDir = flow;
		} else if (wire.connections.iFlowDir >= 0) {
			wire.iFlowDir = wire.connections.iFlowDir;
		}
	}

	/**
	 * Transmit power from the given wire to neighboring wires and queue updates to
	 * those wires.
	 */
	private void transmitPower(WireNode wire) {
		wire.connections.forEach(connection -> {
			if (!connection.offer) {
				return;
			}

			WireNode neighbor = connection.wire;

			int power = Math.max(POWER_MIN, wire.virtualPower - POWER_STEP);
			int iDir = connection.iDir;

			if (neighbor.offerPower(power, iDir)) {
				queueWire(neighbor);
			}
		}, wire.iFlowDir);
	}

	/**
	 * Emit shape updates around the given wire.
	 */
	private void updateNeighborShapes(WireNode wire) {
		BlockPos wirePos = wire.pos;
		BlockState wireState = wire.state;

		for (int iDir : DEFAULT_FULL_UPDATE_ORDER) {
			Node neighbor = getNeighbor(wire, iDir);

			if (!neighbor.isWire()) {
				int iOpp = Directions.iOpposite(iDir);
				Direction opp = Directions.ALL[iOpp];

				updateNeighborShape(neighbor, opp, wirePos, wireState);
			}
		}
	}

	private void updateNeighborShape(Node node, Direction fromDir, BlockPos fromPos, BlockState fromState) {
		BlockPos pos = node.pos;
		BlockState state = level.getBlockState(pos);

		// Shape updates to redstone wire are very expensive, and should never happen
		// as a result of power changes anyway.
		if (!state.isAir() && !state.is(Blocks.REDSTONE_WIRE)) {
			BlockState newState = state.updateShape(fromDir, fromState, level, pos, fromPos);
			Block.updateOrDestroy(state, newState, level, pos, Block.UPDATE_CLIENTS);
		}
	}

	/**
	 * Queue block updates to nodes around the given wire.
	 */
	private void queueNeighbors(WireNode wire) {
		forEachNeighbor(wire, neighbor -> queueNeighbor(neighbor, wire));
	}

	/**
	 * Queue the given node for an update from the given neighboring wire.
	 */
	private void queueNeighbor(Node node, WireNode neighborWire) {
		// Updates to wires are queued when power is transmitted.
		if (!node.isWire()) {
			node.neighborWire = neighborWire;
			updates.offer(node);
		}
	}

	/**
	 * Queue the given wire for a power change. If the wire does not need a power
	 * change (perhaps because its power has already changed), transmit power to
	 * neighboring wires.
	 */
	private void queueWire(WireNode wire) {
		if (needsPowerChange(wire)) {
			updates.offer(wire);
		} else {
			findPowerFlow(wire);
			transmitPower(wire);
		}
	}

	/**
	 * Emit a block update to the given node.
	 */
	private void updateNeighbor(Node node, BlockPos fromPos, Block fromBlock) {
		BlockPos pos = node.pos;
		BlockState state = level.getBlockState(pos);

		// While this check makes sure wires in the network are not given block
		// updates, it also prevents block updates to wires in neighboring networks.
		// While this should not make a difference in theory, in practice, it is
		// possible to force a network into an invalid state without updating it, even
		// if it is relatively obscure.
		// While I was willing to make this compromise in return for some significant
		// performance gains in certain setups, if you are not, you can add all the
		// positions of the network to a set and filter out block updates to wires in
		// the network that way.
		if (!state.isAir() && !state.is(Blocks.REDSTONE_WIRE)) {
			state.neighborChanged(level, pos, fromBlock, fromPos, false);
		}
	}

	@FunctionalInterface
	public interface NodeProvider {

		Node getNeighbor(Node node, int iDir);

	}
}
