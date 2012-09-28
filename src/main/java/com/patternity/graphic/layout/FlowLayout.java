package com.patternity.graphic.layout;

import com.patternity.graphic.Position;
import com.patternity.graphic.dag.Node;

/**
 * A simple hierarchy layout
 * 
 * @author cyrille martraire
 */
public class FlowLayout implements Layout {

	private final int modulo;
	private int i;

	public FlowLayout(int modulo) {
		this.modulo = modulo;
	}

	public Position position(Node node) {
		final int row = i / modulo;
		final int col = i % modulo;
		final Position position = new Position(col, row);
		i++;
		return position;
	}

	public String toString() {
		return "FlowLayout modulo=" + modulo;
	}
}
