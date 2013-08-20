/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
