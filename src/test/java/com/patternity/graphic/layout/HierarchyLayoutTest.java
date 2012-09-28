package com.patternity.graphic.layout;

import java.util.Iterator;

import com.patternity.graphic.dag.DepthFirstIterator;
import com.patternity.graphic.dag.Node;

public class HierarchyLayoutTest extends AbstractLayoutTest {

	public HierarchyLayoutTest(String name) {
		super(name);
	}

	public void testLayout() throws Exception {
		final Layout layout = new HierarchyLayout();

		final Node a = tree();

        final Iterator<?> iterator = new DepthFirstIterator(a);
		final String filename = "HierarchyLayoutTest.svg";
		write(iterator, layout, filename);
	}
}
