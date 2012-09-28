package com.patternity.graphic.util;

import com.patternity.graphic.dag.Node;

import junit.framework.TestCase;

public class NodeTest extends TestCase {

	public NodeTest(String name) {
		super(name);
	}

	public void testLayout() throws Exception {
		final Node a = new Node("a");
		final Node b = new Node("b");
		final Node c = new Node("c");
		final Node d = new Node("d");

		a.add(b);
		a.add(c);
		a.add(d);

		final Node e = new Node("e");
		final Node f = new Node("f");

		d.add(e);
		d.add(f);

		System.out.println(a);

		assertEquals(4, a.getMaxBreadth());
		assertEquals(3, f.getDepth());

		assertEquals(3, a.getMaxDepth());
		assertEquals(1, f.getMaxDepth());

		walk(a);
	}

	private void walk(Node node) {
		final double halfSpanWidth = (node.getMaxBreadth() - 1) / 2.0;
		final double x = node.getRank() + halfSpanWidth;
		final int y = node.getDepth() - 1;
		System.out.println(node.getElement() + ": " + x + ", " + y);

		final Node[] children = node.getNodes();
		for (int i = 0; i < children.length; i++) {
			walk(children[i]);
		}
	}
}
