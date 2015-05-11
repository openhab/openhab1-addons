/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.novelanheatpump;

public enum HeatpumpOperationMode {
	// in german Automatik
	AUTOMATIC(0),
	// in german Aus
	OFF(4),
	// in german Party
	PARTY(2),
	// in german Urlaub
	HOLIDAY(3),
	// in german Zuzeizer
	AUXILIARY_HEATER(1);
	
	private int value;
	
	private HeatpumpOperationMode(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	
	public static HeatpumpOperationMode fromValue(int value){
		for(HeatpumpOperationMode mode : HeatpumpOperationMode.values()){
			if(mode.value == value){
				return mode;
			}
		}
		return null;
	}
	

}
