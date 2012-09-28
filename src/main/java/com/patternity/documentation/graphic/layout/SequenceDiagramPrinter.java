package com.patternity.documentation.graphic.layout;

import java.io.File;
import java.util.Iterator;

import com.patternity.core.metamodel.Element;
import com.patternity.core.metamodel.Method;
import com.patternity.core.metamodel.Note;
import com.patternity.core.metamodel.OccurrenceTemplate;
import com.patternity.core.metamodel.PatternOccurrence;
import com.patternity.core.metamodel.Relation;
import com.patternity.core.metamodel.Role;
import com.patternity.core.metamodel.Type;
import com.patternity.graphic.behavioral.Agent;
import com.patternity.graphic.behavioral.Message;
import com.patternity.graphic.dag.Node;
import com.patternity.graphic.layout.sequence.SequenceLayout;
import com.patternity.util.TemplatedWriter;

/**
 * Prints a sequence diagram from a given LayeredOccurrence
 * 
 * @author cyrille martraire
 */
public class SequenceDiagramPrinter extends DiagramPrinter {

	public static void printDiagram(Element[] orderedElements, OccurrenceTemplate shallow, PatternOccurrence deep) {
		final Node root = new Node(new Message(null, null));

		for (int i = 0; i < orderedElements.length; i++) {
			final Element rawElement = orderedElements[i];

			foreachElement(rawElement, shallow, deep, root);
		}

		final SequenceLayout layout = new SequenceLayout(16);
		final String s = layout.layout(root);

		final String patternName = deep.getPattern().getName();
		final TemplatedWriter writer = new TemplatedWriter(new File(patternName + "_sequence-diagram.svg"),
				new File("template.svg"));
		writer.write(s, "viewBox=\"0 0 1000 1000\"");
	}

	private static void foreachElement(final Element rawElement, OccurrenceTemplate shallow, PatternOccurrence deep,
			final Node root) {
		final Element element = evaluateDefinition(rawElement, shallow, deep);

		if (element instanceof Note) {
			final Note note = (Note) element;
			final Node node = new Node(new com.patternity.graphic.behavioral.Note(note.getText()));
			root.add(node);
			return;
		}

		if (element instanceof PatternOccurrence) {
			final PatternOccurrence occ = (PatternOccurrence) element;
			if (!(occ.getPattern() instanceof Relation)) {
				return;
			}

			// this code is equivalent to a loop unrolled
			final Element source = occ.get(Role.SOURCE);
			Node current = root;
			if (source != null) {
				final Node node = new Node(message(source));
				root.add(node);
				current = node;
			}

			final Element target = occ.get(Role.TARGET);
			Iterator it = strip(target).iterator();
			while (it.hasNext()) {
				final Element eachTarget = (Element) it.next();
				final Node node = new Node(message(eachTarget));
				current.add(node);
			}
		}
	}

	public static Message message(Element element) {
		if (element instanceof PatternOccurrence) {
			// final PatternOccurrence occurrence = (PatternOccurrence) element;
			// final Element member = occurrence.get(Role.ELEMENT);
			// return message(member);
			return null;
		}
		if (element instanceof Method) {
			return message((Method) element);
		}
		if (element instanceof Type) {
			final Method method = new Method();
			method.setType((Type) element);
			return message(method);
		}
		return null;
	}

	public static Message message(Method method) {
		final Type type = method.getType();
		return new Message(new Agent(type.getName(), instanceName(type)), method.isUnknown() ? "..." : method
				.shortSignature());
	}

	public static String instanceName(Type type) {
		return type == null ? "..." : "";
	}
}
