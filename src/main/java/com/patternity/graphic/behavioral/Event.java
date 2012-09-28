package com.patternity.graphic.behavioral;

/**
 * Represents any dynamic event that can happen. It can be overall for every
 * agent (e-g. loop or general comment), or assigned to one particular agent
 * (e-g. Message).
 * 
 * @author cyrille martraire
 */
public class Event {

	private final int kind;

	private final Agent target;

	public Event(int kind, Agent target) {
		this.kind = kind;
		this.target = target;
	}

	public Agent getTarget() {
		return target;
	}

	public int getKind() {
		return kind;
	}

}