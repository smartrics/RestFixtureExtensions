package com.patternity.graphic.behavioral;

import java.util.Arrays;

/**
 * Base class for collaborations that involve one method on several agents.
 * 
 * @author cyrille martraire
 */
public abstract class MultipleDelegation implements Collaboration {

	protected final Agent[] children;
	protected final String method;
	protected final boolean enableEllipsis;
	protected final String comment;

	public MultipleDelegation(String method, Agent[] children, String comment, boolean enableEllipsis) {
		this.method = method;
		this.children = children;
		this.comment = comment;
		this.enableEllipsis = enableEllipsis;
	}

	public Agent[] getChildren() {
		return children;
	}

	public String getMethod() {
		return method;
	}

	public boolean isEnableEllipsis() {
		return enableEllipsis;
	}

	public String getComment() {
		return comment;
	}

	protected String describe() {
		return "method: " + method + " on multiple agents: " + Arrays.asList(children)
				+ (enableEllipsis ? "with ellipsis" : "no ellipsis") + " comment: " + comment;
	}

	public String toString() {
		final String describe = describe();
		return "MultipleDelegation " + describe;
	}
}
