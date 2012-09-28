package com.patternity.graphic.dag;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.apache.commons.collections.iterators.SingletonIterator;


/**
 * An Iterator to walk a node tree depth-first
 * 
 * @author cyrille martraire
 */
public class DepthFirstIterator implements Iterator {

	private final Stack stack = new Stack();

	public DepthFirstIterator(Node root) {
		final SingletonIterator iterator = new SingletonIterator(root);
		stack.push(iterator);
	}

	public boolean hasNext() {
		return !stack.isEmpty() && currentIterator().hasNext();
	}

	private Iterator currentIterator() {
		final Iterator it = (Iterator) stack.peek();
		return it;
	}

	public Object next() {
		if (!hasNext()) {
			throw new NoSuchElementException("No such element in node tree");
		}
		Iterator current = currentIterator();
		final Node node = (Node) current.next();
		if (!current.hasNext()) {
			stack.pop();// remove finished iterator
		}
		if (node.size() > 0) {
			stack.push(node.nodes());// new node, new iterator
		}
		return node;
	}

	public void remove() {
		currentIterator().remove();
	}

	public String toString() {
		return "DepthFirstIterator";
	}

}
