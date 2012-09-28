package com.patternity.graphic;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Represents a simple text that can be rendered
 * 
 * @author cyrille martraire
 */
public class Text {

	private final String text;

	private final int fontSize;

	private final int xSpace;

	private final int ySpace;

	public Text(String text, int fontsize) {
		this(text, fontsize, 5, true);
	}

	public Text(String text, int fontsize, int spacing, boolean center) {
		this(text, fontsize, spacing, spacing, center);
	}

	public Text(String text, int fontsize, int xSpace, int ySpace, boolean center) {
		this.text = text;
		this.fontSize = fontsize;
		this.xSpace = xSpace;
		this.ySpace = ySpace;
	}

	public int getTextWidth() {
		return stringWidth(text, fontSize);
	}

	public int getWidth() {
		return getTextWidth() + 2 * xSpace;
	}

	public int getHeight() {
		return (int) (fontSize * 1.2 + 2 * ySpace);
	}

	public static int textHeight(final int fontSize) {
		return (int) (fontSize * 1.2 + fontSize * 0.3);
	}

	public static int stringWidth(String text, int fontSize) {
		final double thin = 0.5;
		final double normal = 0.7;
		final double thick = 0.8;

		final String thinchars = "1lij|tI;:";
		double sum = 0;
		for (int i = 0; i < text.length(); i++) {
			final char ch = text.charAt(i);
			if (Character.isUpperCase(ch) || ch == '<' || ch == '>') {
				sum += thick;
			} else if (thinchars.indexOf(ch) != -1) {
				sum += thin;
			} else {
				sum += normal;
			}
		}
		return (int) Math.ceil(sum * fontSize);
	}

	public static String[] wrap(String text, int length) {
		final StringBuffer line = new StringBuffer();
		final List list = new ArrayList();
		final StringTokenizer st = new StringTokenizer(text, " \t\n", true);
		int lineLength = 0;
		while (st.hasMoreElements()) {
			String token = st.nextToken();
			if (lineLength > length) {
				list.add(line.toString());
				lineLength = 0;
				line.setLength(0);
			}
			line.append(token);
			lineLength += token.length();
		}
		list.add(line.toString());
		return (String[]) list.toArray(new String[list.size()]);
	}

	/** Escapes XML text */
	public static String escapeXml(String s) {
		s = s.replaceAll(">", "&gt;");
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll("&", "&amp;");
		s = s.replaceAll("'", "&apos;");
		s = s.replaceAll("\"", "&quot;");
		return s;
	}

	public String toString() {
		return "TextBox text: " + text;
	}

}
