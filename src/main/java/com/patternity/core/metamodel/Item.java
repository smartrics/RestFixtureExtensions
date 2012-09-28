package com.patternity.core.metamodel;

import com.patternity.util.Named;

/**
 * Represents an item, as opposed to a relation that connects items.
 * 
 * @author cyrille martraire
 */
public interface Item extends Element, Named, Native {

	String qualifiedName();
}
