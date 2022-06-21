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

/**
 * This class represents a connection between some WireNode (the 'owner') and a
 * neighboring WireNode. Two wires are considered to be connected if power can
 * flow from one wire to the other (and/or vice versa).
 * 
 * @author Space Walker
 */
public class WireConnection {

	/** The connected wire. */
	final WireNode wire;
	/** Cardinal direction to the connected wire. */
	final int iDir;
	/** True if the owner of the connection can provide power to the connected wire. */
	final boolean offer;
	/** True if the connected wire can provide power to the owner of the connection. */
	final boolean accept;

	/** The next connection in the sequence. */
	WireConnection next;

	WireConnection(WireNode wire, int iDir, boolean offer, boolean accept) {
		this.wire = wire;
		this.iDir = iDir;
		this.offer = offer;
		this.accept = accept;
	}
}
