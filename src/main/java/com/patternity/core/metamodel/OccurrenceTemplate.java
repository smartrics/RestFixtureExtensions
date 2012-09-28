package com.patternity.core.metamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.patternity.core.metamodel.Meta.MetaPattern;

/**
 * Represents a template of pattern occurrence: stores one element for each role
 * name, provided the element is an instance of Definition
 * 
 * @author cyrille martraire
 */
public class OccurrenceTemplate implements Element, Definition {

	public static final String OPTIONS = "Options";

	private static final MetaPattern ORDERED_ROLES = new Meta.MetaPattern("OrderedRoles");
	private final Pattern pattern;
	private final Map map = new HashMap();

	public OccurrenceTemplate(Pattern pattern) {
		this.pattern = pattern;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public Element get(Object key) {
		return (Element) map.get(key);
	}

	public Set roleNameSet() {
		return map.keySet();
	}

	public int size() {
		return map.size();
	}

	public Element[] orderedElements() {
		final List list = new ArrayList();
		for (int i = 0;; i++) {
			final Element element = get("" + i);
			if (element == null) {
				break;
			}
			list.add(element);
		}
		return (Element[]) list.toArray(new Element[list.size()]);
	}

	public void addRole(String roleName, Element element) {
		if (element instanceof Definition) {
			map.put(roleName, element);
			return;
		}
		throw new IllegalArgumentException("Expected only definition elements not concrete elements, but received: "
				+ element);
	}

	/**
	 * Convenience method to add a text template
	 */
	public void addTextTemplate(String roleName, String text) {
		addRole(roleName, new TextTemplate(text));
	}

	public boolean isRelationOccurrence() {
		return getPattern() instanceof Relation;
	}

	public boolean isHierarchyOccurrence() {
		return getPattern().equals(PatternOccurrence.HIERARCHY_PATTERN);
	}

	/**
	 * Convenience method to add a template of binary relationship
	 */
	public void addBinaryRelation(String roleName, String sourceRef, Relation relation, String targetRef) {
		final OccurrenceTemplate rel = new OccurrenceTemplate(relation);
		rel.addRole(Role.SOURCE, new ElementReference(sourceRef));
		rel.addRole(Role.TARGET, new ElementReference(targetRef));
		addRole(roleName, rel);
	}

	/**
	 * Convenience method to add a delegation relationship
	 */
	public void addDelegationRelation(String roleName, String sourceRef, String targetRef) {
		addBinaryRelation(roleName, sourceRef, Relation.DELEGATION_RELATION, targetRef);
	}

	/**
	 * Convenience method to add a collaboration relationship
	 */
	public void addCollaborationRelation(String roleName, String sourceRef, String targetRef) {
		addBinaryRelation(roleName, sourceRef, Relation.COLLABORATION_RELATION, targetRef);
	}

	/**
	 * Convenience method to add an inheritance hierarchy from the given root
	 */
	public void addInheritanceHierarchy(String roleName, String rootRef) {
		final OccurrenceTemplate rel = new OccurrenceTemplate(PatternOccurrence.HIERARCHY_PATTERN);
		// rel.addRole(Role.RELATION, Relation.INHERITANCE_RELATION);
		rel.addRole(Role.TARGET, new ElementReference(rootRef));
		addRole(roleName, rel);
	}

	/**
	 * Convenience method to add a meta relationship
	 * 
	 * @param roleName
	 *            ClassDiagram or SequenceDiagram
	 */
	public void addMetaDescription(String roleName, Pattern metaPattern, String[] orderedRoles, String options) {
		if (!(metaPattern instanceof Meta)) {
			throw new IllegalArgumentException("The pattern must be a meta pattern");
		}
		final OccurrenceTemplate rel = new OccurrenceTemplate(metaPattern);
		rel.addRole(OPTIONS, new TextTemplate(options));
		for (int i = 0; i < orderedRoles.length; i++) {
			rel.addRole("" + i, new ElementReference(orderedRoles[i]));
		}
		addRole(roleName, rel);
	}

	public void addStructureFlow(String[] orderedRoles, String options) {
		addMetaDescription("StructureFlow", ORDERED_ROLES, orderedRoles, options);
	}

	public void addDynamics(String[] orderedRoles, String options) {
		addMetaDescription("Dynamics", ORDERED_ROLES, orderedRoles, options);
	}

	/**
	 * @return The Element instance, also a Definition instance
	 */
	public Element get(String roleName) {
		return (Element) map.get(roleName);
	}

	public String toString() {
		return "OccurrenceTemplate " + getPattern();
	}
}
