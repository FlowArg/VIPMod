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

import fr.flowarg.vip3.utils.wire.WireHandler.Directions;
import fr.flowarg.vip3.utils.wire.WireHandler.NodeProvider;

import java.util.Arrays;
import java.util.function.Consumer;

public class WireConnectionManager {

	/** The owner of these connections. */
	final WireNode owner;

	/** The first connection for each cardinal direction. */
	private final WireConnection[] heads;

	private WireConnection head;
	private WireConnection tail;

	/** The total number of connections. */
	int total;

	/**
	 * A 4 bit number that encodes which in direction(s) the owner has connections
	 * to other wires.
	 */
	private int flowTotal;
	/** The direction of flow based connections to other wires. */
	int iFlowDir;

	WireConnectionManager(WireNode owner) {
		this.owner = owner;

		this.heads = new WireConnection[Directions.HORIZONTAL.length];

		this.total = 0;

		this.flowTotal = 0;
		this.iFlowDir = -1;
	}

	void set(NodeProvider nodes) {
		if (total > 0) {
			clear();
		}

		boolean belowIsConductor = nodes.getNeighbor(owner, Directions.DOWN).isConductor();
		boolean aboveIsConductor = nodes.getNeighbor(owner, Directions.UP).isConductor();

		for (int iDir = 0; iDir < Directions.HORIZONTAL.length; iDir++) {
			Node neighbor = nodes.getNeighbor(owner, iDir);

			if (neighbor.isWire()) {
				add(neighbor.asWire(), iDir, true, true);

				continue;
			}

			boolean sideIsConductor = neighbor.isConductor();

			if (!sideIsConductor) {
				Node node = nodes.getNeighbor(neighbor, Directions.DOWN);

				if (node.isWire()) {
					add(node.asWire(), iDir, belowIsConductor, true);
				}
			}
			if (!aboveIsConductor) {
				Node node = nodes.getNeighbor(neighbor, Directions.UP);

				if (node.isWire()) {
					add(node.asWire(), iDir, true, sideIsConductor);
				}
			}
		}

		if (total > 0) {
			iFlowDir = WireHandler.FLOW_IN_TO_FLOW_OUT[flowTotal];
		}
	}

	private void clear() {
		Arrays.fill(heads, null);

		head = null;
		tail = null;

		total = 0;

		flowTotal = 0;
		iFlowDir = -1;
	}

	private void add(WireNode wire, int iDir, boolean offer, boolean accept) {
		add(new WireConnection(wire, iDir, offer, accept));
	}

	private void add(WireConnection connection) {
		if (head == null) {
			head = connection;
			tail = connection;
		} else {
			tail.next = connection;
			tail = connection;
		}

		total++;

		if (heads[connection.iDir] == null) {
			heads[connection.iDir] = connection;
			flowTotal |= (1 << connection.iDir);
		}
	}

	/**
	 * Iterate over all connections. Use this method if the iteration order is not
	 * important.
	 */
	void forEach(Consumer<WireConnection> consumer) {
		for (WireConnection c = head; c != null; c = c.next) {
			consumer.accept(c);
		}
	}

	/**
	 * Iterate over all connections. Use this method if the iteration order is
	 * important.
	 */
	void forEach(Consumer<WireConnection> consumer, int iFlowDir) {
		for (int iDir : WireHandler.CARDINAL_UPDATE_ORDERS[iFlowDir]) {
			for (WireConnection c = heads[iDir]; c != null && c.iDir == iDir; c = c.next) {
				consumer.accept(c);
			}
		}
	}
}
