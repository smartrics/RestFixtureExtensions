package com.patternity.graphic.dag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @pattern Builder Product=Node Builds a tree of Node elements incrementally,
 *          then returns the root of the tree
 * 
 * @author cyrille martraire
 */
public final class NodeBuilder {

	private Node root;

	private final Map nodes = new HashMap();

	/**
	 * @pattern Builder.BuildMethod
	 */
	public void add(Object parent, Object child) {
		final Node childNode = node(child);
		final Node parentNode = node(parent);
		if (parentNode != null && childNode != null) {
			parentNode.add(childNode);
		}
	}

	private Node node(Object element) {
		if (element == null) {
			return null;
		}
		Node node = (Node) nodes.get(element);
		if (node == null) {
			node = new Node(element);
			nodes.put(element, node);
		}
		return node;
	}

	/**
	 * @pattern Builder.GetResultMethod
	 */
	public Node getRootNode() {
		if (root == null) {
			Iterator it = nodes.values().iterator();
			while (it.hasNext()) {
				final Node node = (Node) it.next();
				if (node.isRoot()) {
					root = node;
					return root;
				}
			}
		}
		return root;
	}

	public void clear() {
		nodes.clear();
	}

	public boolean containsKey(Object key) {
		return nodes.containsKey(key);
	}

	public Set keySet() {
		return nodes.keySet();
	}

	public int size() {
		return nodes.size();
	}

	public Collection values() {
		return nodes.values();
	}

	public String toString() {
		return "NodeBuilder: " + nodes.size() + " nodes";
	}
}