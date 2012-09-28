package com.patternity.graphic.behavioral;

import java.util.Stack;


/**
 * Represents a method activation (the concept of a method currently running),
 * including the full call stack, for one particular agent only.
 * 
 * @author cyrille martraire
 */
public class Activation {

	protected final Agent agent;

	protected final Stack callStack = new Stack();

	public Activation(Agent agent) {
		this.agent = agent;
	}

	public Agent getAgent() {
		return agent;
	}

	/**
	 * When target=agent
	 */
	public void enterMethod(String method) {
		callStack.push(method);
	}

	/**
	 * When target=agent
	 */
	public Object exitMethod() {
		return callStack.pop();
	}

	public String currentMethod() {
		return (String) callStack.peek();
	}

	public int depth() {
		return callStack.size();
	}

	public String toString() {
		return "Activation for agent: " + getAgent() + " depth=" + depth();
	}

}