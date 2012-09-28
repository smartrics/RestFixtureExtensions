package com.patternity.graphic.svg;

import java.util.Map;

import com.patternity.graphic.Rectangle;
import com.patternity.graphic.dag.Node;

/**
 * Renders links between elements
 * 
 * @author cyrille martraire
 */
public class LinkRenderer {

	private final Graphic g = new Graphic();

	private Rectangle box(final Object element, Map boxes) {
		final Rectangle box = (Rectangle) boxes.get(element);
		return box;
	}

	/**
	 * @deprecated
	 */
	public void render(Node child, Map boxes, LinkStyle style) {
		if (child.isRoot()) {
			return;
		}
		final Node from = child;
		final Node to = from.getParent();
		render(from, to, boxes, style);

	}

	/**
	 * @deprecated
	 */
	public Rectangle render(final Node from, final Node to, Map boxes, LinkStyle style) {
		final Object item = from.getElement();
		final Object parent = to.getElement();
		return render(item, parent, boxes, style);
	}

	public Rectangle render(final Object item, final Object parent, Map boxes, LinkStyle style) {
		final Rectangle itemBox = box(item, boxes);
		final Rectangle parentBox = box(parent, boxes);

		if (itemBox == null || parentBox == null) {
			return null;
		}
		g.drawLink(itemBox, parentBox, style.getOrientation(), style.getShifts(), style.getMarker(), style.getStyle());
		return null;
	}

	public String getContent() {
		return g.toString();
	}

	public String toString() {
		return "LinkRenderer";
	}

}
