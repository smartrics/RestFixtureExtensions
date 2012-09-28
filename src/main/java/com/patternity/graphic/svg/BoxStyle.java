package com.patternity.graphic.svg;

import com.patternity.graphic.Rectangle;
import com.patternity.graphic.Position;
import com.patternity.graphic.Style;
import com.patternity.util.Named;

/**
 * Represents the style of a box to use to render it as expected
 * 
 * @author cyrille martraire
 * @pattern Immutable
 */
public abstract class BoxStyle implements Style {

	public abstract Rectangle evaluate(final Position pos, final Named named, BoxRenderer renderer);

	public final static BoxStyle TYPE = new BoxStyle() {

		public Rectangle evaluate(final Position pos, final Named named, BoxRenderer renderer) {
			return renderer.printType(pos, named);
		}

		public String toString() {
			return "BoxStyle: TYPE";
		}
	};

	public final static BoxStyle NOTE = new BoxStyle() {

		public Rectangle evaluate(final Position pos, final Named named, BoxRenderer renderer) {
			return renderer.printNote(pos, named);
		}

		public String toString() {
			return "BoxStyle: NOTE";
		}
	};

	public final static BoxStyle PATTERN = new BoxStyle() {

		public Rectangle evaluate(final Position pos, final Named named, BoxRenderer renderer) {
			return renderer.printPattern(pos, named);
		}

		public String toString() {
			return "BoxStyle: PATTERN";
		}

	};

	public final static BoxStyle SET = new BoxStyle() {

		public Rectangle evaluate(final Position pos, final Named named, BoxRenderer renderer) {
			return renderer.printSet(pos, named);
		}

		public String toString() {
			return "BoxStyle: SET";
		}

	};

}
