package com.patternity.core.metamodel;

import com.patternity.util.Named;

/**
 * Represents a role played by an element within a bigger element or occurrence.
 * 
 * @author cyrille martraire
 * 
 * @deprecated
 */
public class Role implements Named {

	/**
	 * pattern NullObject for the role name
	 */
	private final static String UNNAMED = "?";

	public final static String SOURCE = "Source";
	public final static String TARGET = "Target";

	public final static String ELEMENT = "Element";
	public final static String RELATION = "Relation";

	private final String name;

	private final Element element;

	public Role(Element element) {
		this(null, element);
	}

	public Role(String name, Element element) {
		this.name = name == null ? UNNAMED : name;
		if (element == null) {
			throw new IllegalArgumentException("Element cannot be null in role " + this.name);
		}
		this.element = element;
	}

	public String getName() {
		return name;
	}

	public Element getElement() {
		return element;
	}

	public boolean isUnnamed() {
		return name == UNNAMED;// pointer equality
	}

	public boolean sharesNameWith(Named named) {
		return named != null && named.getName().equalsIgnoreCase(name);
	}

	/**
	 * @return true if this Role is equal to the given Role
	 */
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Role)) {
			return false;
		}
		final Role other = (Role) arg0;
		if (this == other) {
			return true;
		}
		return sharesNameWith(other) && other.element.equals(element);
	}

	public int hashCode() {
		return name.hashCode() ^ element.hashCode();
	}

	public String toString() {
		return "Role " + getName() + " => " + getElement();
	}
}
