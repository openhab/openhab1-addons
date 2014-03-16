/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.omnilink.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps item types from the binding string to a ENUM value
 * @author daniel
 *
 */
public enum OmniLinkItemType{
	UNIT("unit"),
	THERMO_HEAT_POINT("thermo_heat_point"),
	THERMO_COOL_POINT("thermo_cool_point"),
	THERMO_SYSTEM_MODE("thermo_system_mode"),
	THERMO_FAN_MODE("thermo_fan_mode"),
	THERMO_HOLD_MODE("thermo_hold_mode"),
	THERMO_TEMP("thermo_temp"),
	ZONE_STATUS_CURRENT("zone_status_current"),
	ZONE_STATUS_LATCHED("zone_status_latched"),
	ZONE_STATUS_ARMING("zone_status_arming"),
	ZONE_STATUS_ALL("zone_status_all"),
	AREA_STATUS_MODE("area_status_mode"),
	AREA_STATUS_ALARM("area_status_alarm"),
	AREA_STATUS_EXIT_DELAY("area_status_exit_delay"),
	AREA_STATUS_ENTRY_DELAY("area_status_entry_delay"),
	AREA_EXIT_TIMER("area_status_exit_timer"),
	AREA_ENTRY_TIMER("area_status_entry_timer"),
	AUX_STATUS("aux_status"),
	AUX_CURRENT("aux_current"),
	AUX_LOW("aux_low"),
	AUX_HIGH("aux_hi"),
	AUDIOZONE_POWER("audiozone_power"),
	AUDIOZONE_SOURCE("audiozone_source"),
	AUDIOZONE_VOLUME("audiozone_volume"),
	AUDIOZONE_MUTE("audiozone_mute"),
	AUDIOZONE_KEY("audiozone_key"),
	AUDIOZONE_TEXT("audiozone_text"), //maybe a json object of fields?
	AUDIOZONE_TEXT_FIELD1("audiozone_field1"),
	AUDIOZONE_TEXT_FIELD2("audiozone_field2"),
	AUDIOZONE_TEXT_FIELD3("audiozone_field3"),
	AUDIOSOURCE_TEXT("audiosource_text"),
	AUDIOSOURCE_TEXT_FIELD1("audiosource_field1"),
	AUDIOSOURCE_TEXT_FIELD2("audiosource_field2"),
	AUDIOSOURCE_TEXT_FIELD3("audiosource_field3"),
	BUTTON("button");
	
	/*
	 * The string found in a binding configuration
	 */
	private String label;
	
	/*
	 * Lookup map to get a OmniLinkItemType from its label
	 */
	private static Map<String, OmniLinkItemType> labelToOmniLinkItemType;

	private OmniLinkItemType(String label) {
		this.label = label;
	}
	
	private static void initMapping() {
		labelToOmniLinkItemType = new HashMap<String, OmniLinkItemType>();
		for (OmniLinkItemType s : values()) {
			labelToOmniLinkItemType.put(s.label, s);
		}
	}

	/**
	 * Returns the label of the ReportValues enumeration
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
	public static OmniLinkItemType getOmniLinkItemType(String label) {
		if (labelToOmniLinkItemType == null) {
			initMapping();
		}
		return labelToOmniLinkItemType.get(label);
	}
		
}

