package com.patternity.graphic.layout.sequence;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.patternity.graphic.behavioral.Activation;
import com.patternity.graphic.behavioral.Agent;
import com.patternity.graphic.behavioral.Event;
import com.patternity.graphic.behavioral.Message;
import com.patternity.graphic.dag.Node;

/**
 * A generator that writes example code snippet from a given tree of methods
 * calls
 * 
 * @author cyrille martraire
 */
public class SnippetGenerator {

	private final Map activations = new HashMap();

	public SnippetGenerator() {
	}

	public String layout(Node root) {
		final StringBuffer out = new StringBuffer();
		walk(root);

		Iterator it = activations.values().iterator();
		while (it.hasNext()) {
			final SnippetActivation activation = (SnippetActivation) it.next();
			out.append(activation.getBody());
		}

		final String content = out.toString();
		return content;
	}

	private SnippetActivation activationFor(Agent agent) {
		SnippetActivation activation = (SnippetActivation) activations.get(agent);
		if (activation == null) {
			activation = new SnippetActivation(agent);
			activations.put(agent, activation);
		}
		return activation;
	}

	private void walk(Node node) {
		final Event event = (Event) node.getElement();
		if (!(event instanceof Message)) {
			// TODO display something
			return;
		}
		final Message message = (Message) event;
		final Agent source = source(node);
		final Agent target = message.getTarget();
		final String method = message.getMethod();

		if (!source.isRoot()) {
			final SnippetActivation activationSource = activationFor(source);
			activationSource.call(target, method);
		}

		final SnippetActivation activation = activationFor(target);
		activation.enterMethod(method);

		final Node[] children = node.getNodes();
		for (int i = 0; i < children.length; i++) {
			final Node child = children[i];
			walk(child);
		}

		activation.exitMethod();
	}

	private Agent source(Node node) {
		if (node.isRoot()) {
			return Agent.ROOT;
		}
		final Message element = (Message) node.getParent().getElement();
		final Agent target = element.getTarget();
		return target == null ? Agent.ROOT : target;
	}

	public String toString() {
		return "SnippetGenerator";
	}

	/**
	 * Subclass of activations for an agent, that takes care of generating the
	 * method signature and body
	 * 
	 * @author cyrille martraire
	 */
	public static class SnippetActivation extends Activation {

		private final Map methodBodies = new HashMap();

		public SnippetActivation(Agent agent) {
			super(agent);
		}

		private StringBuffer bodyFor(String method) {
			StringBuffer body = (StringBuffer) methodBodies.get(method);
			if (body == null) {
				body = new StringBuffer();
				methodBodies.put(method, body);
			}
			return body;
		}

		private void openBody(String method) {
			final StringBuffer body = bodyFor(method);
			body.append("\n  public void " + method + " {");
			body.append("\n    //...");
		}

		private void closeBody(String method) {
			final StringBuffer body = bodyFor(method);
			body.append("\n  }");
		}

		/**
		 * When target=agent
		 */
		public void enterMethod(String method) {
			openBody(method);
			super.enterMethod(method);
		}

		/**
		 * When source=agent
		 */
		public void call(Agent target, String method) {
			String currentMethod = currentMethod();
			final StringBuffer body = bodyFor(currentMethod);
			if (target.equals(agent)) {
				// reflexive call
				body.append("\n    " + method);
			} else {
				body.append("\n    " + target.getName() + "." + method);
			}
			body.append(";");
		}

		/**
		 * When target=agent
		 */
		public Object exitMethod() {
			final String method = (String) super.exitMethod();
			closeBody(method);
			return method;
		}

		public String getBody() {
			final StringBuffer out = new StringBuffer();
			final String type = agent.getType() == null ? "Example" : agent.getType();

			out.append("\n// " + type + ".java");
			out.append("\n\npublic class " + type + " {");
			out.append("\n  //...");

			Iterator it = methodBodies.values().iterator();
			while (it.hasNext()) {
				final StringBuffer body = (StringBuffer) it.next();
				out.append("\n");
				out.append(body.toString());
			}

			out.append("\n}\n");
			return out.toString();
		}

		public String toString() {
			return "Snippet " + super.toString();
		}
	}

}
