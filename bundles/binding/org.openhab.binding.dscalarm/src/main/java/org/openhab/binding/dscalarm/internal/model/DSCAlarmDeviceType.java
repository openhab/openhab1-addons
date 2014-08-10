/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm.internal.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to map device types from the binding string to a ENUM value
 * @author Russell Stephens
 * @since 1.6.0
 */
public enum DSCAlarmDeviceType {
	PANEL("panel"),
	PARTITION("partition"),
	ZONE("zone"),
	KEYPAD("keypad");
	
	/*
	 * The string found in a binding configuration
	 */
	private String label;
	
	/*
	 * Lookup map to get a DSCAlarmDeviceType from its label
	 */
	private static Map<String, DSCAlarmDeviceType> labelToDSCAlarmDeviceType;

	private DSCAlarmDeviceType(String label) {
		this.label = label;
	}
	
	private static void initMapping() {
		labelToDSCAlarmDeviceType = new HashMap<String, DSCAlarmDeviceType>();
		for (DSCAlarmDeviceType s : values()) {
			labelToDSCAlarmDeviceType.put(s.label, s);
		}
	}

	/**
	 * Returns the label of the DSCAlarmItemType Values enumeration
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Lookup function based on the binding type label.
	 * Returns null if the binding type is not found.
	 * @param label the label to lookup
	 * @return enumeration value of the binding type.
	 */
	public static DSCAlarmDeviceType getDSCAlarmDeviceType(String label) {
		if (labelToDSCAlarmDeviceType == null) {
			initMapping();
		}
		return labelToDSCAlarmDeviceType.get(label);
	}
		
}
