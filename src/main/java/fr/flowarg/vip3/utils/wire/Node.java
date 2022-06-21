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

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

/**
 * A Node represents a block in the world. It also holds a few other pieces of
 * information that speed up the calculations in the WireHandler class.
 * 
 * @author Space Walker
 */
public class Node {

	// flags that encode the Node type
	private static final int CONDUCTOR = 0b01;
	private static final int SOURCE    = 0b10;

	final ServerLevel level;
	final Node[] neighbors;

	BlockPos pos;
	BlockState state;
	boolean invalid;

	private int flags;

	/** The previous node in the update queue. */
	Node prev;
	/** The next node in the update queue. */
	Node next;
	/** The priority with which this node was queued. */
	int priority;
	/** The wire that queued this node for an update. */
	WireNode neighborWire;

	Node(ServerLevel level) {
		this.level = level;
		this.neighbors = new Node[WireHandler.Directions.ALL.length];
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Node)) {
			return false;
		}

		Node node = (Node)obj;

		return level == node.level && pos.equals(node.pos);
	}

	@Override
	public int hashCode() {
		return pos.hashCode();
	}

	Node update(BlockPos pos, BlockState state, boolean clearNeighbors) {
		if (state.is(Blocks.REDSTONE_WIRE)) {
			throw new IllegalStateException("Cannot update a regular Node to a WireNode!");
		}

		if (clearNeighbors) {
			Arrays.fill(neighbors, null);
		}

		this.pos = pos.immutable();
		this.state = state;
		this.invalid = false;

		this.flags = 0;

		if (this.state.isRedstoneConductor(this.level, this.pos)) {
			this.flags |= CONDUCTOR;
		}
		if (this.state.isSignalSource()) {
			this.flags |= SOURCE;
		}

		return this;
	}

	/**
	 * Determine the priority with which this node should be queued.
	 */
	int priority() {
		return neighborWire.priority;
	}

	public boolean isWire() {
		return false;
	}

	public boolean isConductor() {
		return (flags & CONDUCTOR) != 0;
	}

	public boolean isSignalSource() {
		return (flags & SOURCE) != 0;
	}

	public WireNode asWire() {
		throw new UnsupportedOperationException("Not a WireNode!");
	}
}
