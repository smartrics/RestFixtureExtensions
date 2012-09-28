package com.patternity.graphic.dag;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.buffer.UnboundedFifoBuffer;
import org.apache.commons.collections.iterators.SingletonIterator;


/**
 * An Iterator to walk a node tree depth-first
 * 
 * @author cyrille martraire
 */
public class BreadthFirstIterator implements Iterator {

	private final Buffer queue = new UnboundedFifoBuffer();

	public BreadthFirstIterator(Node root) {
		final SingletonIterator iterator = new SingletonIterator(root);
		queue.add(iterator);
	}

	public boolean hasNext() {
		return !queue.isEmpty() && currentIterator().hasNext();
	}

	private Iterator currentIterator() {
		return (Iterator) queue.get();
	}

	public Object next() {
		if (!hasNext()) {
			throw new NoSuchElementException("No such element in node tree");
		}
		Iterator current = currentIterator();
		final Node node = (Node) current.next();
		if (node.size() > 0) {
			queue.add(node.nodes());// new node, new iterator
		}
		if (!current.hasNext()) {
			queue.remove();// remove finished iterator
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
