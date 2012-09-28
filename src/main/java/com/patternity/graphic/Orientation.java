package com.patternity.graphic;

/**
 * Represents the orientation of a port on an item, typically to draw lines from
 * one to another.
 * 
 * @author cyrille martraire
 */
public abstract class Orientation {

	public abstract Position getPort(Rectangle box);

	public abstract Position coefficients();

	public abstract Orientation opposite();

	private static final Position EAST_COEFF = new Position(1, 0);

	private static final Position WEST_COEFF = new Position(-1, 0);

	private static final Position NORTH_COEFF = new Position(0, -1);

	private static final Position SOUTH_COEFF = new Position(0, 1);

	public static Orientation orientation(Position from, Position to) {
		final double deltaX = Math.abs(from.getX() - to.getX());
		final double deltaY = Math.abs(from.getY() - to.getY());
		if (deltaX < deltaY) {
			return verticalOrientation(from, to);
		}
		return horizontalOrientation(from, to);
	}

	public static Orientation orientation(Orientation hint, Position from, Position to) {
		if (hint == null) {
			return orientation(from, to);
		}
		if (hint instanceof VerticalOrientation) {
			return Orientation.verticalOrientation(from, to);
		}
		if (hint instanceof HorizontalOrientation) {
			return Orientation.horizontalOrientation(from, to);
		}
		throw new IllegalArgumentException("Unexpected hint orientation: " + hint);
	}

	public static Orientation horizontalOrientation(Position from, Position to) {
		return from.getX() < to.getX() ? EAST : WEST;
	}

	public static Orientation verticalOrientation(Position from, Position to) {
		return from.getY() < to.getY() ? SOUTH : NORTH;
	}

	public static abstract class VerticalOrientation extends Orientation {

	}

	public static abstract class HorizontalOrientation extends Orientation {

	}

	public static Orientation EAST = new HorizontalOrientation() {
		public Position getPort(Rectangle box) {
			return box.getPosition().add(box.getWidth() / 2, 0);
		}

		public Orientation opposite() {
			return WEST;
		}

		public Position coefficients() {
			return EAST_COEFF;
		}

		public String toString() {
			return "EAST";
		}

	};

	public static Orientation WEST = new HorizontalOrientation() {
		public Position getPort(Rectangle box) {
			return box.getPosition().add(-box.getWidth() / 2, 0);
		}

		public Orientation opposite() {
			return EAST;
		}

		public Position coefficients() {
			return WEST_COEFF;
		}

		public String toString() {
			return "WEST";
		}
	};

	public static Orientation NORTH = new VerticalOrientation() {
		public Position getPort(Rectangle box) {
			return box.getPosition().add(0, -box.getHeight() / 2);
		}

		public Orientation opposite() {
			return SOUTH;
		}

		public Position coefficients() {
			return NORTH_COEFF;
		}

		public String toString() {
			return "NORTH";
		}
	};

	public static Orientation SOUTH = new VerticalOrientation() {
		public Position getPort(Rectangle box) {
			return box.getPosition().add(0, box.getHeight() / 2);
		}

		public Orientation opposite() {
			return NORTH;
		}

		public Position coefficients() {
			return SOUTH_COEFF;
		}

		public String toString() {
			return "SOUTH";
		}
	};
}