package com.patternity.graphic;

/**
 * Represents a simple 2D bounding box (width, height) with a position (x,y)
 * 
 * @author cyrille martraire
 */
public class Rectangle {

	private final Position position;
	private final BoundingBox box;

	public final static Rectangle NONE = new Rectangle(Position.NONE, BoundingBox.NONE);

	public Rectangle(int x, int y, int width, int height) {
		this(new Position(x, y), width, height);
	}

	public Rectangle(Position position, int width, int height) {
		this(position, new BoundingBox(width, height));
	}

	public Rectangle(Position position, BoundingBox box) {
		this.position = position;
		this.box = box;
	}

	public Position getPosition() {
		return position;
	}

	public BoundingBox getBox() {
		return box;
	}

	public double getX() {
		return position.getX();
	}

	public double getY() {
		return position.getY();
	}

	public int getWidth() {
		return box.getWidth();
	}

	public int getHeight() {
		return box.getHeight();
	}

	public Position getPort(Orientation orientation) {
		return orientation == null ? Orientation.EAST.getPort(this) : orientation.getPort(this);
	}

	public String toString() {
		return position + " " + box;
	}
}
