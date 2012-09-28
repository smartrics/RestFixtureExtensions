package com.patternity.core.metamodel;

/**
 * Represents a textual element (comment, note, label, source code snippet...)
 * that contains variables in a format (e.g. $roleName$) that can be evaluated
 * later against the metamodel.
 * 
 * @author cyrille martraire
 */
public class TextTemplate implements Element, Definition {

	private final String text;

	public TextTemplate(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public String toString() {
		return "TextTemplate: " + getText();
	}
}
