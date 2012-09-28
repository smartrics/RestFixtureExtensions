package com.patternity.graphic.layout;

import java.util.Iterator;

import com.patternity.graphic.dag.BreadthFirstIterator;
import com.patternity.graphic.dag.Node;

/**
 * @author cyrille martraire
 * 
 */
public class RadialLayoutTest extends AbstractLayoutTest {

	/**
	 * @param name
	 */
	public RadialLayoutTest(String name) {
		super(name);
	}

	public void testLayout() throws Exception {
		final Layout layout = RadialLayout.semi6RadialLayout();

		final Node a = tree();

        final Iterator<?> iterator = new BreadthFirstIterator(a);
		final String filename = "RadialLayoutTest.svg";
		write(iterator, layout, filename);

	}

}
