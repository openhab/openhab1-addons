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
 * HarmonyHubBindingType is a {@link Enum} of possible binding types that an item can be configured for. 
 * @author Dan Cunningham
 * @since 1.7.0
 */
public enum HarmonyHubBindingType {
	CurrentActivity("currentActivity", HarmonyHubBindingDirection.BOTH),
	StartActivity("start", HarmonyHubBindingDirection.OUT),
	PressButton("press", HarmonyHubBindingDirection.OUT);
	
	/**
	 * String used in the binding config
	 */
	private String label;
	/**
	 * Direction that this command goes
	 */
	private HarmonyHubBindingDirection direction;

	/**
	 * helper map retrieve HarmonyHubBindingType by it's label
	 */
	private static Map<String,HarmonyHubBindingType> labelToHarmonyHubBindingType;

	HarmonyHubBindingType(String label, HarmonyHubBindingDirection direction) {
		this.label = label;
		this.direction = direction;
	}

	/**
	 * init our internal map
	 */
	private static void initMapping() {
		labelToHarmonyHubBindingType = new HashMap<String, HarmonyHubBindingType>();
		for(HarmonyHubBindingType h : values()) {
			labelToHarmonyHubBindingType.put(h.label, h);
		}
	}

	/**
	 * Returns the {@link String} label used in bindings configurations
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	public HarmonyHubBindingDirection getDirection() {
		return direction;
	}

	public static HarmonyHubBindingType getHarmonyHubBindingType(String label) {
		if(labelToHarmonyHubBindingType == null) {
			initMapping();
		}
		return labelToHarmonyHubBindingType.get(label);
	}

	public static String PatternString() {
		StringBuilder sb = null;
		for(HarmonyHubBindingType h : values()) {
			if(sb == null) {
				sb = new StringBuilder(h.label);
			} else {
				sb.append("|").append(h.label);
			}
		}
		return sb.toString();
	}
	
	@Override
	public String toString(){
		return getLabel();
	}
}
