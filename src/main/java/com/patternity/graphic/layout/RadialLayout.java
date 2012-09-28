package com.patternity.graphic.layout;

import com.patternity.graphic.Position;
import com.patternity.graphic.dag.Node;

/**
 * A simple hierarchy layout
 * 
 * @author cyrille martraire
 */
public class RadialLayout implements Layout {

	private final static Position[] SPIRAL_9 = { new Position(0, 0), new Position(1, 0), new Position(0, -1),
			new Position(-1, 0), new Position(0, 1), new Position(1, -1), new Position(-1, -1), new Position(-1, 1),
			new Position(1, 1) };

	private final static Position[] HEMISPIRAL_6 = { new Position(0, 0), new Position(1, 0), new Position(0, -1),
			new Position(-1, 0), new Position(1, -1), new Position(-1, -1) };

	private final Position[] layout;
	private int i = 0;

	public final static RadialLayout full9RadialLayout() {
		return new RadialLayout(SPIRAL_9);
	};

	public final static RadialLayout semi6RadialLayout() {
		return new RadialLayout(HEMISPIRAL_6);
	};

	public RadialLayout(Position[] layout) {
		this.layout = layout;
	}

	public Position position(Node node) {
		final Position position = layout[i].add(Position.UNITY);
		i++;
		return position;
	}

	public String toString() {
		return "RadialLayout " + layout.length + " slots";
	}

}
