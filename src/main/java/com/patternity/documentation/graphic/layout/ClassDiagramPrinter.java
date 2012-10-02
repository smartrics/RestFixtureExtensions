package com.patternity.documentation.graphic.layout;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.patternity.core.metamodel.Element;
import com.patternity.core.metamodel.Note;
import com.patternity.core.metamodel.OccurrenceTemplate;
import com.patternity.core.metamodel.Pattern;
import com.patternity.core.metamodel.PatternOccurrence;
import com.patternity.core.metamodel.Relation;
import com.patternity.core.metamodel.Role;
import com.patternity.core.metamodel.Type;
import com.patternity.graphic.BoundingBox;
import com.patternity.graphic.Position;
import com.patternity.graphic.dag.Node;
import com.patternity.graphic.layout.GridLayout;
import com.patternity.graphic.layout.HierarchyLayout;
import com.patternity.graphic.svg.BoxRenderer;
import com.patternity.graphic.svg.BoxStyle;
import com.patternity.graphic.svg.LinkRenderer;
import com.patternity.graphic.svg.LinkStyle;
import com.patternity.util.FileUtils;
import com.patternity.util.TemplatedWriter;

/**
 * Prints a diagram from a given LayeredOccurrence
 * 
 * @author cyrille martraire
 */
public class ClassDiagramPrinter extends DiagramPrinter {

	private final BoundingBox grid = new BoundingBox(140, 120);
	private final BoundingBox cell = new BoundingBox(100, 80);
	private final GridLayout gridLayout = GridLayout.gridLR(new HierarchyLayout(), grid, Position.ZERO);
	private final BoxRenderer renderer = new BoxRenderer(gridLayout, cell, 16);
	private final LinkRenderer linkRenderer = new LinkRenderer();

	private final Map boxes = new HashMap();
	private final StringBuffer buffer = new StringBuffer();

	private int depth = 0;
	private File baseDir;

	public ClassDiagramPrinter(File baseDir) {
		this.baseDir = baseDir;
		FileUtils.makeDirs(baseDir);
	}

	public void printDiagram(Element[] orderedElements, OccurrenceTemplate shallow, PatternOccurrence deep) {
		final String diagram = "class-diagram";

		final Hierarchy printTree = new Hierarchy();
		processElements(orderedElements, shallow, deep, printTree);
		System.out.println("***");
		System.out.println(printTree.getRootNode());
		System.out.println(printTree.getRelations());

		// actual print
		print(printTree);

		final String s = buffer.toString();

		final String patternName = deep.getPattern().getName();
		final TemplatedWriter writer = new TemplatedWriter(new File(this.baseDir, patternName + "_" + diagram + ".svg"),
				new File("template.svg"));
		writer.write(s, "viewBox=\"0 0 1000 1000\"");
	}

	/**
	 * Resolves definitions
	 */
	private void processElements(Element[] orderedElements, OccurrenceTemplate shallow, PatternOccurrence deep,
			final Hierarchy printTree) {
		for (int i = 0; i < orderedElements.length; i++) {
			final Element rawElement = orderedElements[i];
			foreachElement(rawElement, shallow, deep, printTree);
		}
	}

	private void foreachElement(final Element rawElement, OccurrenceTemplate shallow, PatternOccurrence deep,
			final Hierarchy printTree) {
		final Element element = evaluateDefinition(rawElement, shallow, deep);

		walk(printTree, element);
	}

	private void addRelation(Hierarchy printTree, Element element) {
		// We basically only print relationships
		if (!(element instanceof PatternOccurrence)) {
			return;
		}
		final PatternOccurrence occ = (PatternOccurrence) element;
		if (!occ.isRelationOccurrence()) {
			return;
		}
		final Relation relation = (Relation) occ.getPattern();
		if (relation.getKind().equalsIgnoreCase(Relation.DELEGATION)) {
			System.out.println("!!! IGNORED DELEGATION relation");
			return;
		}
		// this code is equivalent to a loop unrolled
		final Element source = occ.get(Role.SOURCE);
		if (source == null) {
			return;
		}

		final Element target = gotoTop(occ.get(Role.TARGET));
		// TODO skip to top of hierarchy or main participant of pattern

		System.out.println("adding relation: " + source + " => " + target + " (" + relation.getKind() + ")");
		printTree.add(source, target, relation.getKind());

		// Iterator it = strip(target).iterator();
		// while (it.hasNext()) {
		// final Element eachTarget = (Element) it.next();
		// printTree.add(source, eachTarget, relation.getKind());
		// }
	}

	private Element gotoTop(Element element) {
		if (element instanceof PatternOccurrence) {
			final PatternOccurrence occ = (PatternOccurrence) element;
			if (occ.isHierarchyOccurrence()) {
				return occ.get("Top");
			}
		}
		return element;
	}

	// TODO there is a tree-walking pb; also Set occurrence has no internal
	// relationship hence is breaking tree walking
	private void walk(Hierarchy printTree, Element element) {
		depth++;
		String indent = "";
		for (int i = 0; i < depth; i++) {
			indent += "  ";
		}
		System.out.println(indent + "walk: " + element);
		addRelation(printTree, element);

		if (element instanceof PatternOccurrence) {
			final PatternOccurrence occ = (PatternOccurrence) element;
			final boolean isSet = occ.isSetOccurrence();
			final Iterator it = occ.allElements().iterator();
			while (it.hasNext()) {
				final Element member = (Element) it.next();
				walk(printTree, member);
				if (isSet) {
					final PatternOccurrence missingCollaboration = Relation.COLLABORATION_RELATION.newOccurrence(occ,
							member);
					addRelation(printTree, missingCollaboration);
				}
			}
		}
		depth--;
	}

	private void print(final Hierarchy hierarchy) {
		printHierarchyBoxes(hierarchy);
		printLinks(hierarchy.getRelations());
	}

	private void printLinks(final Collection links) {
		Iterator it = links.iterator();
		while (it.hasNext()) {
			final BinaryRelation binaryRelation = (BinaryRelation) it.next();
			printRelation(binaryRelation);
		}
	}

	/**
	 * Prints every box of every node in the given hierarchy using a
	 * HierarchyLayout against a GridLayout; each time it is called the
	 * rendering is shifted to the right by Width.
	 * 
	 * @param hierarchy
	 */
	protected void printHierarchyBoxes(Hierarchy hierarchy) {
		final Iterator iterator = hierarchy.dfsIterator();
		while (iterator.hasNext()) {
			final Node node = (Node) iterator.next();
			renderBox(node);
		}
		buffer.append("\n" + renderer.getContent());
	}

	private void printRelation(BinaryRelation relation) {
		final Element source = relation.getSource();
		final Element target = relation.getTarget();
		if (source == null || target == null) {
			return;
		}

		final String kind = relation.getKind();
		renderLink(source, target, kind);
	}

	// --- low level rendering

	private void renderBox(final Node node) {
		final BoxStyle boxStyle = boxStyleFor(node.getElement());
		if (boxStyle == null) {
			return;
		}
		renderer.render(node, boxes, boxStyle);
	}

	public void renderLink(final Element source, final Element target, final String kind) {
		final LinkStyle linkStyle = linkStyleFor(kind);
		if (linkStyle == null) {
			return;
		}
		linkRenderer.render(source, target, boxes, linkStyle);
		buffer.append("\n" + renderer.getContent());
	}

	protected BoxStyle boxStyleFor(Object item) {
		if (item instanceof Type) {
			return BoxStyle.TYPE;
		} else if (item instanceof Note) {
			return BoxStyle.NOTE;
		} else if (item instanceof PatternOccurrence) {
			final PatternOccurrence occurrence = (PatternOccurrence) item;
			final Pattern pattern = occurrence.getPattern();
			if (pattern.equals(PatternOccurrence.SET_PATTERN)) {
				return BoxStyle.SET;
			} else {
				return BoxStyle.PATTERN;
			}
		}
		return null;
	}

	protected LinkStyle linkStyleFor(final String kind) {
		if (kind.equals(Relation.INHERITANCE)) {
			return LinkStyle.GENERALIZATION;
		} else if (kind.equals(Relation.DELEGATION)) {
			return LinkStyle.DELEGATION;
		} else if (kind.equals(Relation.ALLOCATION)) {
			return LinkStyle.ALLOCATION;
		} else if (kind.equals(Relation.COLLABORATION)) {
			return LinkStyle.COLLABORATION;
		}
		return null;
	}

}
