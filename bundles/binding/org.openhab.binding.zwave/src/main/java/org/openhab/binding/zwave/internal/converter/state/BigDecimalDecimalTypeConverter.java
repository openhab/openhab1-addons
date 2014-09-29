/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter.state;

import java.math.BigDecimal;

import org.openhab.core.library.types.DecimalType;

/**
 * Converts from an {@link BigDecimal} to a {@link DecimalType}
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class BigDecimalDecimalTypeConverter extends
		ZWaveStateConverter<BigDecimal, DecimalType> {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DecimalType convert(BigDecimal value) {
		return new DecimalType(value);
	}

}
