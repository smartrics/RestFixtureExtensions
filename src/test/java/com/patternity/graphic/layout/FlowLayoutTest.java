package com.patternity.graphic.layout;

import java.util.Iterator;

import com.patternity.graphic.dag.BreadthFirstIterator;
import com.patternity.graphic.dag.Node;

/**
 * @author cyrille martraire
 * 
 */
public class FlowLayoutTest extends AbstractLayoutTest {

	/**
	 * @param name
	 */
	public FlowLayoutTest(String name) {
		super(name);
	}

	public void testLayout() throws Exception {
		final Layout layout = new FlowLayout(3);

		final Node a = tree();

        final Iterator<?> iterator = new BreadthFirstIterator(a);
		final String filename = "FlowLayoutTest.svg";
		write(iterator, layout, filename);

	}

}
