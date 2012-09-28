package com.patternity.graphic.svg;

import java.util.Map;

import com.patternity.graphic.Rectangle;
import com.patternity.graphic.BoundingBox;
import com.patternity.graphic.Position;
import com.patternity.graphic.Text;
import com.patternity.graphic.dag.Node;
import com.patternity.graphic.layout.Layout;
import com.patternity.util.Named;

/**
 * Renders elements into shapes according to their given positions calculated by
 * the member layout Strategy.
 * 
 * @author cyrille martraire
 */
public class BoxRenderer {

	private final BoundingBox cell;
	private final int fontSize;

	private final Layout layout;
	private final Graphic g = new Graphic();

	public BoxRenderer(Layout layout, BoundingBox cell, int fontSize) {
		this.layout = layout;
		this.cell = cell;
		this.fontSize = fontSize;
	}

	public String getContent() {
		return g.toString();
	}

	public void render(final Node node, Map boxes, BoxStyle boxStyle) {
		if (boxStyle == null) {
			return;
		}
		final Object item = node.getElement();

		// translate for alignment
		final Position pos = layout.position(node);

		final Rectangle boundingBox = print(item, pos, boxStyle);
		boxes.put(item, boundingBox);
	}

	public Rectangle print(final Object item, final Position pos, BoxStyle boxStyle) {
		final Named named = (Named) item;
		final Rectangle boundingBox = boxStyle.evaluate(pos, named, this);
		return boundingBox;
	}

	protected Rectangle printPattern(final Position pos, final Named named) {
		Rectangle boundingBox = new Rectangle(pos, cell);
		final int x = (int) pos.getX();
		final int y = (int) pos.getY();
		final int boxWidth = cell.getWidth();
		final int boxHeight = cell.getHeight();
		g.drawEllipse(x, y, boxWidth / 2, boxHeight / 2, "pattern", "");
		final int cornerY = y - boxHeight / 2;
		final int textHeight = Text.textHeight(fontSize);
		g.drawText(x, cornerY + textHeight, named.getName(), "name");

		return boundingBox;
	}

	protected Rectangle printSet(final Position pos, final Named named) {
		Rectangle boundingBox = new Rectangle(pos, cell);
		g.drawDescription("hidden: " + named.getName());
		boundingBox = new Rectangle(pos, 0, 0);
		return boundingBox;
	}

	protected Rectangle printNote(final Position pos, final Named named) {
		Rectangle boundingBox = new Rectangle(pos, cell);
		final int x = (int) pos.getX();
		final int y = (int) pos.getY();
		final int boxWidth = cell.getWidth();
		final int boxHeight = cell.getHeight();
		final int cornerX = x - boxWidth / 2;
		final int cornerY = y - boxHeight / 2;
		g.drawNote(cornerX, cornerY, boxWidth, boxHeight, 15, "note");
		final int textHeight = Text.textHeight(fontSize);
		g.drawText(cornerX + fontSize, cornerY + textHeight, named.getName(), "message");

		return boundingBox;
	}

	protected Rectangle printType(final Position pos, final Named named) {
		Rectangle boundingBox = new Rectangle(pos, cell);
		final int x = (int) pos.getX();
		final int y = (int) pos.getY();
		final int boxWidth = cell.getWidth();
		final int boxHeight = cell.getHeight();
		final Position corner = pos.add(-boxWidth / 2, -boxHeight / 2);
		g.drawRectangle(corner, boxWidth, boxHeight, "classbox");
		final int cornerY = y - boxHeight / 2;
		final int textHeight = Text.textHeight(fontSize);
		g.drawText(x, cornerY + textHeight, named.getName(), "name");

		return boundingBox;
	}

	public String toString() {
		return "BoxRenderer cell=" + cell + " fontsize=" + fontSize;
	}

}
