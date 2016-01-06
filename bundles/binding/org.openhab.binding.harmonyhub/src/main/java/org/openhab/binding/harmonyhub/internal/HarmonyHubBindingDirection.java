/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.harmonyhub.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Bindings may be inbound, outbound or both directions.
 * @author Dan Cunningham
 *
 */
public enum HarmonyHubBindingDirection {
	IN("<"),
	OUT(">"),
	BOTH("*");

	private String label;

	private static Map<String,HarmonyHubBindingDirection> labelToHarmonyHubBindingDirection;

	HarmonyHubBindingDirection(String label) {
		this.label = label;
	}

	private static void initMapping() {
		labelToHarmonyHubBindingDirection = new HashMap<String, HarmonyHubBindingDirection>();
		for(HarmonyHubBindingDirection h : values()) {
			labelToHarmonyHubBindingDirection.put(h.label, h);
		}
	}

	public String getLabel() {
		return label;
	}

	public static HarmonyHubBindingDirection getHarmonyHubBindingDirection(String label) {
		if(labelToHarmonyHubBindingDirection == null) {
			initMapping();
		}
		return labelToHarmonyHubBindingDirection.get(label);
	}
}
