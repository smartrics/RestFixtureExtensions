package com.patternity.graphic.behavioral;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.patternity.graphic.dag.Node;

/**
 * Represents a collaboration between two agents, one calling the other that
 * calls it back. This ping-pong can be repeated several times. The first agent
 * can be called through any method.
 * 
 * Typical for observer push-pull and visitor ping-pong collaborations.
 * 
 * @author cyrille martraire
 */
public class PingPong implements Collaboration {

	private final Message[] messages;

	public PingPong(Message call, Message callback) {
		this(call, callback, 1);
	}

	public final static Message[] repeat(Message call, Message callback, int repeat) {
		final List calls = new ArrayList();
		for (int i = 0; i < repeat; i++) {
			calls.add(call);
			calls.add(callback);
		}
		return (Message[]) calls.toArray(new Message[calls.size()]);
	}

	public PingPong(Message call, Message callback, int repeat) {
		this(repeat(call, callback, repeat));
	}

	public PingPong(Message[] messages) {
		this.messages = messages;
	}

	public void append(Node initialNode) {
		Node previousNode = null;
		for (int i = 0; i < messages.length; i++) {
			final Message message = messages[i];
			final Node node = new Node(message);
			if (previousNode == null) {
				initialNode.add(node);
			} else {
				previousNode.add(node);
			}
			previousNode = node;
		}
	}

	public String toString() {
		return "PingPong messages: " + Arrays.asList(messages);
	}
}
