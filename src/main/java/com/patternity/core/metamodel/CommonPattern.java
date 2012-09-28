package com.patternity.core.metamodel;


/**
 * @author cyrille martraire
 * 
 */
public class CommonPattern implements Pattern {

	private final String name;

	public CommonPattern(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return "CommonPattern: " + getName();
	}
}
