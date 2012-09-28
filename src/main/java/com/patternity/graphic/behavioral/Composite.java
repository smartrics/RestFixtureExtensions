package com.patternity.graphic.behavioral;

import java.text.MessageFormat;

import com.patternity.graphic.dag.Node;

/**
 * Represents a composite collaboration between one composite and its children.
 * The first agent is necessarily called through the same method than the method
 * called for each child.
 * 
 * @author cyrille martraire
 */
public class Composite extends MultipleDelegation {

	public static final String SELF_ASSIGNMENT_DESC = "Call each child.{1} until the work is handled";
	public static final String SHARED_EFFORT_DESC = "Always call each child.{1}";

	public final static Composite newCompositeSharedEffort(final Message genericEvent) {
		return newComposite(genericEvent, SHARED_EFFORT_DESC);
	}

	public final static Composite newCompositeSelfAssignment(final Message genericEvent) {
		return newComposite(genericEvent, SELF_ASSIGNMENT_DESC);
	}

	public final static Composite newComposite(final Message genericEvent, final String commentPattern) {
		final Agent[] children = genericEvent.getTarget().agents1_N(4);
		final String method = genericEvent.getMethod();
		final boolean enableEllipsis = true;
		final String comment = MessageFormat.format(commentPattern, new Object[] { children[0].getName(), method });

		return new Composite(children, method, enableEllipsis, comment);
	}

	public final static Composite newComposite(final Message genericEvent, final String commentPattern, final int n) {
		final Agent[] children = genericEvent.getTarget().agents1_N(n);
		final String method = genericEvent.getMethod();
		final boolean enableEllipsis = n > 2;
		final String comment = MessageFormat.format(commentPattern, new Object[] { children[0].getName(), method });

		return new Composite(children, method, enableEllipsis, comment);
	}

	public Composite(final Agent[] children, final String method, final boolean enableEllipsis, final String comment) {
		super(method, children, comment, enableEllipsis);
	}

	public void append(Node compositeNode) {
		final String note = comment == null ? "..." : comment;
		for (int i = 0; i < children.length; i++) {
			final boolean lastBut1 = enableEllipsis && i == children.length - 2;
			children[i] = lastBut1 ? Agent.ELLIPSIS : children[i];
			final Agent child = children[i];

			if (lastBut1) {
				compositeNode.add(new Node(new Note(child, note)));
			} else {
				compositeNode.add(new Node(new Message(child, method)));
			}
		}
	}

	public String toString() {
		return "Composite " + describe();
	}

}
