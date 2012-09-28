package com.patternity.graphic.svg;

import com.patternity.graphic.Style;
import com.patternity.graphic.Styled;

/**
 * Represents a link between two boxes, with a style.
 * 
 * @author cyrille martraire
 */
public final class Link implements Styled {

	private final Object from;
	private final Object to;
	private final LinkStyle style;

	public Link(Object from, Object to, LinkStyle style) {
		this.from = from;
		this.to = to;
		this.style = style;
	}

	public Object getFrom() {
		return from;
	}

	public Object getTo() {
		return to;
	}

	public Style getStyle() {
		return style;
	}

	public String toString() {
		return "Link: " + from + " " + to + " " + style;
	}

}
