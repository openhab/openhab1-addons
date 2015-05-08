/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.internal;

import org.openmuc.jsml.structures.ASNObject;
import org.openmuc.jsml.structures.Integer16;
import org.openmuc.jsml.structures.Integer32;
import org.openmuc.jsml.structures.Integer64;
import org.openmuc.jsml.structures.Integer8;
import org.openmuc.jsml.structures.OctetString;
import org.openmuc.jsml.structures.SML_Boolean;
import org.openmuc.jsml.structures.SML_ListEntry;
import org.openmuc.jsml.structures.SML_Unit;
import org.openmuc.jsml.structures.SML_Value;
import org.openmuc.jsml.structures.Unsigned16;
import org.openmuc.jsml.structures.Unsigned32;
import org.openmuc.jsml.structures.Unsigned64;


/**
 * Proxy class to encapsulate a openMUC SML_ListEntry-Object to read informations.
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
public final class SmlValue {
	
	/**
	* Stores the original value object from jSML
	*/
	private SML_ListEntry smlListEntry;
	
	/**
	* Constructor
	*/
	public SmlValue(SML_ListEntry listEntry){
		smlListEntry = listEntry;
	}
	
	/**
	 * Gets the values unit.
	 * @return the values unit if available - Integer.MIN_VALUE.
	*/
	public int getUnit() {
		int unit = Integer.MIN_VALUE;
		
		if(smlListEntry != null) {
			unit = smlListEntry.getUnit().getVal();		
		}
		
		return unit;
	}
	
	/**
	 * Gets the values unit.
	 * @return the string representation of the values unit  - otherwise null.
	 */
	private String getUnitName(){
		String unit = null;

		if(smlListEntry != null) {
			switch(smlListEntry.getUnit().getVal()){
				case SML_Unit.YEAR:
					unit = SmlReaderConstants.UnitConstants.YEAR;
					break;
				case SML_Unit.MONTH:
					unit = SmlReaderConstants.UnitConstants.MONTH;
					break;
				case SML_Unit.WEEK:
					unit = SmlReaderConstants.UnitConstants.WEEK;
					break;
				case SML_Unit.DAY:
					unit = SmlReaderConstants.UnitConstants.DAY;
					break;
				case SML_Unit.HOUR:
					unit = SmlReaderConstants.UnitConstants.HOUR;
					break;
				case SML_Unit.MIN:
					unit = SmlReaderConstants.UnitConstants.MIN;
					break;
				case SML_Unit.SECOND:
					unit = SmlReaderConstants.UnitConstants.SECOND;
					break;
				case SML_Unit.DEGREE:
					unit = SmlReaderConstants.UnitConstants.DEGREE;
					break;
				case SML_Unit.DEGREE_CELSIUS:
					unit = SmlReaderConstants.UnitConstants.DEGREE_CELSIUS;
					break;
				case SML_Unit.CURRENCY:
					unit = SmlReaderConstants.UnitConstants.CURRENCY;
					break;
				case SML_Unit.METRE:
					unit = SmlReaderConstants.UnitConstants.METRE;
					break;
				case SML_Unit.METRE_PER_SECOND:
					unit = SmlReaderConstants.UnitConstants.METRE_PER_SECOND;
					break;
				case SML_Unit.CUBIC_METRE:
					unit = SmlReaderConstants.UnitConstants.CUBIC_METRE;
					break;
				case SML_Unit.CUBIC_METRE_CORRECTED:
					unit = SmlReaderConstants.UnitConstants.CUBIC_METRE_CORRECTED;
					break;
				case SML_Unit.CUBIC_METRE_PER_HOUR:
					unit = SmlReaderConstants.UnitConstants.CUBIC_METRE_PER_HOUR;
					break;
				case SML_Unit.CUBIC_METRE_PER_HOUR_CORRECTED:
					unit = SmlReaderConstants.UnitConstants.CUBIC_METRE_PER_HOUR_CORRECTED;
					break;
				case SML_Unit.CUBIC_METRE_PER_DAY:
					unit = SmlReaderConstants.UnitConstants.CUBIC_METRE_PER_DAY;
					break;
				case SML_Unit.CUBIC_METRE_PER_DAY_CORRECTED:
					unit = SmlReaderConstants.UnitConstants.CUBIC_METRE_PER_DAY_CORRECTED;
					break;
				case SML_Unit.LITRE:
					unit = SmlReaderConstants.UnitConstants.LITRE;
					break;
				case SML_Unit.KILOGRAM:
					unit = SmlReaderConstants.UnitConstants.KILOGRAM;
					break;
				case SML_Unit.NEWTON:
					unit = SmlReaderConstants.UnitConstants.NEWTON;
					break;
				case SML_Unit.NEWTONMETER:
					unit = SmlReaderConstants.UnitConstants.NEWTONMETER;
					break;
				case SML_Unit.PASCAL:
					unit = SmlReaderConstants.UnitConstants.PASCAL;
					break;
				case SML_Unit.BAR:
					unit = SmlReaderConstants.UnitConstants.BAR;
					break;
				case SML_Unit.JOULE:
					unit = SmlReaderConstants.UnitConstants.JOULE;
					break;
				case SML_Unit.JOULE_PER_HOUR:
					unit = SmlReaderConstants.UnitConstants.JOULE_PER_HOUR;
					break;
				case SML_Unit.WATT:
					unit = SmlReaderConstants.UnitConstants.WATT;
					break;
				case SML_Unit.VOLT_AMPERE:
					unit = SmlReaderConstants.UnitConstants.VOLT_AMPERE;
					break;
				case SML_Unit.VAR:
					unit = SmlReaderConstants.UnitConstants.VAR;
					break;
				case SML_Unit.WATT_HOUR:
					unit = SmlReaderConstants.UnitConstants.WATT_HOUR;
					break;
				case SML_Unit.VOLT_AMPERE_HOUR:
					unit = SmlReaderConstants.UnitConstants.VOLT_AMPERE_HOUR;
					break;
				case SML_Unit.VAR_HOUR:
					unit = SmlReaderConstants.UnitConstants.VAR_HOUR;
					break;
				case SML_Unit.AMPERE:
					unit = SmlReaderConstants.UnitConstants.AMPERE;
					break;
				case SML_Unit.COULOMB:
					unit = SmlReaderConstants.UnitConstants.COULOMB;
					break;
				case SML_Unit.VOLT:
					unit = SmlReaderConstants.UnitConstants.VOLT;
					break;
				case SML_Unit.VOLT_PER_METRE:
					unit = SmlReaderConstants.UnitConstants.VOLT_PER_METRE;
					break;
				case SML_Unit.FARAD:
					unit = SmlReaderConstants.UnitConstants.FARAD;
					break;
				case SML_Unit.OHM:
					unit = SmlReaderConstants.UnitConstants.OHM;
					break;
				case SML_Unit.OHM_METRE:
					unit = SmlReaderConstants.UnitConstants.OHM_METRE;
					break;
				case SML_Unit.WEBER:
					unit = SmlReaderConstants.UnitConstants.WEBER;
					break;
				case SML_Unit.TESLA:
					unit = SmlReaderConstants.UnitConstants.TESLA;
					break;
				case SML_Unit.AMPERE_PER_METRE:
					unit = SmlReaderConstants.UnitConstants.AMPERE_PER_METRE;
					break;
				case SML_Unit.HENRY:
					unit = SmlReaderConstants.UnitConstants.HENRY;
					break;
				case SML_Unit.HERTZ:
					unit = SmlReaderConstants.UnitConstants.HERTZ;
					break;
				case SML_Unit.ACTIVE_ENERGY_METER_CONSTANT_OR_PULSE_VALUE:
					unit = SmlReaderConstants.UnitConstants.ACTIVE_ENERGY_METER_CONSTANT_OR_PULSE_VALUE;
					break;
				case SML_Unit.REACTIVE_ENERGY_METER_CONSTANT_OR_PULSE_VALUE:
					unit = SmlReaderConstants.UnitConstants.REACTIVE_ENERGY_METER_CONSTANT_OR_PULSE_VALUE;
					break;
				case SML_Unit.APPARENT_ENERGY_METER_CONSTANT_OR_PULSE_VALUE:
					unit = SmlReaderConstants.UnitConstants.APPARENT_ENERGY_METER_CONSTANT_OR_PULSE_VALUE;
					break;
				case SML_Unit.VOLT_SQUARED_HOURS:
					unit = SmlReaderConstants.UnitConstants.VOLT_SQUARED_HOURS;
					break;
				case SML_Unit.AMPERE_SQUARED_HOURS:
					unit = SmlReaderConstants.UnitConstants.AMPERE_SQUARED_HOURS;
					break;
				case SML_Unit.KILOGRAM_PER_SECOND:
					unit = SmlReaderConstants.UnitConstants.KILOGRAM_PER_SECOND;
					break;
				case SML_Unit.KELVIN:
					unit = SmlReaderConstants.UnitConstants.KELVIN;
					break;
				case SML_Unit.VOLT_SQUARED_HOUR_METER_CONSTANT_OR_PULSE_VALUE:
					unit = SmlReaderConstants.UnitConstants.VOLT_SQUARED_HOUR_METER_CONSTANT_OR_PULSE_VALUE;
					break;
				case SML_Unit.AMPERE_SQUARED_HOUR_METER_CONSTANT_OR_PULSE_VALUE:
					unit = SmlReaderConstants.UnitConstants.AMPERE_SQUARED_HOUR_METER_CONSTANT_OR_PULSE_VALUE;
					break;
				case SML_Unit.METER_CONSTANT_OR_PULSE_VALUE:
					unit = SmlReaderConstants.UnitConstants.METER_CONSTANT_OR_PULSE_VALUE;
					break;
				case SML_Unit.PERCENTAGE:
					unit = SmlReaderConstants.UnitConstants.PERCENTAGE;
					break;
				case SML_Unit.AMPERE_HOUR:
					unit = SmlReaderConstants.UnitConstants.AMPERE_HOUR;
					break;
				case SML_Unit.ENERGY_PER_VOLUME:
					unit = SmlReaderConstants.UnitConstants.ENERGY_PER_VOLUME;
					break;
				case SML_Unit.CALORIFIC_VALUE:
					unit = SmlReaderConstants.UnitConstants.CALORIFIC_VALUE;
					break;
				case SML_Unit.MOLE_PERCENT:
					unit = SmlReaderConstants.UnitConstants.MOLE_PERCENT;
					break;
				case SML_Unit.MASS_DENSITY:
					unit = SmlReaderConstants.UnitConstants.MASS_DENSITY;
					break;
				case SML_Unit.PASCAL_SECOND:
					unit = SmlReaderConstants.UnitConstants.PASCAL_SECOND;
					break;
				case SML_Unit.RESERVED:
					unit = SmlReaderConstants.UnitConstants.RESERVED;
					break;
				case SML_Unit.OTHER_UNIT:
					unit = SmlReaderConstants.UnitConstants.OTHER_UNIT;
					break;
				case SML_Unit.COUNT:
					unit = SmlReaderConstants.UnitConstants.COUNT;
					break;
			}			
		}
		else{
			unit = null;
		}
		
		return unit;
	}
	
	/**
	* Gets the scaler which has to be applied to the value.
	* @return scaler which has to be applied to the value.
	*/
	private double getScaler()	{
		int scaler = 0;
		
		if(smlListEntry != null && smlListEntry.getScaler().isSelected()) {
			byte scalerByte = smlListEntry.getScaler().getVal();
			
			scaler = Integer.parseInt(String.format("%02x", scalerByte), 16);
			
			if(scaler == 255)
				scaler = -1;
		}

		return Math.pow(10, scaler);
	}
	
	/**
	* Scales the value if necessary
	* @return a string representation of the scaled value.
	*/
	private String scaleValue(String originalValue) {
		double scaledValue = Double.parseDouble(originalValue);
		scaledValue *= getScaler();
		
		return Double.toString(scaledValue);
	}
	
	@Override
	public String toString(){
		return "Value: '" + this.getValue() + "' Unit: '" + this.getUnitName() + "' Scaler:'" + this.getScaler() + "'";
	}
	
	/**
	 * Gets the value
	 * @return the value as String if available - otherwise null.
	 */
	public String getValue() {
		String value = null;
		
		if(smlListEntry != null) {
			SML_Value smlValue = smlListEntry.getValue();
			ASNObject choice = smlValue.getChoice();

			if(SML_Boolean.class.isInstance(choice)) {
				value =  Boolean.toString(((SML_Boolean)choice).getVal()); 
			}
			else if( choice instanceof OctetString) {
				value = ((OctetString)choice).toString();
			}
			else if(Integer8.class.isInstance(choice)) {
				value = String.format("0x%02x", ((Integer8)choice).getVal());
			}
			else if(Integer16.class.isInstance(choice) || Unsigned16.class.isInstance(choice)) {
				value = scaleValue(Short.toString(((Integer16)choice).getVal()));
			}
			else if(Integer32.class.isInstance(choice) || Unsigned32.class.isInstance(choice)) {
				value = scaleValue(Integer.toString(((Integer32)choice).getVal()));
			}
			else if(Integer64.class.isInstance(choice) || Unsigned64.class.isInstance(choice)) {
				value = scaleValue(Long.toString(((Integer64)choice).getVal()));
			}
			else {
				value = null;
			}
		}
		
		return value;
	}
}
