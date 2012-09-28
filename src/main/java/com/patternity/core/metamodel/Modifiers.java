package com.patternity.core.metamodel;

/**
 * Represents any kind of modifiers, from the programming language (abstract,
 * static, public, private...) or additional from technical aspects (async...)
 * 
 * @author cyrille martraire
 */
public class Modifiers {

	public static final Modifiers SYNC = new Modifiers(false, true);
	public static final Modifiers ASYNC = new Modifiers(true, true);

	public static final Modifiers ABSTRACT = new Modifiers(false, true);
	public static final Modifiers NON_ABSTRACT = new Modifiers(false, false);

	private final boolean isAbstract;
	private final boolean isAsync;

	public Modifiers(boolean isAbstract, boolean isAsync) {
		this.isAbstract = isAbstract;
		this.isAsync = isAsync;
	}

	public boolean isAsync() {
		return isAsync;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public String toString() {
		return (isAsync ? "Async" : "") + (isAbstract ? "Abstract" : "");
	}
}
