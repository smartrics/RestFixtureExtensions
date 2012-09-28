package com.patternity.graphic.behavioral;

/**
 * Represents an invocation of a method or any function, along with its details
 * (return value, sync or async etc.)
 * 
 * @author cyrille martraire
 * @deprecated
 */
public class Invocation {

	private final String method;
	private final String args;
	private final String returnValue;
	private final String kind;
	private final String guard;

	public Invocation(String signature) {
		this(signature, "", null, null, null);
	}

	public Invocation(String method, String args, String returnValue, String kind, String guard) {
		this.method = method;
		this.args = args;
		this.returnValue = returnValue;
		this.kind = kind;
		this.guard = guard;
	}

	public String getSignature() {
		return getMethod() + getArgs();
	}

	public String getArgs() {
		return args;
	}

	public String getMethod() {
		return method;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public String getKind() {
		return kind;
	}

	public boolean isAsync() {
		return kind != null;
	}

	public boolean hasGuard() {
		return guard != null;
	}

	public String getGuard() {
		return guard;
	}

	public String toString() {
		return "Invocation " + getMethod() + "()";
	}

}
