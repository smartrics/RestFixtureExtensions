package com.patternity.documentation.graphic.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;

import com.patternity.core.metamodel.Element;
import com.patternity.core.metamodel.Item;
import com.patternity.core.metamodel.PatternOccurrence;
import com.patternity.core.metamodel.Relation;
import com.patternity.core.metamodel.Role;
import com.patternity.graphic.dag.BreadthFirstIterator;
import com.patternity.graphic.dag.DepthFirstIterator;
import com.patternity.graphic.dag.Node;
import com.patternity.graphic.dag.NodeBuilder;

/**
 * Represents a hierarchy of elements. It can also be seen as a composite of
 * binary relations.
 * 
 * @author cyrille martraire
 */
public class Hierarchy {

	private final NodeBuilder builder;
	private final String name;

	/**
	 * List (CompoundOccurrence)
	 */
	private final List relations = new ArrayList();

	public Hierarchy() {
		this(null);
	}

	public Hierarchy(String name) {
		this.name = name;
		builder = new NodeBuilder();
	}

	public String getName() {
		return name;
	}

	public Node getRootNode() {
		return builder.getRootNode();
	}

	public void add(Element parent, Element child) {
		add(parent, child, Relation.INHERITANCE_RELATION.getKind());
	}

	public void add(Element parent, Element child, String kind) {
		builder.add(parent, child);
		if (parent != null && child != null) {
			if (kind.equalsIgnoreCase(Relation.INHERITANCE)) {
				addRelation(new BinaryRelation(kind, parent, child));
			} else {
				addRelation(new BinaryRelation(kind, child, parent));
			}
		}
	}

	public void addRelation(BinaryRelation relation) {
		relations.add(relation);
	}

	private final static BinaryRelation toBinaryRelation(Element element) {
		if (element instanceof PatternOccurrence) {
			final PatternOccurrence occurrence = (PatternOccurrence) element;
			if (!occurrence.isRelationOccurrence()) {
				return null;
			}
			final Relation relation = (Relation) occurrence.getPattern();
			final String kind = relation.getKind();
			final Element source = occurrence.get(Role.SOURCE);
			final Element target = occurrence.get(Role.TARGET);
			if (source == null || target == null) {
				return null;
			}
			// swap source and target here...
			return new BinaryRelation(kind, target, source);
		}
		return null;
	}

	/**
	 * Add a root element as a collaboration
	 */
	public void addElement(Element element) {
		addElement(element, null);
	}

	/**
	 * Add an element as a collaboration
	 */
	public void addElement(Element element, Element parent) {
		addElement(element, Relation.COLLABORATION_RELATION.getKind(), parent);
	}

	/**
	 * Add an element as a relation of the given kind
	 */
	public void addElement(Element element, String kind, Element parent) {
		addElement(element, kind, parent, false);
	}

	/**
	 * Main add method
	 */
	public void addElement(Element element, String kind, Element parent, boolean transparent) {
		final BinaryRelation binaryRelation = toBinaryRelation(element);
		if (binaryRelation != null) {
			addRelation(binaryRelation);
			return;
		}

		if (element instanceof Item) {
			final Item item = (Item) element;
			add(parent, item, kind);
			return;
		}
		if (element instanceof PatternOccurrence) {
			final PatternOccurrence occurrence = (PatternOccurrence) element;

			if (occurrence.isHierarchyOccurrence()) {
				final Hierarchy hierarchy = newHierarchy(occurrence);
				addHierarchy(parent, hierarchy, Relation.COLLABORATION_RELATION.getKind());
			} else {
				addCompound(occurrence, kind, parent, transparent);
			}
		}
	}

	/**
	 * Converts a PatternOccurrence for pattern hierarchy into a Hierarchy
	 * object
	 */
	private Hierarchy newHierarchy(PatternOccurrence occurrence) {
		final Hierarchy hierarchy = new Hierarchy();

		// addAllElements(occurrence.getCollection(Role.ELEMENT));

		Iterator it = occurrence.getCollection(Role.RELATION).iterator();
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			final BinaryRelation binaryRelation = toBinaryRelation(element);
			if (binaryRelation != null) {
				hierarchy.add(binaryRelation.getSource(), binaryRelation.getTarget(), binaryRelation.getKind());
			}
		}
		return hierarchy;
	}

	/**
	 * Add a compound element as a sub-node in this hierarchy
	 */
	public void addCompound(PatternOccurrence occurrence, String kind, Element parent, boolean transparent) {
		if (!transparent) {
			add(parent, occurrence, Relation.COLLABORATION_RELATION.getKind());
		}
		final Iterator it = occurrence.allRoles().iterator();
		while (it.hasNext()) {
			final Role role = (Role) it.next();
			final Element element = role.getElement();
			if (!transparent) {
				addElement(element, Relation.COLLABORATION_RELATION.getKind(), occurrence, false);
			} else {
				addElement(element, kind, parent, transparent);
			}
		}
	}

	/**
	 * Add all elements correctly (does the internal dispatch between relations
	 * and other elements)
	 * 
	 * @see #addElement(Element)
	 */
	public void addAllElements(Collection elements) {
		final Iterator it = elements.iterator();
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			addElement(element);
		}
	}

	/**
	 * Special case to add a full hierarchy as a sub-tree in this hierarchy
	 */
	public void addHierarchy(Element parent, Hierarchy hierarchy, String kind) {
		final Iterator it = hierarchy.dfsIterator();
		while (it.hasNext()) {
			final Node node = (Node) it.next();
			if (node.isRoot()) {
				final Element root = (Element) node.getElement();
				builder.add(parent, root);
				addRelation(new BinaryRelation(kind, root, parent));
			} else {
				builder.add((Element) node.getParent().getElement(), (Element) node.getElement());
			}
		}
		addAllRelations(hierarchy.getRelations());
	}

	private void addAllRelations(Collection relations) {
		final Iterator it = relations.iterator();
		while (it.hasNext()) {
			final BinaryRelation relation = (BinaryRelation) it.next();
			addRelation(relation);
		}
	}

	public Element getRootElement() {
		return (Element) getRootNode().getElement();
	}

	/**
	 * @return A Collection of CompoundOccurrence instances that represent every
	 *         exact relation between items
	 */
	public Collection getRelations() {
		return relations;
	}

	/**
	 * @return An Iterator on Node instances
	 */
	public Iterator dfsIterator() {
		return new DepthFirstIterator(getRootNode());
	}

	/**
	 * @return An Iterator on Node instances
	 */
	public Iterator leaves() {
		return new FilterIterator(dfsIterator(), new Predicate() {

			public boolean evaluate(Object arg0) {
				Node node = (Node) arg0;
				return node.isLeaf();
			}

		});
	}

	/**
	 * @return An Iterator on Node instances
	 */
	public Iterator bfsIterator() {
		return new BreadthFirstIterator(getRootNode());
	}

	public String toString() {
		return "Hierarchy root=" + getRootElement();
	}

}
