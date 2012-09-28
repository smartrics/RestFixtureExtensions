package com.patternity.graphic.layout.sequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.patternity.graphic.behavioral.Activation;
import com.patternity.graphic.behavioral.Agent;
import com.patternity.graphic.behavioral.Event;
import com.patternity.graphic.behavioral.Message;
import com.patternity.graphic.behavioral.Note;
import com.patternity.graphic.dag.Node;
import com.patternity.graphic.svg.Graphic;

/**
 * A sequence layout
 * 
 * @author cyrille martraire
 */
public class SequenceLayout {

    private static final int TIME_STEP = 30;

	private static final int AGENT_STEP = 200;

	private static final int HALF_ACTIVATION_WIDTH = 10;

	private static final int ACTIVATION_WIDTH = 2 * HALF_ACTIVATION_WIDTH;

	private final int fontsize;

	private final Map xAgents = new HashMap();

	private final Map activations = new HashMap();

	private int width = 0;
	private int height = 0;

	private int x = 0;
	private int y = 0;

    private int timeStep = TIME_STEP;
    private int agentStep = AGENT_STEP;

	private final Graphic g = new Graphic();

	public SequenceLayout(int fontSize) {
		fontsize = 16;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

    public int getTimeStep() {
        return timeStep;
    }

    public int getAgentStep() {
        return agentStep;
    }

    public void setAgentStep(int agentStep) {
        this.agentStep = agentStep;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }
	public String layout(Node root) {
		x = 0;
        y = timeStep;

		final List agents = collectAgents(root);

        final int totalActivationLength = (getActivationTime(root) + 2) * timeStep;
        height = totalActivationLength + 2 * timeStep;
        width = agents.size() * agentStep + 2 * timeStep;

		g.drawDescription("Agents and lifelines of the sequence diagram");

		printLifelines(agents, totalActivationLength);

		g.drawDescription("Messages");
		walk(root);

		return g.toString();
	}

	private void printLifelines(Collection agents, int height) {
        x += agentStep;

		Iterator it = agents.iterator();
		while (it.hasNext()) {
			final Agent agent = (Agent) it.next();
			if (agent == null) {
				continue;
			}
			xAgents.put(agent, new Integer(x));

			final String style = "name";
			final String entityLabel = agent.isEllipsis() ? "..." : agent.getName() + ":" + agent.getType();
			g.drawText(x, y, entityLabel, style);

            final int lifeTop = y + timeStep / 2;
			final int lifeBottom = lifeTop + height;
			if (agent.hasLifeline()) {
				g.drawLine(x, lifeTop, x, lifeBottom, "lifeline");
			}

            x += agentStep;
		}

		g.newline();
	}

	private void walk(Node node) {
		final Event event = (Event) node.getElement();
		if (event instanceof Message) {
			final Message message = (Message) event;
			processMessage(node, message);
		} else if (event instanceof Note) {
			final Note note = (Note) event;
			processNote(node, note);
		}
	}

	private void processNote(Node node, Note note) {
		final Agent target = note.getTarget();
		final int xNote = target.isAll() ? width / 2 : x(target);
		final String label = note.getLabel();
        y += timeStep;
		if (target.isAll()) {
            g.drawNote(0, y + fontsize / 2, width, timeStep, 2 * fontsize, "classbox");
		}
        y += timeStep;
		g.drawText(xNote, y, label, "name");
        y += timeStep;
	}

	private void processMessage(Node node, Message message) {
		final Agent source = source(node);
		final Agent target = message.getTarget();
        final String method = message.getMethod();
        final String result = message.getResult();

		printMessage(source, method, target, message);

		if (target == null) {
			final Node[] children = node.getNodes();
			for (int i = 0; i < children.length; i++) {
				final Node child = children[i];
				walk(child);
			}
			return;
		}

		final Activation activation = activationFor(target);
		activation.enterMethod(method);

        final int activationTime = getActivationTime(node) * timeStep;
		final int x = x(target, activation.depth());
		if (target.isActivable()) {
			printActivation(x, y, activationTime);
		}

		final Node[] children = node.getNodes();
		for (int i = 0; i < children.length; i++) {
			final Node child = children[i];
			walk(child);
		}

		activation.exitMethod();
        if (result != null) {
            printMessageResult(source, result, target, message);
        }
        y += timeStep;
	}

	protected Activation activationFor(Agent agent) {
		Activation activation = (Activation) activations.get(agent);
		if (activation == null) {
			activation = new Activation(agent);
			activations.put(agent, activation);
		}
		return activation;
	}

	public static int getActivationTime(Node node) {
		if (node.isLeaf()) {
			return 1;
		}
		int width = 0;
		final Node[] children = node.getNodes();
		for (int i = 0; i < children.length; i++) {
			final Node child = children[i];
			width += 1 + getActivationTime(child) + 1;
		}
		return width;
	}

	public static List collectAgents(Node node) {
		final List agents = new ArrayList();
		collectAgents(node, agents);
		return agents;
	}

	private static void collectAgents(Node node, final List agents) {
		final Event event = (Event) node.getElement();
		if (event.getTarget() != null && event.getTarget().isRegular() && !agents.contains(event.getTarget())) {
			agents.add(event.getTarget());
		}

		final Node[] children = node.getNodes();
		for (int i = 0; i < children.length; i++) {
			final Node child = children[i];
			collectAgents(child, agents);
		}
	}

	private Agent source(Node node) {
		if (node.isRoot()) {
			return Agent.ROOT;
		}
		final Event event = (Event) node.getParent().getElement();
		final Agent target = event.getTarget();
		return target == null ? Agent.ROOT : target;
	}

	private int x(final Agent agent, final int depth) {
		final Integer xEntity = (Integer) xAgents.get(agent);
		final int x = xEntity == null ? 0 : xEntity.intValue() + (depth - 1) * HALF_ACTIVATION_WIDTH;
		return x;
	}

	private int x(Agent agent) {
		if (agent.isRoot()) {
            y += timeStep;
			return 0;
		}
		final Activation activation = activationFor(agent);
		return x(agent, activation.depth());
	}

	protected void printActivation(final int x, final int y, final int activationTime) {
		final int x0 = x - HALF_ACTIVATION_WIDTH;
		g.drawRectangle(x0, y, ACTIVATION_WIDTH, activationTime, "classbox");
	}

    private void printMessage(Agent source, String method, Agent target, Message message) {
        y += timeStep;

        if (target == null || method == null) {
            return;
        }

        if (source == null) {// never happens
            printStraightMessage(Agent.ROOT, method, target, message);
            return;
        }

        if (target.equals(source)) {
            printReflectiveMessage(source, method);
        } else {
            printStraightMessage(source, method, target, message);
        }

    }

    private void printMessageResult(Agent source, String result, Agent target, Message message) {
        if (target == null || result == null) {
            return;
        }

        if (source == null) {// never happens
            printStraightMessage(target, result, Agent.ROOT, message);
            return;
        }

        if (target.equals(source)) {
            printReflectiveMessage(source, result);
        } else {
            printStraightMessage(target, result, source, message);
        }

    }

	private void printStraightMessage(Agent source, String method, Agent target, Message message) {
		final int xFrom = x(source);
		final int xTo = x(target);
		final boolean forward = xFrom < xTo;
		final int x1 = xFrom + (forward ? HALF_ACTIVATION_WIDTH : -HALF_ACTIVATION_WIDTH);
		final int x2 = xTo + (forward ? 0 : ACTIVATION_WIDTH);
		final String path = "M " + x1 + " " + y + " L " + x2 + " " + y;
		g.drawPath(path, "closedHead", message.isCreation() ? "dasharrow" : "arrow");

		final int xText = x1 + (forward ? HALF_ACTIVATION_WIDTH : -HALF_ACTIVATION_WIDTH);
		final int yText = y - fontsize / 2;
		final String style = forward ? "message" : "backmessage";
		g.drawText(xText, yText, method, style);
	}

	private void printReflectiveMessage(Agent source, String method) {
		final int x = x(source) + ACTIVATION_WIDTH;
		final int dx = fontsize * 2;
		final int dy = fontsize / 2;
		final String path = "M " + x + " " + y + " l " + dx + " 0 l 0 " + dy + " l -" + dx + " 0";
		g.drawPath(path, "closedHead", "arrow");

		final int xText = x + HALF_ACTIVATION_WIDTH + dx;
		final int yText = y + 12 - fontsize / 2;
		final String style = "message";
		g.drawText(xText, yText, method, style);
	}

}
