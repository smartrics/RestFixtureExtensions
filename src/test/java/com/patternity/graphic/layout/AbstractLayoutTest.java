package com.patternity.graphic.layout;

import java.io.File;
import java.util.Iterator;

import junit.framework.TestCase;

import com.patternity.graphic.BoundingBox;
import com.patternity.graphic.dag.Node;
import com.patternity.graphic.svg.BoxStyle;
import com.patternity.graphic.svg.Diagram;
import com.patternity.graphic.svg.DiagramRenderer;
import com.patternity.graphic.svg.LinkStyle;
import com.patternity.util.Named;
import com.patternity.util.TemplatedWriter;

public abstract class AbstractLayoutTest extends TestCase {

	private char label = 'A';

	public AbstractLayoutTest(String name) {
		super(name);
	}

	protected String nextLabel() {
		return String.valueOf(label++);
	}

    public void write(final Iterator<?> iterator, final Layout layout, final String filename) {
		// Build diagram with its styled elements
		final Diagram diagram = new Diagram();
		int count = 0;
		final BoxStyle[] styles = { BoxStyle.TYPE, BoxStyle.NOTE, BoxStyle.PATTERN, BoxStyle.SET };
		while (iterator.hasNext()) {
			final Node node = (Node) iterator.next();
			// check each box style in turn
			final BoxStyle boxStyle = styles[count++ % styles.length];
			if (boxStyle != null) {
				diagram.addBox(node, boxStyle);
			}
			final LinkStyle linkStyle = LinkStyle.GENERALIZATION;
			if (linkStyle != null) {
				diagram.addLinkToParent(node, linkStyle);
			}
		}

		final String content = render(diagram, layout);
		String tempDirName = System.getProperty("java.io.tmpdir");
		final File tempDir = new File(tempDirName);
		final TemplatedWriter writer = new TemplatedWriter(new File(tempDir, filename), new File("template.svg"));
		writer.write(content, "viewBox=\"0 0 1000 1000\"");
	}

	protected String render(final Diagram diagram, final Layout layout) {
		// render and write to file
		final double paddingRatio = 0.30;
		final BoundingBox grid = new BoundingBox(120, 140);
		final int fontsize = 16;
		final DiagramRenderer diagramRenderer = new DiagramRenderer(grid, paddingRatio, layout, fontsize, true);
		diagramRenderer.render(diagram);

		final String content = diagramRenderer.getContent();
		return content;
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
