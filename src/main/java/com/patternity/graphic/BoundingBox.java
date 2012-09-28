package com.patternity.graphic;

/**
 * Represents a simple box (width, height)
 * 
 * @author cyrille martraire
 */
public class BoundingBox {

	private final int width;
	private final int height;

	public final static BoundingBox NONE = new BoundingBox(0, 0);

	public BoundingBox(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isZero() {
		return width == 0 && height == 0;
	}

	public boolean isPositive() {
		return width > 0 && height > 0;
	}

	public BoundingBox transpose() {
		return new BoundingBox(height, width);
	}

	public BoundingBox scale(final double ratio) {
		return new BoundingBox((int) (height * ratio), (int) (width * ratio));
	}

	public String toString() {
		return "width=" + getWidth() + " height=" + getHeight();
	}
}
