/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oceanic;

import java.io.InvalidClassException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openhab.core.items.Item;
import org.openhab.core.library.types.*;
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
public enum OceanicValueSelector {


	getSRN ("serial", StringType.class,ValueSelectorType.GET),
	getMAC ("mac", StringType.class,ValueSelectorType.GET),
	getDNA ("name", StringType.class,ValueSelectorType.GET),
	getSCR ("type", StringType.class,ValueSelectorType.GET)
	{	
		@Override
		public String convertValue(String value) {
			int index = Integer.valueOf(value);
			String convertedValue = value;
			switch (index) {
			case 0: 
				convertedValue = "Single";
				break;
			case 1: 
				convertedValue = "Double Alternative";
				break;
			case 2: 
				convertedValue = "Triple Alternative";
				break;			
			case 3: 
				convertedValue = "Double Parallel";
				break;			
			case 4: 
				convertedValue = "Triple Parallel";
				break;			
			case 5: 
				convertedValue = "Single Filter";
				break;			
			case 6: 
				convertedValue = "Double Filter";
				break;			
			case 7: 
				convertedValue = "Triple Filter";
				break;	
			default: break;
			}
			return convertedValue;
		}
	},
	getALM ("alarm", StringType.class,ValueSelectorType.GET)
	{	
		@Override
		public String convertValue(String value) {
			int index = Integer.valueOf(value);
			String convertedValue = value;
			switch (index) {
			case 0: 
				convertedValue = "No Alarm";
				break;
			case 1: 
				convertedValue = "Lack of salt during regeneration";
				break;
			case 2: 
				convertedValue = "Water pressure too low";
				break;			
			case 3: 
				convertedValue = "Water pressure too high";
				break;			
			case 4: 
				convertedValue = "Pressure sensor failure";
				break;			
			case 5: 
				convertedValue = "Camshaft failure";
				break;			
			default: break;
			}
			return convertedValue;
		}
	},
	getNOT ("alert", StringType.class,ValueSelectorType.GET)
	{	
		@Override
		public String convertValue(String value) {
			int index = Integer.valueOf(value);
			String convertedValue = value;
			switch (index) {
			case 0: 
				convertedValue = "No Alert";
				break;
			case 1: 
				convertedValue = "Immenent lack of salt";
				break;		
			default: break;
			}
			return convertedValue;
		}
	},
	getFLO ("totalflow", DecimalType.class,ValueSelectorType.GET),
	getRES ("reserve", DecimalType.class,ValueSelectorType.GET),
	getCYN ("cycle", StringType.class,ValueSelectorType.GET),
	getCYT ("endofcycle", StringType.class, ValueSelectorType.GET),
	getRTI ("endofgeneration", StringType.class, ValueSelectorType.GET),
	getWHU ("hardnessunit", StringType.class, ValueSelectorType.GET)
	{	
		@Override
		public String convertValue(String value) {
			int index = Integer.valueOf(value);
			String convertedValue = value;
			switch (index) {
			case 0: 
				convertedValue = "dH";
				break;
			case 1: 
				convertedValue = "fH";
				break;
			case 2: 
				convertedValue = "e";
				break;			
			case 3: 
				convertedValue = "mg CaCO3/l";
				break;			
			case 4: 
				convertedValue = "ppm";
				break;			
			case 5: 
				convertedValue = "mmol/l";
				break;			
			case 6: 
				convertedValue = "mval/l";
				break;				
			default: break;
			}
			return convertedValue;
		}
	},
	getIWH ("inlethardness", DecimalType.class,ValueSelectorType.GET),	
	getOWH ("outlethardness", DecimalType.class,ValueSelectorType.GET),
	getRG1 ("cylinderstate", StringType.class, ValueSelectorType.GET)
	{	
		@Override
		public String convertValue(String value) {
			int index = Integer.valueOf(value);
			String convertedValue = value;
			switch (index) {
			case 0: 
				convertedValue = "No regeneration";
				break;
			case 1: 
				convertedValue = "Paused";
				break;
			case 2: 
				convertedValue = "Regeneration";
				break;					
			default: break;
			}
			return convertedValue;
		}
	},
	setSV1 ("salt", DecimalType.class,ValueSelectorType.SET),
	getSV1 ("salt", DecimalType.class,ValueSelectorType.GET),
	setSIR ("regeneratenow", OnOffType.class,ValueSelectorType.SET),
	setSDR ("regeneratelater", OnOffType.class,ValueSelectorType.SET),
	setSMR ("multiregenerate", OnOffType.class,ValueSelectorType.SET),
	getMOF ("consumptionmonday", DecimalType.class,ValueSelectorType.GET),
	getTUF ("consumptiontuesday", DecimalType.class,ValueSelectorType.GET),
	getWEF ("consumptionwednesday", DecimalType.class,ValueSelectorType.GET),
	getTHF ("consumptionthursday", DecimalType.class,ValueSelectorType.GET),
	getFRF ("consumptionfriday", DecimalType.class,ValueSelectorType.GET),
	getSAF ("consumptionsaturday", DecimalType.class,ValueSelectorType.GET),
	getSUF ("consumptionsunday", DecimalType.class,ValueSelectorType.GET),
	getTOF ("consumptiontoday", DecimalType.class,ValueSelectorType.GET),
	getYEF ("consumptionyesterday", DecimalType.class,ValueSelectorType.GET),
	getCWF ("consumptioncurrentweek", DecimalType.class,ValueSelectorType.GET),
	getLWF ("consumptionlastweek", DecimalType.class,ValueSelectorType.GET),
	getCMF ("consumptioncurrentmonth", DecimalType.class,ValueSelectorType.GET),
	getLMF ("consumptionlastmonth", DecimalType.class,ValueSelectorType.GET),
	getCOF ("consumptioncomplete", DecimalType.class,ValueSelectorType.GET),
	getUWF ("consumptionuntreated", DecimalType.class,ValueSelectorType.GET),
	getTFO ("consumptionpeaklevel", DecimalType.class,ValueSelectorType.GET),
	getPRS ("pressure", DecimalType.class,ValueSelectorType.GET),
	getMXP ("maxpressure", DecimalType.class,ValueSelectorType.GET),
	getMNP ("minpressure", DecimalType.class,ValueSelectorType.GET),
	getMXF ("maxflow", DecimalType.class,ValueSelectorType.GET),
	getLAR ("lastgeneration", DateTimeType.class, ValueSelectorType.GET)
	{
		@Override
		public String convertValue(String value) {

			final SimpleDateFormat IN_DATE_FORMATTER = new SimpleDateFormat("dd.mm.yy HH:mm:ss");
			final SimpleDateFormat OUT_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");


			Date date = null;
			String convertedValue = null;

			try {
				date = IN_DATE_FORMATTER.parse(value);
				convertedValue = OUT_DATE_FORMATTER.format(date);
			}
			catch (ParseException fpe) {
				throw new IllegalArgumentException(value + " is not in a valid format.", fpe);
			}

			return convertedValue;
		}
	},
	getNOR ("normalregenerations", DecimalType.class,ValueSelectorType.GET),
	getSRE ("serviceregenerations", DecimalType.class,ValueSelectorType.GET),
	getINR ("incompleteregenerations", DecimalType.class,ValueSelectorType.GET),
	getTOR ("allregenerations", DecimalType.class,ValueSelectorType.GET)
	;

	static final Logger logger = LoggerFactory.getLogger(OceanicValueSelector.class);

	private final String text;
	private Class<? extends Type> typeClass;
	private ValueSelectorType typeValue;

	private OceanicValueSelector(final String text, Class<? extends Type> typeClass, ValueSelectorType typeValue) {
		this.text = text;
		this.typeClass = typeClass;
		this.typeValue = typeValue;
	}

	@Override
	public String toString() {
		return text;
	}

	public Class<? extends Type> getTypeClass() {
		return typeClass;
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

		for (OceanicValueSelector c : OceanicValueSelector.values()) {
			if (c.text.equals(valueSelector) && item !=null) {
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
	public static OceanicValueSelector getValueSelector(String valueSelectorText, ValueSelectorType valueSelectorType)
			throws IllegalArgumentException {

		for (OceanicValueSelector c : OceanicValueSelector.values()) {
			if (c.text.equals(valueSelectorText) && c.typeValue==valueSelectorType) {
				return c;
			}
		}

		throw new IllegalArgumentException("Not valid value selector");
	}

	public static ValueSelectorType getValueSelectorType(String valueSelectorText)
			throws IllegalArgumentException {

		for (OceanicValueSelector c : OceanicValueSelector.values()) {
			if (c.text.equals(valueSelectorText)) {
				return c.typeValue;
			}
		}

		throw new IllegalArgumentException("Not valid value selector");

	}

	public String convertValue(String value) {
		return value;
	}

	public enum ValueSelectorType {
		GET,SET
	}


}