/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.constants;

import java.util.HashMap;

import org.openhab.binding.digitalstrom.internal.client.entity.Scene;

/**
 * Apartment scenes are usually called in a whole zone or apartment using the broadcast group (=0).
 * They have a id between 64 and 127
 * 
 * @author 	Alexander Betker
 * @see 	digitalSTROM wiki on http://redmine.digitalstrom.org/projects/dss/wiki/Scene_table
 * @since	1.3.0
 * @version	digitalSTROM-API 1.14.5
 */
public enum ApartmentSceneEnum implements Scene {
	
	DEEP_OFF				(68),
	ENERGY_OVERLOAD			(66),
	STANDBY					(67),
	ZONE_ACTIVE				(75),
	ALARM_SIGNAL			(74),
	AUTO_STANDBY			(64),
	ABSENT					(72),
	PRESENT					(71),
	SLEEPING				(69),
	WAKEUP					(70),
	DOOR_BELL				(73),
	PANIC					(65);
	
	private final int sceneNumber;
	
	static final HashMap<Integer, ApartmentSceneEnum> apartmentScenes = new HashMap<Integer, ApartmentSceneEnum>();
	
	static{
		for(ApartmentSceneEnum as : ApartmentSceneEnum.values()){
			apartmentScenes.put(as.getSceneNumber(), as);
		}
	}

	private ApartmentSceneEnum(int sceneNumber) {
		this.sceneNumber = sceneNumber;
	}

	public static ApartmentSceneEnum getApartmentScene(int sceneNumber){
		return apartmentScenes.get(sceneNumber);
	} 
	
	@Override
	public int getSceneNumber() {
		return this.sceneNumber;
	}
	
}
