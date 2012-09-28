package com.patternity.core.metamodel;

/**
 * Represents a reference to some member of a pattern
 * 
 * @author cyrille martraire
 */
public class ElementReference implements Element, Definition {

	private final String rolename;

	public ElementReference(String rolename) {
		this.rolename = rolename;
	}

	public String getRolename() {
		return rolename;
	}

	public Element resolve(PatternOccurrence container) {
		final Element resolved = container.get(rolename);
		if (resolved == null) {
			return null;
		}
		// return dig(resolved);
		return resolved;
	}

	public final static Element dig(final Element member) {
		if (member instanceof PatternOccurrence) {
			final PatternOccurrence compound = (PatternOccurrence) member;
			final Element resolved = ((PatternOccurrence) member).get("head");
			if (resolved == null || resolved.equals(compound)) {
				return resolved;// prevent cycle
			}
			return dig(resolved);
		}
		return member;
	}

	public String toString() {
		return "Ref to " + rolename;
	}

}
