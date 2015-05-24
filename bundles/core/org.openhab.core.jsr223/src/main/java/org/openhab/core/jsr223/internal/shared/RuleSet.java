/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
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
