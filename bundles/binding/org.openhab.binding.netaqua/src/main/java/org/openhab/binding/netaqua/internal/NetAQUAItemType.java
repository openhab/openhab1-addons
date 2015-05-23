/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netaqua.internal;

import org.apache.commons.lang.StringUtils;

/**
 * @author Markus Fritze
 * @since 1.7.0
 * 
 * This enum holds all the different measures and states available
 * to be retrieved by the Enphase Energy binding
 */
public enum NetAQUAItemType {
	LAST_WATERING_CYCLE("last_cycle"),
	NEXT_WATERING_CYCLE("next_cycle"),
	LOCAL_TEMPERATURE("ext_temperature"),
	SYSTEM_TEMPERATURE("int_temperature"),
	LOCAL_RAINFALL("rainfall"),
	MODEL("nA_model"),
	NAME("netaqua_alias"),
	CURRENTLY_WATERING("watering"),
	CURRENT_CYCLE("cycle_nbr"),
	CURRENT_ZONE("zone_nbr"),
	WATERING_TIME_LEFT("time_left"),
	SERIAL_NUMBER("nA_hw_ver"),
	APPLICATION_VERSION("nA_app_ver"),
	BOOTLOADER_VERSION("nA_boot_ver"),
	ZONE_NAME("a_z_alias"),
	CYCLE_NAME("cycle_alias"),
	CYCLE_FREQUENCY("frequency_type"),
	CYCLE_ENABLED("checkbox2_"),
	CYCLE_START_HOUR12("hour12_"),
	CYCLE_START_MINUTE("minute_"),
	CYCLE_START_AMPM("ampm_"),
	CYCLE_DURATION("cycle_duration"),
	UPTIME("nA_uptime"),
	TEST_ALL_ZONES("test_all_zones"),
	TEST_NEXT_ZONE("test_next_zones"),
	WATERING_NOTIFICATIONS("watering_notifications"),
	RUN_CYCLE("run_cycle"),
	RUN_ZONE("run_zone");

	String item;

	private NetAQUAItemType(String item) {
		this.item = item;
	}

	public String getItem() {
		return item;
	}
	
	public static NetAQUAItemType fromString(String item) {
		if (!StringUtils.isEmpty(item)) {
			for (NetAQUAItemType itemType : NetAQUAItemType.values()) {
				if (itemType.toString().equalsIgnoreCase(item)) {
					return itemType;
				}
			}
		}
		throw new IllegalArgumentException("Invalid item: " + item);
	}
}
