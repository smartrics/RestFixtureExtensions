package com.patternity.core.metamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;

import com.patternity.util.Named;

/**
 * Represents any occurrence of a pattern.
 * 
 * @author cyrille martraire
 */
public class PatternOccurrence implements Element, Named {

	private final Pattern pattern;

	/** Map (String, Collection(Role)) */
	private final MultiMap members = new MultiHashMap();

	public final static Pattern SET_PATTERN = new Pattern() {

		public String getName() {
			return "Set";
		}

		public String toString() {
			return getName();
		}

	};

	public final static Pattern HIERARCHY_PATTERN = new Pattern() {

		public String getName() {
			return "Hierarchy";
		}

		public String toString() {
			return getName();
		}

	};

	/**
	 * @return A PatternOccurrence used as a set of Role instances built from a
	 *         collection of Role instances or Element instances
	 */
	private final static Element toElement(Collection elements, final boolean forceSet) {
		if (forceSet) {
			return newSetOccurrence(elements);
		}
		if (elements == null || elements.isEmpty()) {
			return null;
		}
		if (elements.size() == 1) {
			return (Element) elements.iterator().next();
		}
		return newSetOccurrence(elements);
	}

	/**
	 * @pattern StaticCreator
	 * 
	 * @return A PatternOccurrence for the hierarchy pattern
	 */
	public final static PatternOccurrence newHierarchyOccurrence() {
		return new PatternOccurrence(HIERARCHY_PATTERN);
	}

	/**
	 * @pattern StaticCreator
	 * 
	 * @return A PatternOccurrence used as a set of Role instances built from a
	 *         collection of Role instances or Element instances
	 */
	public final static PatternOccurrence newSetOccurrence(Collection elements) {
		if (elements == null) {
			return null;
		}
		final PatternOccurrence occ = new PatternOccurrence(SET_PATTERN);
		Iterator it = elements.iterator();
		while (it.hasNext()) {
			final Object next = it.next();
			if (next instanceof Element) {
				final Element element = (Element) next;
				occ.add(Role.ELEMENT, element);
				// TODO occ.add(occ, Relation.COLLABORATION_RELATION, element);
			} else if (next instanceof Role) {
				final Role role = (Role) next;
				occ.add(role);
			}
		}
		return occ;
	}

	public boolean isRelationOccurrence() {
		return getPattern() instanceof Relation;
	}

	public boolean isSetOccurrence() {
		return getPattern().equals(SET_PATTERN);
	}

	public boolean isHierarchyOccurrence() {
		return getPattern().equals(HIERARCHY_PATTERN);
	}

	public PatternOccurrence(Pattern pattern) {
		this.pattern = pattern;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public String getName() {
		return pattern.getName();
	}

	public boolean isOccurrenceOf(Pattern pattern) {
		return this.pattern.equals(pattern);
	}

	/**
	 * Convenience adder to add an element in the role UNNAMED
	 */
	public final void add(Element element) {
		add(new Role(element));
	}

	/**
	 * Convenience adder to add an element in its role
	 */
	public final void add(String roleName, Element element) {
		add(new Role(roleName, element));
	}

	/**
	 * Convenience adder for hierarchies to add the element and its relationship
	 * to its parent element at the same time, using default roles names
	 * Role.ELEMENT and Role.RELATION.
	 */
	public void add(Element element, Relation kind, Element parent) {
		add(Role.ELEMENT, element, Role.RELATION, kind, parent);
	}

	/**
	 * Convenience adder for hierarchies to add the element and its relationship
	 * to its parent element at the same time
	 */
	public void add(String roleName, Element element, String relationRoleName, Relation kind, Element parent) {
		add(roleName, element);
		if (parent != null && element != null && kind != null) {
			final PatternOccurrence relationship = kind.newOccurrence(element, parent);
			add(relationRoleName, relationship);
		}
	}

	/**
	 * Nominal adder to add a role (role name, element)
	 */
	public final void add(Role named) {
		members.put(named.getName(), named);
	}

	public final void remove(Role named) {
		members.remove(named.getName(), named);
	}

	/**
	 * @return A List of Role instances flattened (multiple elements for one
	 *         same role name are side by side instead of being grouped in a Set
	 *         occurrence)
	 */
	public List allFlatRoles() {
		return new ArrayList(members.values());
	}

	/**
	 * @return A List of Role instances, such as each role name has at most one
	 *         Element (that may be a Set occurrence when multiple elements
	 *         exist for the same role name)
	 */
	public List allRoles() {
		final ArrayList list = new ArrayList();
		Iterator it = members.entrySet().iterator();
		while (it.hasNext()) {
			final Entry entry = (Entry) it.next();
			final String roleName = (String) entry.getKey();
			final Collection roles = (Collection) entry.getValue();
			Element element = null;
			if (roles == null || roles.isEmpty()) {
				//
			} else if (roles.size() == 1) {
				final Role role = (Role) roles.iterator().next();
				element = (Element) role.getElement();
			} else {
				element = newSetOccurrence(toElements(roles));
			}
			list.add(new Role(roleName, element));
		}
		return list;
	}

	/**
	 * @return A List of Element instances
	 */
	public List allElements() {
		return toElements(members.values());
	}

	/**
	 * Converts a collection of roles into a collection of their element
	 */
	public static final List toElements(Collection roles) {
		if (roles == null) {
			return null;
		}
		final List list = new ArrayList();
		final Iterator it = roles.iterator();
		while (it.hasNext()) {
			final Object next = it.next();
			final Role role = (Role) next;
			list.add(role.getElement());
		}
		return list;
	}

	/**
	 * A Collection of Role instances
	 */
	public Collection getRoles(String name) {
		final Collection collection = (Collection) members.get(name);
		return collection == null ? null : Collections.unmodifiableCollection(collection);
	}

	/**
	 * A Collection of Element instances
	 */
	public Collection getCollection(String name) {
		if ("head".equalsIgnoreCase(name)) {
			// TODO make it clean
			return Collections.singleton(get("abstraction"));
		}
		return toElements((Collection) members.get(name));

	}

	/**
	 * A Collection of Role instances
	 */
	public Element get(String name) {
		final Collection elements = getCollection(name);
		return toElement(elements, false);
	}

	public int numberOf(String name) {
		final Collection collection = (Collection) members.get(name);
		return collection == null ? 0 : collection.size();
	}

	public Set getNameSet() {
		return new HashSet(members.keySet());
	}

	public String toString() {
		return "PatternOccurrence:" + getPattern().getName() + ": " + members.size() + " roles /" + members.size()
				+ " keys";
	}

	public final static String describe(Element element) {
		final StringBuffer buffer = new StringBuffer();
		describe("  ", buffer, element);
		return buffer.toString();
	}

	protected final static void describe(String indent, StringBuffer buffer, Element element) {
		buffer.append("\n");
		buffer.append(indent);
		buffer.append(element.toString());
		if (!(element instanceof PatternOccurrence)) {
			return;
		}
		final PatternOccurrence occ = (PatternOccurrence) element;
		if (occ.isRelationOccurrence()) {
			return;
		}
		Iterator it = occ.allElements().iterator();
		while (it.hasNext()) {
			// final String rolename = (String) it.next();
			final Element member = (Element) it.next();
			describe(indent + "  ", buffer, member);
			// buffer.append(" (" + rolename + ")");
		}
	}

}
