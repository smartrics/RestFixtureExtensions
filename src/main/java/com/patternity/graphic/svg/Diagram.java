package com.patternity.graphic.svg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.patternity.graphic.dag.DepthFirstIterator;
import com.patternity.graphic.dag.Node;

/**
 * Represents a diagram, made of boxes and links.
 * 
 * @author cyrille martraire
 */
public final class Diagram {

	private final List<Box> boxes = new ArrayList<Box>();
	private final List<Link> links = new ArrayList<Link>();

	public void addBox(Node node, BoxStyle style) {
		boxes.add(new Box(node, style));
	}

	public void addLink(Object from, Object to, LinkStyle style) {
		links.add(new Link(from, to, style));
	}

	/**
	 * Convenience method
	 */
	public void addLinkToParent(Node node, LinkStyle style) {
		final Node parent = node.getParent();
		if (parent == null) {
			return;
		}
		addLink(node.getElement(), parent.getElement(), style);
	}

	/**
	 * Convenience method
	 */
	public void addHierarchy(Node node, BoxStyle boxStyle, LinkStyle linkStyle) {
		final Iterator<?> it = new DepthFirstIterator(node);
		while (it.hasNext()) {
			final Node n = (Node) it.next();
			addBox(n, boxStyle);
			addLinkToParent(n, linkStyle);
		}
	}

	/**
	 * Convenience method
	 */
	public void addSubDiagram(Diagram subDiagram, Node to, LinkStyle linkStyle) {
		if (subDiagram.numberOfBoxes() > 0) {
			final Node rootNode = subDiagram.getFirstBox().getNode();
			to.add(rootNode);
			addLink(rootNode.getElement(), to.getElement(), linkStyle);

			final Iterator it = subDiagram.getBoxes().iterator();
			while (it.hasNext()) {
				Box box = (Box) it.next();
				addBox(box.getNode(), (BoxStyle) box.getStyle());
			}
		}

		final Iterator it = subDiagram.getLinks().iterator();
		while (it.hasNext()) {
			Link link = (Link) it.next();
			addLink(link.getFrom(), link.getTo(), (LinkStyle) link.getStyle());
		}
	}

	public int numberOfBoxes() {
		return boxes.size();
	}

	public int numberOfLinks() {
		return links.size();
	}

	public List getBoxes() {
		return boxes;
	}

	public Box getFirstBox() {
		return boxes.isEmpty() ? null : (Box) boxes.get(0);
	}

	public List getLinks() {
		return links;
	}

	public void clear() {
		boxes.clear();
		links.clear();
	}

	public String toString() {
		return "Diagram: " + numberOfBoxes() + " boxes, " + numberOfLinks() + " links";
	}

}
