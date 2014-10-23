/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import java.math.BigDecimal;

import org.openhab.core.library.types.DecimalType;
/**
 * Converts between openHAB DecimalType and Homematic values.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class DecimalTypeConverter extends AbstractNumberTypeConverter<DecimalType> {

	/**
	 * Creates a DecimalType from the value.
	 */
	@Override
	protected DecimalType createType(BigDecimal value) {
		return new DecimalType(value);
	}

}
