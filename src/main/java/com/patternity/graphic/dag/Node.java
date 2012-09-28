package com.patternity.graphic.dag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.EmptyIterator;

/**
 * Generic node class to represent tree structures (DAG)
 * 
 * @author cyrille martraire
 */
public class Node {

	private final Object element;

	private final List nodes = new ArrayList();

	private Node parent = null;

	public Node(Object element) {
		this.element = element;
	}

	public Object getElement() {
		return element;
	}

	public int size() {
		return nodes.size();
	}

	public boolean isLeaf() {
		return nodes.size() == 0;
	}

	public int getDepth() {
		if (isRoot()) {
			return 1;
		}
		return getParent().getDepth() + 1;
	}

	public int getFilteredDepth(Predicate filter) {
		final int parentDepth = isRoot() ? 0 : getParent().getFilteredDepth(filter);
		return filter.evaluate(element) ? parentDepth + 1 : parentDepth;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public int getRank() {
		if (isRoot()) {
			return 0;
		}
		return parent.nodes.indexOf(this);
	}

	public Node getPrevious() {
		if (isRoot()) {
			return null;
		}
		final int rank = getRank();
		if (rank == 0) {
			return null;
		}
		return (Node) parent.nodes.get(rank - 1);
	}

	public Node getNext() {
		if (isRoot()) {
			return null;
		}
		final int rank = getRank();
		if (rank == parent.size() - 1) {
			return null;
		}
		return (Node) parent.nodes.get(rank + 1);
	}

	public Iterator nodes() {
		return isLeaf() ? EmptyIterator.INSTANCE : nodes.iterator();
	}

	public Node[] getNodes() {
		return (Node[]) nodes.toArray(new Node[nodes.size()]);
	}

	public void add(Node node) {
		nodes.add(node);
		node.setParent(this);
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getMaxBreadth() {
		if (isLeaf()) {
			return 1;
		}
		int width = 0;
		final Node[] children = getNodes();
		for (int i = 0; i < children.length; i++) {
			final Node child = children[i];
			width += child.getMaxBreadth();
		}
		return width;
	}

	public int getActivationLength() {
		if (isLeaf()) {
			return 1;
		}
		int width = 0;
		final Node[] children = getNodes();
		for (int i = 0; i < children.length; i++) {
			final Node child = children[i];
			width += child.getMaxBreadth();
		}
		return width;
	}

	public int getMaxDepth() {
		int max = 0;
		final Node[] children = getNodes();
		for (int i = 0; i < children.length; i++) {
			final Node child = children[i];
			max = Math.max(max, child.getMaxDepth());
		}
		return max + 1;
	}

	protected String describe(String indent) {
		final StringBuffer buffer = new StringBuffer(indent + getElement() + " depth=" + getDepth() + " breadth="
				+ getMaxBreadth());
		final Node[] children = getNodes();
		for (int i = 0; i < children.length; i++) {
			final Node child = children[i];
			buffer.append("\n" + child.describe("  " + indent));
		}
		return buffer.toString();
	}

	public String toString() {
		return describe("");
	}

}