/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rme;

import java.io.InvalidClassException;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents all valid value selectors which could be processed by this
 * binding.
 * 
 * @author Karel Goderis
 * @since 1.5.0
 */
public enum RMEValueSelector {


	LEVEL ("WaterLevel", PercentType.class,1),
	MODE ("Mode", OnOffType.class,2),
	SOURCE ("Source", OnOffType.class,3),
	EXITPUMP ("ExitPump", OnOffType.class,4),
	ENTRYPUMP ("EntryPump", OnOffType.class,5),
	WATEREXCHANGE ("WaterExchange", OnOffType.class,6),
	CISTERNSUPPLY ("CisternSupply", OnOffType.class,7),
	OVERFLOWALARM ("OverflowAlarm", OnOffType.class,8),
	CISTERNBLOCKEDALARM ("CisternBlockedAlarm", OnOffType.class,9),
	FILTERCLEANING ("FilterCleaning", OnOffType.class,10)
	;

	static final Logger logger = LoggerFactory.getLogger(RMEValueSelector.class);
	
	private final String text;
	private Class<? extends Type> typeClass;
	private int fieldIndex;

	private RMEValueSelector(final String text, Class<? extends Type> typeClass, int index) {
		this.text = text;
		this.typeClass = typeClass;
		this.fieldIndex = index;
	}

	@Override
	public String toString() {
		return text;
	}

	public Class<? extends Type> getTypeClass() {
		return typeClass;
	}
	
	public int getFieldIndex() {
		return fieldIndex;
	}

	/**
	 * Procedure to validate selector string.
	 * 
	 * @param valueSelector
	 *            selector string e.g. RawData, Command, Temperature
	 * @return true if item is valid.
	 * @throws IllegalArgumentException
	 *             Not valid value selector.
	 * @throws InvalidClassException
	 *             Not valid class for value selector.
	 */
	public static boolean validateBinding(String valueSelector,
			Item item) throws IllegalArgumentException,
			InvalidClassException {

		for (RMEValueSelector c : RMEValueSelector.values()) {
			if (c.text.equals(valueSelector) && item !=null) {

				logger.debug("Accepted types are {}",item.getAcceptedDataTypes());
				logger.debug("typeclass is {}",c.getTypeClass());
				
				if (item.getAcceptedDataTypes().contains(c.getTypeClass()))
					return true;
				else
					throw new InvalidClassException(
							"Not valid class for value selector");
			}
		}

		throw new IllegalArgumentException("Not valid value selector");

	}

	/**
	 * Procedure to convert selector string to value selector class.
	 * 
	 * @param valueSelectorText
	 *            selector string e.g. RawData, Command, Temperature
	 * @return corresponding selector value.
	 * @throws InvalidClassException
	 *             Not valid class for value selector.
	 */
	public static RMEValueSelector getValueSelector(String valueSelectorText)
			throws IllegalArgumentException {

		for (RMEValueSelector c : RMEValueSelector.values()) {
			if (c.text.equals(valueSelectorText)) {
				return c;
			}
		}

		throw new IllegalArgumentException("Not valid value selector");
	}

}