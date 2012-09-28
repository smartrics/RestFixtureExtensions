package com.patternity.graphic.behavioral;

/**
 * Represents a note with a label that can be assigned to one agent or overall
 * for every agent.
 * 
 * @author cyrille martraire
 */
public class Note extends Event {

	private final String label;

	public Note(String label) {
		this(Agent.ALL, label);
	}

	public Note(Agent agent, String label) {
		super(0, agent);
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String toString() {
		return getTarget().isAll() ? getLabel() : getTarget() + ": " + getLabel();
	}

}