/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter.state;

import org.openhab.core.library.types.PercentType;

/**
 * Converts from a {@link Integer} to a {@link PercentType}
 * Only processes for the BATTERY command class.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class BatteryPercentTypeConverter extends
		ZWaveStateConverter<Integer, PercentType> {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PercentType convert(Integer value) {
		if (value <= 0 || value == 0xFF)
			return PercentType.ZERO;
		else if (value > 0 && value < 100)
			return new PercentType(value);
		else
			return PercentType.HUNDRED;
	}
}
