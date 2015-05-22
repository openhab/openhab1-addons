package org.openhab.core.jsr223.internal.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class holds all rules implemented by a script
 * 
 * @author Simon Merschjohann
 * @since 1.7.0
 * 
 */
public class RuleSet {
	private List<Rule> rules;

	public RuleSet() {
		this.rules = new ArrayList<Rule>();
	}

	public RuleSet(Rule... rules) {
		this.rules = Arrays.asList(rules);
	}

	public void addRule(Rule rule) {
		this.rules.add(rule);
	}

	public void removeRule(Rule rule) {
		this.rules.remove(rule);
	}

	public List<Rule> getRules() {
		return this.rules;
	}
}
