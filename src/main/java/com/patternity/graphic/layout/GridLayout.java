package com.patternity.graphic.layout;

import com.patternity.graphic.BoundingBox;
import com.patternity.graphic.Position;
import com.patternity.graphic.dag.Node;

/**
 * A decorator layout that applies a grid with actual dimensions to a layout
 * done previously.
 * 
 * @pattern Decorator target=Layout
 * 
 * @author cyrille martraire
 * 
 */
public class GridLayout implements Layout {

	private static final Position CENTER = new Position(0.5, 0.5);

	private final Layout layout;

	private final BoundingBox grid;

	private final Position flip;
	private final boolean tranpose;
	private final Position origin;

	private final Position scale;

	/**
	 * Left-Right flow layout
	 */
	public final static GridLayout gridLR(Layout layout, BoundingBox grid) {
		return gridLR(layout, grid, null);
	}

	/**
	 * Left-Right flow layout
	 */
	public final static GridLayout gridLR(Layout layout, BoundingBox grid, Position origin) {
		return new GridLayout(layout, grid, Position.IDENTITY, false, origin);
	}

	/**
	 * Top-Bottom flow layout
	 */
	public final static GridLayout gridTB(Layout layout, BoundingBox grid) {
		return gridTB(layout, grid, null);
	}

	/**
	 * Top-Bottom flow layout
	 */
	public final static GridLayout gridTB(Layout layout, BoundingBox grid, Position origin) {
		return new GridLayout(layout, grid.transpose(), Position.IDENTITY, true, origin);
	}

	public GridLayout(Layout layout, BoundingBox grid) {
		this(layout, grid, Position.IDENTITY, false, null);
	}

	public GridLayout(Layout layout, BoundingBox grid, Position flip, final boolean tranpose, Position origin) {
		this.layout = layout;
		this.grid = grid;
		this.flip = flip;
		this.tranpose = tranpose;
		this.origin = origin;
		scale = grid == null ? flip : flip.multiply(grid.getWidth(), grid.getHeight());
	}

	public Position position(Node node) {
		// delegate initial position
		Position position = layout.position(node);

		// translate for alignment
		position = position.add(CENTER);

		// multiply to grid dimensions
		position = position.multiply(scale);

		// transpose x and y if needed
		if (tranpose) {
			position = position.transpose();
		}

		// translate to change origin
		if (origin != null) {
			position = position.add(origin);
		}
		return position;
	}

	public String toString() {
		return "GridLayout grid=" + grid + " on " + layout;
	}
}
