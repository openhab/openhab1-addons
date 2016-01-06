/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.novelanheatpump;

public enum HeatpumpCoolingOperationMode {
	// in german Automatik
	AUTOMATIC(1),
	// in german Aus
	OFF(0);
	
	private int value;
	
	private HeatpumpCoolingOperationMode(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	
	public static HeatpumpCoolingOperationMode fromValue(int value){
		for(HeatpumpCoolingOperationMode mode : HeatpumpCoolingOperationMode.values()){
			if(mode.value == value){
				return mode;
			}
		}
		return null;
	}
	

}
