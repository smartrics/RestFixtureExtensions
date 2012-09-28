package com.patternity.graphic.svg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.patternity.graphic.BoundingBox;
import com.patternity.graphic.layout.GridLayout;
import com.patternity.graphic.layout.Layout;

/**
 * Renders a diagram graphically
 * 
 * @author cyrille martraire
 */
public final class DiagramRenderer {

	private final BoxRenderer boxRenderer;
	private final LinkRenderer linkRenderer;
	private final boolean linksFirst;

	public DiagramRenderer(final BoundingBox grid, final double paddingRatio, final Layout layout, final int fontsize,
			final boolean linksFirst) {
		this(new BoxRenderer(new GridLayout(layout, grid), grid.scale(1. - paddingRatio), fontsize),
				new LinkRenderer(), linksFirst);
	}

	public DiagramRenderer(BoxRenderer boxRenderer, LinkRenderer linkRenderer, final boolean linksFirst) {
		this.boxRenderer = boxRenderer;
		this.linkRenderer = linkRenderer;
		this.linksFirst = linksFirst;
	}

	public void render(final Diagram diagram) {
		final Map boxes = new HashMap();
		Iterator it = diagram.getBoxes().iterator();
		while (it.hasNext()) {
			final Box box = (Box) it.next();
			boxRenderer.render(box.getNode(), boxes, (BoxStyle) box.getStyle());
		}

		// for each link, render it
		it = diagram.getLinks().iterator();
		while (it.hasNext()) {
			final Link link = (Link) it.next();
			linkRenderer.render(link.getFrom(), link.getTo(), boxes, (LinkStyle) link.getStyle());
		}
	}

	public String getContent() {
		String nodes = boxRenderer.getContent();
		String links = linkRenderer.getContent();
		final String content = linksFirst ? links + "\n" + nodes : nodes + "\n" + links;
		return content;
	}

	public String toString() {
		return "DiagramRenderer boxRenderer: " + boxRenderer + " linkRenderer: " + linkRenderer;
	}
}
