package com.patternity.core.metamodel;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a type (class, interface, subset of operations...)
 * 
 * @author cyrille martraire
 */
public class Type implements Item {

	private final String name;
	private final Modifiers modifiers;
	private final List methods = new ArrayList();

	public Type(String name) {
		this(name, Modifiers.NON_ABSTRACT);
	}

	public Type(String name, Modifiers modifiers) {
		this.name = name;
		this.modifiers = modifiers;
	}

	public String getName() {
		return name;
	}

	public String qualifiedName() {
		return name;
	}

	public Modifiers getModifiers() {
		return modifiers;
	}

	public void add(Method method) {
		methods.add(method);
	}

	public boolean hasMethods() {
		return !methods.isEmpty();
	}

	public Method[] getMethods() {
		return (Method[]) methods.toArray(new Method[methods.size()]);
	}

	public String toString() {
		return "Type " + getName();
	}

}
