package com.patternity.core.metamodel;

/**
 * Represents a pattern occurrence made of several layers (levels: DEEP,
 * SHALLOW, META)
 * 
 * @author cyrille martraire
 */
public class LayeredOccurrence {

	public final static int DEEP = 0;
	public final static int SHALLOW = 1;
	public final static int META = 2;

	private final Pattern pattern;
	private final PatternOccurrence deep;
	private final OccurrenceTemplate shallow;
	private final OccurrenceTemplate meta;

	public LayeredOccurrence(Pattern pattern) {
		this.pattern = pattern;

		deep = new PatternOccurrence(pattern);
		shallow = new OccurrenceTemplate(pattern);
		meta = new OccurrenceTemplate(pattern);

	}

	public Pattern getPattern() {
		return pattern;
	}

	public PatternOccurrence deepLayer() {
		return deep;
	}

	public OccurrenceTemplate shallowLayer() {
		return shallow;
	}

	public OccurrenceTemplate metaLayer() {
		return meta;
	}

	public String toString() {
		return "LayeredOccurrence";
	}
}
