package com.patternity.graphic.behavioral;

/**
 * Represents a message being sent to a target agent
 * 
 * @author cyrille martraire
 */
public class Message extends Event {

	private final String method;

	private final String result;

	public static final int SYNC = 0;

	public static final int ASYNC = 1;

	public static final int CREATE = 2;

	public static final int DESTROY = 3;

	public Message(Agent target, String method) {
		this(SYNC, target, method, null);
	}

	public Message(int kind, Agent target, String method, String result) {
		super(kind, target);
		this.method = method;
		this.result = result;
	}

	public String getMethod() {
		return method;
	}

	public String getResult() {
		return result;
	}

	public boolean isCreation() {
		return getKind() == CREATE;
	}

	public boolean isAsync() {
		return getKind() == ASYNC;
	}

	public boolean isDestroy() {
		return getKind() == DESTROY;
	}

	public String toString() {
		return getTarget() + "." + getMethod();
	}

}