package com.patternity.graphic.behavioral;

/**
 * Represents an object that participates in a dynamic collaboration (typically
 * displayed in a sequence diagram)
 * 
 * @author cyrille martraire
 */
public class Agent {

	private static final String[] NO_STEREOTYPE = new String[0];

	private final String name;

	private final String type;

	private final String[] stereotypes;

	private final boolean isActivable;

	public static final Agent ROOT = new Agent(null, null, false, new String[] { "ROOT" });

	public static final Agent ALL = new Agent(null, null, false, new String[] { "ALL" });

	public static final Agent ELLIPSIS = new Agent("...", null, false, new String[] { "ELLIPSIS" });

	/**
	 * Generates a list of agent instances from this agent, with names
	 * post-fixed with the number index or "N" for the last one.
	 */
	public Agent[] agents1_N(final int n) {
		final Agent[] agents = new Agent[n];
		for (int i = 0; i < n; i++) {
			final String index = i == n - 1 ? "N" : String.valueOf(i + 1);
			final Agent agent = new Agent(type, name + " " + index, true);
			agents[i] = agent;
		}
		return agents;
	}

	public Agent(String type, String name) {
		this(type, name, true, NO_STEREOTYPE);
	}

	public Agent(String type, String name, boolean isActivable) {
		this(type, name, isActivable, NO_STEREOTYPE);
	}

	public Agent(String type, String name, boolean isActivable, String[] stereotypes) {
		this.type = type == null ? "" : type;
		this.name = name == null ? "" : name;
		this.stereotypes = stereotypes;
		this.isActivable = isActivable;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String[] getStereotypes() {
		return stereotypes;
	}

	public boolean isActivable() {
		return isActivable;
	}

	public boolean hasLifeline() {
		return !isRoot() && !isEllipsis();
	}

	public boolean isEllipsis() {
		return this == ELLIPSIS;
	}

	public boolean isAll() {
		return this == ALL;
	}

	public boolean isRoot() {
		return this == ROOT;
	}

	public boolean isRegular() {
		return this != ROOT && this != ALL;
	}

	/**
	 * @return true if this Agent is equal to the given Agent
	 */
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Agent)) {
			return false;
		}
		final Agent other = (Agent) arg0;
		if (this == other) {
			return true;
		}
		return toString().equals(other.toString());
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public String toString() {
		return isEllipsis() ? "..." : name + ":" + type;
	}
}