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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.dsmr.internal.DSMRPort;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CosemValue represents the mapping between COSEM formatted values and openHAB
 * type values
 * 
 * @author M. Volaart
 * @since 1.7.0
 * @param <T>
 *            the openHAB type (subclass of {@link State})
 */
public abstract class CosemValue<T extends State> {
	/* logger */
	private static final Logger logger = LoggerFactory
			.getLogger(DSMRPort.class);

	/** openHAB value */
	protected T value;

	/* unit of this cosemValue */
	private final String unit;

	/* DSMR item identifier used by openHAB items */
	private final String dsmrItemId;

	/**
	 * Creates a CosemValue
	 * 
	 * @param unit
	 *            the unit of the value
	 * @param dsmrItemId
	 *            the DSMR item identifier
	 */
	protected CosemValue(String unit, String dsmrItemId) {
		if (dsmrItemId == null) {
			logger.warn("dsmrBindingId = null is not supported, using empty String");
			dsmrItemId = "";
		}
		this.dsmrItemId = dsmrItemId;
		this.unit = unit;
	}

	/**
	 * Parses the string value to the openHAB type
	 * 
	 * @param cosemValue
	 *            the COSEM value to parse
	 * @return T the openHAB value of this COSEM value
	 * @throws ParseException
	 *             if parsing failed
	 */
	protected abstract T parse(String cosemValue) throws ParseException;

	/**
	 * Sets the value of this CosemValue
	 * <p>
	 * This method will automatically parse the unit and the value of the COSEM
	 * value string
	 * 
	 * @param cosemValue
	 *            the cosemValue
	 * @throws ParseException
	 *             if parsing failed
	 */
	public void setValue(String cosemValue) throws ParseException {
		if (unit.length() > 0) {
			// Check if COSEM value has a unit, check and parse the value
			Pattern p = Pattern.compile("(.*)\\*" + unit);
			Matcher m = p.matcher(cosemValue);

			if (m.matches()) {
				value = parse(m.group(1));
			} else {
				throw new ParseException("Unit of " + cosemValue + " is not "
						+ unit, 0);
			}
		} else {
			// COSEM value does not have a unit, parse value
			value = parse(cosemValue);
		}
	}

	/**
	 * Return the openHAB value
	 * 
	 * @return openHAB value subclass of {@link State}
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Returns the unit of this COSEM value
	 * 
	 * @return the unit of this COSEM value
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the dsmrItemId of this COSEM value
	 * 
	 * @return the dsmrItemId of this COSEM value
	 */
	public String getDsmrItemId() {
		return dsmrItemId;
	}

	/**
	 * Returns String representation of this CosemValue
	 * 
	 * @return String representation of this CosemValue
	 */
	public String toString() {
		if (value != null) {
			return value.toString();
		} else {
			return "CosemValue is not initialized yet";
		}
	}
}
