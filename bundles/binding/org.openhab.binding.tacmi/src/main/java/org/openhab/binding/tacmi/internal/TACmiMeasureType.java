/**
 * Copyright (c) 2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tacmi.internal;

import org.apache.commons.lang.StringUtils;

/**
 * This enum holds all the different measures and states available to be
 * retrieved by the TACmi binding Not the complete list yet.
 *        
 * @author Timo Wendt
 * @since 1.8.0
 */
public enum TACmiMeasureType {
	NONE(0), TEMPERATURE(1), SECONDS(4), UNSUPPORTED(-1);

	int measure;

	private TACmiMeasureType(int measure) {
		this.measure = measure;
	}

	public int getMeasure() {
		return measure;
	}

	/**
	 * Return the measure type for the specified name.
	 * 
	 * @param measure
	 * @return
	 */
	public static TACmiMeasureType fromString(String measure) {
		if (!StringUtils.isEmpty(measure)) {
			for (TACmiMeasureType measureType : TACmiMeasureType.values()) {
				if (measureType.toString().equalsIgnoreCase(measure)) {
					return measureType;
				}
			}
		}
		throw new IllegalArgumentException("Invalid measure: " + measure);
	}
}
