package com.patternity.graphic.layout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.patternity.graphic.Position;
import com.patternity.graphic.dag.Node;

/**
 * A simple hierarchy layout
 * 
 * @author cyrille martraire
 */
public class HierarchyLayout implements Layout {

	public Map layout(Iterator iterator) {
		final Map positions = new HashMap();
		int i = 0;
		while (iterator.hasNext()) {
			final Node node = (Node) iterator.next();

			final Position position = position(node);
			positions.put(node.getElement(), position);
			i++;
		}
		return positions;
	}

	public Position position(Node node) {
		final int column = column(node);

		final double widthExcess = node.getMaxBreadth() - 1;
		final double halfSpanWidth = widthExcess / 2.0;
		final double x = halfSpanWidth + column;
		final double y = node.getDepth() - 1;
		final Position position = new Position(x, y);

		return position;
	}

	private static int column(Node node) {
		if (node.isRoot()) {
			return 0;
		}
		final Node previous = node.getPrevious();
		if (previous != null) {
			return column(previous) + previous.getMaxBreadth();
		}
		return column(node.getParent());
	}

	public String toString() {
		return "HierarchyLayout";
	}

}
