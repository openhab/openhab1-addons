/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

/**
 * @author Rob Nielsen
 * @since 1.8.0
 * 
 *         This enum holds all the different pressure units for the Netatmo
 *         binding, along with conversion methods
 */
public enum NetatmoPressureUnit {
	MBAR("mbar"), INHG("inHg"), MMHG("mmHg");

	public static final NetatmoPressureUnit DEFAULT_PRESSURE_UNIT = NetatmoPressureUnit.MBAR;

	private static final BigDecimal MBAR_TO_INHG = new BigDecimal("0.0295");

	private static final BigDecimal MBAR_TO_MMHG = new BigDecimal("0.7500");

	String pressureUnit;

	private NetatmoPressureUnit(String pressureUnit) {
		this.pressureUnit = pressureUnit;
	}

	public String getpressureUnit() {
		return pressureUnit;
	}

	public static NetatmoPressureUnit fromString(String pressureUnit) {
		if (!StringUtils.isEmpty(pressureUnit)) {
			for (NetatmoPressureUnit pressureUnitType : NetatmoPressureUnit
					.values()) {
				if (pressureUnitType.toString().equalsIgnoreCase(pressureUnit)) {
					return pressureUnitType;
				}
			}
		}
		throw new IllegalArgumentException("Invalid pressureUnit: "
				+ pressureUnit);
	}
	
	/**
	 * Convert to appropriate measurement.
	 *
	 * The Barometer is accurate to +-1 mbar or +- 0.03 inHg
	 *
	 * @param value
	 *            pressure in mbars
	 *
	 * @return value in proper measurement
	 */
	public BigDecimal convertPressure(BigDecimal value) {
		if (this == DEFAULT_PRESSURE_UNIT) {
			return value;
		} else if (this == INHG) {
			return value.multiply(MBAR_TO_INHG);
		} else { // MMGH
			return value.multiply(MBAR_TO_MMHG);
		}
	}
}
