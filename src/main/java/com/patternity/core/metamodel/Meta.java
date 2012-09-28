package com.patternity.core.metamodel;

/**
 * Declares the fact that this instance describes something about patterns, such
 * as the ordering of their roles in structural relationships or dynamic
 * interactions.
 * 
 * @author cyrille martraire
 */
public interface Meta {

	/**
	 * Meta pattern
	 * 
	 * @author cyrille martraire
	 */
	public static final class MetaPattern implements Meta, Pattern {

		private final String name;

		public MetaPattern(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		/**
		 * @return true if this MetaPattern is equal to the given MetaPattern
		 */
		public boolean equals(Object arg0) {
			if (!(arg0 instanceof MetaPattern)) {
				return false;
			}
			final MetaPattern other = (MetaPattern) arg0;
			if (this == other) {
				return true;
			}
			return other.name.equals(name);
		}

		public int hashCode() {
			return name.hashCode();
		}

		public String toString() {
			return "MetaPattern: " + getName();
		}

	}

}
