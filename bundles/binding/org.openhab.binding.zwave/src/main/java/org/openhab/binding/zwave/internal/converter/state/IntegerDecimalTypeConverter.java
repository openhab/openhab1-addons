/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter.state;

import org.openhab.core.library.types.DecimalType;

/**
 * Converts from an {@link Integer} to a {@link DecimalType}
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class IntegerDecimalTypeConverter extends
		ZWaveStateConverter<Integer, DecimalType> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DecimalType convert(Integer value) {
		return new DecimalType(value);
	}

}
