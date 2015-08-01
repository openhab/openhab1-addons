/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enphaseenergy.internal;

import org.apache.commons.lang.StringUtils;

/**
 * @author Markus Fritze
 * @since 1.7.0
 * 
 * This enum holds all the different measures and states available
 * to be retrieved by the Enphase Energy binding
 */
public enum EnphaseenergyItemType {
	MODULES("modules"),
	SIZE_W("size_w"),
	CURRENT_POWER("current_power"),
	ENERGY_TODAY("energy_today"),
	ENERGY_LIFETIME("energy_lifetime"),
	SUMMARY_DATE("summary_date"),
	SOURCE("source"),
	STATUS("status"),
	OPERATIONAL_AT("operational_at"),
	LAST_REPORT_AT("last_report_at");

	String measure;

	private EnphaseenergyItemType(String measure) {
		this.measure = measure;
	}

	public String getMeasure() {
		return measure;
	}
	
	public static EnphaseenergyItemType fromString(String measure) {
		if (!StringUtils.isEmpty(measure)) {
			for (EnphaseenergyItemType measureType : EnphaseenergyItemType.values()) {
				if (measureType.toString().equalsIgnoreCase(measure)) {
					return measureType;
				}
			}
		}
		throw new IllegalArgumentException("Invalid measure: " + measure);
	}
}
