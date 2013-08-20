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
package org.openhab.binding.digitalstrom.internal.client.entity;

import java.util.List;

import org.openhab.binding.digitalstrom.internal.client.constants.OutputModeEnum;
import org.openhab.binding.digitalstrom.internal.client.events.DeviceListener;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public interface Device {
	
	public DSID getDSID();
	
	
	public String getName();
	
	public void setName(String name);
	
	
	public int getZoneId();
	
	/**
	 * This device is available in his zone or not.
	 * Every 24h the dSM (meter) checks, if the devices are
	 * plugged in
	 * 
	 * @return	true, if device is plugged into the wall
	 */
	public boolean isPresent();
	
	
	public boolean isOn();
	
	public void setIsOn(boolean flag);
	
	
	public boolean isDimmable();
	
	public boolean isRollershutter();
	
	/**
	 * There are different modes for devices (for example: 
	 * a device can be in dim mode or not). Please have
	 * a look at the name of this enum (a little bit self-explaining)
	 * 
	 * @return	the current mode of this device
	 */
	public OutputModeEnum getOutputMode();
	
	
	public void increase();
	
	public void decrease();
	
	
	public int getSlatPosition();
	
	public void setSlatPosition(int position);
	
	public int getMaxSlatPosition();
	
	public int getMinSlatPosition();
	
		
	public int getOutputValue();
	
	public void setOutputValue(int value);
	
	public int getMaxOutPutValue();
	
	
	public int getPowerConsumption();
	
	/**
	 * current power consumption in watt
	 * @param powerConsumption in w
	 */
	public void setPowerConsumption(int powerConsumption);
	
	/**
	 * to get the energy meter value of this device
	 * @return	energy meter value in wh
	 */
	public int getEnergyMeterValue();
	
	public void setEnergyMeterValue(int value);
	
	/**
	 * amperage of this device
	 * @return	electric meter value in mA 
	 */
	public int getElectricMeterValue();
	
	public void setElectricMeterValue(int electricMeterValue);
	
	
	public List<Short> getGroups();
	
	
	public short getSceneOutputValue(short sceneId);
	
	public void setSceneOutputValue(short sceneId, short value);
	
	/**
	 * This configuration is very important. The devices can
	 * be configured to not react to some commands (scene calls).
	 * So you can't imply that a device automatically turns on (by default yes,
	 * but if someone configured his own scenes, then maybe not) after a
	 * scene call. This method returns true or false, if the configuration 
	 * for this sceneID already has been read
	 * 
	 * @param sceneId	the sceneID
	 * @return			true if this device has the config for this specific scene
	 */
	public boolean containsSceneConfig(short sceneId);
	
	/**
	 * Add the config for this scene. The config has the configuration
	 * for the specific sceneID.
	 * 
	 * @param sceneId	scene call id
	 * @param sceneSpec	config for this sceneID
	 */
	public void addSceneConfig(short sceneId, DeviceSceneSpec sceneSpec);
	
	/**
	 * Should the device react on this scene call or not 
	 * @param sceneId	scene call id
	 * @return			true, if this device should react on this sceneID
	 */
	public boolean doIgnoreScene(short sceneId);
	
	/**
	 * To get notifications if something happens
	 * (for example a new metering value)
	 * 
	 * @param listener
	 */
	public void addDeviceListener(DeviceListener listener);
	
	/**
	 * Don't get notifications anymore
	 * 
	 * @param listener
	 */
	public void removeDeviceListener(DeviceListener listener);
	
	/**
	 * To send notifications
	 * 
	 * @param dsid	the device unique id
	 * @param event	what happend
	 */
	public void notifyDeviceListener(String dsid);
	
}
