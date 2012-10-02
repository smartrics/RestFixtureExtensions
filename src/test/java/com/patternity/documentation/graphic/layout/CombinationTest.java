package com.patternity.documentation.graphic.layout;

import java.io.File;

import org.junit.Test;

import com.patternity.graphic.BoundingBox;
import com.patternity.graphic.Position;
import com.patternity.graphic.dag.Node;
import com.patternity.graphic.layout.FlowLayout;
import com.patternity.graphic.layout.HierarchyLayout;
import com.patternity.graphic.svg.BoxStyle;
import com.patternity.graphic.svg.Diagram;
import com.patternity.graphic.svg.DiagramRenderer;
import com.patternity.graphic.svg.LinkStyle;
import com.patternity.util.FileUtils;
import com.patternity.util.Named;
import com.patternity.util.TemplatedWriter;

/**
 * Generates many combinations of two sub-diagrams (tree or single element)
 * connected together through a varying link, with the goal of generating the
 * class diagrams of most of design patterns through this process.
 * 
 * @author cyrille martraire
 */
public class CombinationTest {
	private char label = 'A';
	private FlowLayout flow = new FlowLayout(3);

	protected String nextLabel() {
		return String.valueOf(label++);
	}

	public final int NONE = 0;
	public final int INHERITANCE_HIERARCHY = NONE + 1;
	public final int SET = INHERITANCE_HIERARCHY + 1;

	public final int DELEGATION = SET + 1;
	public final int ALLOCATION = DELEGATION + 1;

	public final int DISTINCT = ALLOCATION + 1;
	public final int SELF = DISTINCT + 1;
	public final int TO_PARENT = SELF + 1;// FROM ONE CHILD
	public final int FROM_PARENT = TO_PARENT + 1;// TO EVERY CHILD (template method)

	private final StringBuffer buffer = new StringBuffer();


	@Test
	public void testIt() throws Exception {
		final int[] items = { INHERITANCE_HIERARCHY, SET };
		final int[] items2 = { NONE, INHERITANCE_HIERARCHY, SET };
		final LinkStyle[] links = { LinkStyle.DELEGATION, LinkStyle.ALLOCATION };
		final int[] relations = { SELF, TO_PARENT, FROM_PARENT };

		File outputDir = new File("target/patternity.temp");
		FileUtils.makeDirs(outputDir);
		for (int i = 0; i < items.length; i++) {
			final int item1 = items[i];
			for (int j = 0; j < items2.length; j++) {
				final int item2 = items2[j];
				for (int k = 0; k < links.length; k++) {
					final LinkStyle linkStyle = links[k];

					if (item2 == NONE) {
						if (item1 == INHERITANCE_HIERARCHY) {
							for (int l = 0; l < relations.length; l++) {
								final int relation = relations[l];
								execute(outputDir, item1, item2, linkStyle, relation);
							}
						} else {
							execute(outputDir, item1, item2, linkStyle, SELF);
						}
					} else {
						execute(outputDir, item1, item2, linkStyle, DISTINCT);
					}
				}
			}
		}
	}

	public void execute(File baseDir, int item1, int item2, LinkStyle linkStyle, int relation) {
		executeEach(item1, item2, linkStyle, relation);

		final String filename = "Combinations" + ".svg";

		write(baseDir, buffer.toString(), filename);
	}

	protected static String translate(final String content, final Position position) {
		return translate(content, position, null);
	}

	protected static String translate(final String content, final Position position, String title) {
		final StringBuffer b = new StringBuffer();
		final double x = position.getX() * 1000;
		final double y = position.getY() * 700;
		if (title != null) {
			b.append("\n <text x=\"" + x + "\" y=\"" + (y + 20) + "\" class=\"classbox\" > " + title + "</text>");
		}
		b.append("\n <g transform=\"translate(" + x + "," + (y + 0) + ")\" >");
		b.append(content);
		b.append("\n </g>");
		return b.toString();
	}

	private void executeEach(int item1, int item2, LinkStyle linkStyle, int relation) {
		reset();
		final String combinationName = name(item1) + " " + name(item2) + " " + name(linkStyle) + " " + name(relation);
		System.out.println(combinationName);

		final Diagram diagram = new Diagram();
		final Node root = newNode("Pattern");
		diagram.addBox(root, BoxStyle.PATTERN);

		final Diagram d1 = subDiagram(item1);
		final Diagram d2 = subDiagram(item2);

		if (d1 != null) {
			diagram.addSubDiagram(d1, root, LinkStyle.COLLABORATION);
		}

		// add if existing
		if (d2 != null) {
			diagram.addSubDiagram(d2, root, LinkStyle.COLLABORATION);
		}

		// add relation
		relation(relation, diagram, d1, d2, linkStyle);

		// render and write to file
		final double paddingRatio = 0.30;
		final BoundingBox grid = new BoundingBox(120, 160);
		final int fontsize = 16;
		final DiagramRenderer diagramRenderer = new DiagramRenderer(grid, paddingRatio, new HierarchyLayout(),
				fontsize, true);
		diagramRenderer.render(diagram);

		final String content = diagramRenderer.getContent();

		final Position position = flow.position(null);
		buffer.append(translate(content, position, combinationName));
	}

	protected void write(File baseDir, final String content, final String filename) {
		FileUtils.makeDirs(baseDir);
		final TemplatedWriter writer = new TemplatedWriter(new File(baseDir, filename), new File("template.svg"));
		writer.write(content, "viewBox=\"0 0 1500 3000\"");
	}

	private void reset() {
		label = 'A';
	}

	private void relation(int kind, Diagram diagram, Diagram d1, Diagram d2, LinkStyle linkStyle) {
		// SELF, TO_PARENT, FROM_PARENT
		final LinkStyle linkStyleBis = linkStyle.isManhattan() ? linkStyle : linkStyle.toManhattan(LinkStyle.SHIFTS
				.multiply(3, 2));

		final Object from = d1.getFirstBox().getNode().getElement();
		switch (kind) {
		case DISTINCT: {
			if (d2 != null) {
				diagram.addLink(d1.getFirstBox().getNode().getElement(), d2.getFirstBox().getNode().getElement(),
						linkStyle);
				return;
			}
		}
		case SELF:
			diagram.addLink(from, from, linkStyleBis);
			return;
		case TO_PARENT: {
			final Node node = d1.getFirstBox().getNode();
			final Node child = node.getNodes()[2];
			diagram.addLink(child.getElement(), node.getElement(), linkStyle);
			return;
		}
		case FROM_PARENT: {
			final Node node = d1.getFirstBox().getNode();
			final Node child = node.getNodes()[2];
			diagram.addLink(node.getElement(), child.getElement(), linkStyle);
			return;
		}
		default:
			break;
		}
	}

	private String name(LinkStyle linkStyle) {
		if (linkStyle.getStyle().indexOf("dash") != -1) {
			return "Allocation";
		}
		return "Delegation";
	}

	private String name(final int kind) {
		switch (kind) {
		case DISTINCT:
			return "Distinct";
		case SELF:
			return "Self";
		case TO_PARENT:
			return "To_Parent";
		case FROM_PARENT:
			return "From_parent";
		case INHERITANCE_HIERARCHY:
			return "Tree";
		case SET:
			return "Set";
		default:
			break;
		}
		return "";
	}

	private Diagram subDiagram(int key) {
		final Diagram diagram = new Diagram();
		switch (key) {
		case INHERITANCE_HIERARCHY:
			diagram.addHierarchy(tree(), BoxStyle.TYPE, LinkStyle.GENERALIZATION);
			return diagram;
		case SET:
			diagram.addBox(single(), BoxStyle.TYPE);
			return diagram;
		default:
			break;
		}
		return null;
	}

	public Node tree(final int count) {
		final Node a = newNode(nextLabel());
		if (count == 1) {
			return a;
		}
		final Node b = newNode(nextLabel());
		final Node c = newNode(nextLabel());
		final Node d = newNode(nextLabel());

		a.add(b);
		a.add(c);
		a.add(d);

		final Node e = newNode(nextLabel());
		final Node f = newNode(nextLabel());

		d.add(e);
		d.add(f);
		return a;
	}

	public Node single() {
		return tree(1);
	}

	public Node tree() {
		return tree(6);
	}

	public Node newNode(final String name) {
		return new Node(new Named() {

			public String getName() {
				return name;
			}

			public String toString() {
				return name;
			}

		});
	}

}
