package com.patternity.graphic;

/**
 * Represents a simple 2D position (x,y)
 * 
 * @author cyrille martraire
 */
public class Position {

	private final double x;
	private final double y;

	public static final Position NONE = new Position(0, 0);

	public static final Position ZERO = new Position(0, 0);

	public static final Position UNITY = new Position(1, 1);

	public static final Position IDENTITY = new Position(1, 1);

	public static final Position V_FLIP = new Position(1, -1);

	public static final Position H_FLIP = new Position(-1, 1);

	public static final Position INVERSE = new Position(-1, -1);

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public boolean isZero() {
		return x == 0 && y == 0;
	}

	public boolean isPositive() {
		return x >= 0 && y >= 0;
	}

	public Position add(double x, double y) {
		return new Position(this.x + x, this.y + y);
	}

	public Position add(Position position) {
		return add(position.x, position.y);
	}

	public Position transpose() {
		return new Position(y, x);
	}

	public static Position min(Position pos1, Position pos2) {
		return new Position(Math.min(pos1.x, pos2.x), Math.min(pos1.y, pos2.y));
	}

	public static Position max(Position pos1, Position pos2) {
		return new Position(Math.max(pos1.x, pos2.x), Math.max(pos1.y, pos2.y));
	}

	public Position multiply(double xCoeff, double yCoeff) {
		return new Position(x * xCoeff, y * yCoeff);
	}

	public Position multiply(Position coeffs) {
		return multiply(coeffs.x, coeffs.y);
	}

	/**
	 * @return true if this Position is equal to the given Position
	 */
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Position)) {
			return false;
		}
		final Position other = (Position) arg0;
		if (this == other) {
			return true;
		}
		return other.x == x && other.y == y;
	}

	public int hashCode() {
		return (int) x ^ (int) y;
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
