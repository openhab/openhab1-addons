/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal.converter.state;

import org.openhab.binding.vera.internal.converter.VeraStateConverter;
import org.openhab.core.library.types.DecimalType;

/**
 * Converts an {@link Integer} value into a {@link DecimalType} state.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class IntegerDecimalTypeConverter extends VeraStateConverter<Integer, DecimalType> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DecimalType convert(Integer value) {
		return new DecimalType(value.longValue());
	}

}
