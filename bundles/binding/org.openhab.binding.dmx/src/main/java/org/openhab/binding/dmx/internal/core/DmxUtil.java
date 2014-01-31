/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.core;

import java.math.BigDecimal;

import org.openhab.core.library.types.PercentType;

/**
 * Utility class for converting and calculating DMX values.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxUtil {

	/**
	 * Hidden default constructor.
	 */
	private DmxUtil() {

	}

	/**
	 * Limit a value to the valid DMX range.
	 * 
	 * @param value
	 *            start value
	 * @return a value in the 0-255 range
	 */
	public static int capDmxValue(int value) {
		if (value > DmxChannel.DMX_MAX_VALUE) {
			return DmxChannel.DMX_MAX_VALUE;
		} else if (value < DmxChannel.DMX_MIN_VALUE) {
			return DmxChannel.DMX_MIN_VALUE;
		} else {
			return value;
		}
	}

	/**
	 * Convert a percent type to a 0-255 scale value.
	 * 
	 * @param pt
	 *            percent type to convert
	 * @return converted value 0-255
	 */
	public static int getByteFromPercentType(PercentType pt) {
		return pt
				.toBigDecimal()
				.multiply(BigDecimal.valueOf(DmxChannel.DMX_MAX_VALUE))
				.divide(PercentType.HUNDRED.toBigDecimal(), 0,
						BigDecimal.ROUND_UP).intValue();
	}

	/**
	 * Convert a 0-255 scale value to a percent type.
	 * 
	 * @param pt
	 *            percent type to convert
	 * @return converted value 0-255
	 */
	public static PercentType getPercentTypeFromByte(int value) {
		return new PercentType(BigDecimal
				.valueOf(value)
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(DmxChannel.DMX_MAX_VALUE), 0,
						BigDecimal.ROUND_UP).intValue());
	}

	/**
	 * For a given RGB value, calculate the missing W value.
	 * 
	 * @param red
	 *            input value
	 * @param green
	 *            input value
	 * @param blue
	 *            input value
	 * @return white value
	 */
	public static int calculateWhite(int red, int green, int blue) {
		int low = (red <= green)
				? ((red <= blue) ? red : blue)
				: ((green <= blue) ? green : blue);
		int high = (red >= green)
				? ((red >= blue) ? red : blue)
				: ((green >= blue) ? green : blue);
		if (high == 0) {
			return 0;
		}
		int saturation = 100 * ((high - low) / high);
		return (DmxChannel.DMX_MAX_VALUE - saturation)
				/ DmxChannel.DMX_MAX_VALUE * (red + green + blue) / 3;
	}

	/**
	 * Dim the provided input value to the given output level.
	 * 
	 * @return dimmed value
	 */
	public static int getOutputValue(int input, int outputLevel) {
		BigDecimal value = BigDecimal.valueOf(input);
		return value
				.multiply(BigDecimal.valueOf(outputLevel))
				.divide(PercentType.HUNDRED.toBigDecimal(), 0,
						BigDecimal.ROUND_UP).intValue();
	}
}
