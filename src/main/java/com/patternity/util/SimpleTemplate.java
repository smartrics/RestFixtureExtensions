package com.patternity.util;

import java.util.StringTokenizer;

import org.apache.commons.collections.Transformer;

/**
 * Very simple template engine that extracts $..$ variables (or any other delim
 * char) and evaluates them through a preset transformer.
 * 
 * @author cyrille martraire
 */
public class SimpleTemplate {
	private final String delim;

	private final Transformer transformer;

	public SimpleTemplate(final Transformer transformer) {
		this("$", transformer);
	}

	public SimpleTemplate(final String delim, final Transformer transformer) {
		this.delim = delim;
		this.transformer = transformer;
	}

	public String process(String template) {
		final StringBuffer buffer = new StringBuffer();
		final StringTokenizer st = new StringTokenizer(template, delim);
		while (st.hasMoreTokens()) {
			final String token = st.nextToken();
			final String variable = evaluate(token);
			if (variable != null) {
				buffer.append(variable);
			} else {
				buffer.append("(");
				buffer.append(token);
				buffer.append(")");
			}
		}
		return buffer.toString();
	}

	protected String evaluate(String token) {
		return (String) transformer.transform(token);
	}

	public String toString() {
		return "SimpleTemplate using " + transformer + " to evaluate variables in the form: " + delim + "..." + delim;
	}
}
