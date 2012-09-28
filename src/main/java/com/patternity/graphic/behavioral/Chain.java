package com.patternity.graphic.behavioral;

import java.text.MessageFormat;

import com.patternity.graphic.dag.Node;

/**
 * Represents a chain collaboration between each agent and its successor. The
 * first agent is necessarily called through the same method than the method
 * called throughout the chain.
 * 
 * @author cyrille martraire
 */
public class Chain extends MultipleDelegation {

	public static final String SELF_ASSIGNMENT_DESC = "Forward to successor.{1} until the request is handled";
	public static final String SHARED_EFFORT_DESC = "Always call successor.{1}";

	public final static Chain newChainSharedEffort(final Message genericEvent) {
		return newChain(genericEvent, SHARED_EFFORT_DESC);
	}

	public final static Chain newChainSelfAssignment(final Message genericEvent) {
		return newChain(genericEvent, SELF_ASSIGNMENT_DESC);
	}

	public final static Chain newChain(final Message genericEvent, final String commentPattern) {
		final Agent[] children = genericEvent.getTarget().agents1_N(4);
		final String method = genericEvent.getMethod();
		final boolean enableEllipsis = true;
		final String comment = MessageFormat.format(commentPattern, new Object[] { children[0].getName(), method });

		return new Chain(children, method, enableEllipsis, comment);
	}

	public final static Chain newChain(final Message genericEvent, final String commentPattern, final int n) {
		final Agent[] children = genericEvent.getTarget().agents1_N(n);
		final String method = genericEvent.getMethod();
		final boolean enableEllipsis = n > 2;
		final String comment = MessageFormat.format(commentPattern, new Object[] { children[0].getName(), method });

		return new Chain(children, method, enableEllipsis, comment);
	}

	public Chain(final Agent[] children, final String method, final boolean enableEllipsis, final String comment) {
		super(method, children, comment, enableEllipsis);
	}

	public void append(final Node initialNode) {
		Node firstNode = null;
		Node previousNode = null;
		for (int i = 0; i < children.length; i++) {
			final boolean last = enableEllipsis && i == children.length - 1;
			children[i] = last ? new Agent(children[i].getType(), "... ", false) : children[i];
			final Agent child = children[i];

			final Event event = new Message(child, method);

			final Node node = new Node(event);
			if (firstNode == null) {
				firstNode = node;
			} else {
				previousNode.add(node);
			}
			previousNode = node;
		}
		if (firstNode != null) {
			initialNode.add(firstNode);
		} else {
			return;
		}
		if (comment != null) {
			firstNode.add(new Node(new Note(comment)));
		}
	}

	public String toString() {
		return "Chain " + describe();
	}
}
