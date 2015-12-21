/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tacmi.internal.message;

import java.math.BigDecimal;

import org.openhab.binding.tacmi.internal.TACmiGenericBindingProvider;
import org.openhab.binding.tacmi.internal.TACmiMeasureType;
import org.openhab.core.library.types.DecimalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles analog values as used in the analog message.
 * 
 * @author Timo Wendt
 * @since 1.8.0
 */
public final class AnalogValue {
	public BigDecimal value;
	public TACmiMeasureType measureType;

	private static Logger logger = LoggerFactory.getLogger(AnalogValue.class);

	/**
	 * Create new AnalogValue with specified value and type
	 */
	public AnalogValue(int rawValue, int type) {
		switch (type) {
		case 1:
			measureType = TACmiMeasureType.TEMPERATURE;
			value = new BigDecimal(rawValue / 10.0);
			break;
		case 4:
			measureType = TACmiMeasureType.SECONDS;
			value = new BigDecimal(rawValue);
			break;
		default:
			measureType = TACmiMeasureType.UNSUPPORTED;
		}
	}
}
