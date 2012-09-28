package com.patternity.graphic.svg;

import java.text.MessageFormat;

import com.patternity.graphic.Rectangle;
import com.patternity.graphic.Orientation;
import com.patternity.graphic.Position;
import com.patternity.graphic.Orientation.HorizontalOrientation;

/**
 * Represents a graphic canvas with drawing operations to draw shapes and text
 * on.
 * 
 * @author cyrille martraire
 */
public class Graphic {

	private final StringBuffer out = new StringBuffer();

	public void drawRectangle(final Position pos, int width, int height, String style) {
		drawRectangle((int) pos.getX(), (int) pos.getY(), width, height, style);
	}

	public void drawRectangle(final int x, final int y, int width, int height, String style) {
		drawRectangle(x, y, width, height, style, "");
	}

	public void drawEllipse(final int x, final int y, int rx, int ry, String style, String misc) {
		final String pattern = "<ellipse cx=\"{0}\" cy=\"{1}\" rx=\"{2}\" ry=\"{3}\" {5} class=\"{4}\" />";
		final Object[] objects = new Object[] { Integer.toString(x), Integer.toString(y), Integer.toString(rx),
				Integer.toString(ry), style, misc };
		final String rect = MessageFormat.format(pattern, objects);
		newline();
		out.append(rect);
	}

	public void drawRectangle(final int x, final int y, int width, int height, String style, String misc) {
		final String pattern = "<rect x=\"{0}\" y=\"{1}\" width=\"{2}\" height=\"{3}\" {5} class=\"{4}\" />";
		final Object[] objects = new Object[] { Integer.toString(x), Integer.toString(y), Integer.toString(width),
				Integer.toString(height), style, misc };
		final String rect = MessageFormat.format(pattern, objects);
		newline();
		out.append(rect);
	}

	public void drawLine(final int x, final int y, int x2, int y2, String style) {
		drawLine(x, y, x2, y2, style, "");
	}

	public void drawLine(final int x, final int y, int x2, int y2, String style, String misc) {
		final String pattern = "<line x1=\"{0}\" y1=\"{1}\" x2=\"{2}\" y2=\"{3}\" {5} class=\"{4}\" />";
		final Object[] objects = new Object[] { Integer.toString(x), Integer.toString(y), Integer.toString(x2),
				Integer.toString(y2), style, misc };
		final String rect = MessageFormat.format(pattern, objects);
		newline();
		out.append(rect);
	}

	public void drawLink(final Rectangle fromBox, Rectangle toBox, Orientation hint, Position shifts,
			final String marker, final String style) {
		if (fromBox.getPosition().equals(toBox.getPosition())) {
			// reflexive
			drawLoopPath(fromBox.getPort(Orientation.EAST), shifts, marker, style);
			return;
		}
		final Orientation dir = Orientation.orientation(hint, fromBox.getPosition(), toBox.getPosition());
		final Position from = fromBox.getPort(dir);
		final Position to = toBox.getPort(dir.opposite());

		if (shifts == null || shifts.isZero()) {
			drawPath(from, to, marker, style);
			return;
		}
		if (!shifts.isPositive()) {
			// more work...
			return;
		}

		// negate shifts dimensions when going backward x (e-g WEST) or y
		// (e-g NORTH) axis
		final Position transform = shifts.multiply(dir.coefficients());
		final Position via1 = from.add(transform);
		final Position via2 = dir instanceof HorizontalOrientation ? new Position(via1.getX(), to.getY())
				: new Position(to.getX(), via1.getY());

		final String path = moveTo(from) + " " + lineTo(via1) + " " + lineTo(via2) + " " + lineTo(to);
		drawPath(path, marker, style);
	}

	public void drawLoopPath(final Position pos, Position dimensions, final String marker, final String style) {
		final int dx = (int) dimensions.getX();
		final int dy = (int) dimensions.getY();
		final String path = moveTo(pos.add(0, -dy)) + " h " + dx + " v " + dy + " h -" + dx;
		drawPath(path, marker, style);
	}

	public static String moveTo(final Position position) {
		return "M " + position.getX() + "," + position.getY();
	}

	public static String lineTo(final Position position) {
		return "L " + position.getX() + "," + position.getY();
	}

	public void drawPath(final Position pos1, Position pos2, final String marker, final String style) {
		final String path = moveTo(pos1) + " " + lineTo(pos2);
		drawPath(path, marker, style);
	}

	public void drawPath(final String path, final String marker, final String style) {
		drawPath(path, marker, style, "");
	}

	public void drawPath(final String path, final String marker, final String style, final String misc) {
		final String pattern = "<path d=\"{0}\" marker-end=\"url(#{1})\" {3} class=\"{2}\" />";
		final String link = MessageFormat.format(pattern, new Object[] { path, marker, style, misc });
		newline();
		out.append(link);
	}

	public void drawText(final int x, final int y, String method, final String style) {
		drawText(x, y, method, style, "");
	}

	public void drawText(final int x, final int y, String method, final String style, final String misc) {
		final String textPattern = "<text x=\"{0}\" y=\"{1}\" {4} class=\"{2}\">{3}</text>";
		final String t = MessageFormat.format(textPattern, new Object[] { Integer.toString(x), Integer.toString(y),
				style, method, misc });
		newline();
		out.append(t);
	}

	public void drawNote(int x, int y, int width, int heigth, int corner, String style) {
		final String start = (x + width - corner) + "," + y;
		final String end = (x + width) + "," + (y + corner);
		final String path = start + " " + x + "," + y + " " + x + "," + (y + heigth) + " " + (x + width) + ","
				+ (y + heigth) + " " + end + " " + start + " " + (x + width - corner) + "," + (y + corner) + " " + end;
		final String note = MessageFormat.format("<polygon points=\"{0}\" class=\"{1}\" />",
				new Object[] { path, style });
		newline();
		out.append(note);
	}

	public void drawDescription(final String desc) {
		out.append("\n<desc>");
		out.append(desc);
		out.append("</desc>");
	}

	public void newline() {
		out.append("\n");
	}

	public void println(final String s) {
		newline();
		out.append(s);
	}

	public String toString() {
		return out.toString();
	}

}
