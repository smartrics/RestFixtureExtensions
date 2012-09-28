package com.patternity.graphic.svg;

import com.patternity.graphic.Orientation;
import com.patternity.graphic.Position;
import com.patternity.graphic.Style;

/**
 * Represents the style of links, including every display attribute
 * 
 * @author cyrille martraire
 * @pattern Immutable
 */
public class LinkStyle implements Style {

	private final boolean reverse;

	private final String marker;

	private final String style;

	private final Orientation orientation;

	private final Position shifts;

	public static final Position SHIFTS = new Position(12, 12);

	public final static LinkStyle GENERALIZATION = new LinkStyle(false, "arrow", "emptyHead", SHIFTS, Orientation.NORTH);
	public final static LinkStyle SPECIALIZATION = new LinkStyle(true, "arrow", "emptyHead", SHIFTS, Orientation.NORTH);

	public final static LinkStyle ALLOCATION = new LinkStyle(false, "dasharrow", "openHead", null, null);
	public final static LinkStyle REVERSE_ALLOCATION = new LinkStyle(true, "dasharrow", "openHead", null, null);

	public final static LinkStyle DELEGATION = new LinkStyle(false, "arrow", "openHead", null, null);
	public final static LinkStyle DELEGATION_MANHATTAN = new LinkStyle(false, "arrow", "openHead", SHIFTS,
			Orientation.NORTH);
	public final static LinkStyle REVERSE_DELEGATION = new LinkStyle(true, "arrow", "openHead", null, Orientation.NORTH);

	public final static LinkStyle COLLABORATION = new LinkStyle(false, "dasharrow", "none", null, Orientation.NORTH);
	public final static LinkStyle RELATION = new LinkStyle(false, "dasharrow", "none", null, Orientation.NORTH);

	public LinkStyle(boolean reverse, String style, String marker, Position shifts, Orientation orientation) {
		this.reverse = reverse;
		this.style = style;
		this.marker = marker;
		this.shifts = shifts;
		this.orientation = orientation;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public boolean isReverse() {
		return reverse;
	}

	public String getMarker() {
		return marker;
	}

	public String getStyle() {
		return style;
	}

	public Position getShifts() {
		return shifts;
	}

	public boolean isManhattan() {
		return shifts != null;
	}

	public LinkStyle toManhattan(Position newShifts) {
		return new LinkStyle(reverse, style, marker, newShifts, orientation);
	}

	public String toString() {
		return "LinkStyle " + (reverse ? "reverse" : "") + " " + getStyle() + " " + getMarker() + " "
				+ getOrientation() + " " + getShifts();
	}
}