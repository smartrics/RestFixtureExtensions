package com.patternity.core.metamodel;


/**
 * Represents a method element
 * 
 * @author cyrille martraire
 */
public class Method implements Item {

	private static final String UNKNOWN = "...";
	private final String args;
	private final String returnType;
	private final String name;
	private final Modifiers modifiers;

	private Type type;

	public Method() {
		this(UNKNOWN, "", null, Modifiers.SYNC);
	}

	public Method(String name) {
		this(name, null, null, Modifiers.SYNC);
	}

	public Method(String name, String args, String returnType, Modifiers modifiers) {
		this.name = name;
		this.args = args;
		this.returnType = returnType;
		this.modifiers = modifiers;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getArgs() {
		return args;
	}

	public String getReturnType() {
		return returnType;
	}

	public String getName() {
		return name;
	}

	public boolean isUnknown() {
		return name == UNKNOWN;
	}

	public Modifiers getModifiers() {
		return modifiers;
	}

	public boolean isAbstract() {
		return modifiers.isAbstract();
	}

	public String qualifiedName() {
		return namespace() + "#" + signature();
	}

	public String namespace() {
		return (getType() == null ? "" : getType().qualifiedName());
	}

	public String signature() {
		return getName() + "(" + getArgs() + ")";
	}

	public String shortSignature() {
		return getName() + "()";
	}

	public String toString() {
		return "Method " + getName();
	}

}
