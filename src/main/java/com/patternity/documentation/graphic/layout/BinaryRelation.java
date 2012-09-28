package com.patternity.documentation.graphic.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.patternity.core.metamodel.Element;

/**
 * Represents a relation between two elements (pattern, type, method), such as
 * Inheritance, Delegation, Allocation etc.
 * 
 * @author cyrille martraire
 */
public class BinaryRelation {

	private final String label;
	private final Element source;
	private final Element target;
	private final String kind;

	public BinaryRelation(String kind, Element source, Element target) {
		this("", kind, source, target);
	}

	public BinaryRelation(String label, String kind, Element source, Element target) {
		this.kind = kind;
		this.label = label;
		this.source = source;
		this.target = target;
	}

	public Collection<Element> allElements() {
		final List<Element> list = new ArrayList<Element>();
		list.add(source);
		list.add(target);
		return list;
	}

	public String getKind() {
		return kind;
	}

	public String getLabel() {
		return label;
	}

	public Element getSource() {
		return source;
	}

	public Element getTarget() {
		return target;
	}

	public String toString() {
		return "BinaryRelation " + getLabel() + " source=" + source + " target=" + target + " kind=" + getKind();
	}

}
