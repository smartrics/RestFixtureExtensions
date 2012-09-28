package com.patternity.core.metamodel;

/**
 * Represents a relation pattern, to express relationships between participants.
 * 
 * @pattern Taxonomy
 * 
 * @author cyrille martraire
 */
public interface Relation extends Pattern {

	String getKind();

	PatternOccurrence newOccurrence(Element source, Element target);

	public static final String INHERITANCE = "INHERITANCE";

	public static final String DELEGATION = "DELEGATION";

	public static final String DEPENDENCY = "DEPENDENCY";

	// typical creation link
	public static final String ALLOCATION = "ALLOCATION";

	// typical pattern link
	public static final String COLLABORATION = "COLLABORATION";

	// typical comment link
	public static final String DESCRIPTION = "DESCRIPTION";

	// usually not rendered as such
	public static final String CONTAINMENT = "CONTAINMENT";

	// typical invisible link
	public static final String GHOST = "GHOST";

	public static class RelationPattern implements Relation {

		private final String kind;

		public RelationPattern(String kind) {
			this.kind = kind;
		}

		public String getKind() {
			return kind;
		}

		public String getName() {
			return kind;
		}

		public PatternOccurrence newOccurrence(Element source, Element target) {
			final PatternOccurrence occ = new PatternOccurrence(this);
			occ.add(new Role(Role.SOURCE, source));
			occ.add(new Role(Role.TARGET, target));
			return occ;
		}

		public String toString() {
			return "RelationPattern " + getKind();
		}
	};

	public static final Relation INHERITANCE_RELATION = new RelationPattern(INHERITANCE);

	public static final Relation DELEGATION_RELATION = new RelationPattern(DELEGATION);

	public static final Relation ALLOCATION_RELATION = new RelationPattern(ALLOCATION);

	public static final Relation COLLABORATION_RELATION = new RelationPattern(COLLABORATION);
}
