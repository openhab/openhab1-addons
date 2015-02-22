/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal.cosem;

import java.text.ParseException;

import org.openhab.core.library.types.DecimalType;

/**
 * CosemFloat represents a float value
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public class CosemFloat extends CosemValue<DecimalType> {

	/**
	 * Creates a new CosemFloat
	 * 
	 * @param unit
	 *            the unit of the value
	 * @param bindingSuffix
	 *            the suffix to use for the DSMR binding identifier
	 */
	public CosemFloat(String unit, String bindingSuffix) {
		super(unit, bindingSuffix);
	}

	/**
	 * Parses a String value (that represents a float) to an openHAB DecimalType
	 * 
	 * @param cosemValue
	 *            the value to parse
	 * @return {@link DecimalType} on success
	 * @throws ParseException
	 *             if parsing failed
	 */
	@Override
	protected DecimalType parse(String cosemValue) throws ParseException {
		try {
			return new DecimalType(Float.parseFloat(cosemValue));
		} catch (NumberFormatException nfe) {
			throw new ParseException("Failed to parse value " + value
					+ " as float", 0);
		}
	}
}
