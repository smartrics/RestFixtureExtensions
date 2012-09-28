package com.patternity.util;

import java.util.Iterator;

/**
 * Decorates an iterator such that it only returns the first N elements.
 * 
 * @pattern Decorator target=java.util.Iterator
 *          valueadded="it only returns the first N elements"
 * 
 * @author cyrille martraire
 */
public class LimitedIterator<T> implements Iterator<T> {

    private final Iterator<T> it;
	private final int limit;
	private int count = 0;

    public LimitedIterator(Iterator<T> it, int limit) {
		this.it = it;
		this.limit = limit;
	}

	public boolean hasNext() {
		return count < limit && it.hasNext();
	}

    public T next() {
		count++;
		return it.next();
	}

	/**
	 * To call BEFORE calling next()
	 * 
	 * @return true if the next element is the last that will be returned, so
	 *         that it can be replaced with the ellipsis "..."
	 */
	public boolean isLastElement() {
		return count == limit - 1;
	}

	public void remove() {
		it.remove();
	}

	public String toString() {
		return "LimitedIterator limit=" + limit;
	}

}
