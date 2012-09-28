package com.patternity.core.metamodel;

import java.util.StringTokenizer;

import com.patternity.util.Named;

/**
 * Represents a textual element (comment, note, label, source code snippet...)
 * 
 * @author cyrille martraire
 */
public class Note implements Element, Native, Named {

	private final String text;

	public Note(String text) {
		this.text = text;
	}

	/**
	 * @return The full text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return The first line of text before carriage return
	 */
	public String getLabel() {
		if (text == null) {
			return null;
		}
		final StringTokenizer st = new StringTokenizer(text, "\n");
		if (st.hasMoreTokens()) {
			return st.nextToken();// 1st line before carriage return
		}
		return text;
	}

	public String getName() {
		return getLabel();
	}

	public String toString() {
		return "Note: " + getText();
	}
}
