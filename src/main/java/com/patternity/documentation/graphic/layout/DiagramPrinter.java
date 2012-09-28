package com.patternity.documentation.graphic.layout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.patternity.core.metamodel.Definition;
import com.patternity.core.metamodel.Element;
import com.patternity.core.metamodel.ElementReference;
import com.patternity.core.metamodel.LayeredOccurrence;
import com.patternity.core.metamodel.Native;
import com.patternity.core.metamodel.Note;
import com.patternity.core.metamodel.OccurrenceTemplate;
import com.patternity.core.metamodel.PatternOccurrence;
import com.patternity.core.metamodel.Relation;
import com.patternity.core.metamodel.Role;
import com.patternity.core.metamodel.TextTemplate;
import com.patternity.core.metamodel.Type;
import com.patternity.graphic.dag.Node;
import com.patternity.util.Named;
import com.patternity.util.SimpleTemplate;

/**
 * Prints a sequence diagram from a given LayeredOccurrence
 * 
 * @author cyrille martraire
 * 
 */
public class DiagramPrinter {

	// TODO REMOVE ASAP!!!!
	public static Map subTypesHorribleHack;

	/**
	 * The meta layer drives the process (print diagram...)
	 * 
	 * @param occurrence
	 *            The occurrence to process
	 */
	public static void process(File baseDir, LayeredOccurrence occurrence) {
		final OccurrenceTemplate metaLayer = occurrence.metaLayer();
		Iterator it = metaLayer.roleNameSet().iterator();
		while (it.hasNext()) {
			final String roleName = (String) it.next();
			final Element element = metaLayer.get(roleName);
			if (element instanceof OccurrenceTemplate) {
				final OccurrenceTemplate template = (OccurrenceTemplate) element;
				printDiagram(baseDir, template, occurrence);
			}
		}
	}

	private static void printDiagram(File baseDir, OccurrenceTemplate flowLayer, LayeredOccurrence occurrence) {
		final TextTemplate optionsTemplate = (TextTemplate) flowLayer.get(OccurrenceTemplate.OPTIONS);
		final String options = optionsTemplate.getText();

		final Element[] orderedElements = flowLayer.orderedElements();

		final OccurrenceTemplate shallow = occurrence.shallowLayer();
		final PatternOccurrence deep = occurrence.deepLayer();

		if (options.startsWith("class-diagram")) {
			new ClassDiagramPrinter(baseDir).printDiagram(orderedElements, shallow, deep);
		} else if (options.startsWith("sequence-diagram")) {
			SequenceDiagramPrinter.printDiagram(orderedElements, shallow, deep);
		}
	}

	/**
	 * Given any Element, strip it if it is a set
	 * 
	 * @return A List of 1 or more Element instance(s)
	 */
	public static List strip(Element element) {
		if (element instanceof PatternOccurrence) {
			final PatternOccurrence occ = (PatternOccurrence) element;
			if (occ.isSetOccurrence()) {
				return occ.allElements();
			}
			if (occ.isHierarchyOccurrence()) {
				return new ArrayList(occ.getCollection("Top"));
			}
		}
		return Collections.singletonList(element);
	}

	/**
	 * Given a List of Element instances, simplify it to be the simplest Element
	 * possible
	 * 
	 * @return An Element (may be composite)
	 */
	public static Element join(List list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() == 1) {
			return (Element) list.get(0);
		}
		return PatternOccurrence.newSetOccurrence(list);
	}

	public static Element evaluateDefinition(Element rawElement, OccurrenceTemplate shallow, PatternOccurrence deep) {
		if (!(rawElement instanceof Definition)) {
			return rawElement;
		}
		if (rawElement instanceof OccurrenceTemplate) {
			final OccurrenceTemplate template = (OccurrenceTemplate) rawElement;
			if (template.isHierarchyOccurrence()) {
				return evaluateHierarchy(template, deep, shallow);
			}
			return evaluateTemplate(template, deep, shallow);
		}
		if (rawElement instanceof ElementReference) {
			final ElementReference ref = (ElementReference) rawElement;
			return evaluateReference(ref, deep, shallow);
		}
		if (rawElement instanceof TextTemplate) {
			final TextTemplate text = (TextTemplate) rawElement;
			return evaluateText(text, deep, shallow);
		}
		throw new IllegalArgumentException("Could not resolve: " + rawElement);
	}

	private static Element evaluateHierarchy(OccurrenceTemplate template, PatternOccurrence deep,
			OccurrenceTemplate shallow) {
		final Element rawTopElement = template.get(Role.TARGET);
		final Element superType = evaluateDefinition(rawTopElement, shallow, deep);

		Iterator it = strip(superType).iterator();
		final List list = new ArrayList();
		while (it.hasNext()) {
			final Element eachTarget = (Element) it.next();
			if (eachTarget == null || !(eachTarget instanceof Type)) {
				throw new IllegalArgumentException("Could not resolve Reference to top of Hierarchy: " + template);
			}
			final Element hierarchyOccurrence = toHierarchyOccurrence(eachTarget);
			list.add(hierarchyOccurrence);
		}

		return join(list);
	}

	// TODO NEED FULL REWORK
	protected static Element toHierarchyOccurrence(final Element superType) {
		// TODO Move this code away to pluggable transformer and make it really
		// recursive, so far it is just stub
		final PatternOccurrence hierarchy = PatternOccurrence.newHierarchyOccurrence();
		hierarchy.add(superType, Relation.INHERITANCE_RELATION, null);
		hierarchy.add("Top", superType);
		final Type[] subTypes = (Type[]) subTypesHorribleHack.get(superType);
		for (int i = 0; i < subTypes.length; i++) {
			hierarchy.add(superType, Relation.INHERITANCE_RELATION, subTypes[i]);
		}
		return hierarchy;
	}

	private static Element evaluateText(final TextTemplate text, final PatternOccurrence deep,
			final OccurrenceTemplate shallow) {
		final SimpleTemplate template = new SimpleTemplate(null) {

			protected String evaluate(String variable) {
				final String rolename = variable;
				Element resolved = deep.get(rolename);
				if (resolved != null) {
					return print(resolved);
				}
				resolved = shallow == null ? null : shallow.get(rolename);
				if (resolved != null) {
					return print(resolved);
				}
				return variable;
			}

			private final String print(Element element) {
				if (element instanceof Note) {
					Note note = (Note) element;
					return note.getText();
				}
				if (element instanceof Named) {
					Named named = (Named) element;
					return named.getName();
				}
				return null;
			}
		};
		final String evaluatedText = template.process(text.getText());
		return new Note(evaluatedText);
	}

	private static PatternOccurrence evaluateTemplate(OccurrenceTemplate template, PatternOccurrence deep,
			OccurrenceTemplate shallow) {
		Iterator it = template.roleNameSet().iterator();
		final PatternOccurrence occurrence = new PatternOccurrence(template.getPattern());
		while (it.hasNext()) {
			final String roleName = (String) it.next();
			final Element rawElement = template.get(roleName);

			final Element resolved = evaluateDefinition(rawElement, shallow, deep);
			if (resolved != null) {
				occurrence.add(roleName, resolved);
			}
		}
		return occurrence;
	}

	private static Element evaluateReference(final ElementReference ref, PatternOccurrence deep,
			OccurrenceTemplate shallow) {
		final String rolename = ref.getRolename();
		if (rolename.equalsIgnoreCase("Self")) {
			return deep;
		}
		Element resolved = deep.get(rolename);
		if (resolved != null) {
			return resolved;
		}
		final Element rawShallowElement = shallow == null ? null : shallow.get(rolename);
		resolved = evaluateDefinition(rawShallowElement, shallow, deep);
		if (resolved != null) {
			return resolved;
		}
		throw new IllegalArgumentException("Could not resolve Reference: " + rolename);
	}

}
