/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
	
		if(smlListEntry != null && smlListEntry.getUnit().getVal() > 0) {
			SmlUnit smlUnit = SmlUnit.values()[smlListEntry.getUnit().getVal() - 1];
			unit = smlUnit.name();
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
