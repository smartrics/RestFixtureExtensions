package com.patternity.graphic.svg;

import com.patternity.graphic.Style;
import com.patternity.graphic.Styled;
import com.patternity.graphic.dag.Node;

/**
 * Represents a box and its style.
 * 
 * @author cyrille martraire
 */
public final class Box implements Styled {

	private final Node node;
	private final BoxStyle style;

	public Box(Node node, BoxStyle style) {
		this.node = node;
		this.style = style;
	}

	public Style getStyle() {
		return style;
	}

	public Node getNode() {
		return node;
	}

	public String toString() {
		return "Box: " + node + " " + style;
	}

}
