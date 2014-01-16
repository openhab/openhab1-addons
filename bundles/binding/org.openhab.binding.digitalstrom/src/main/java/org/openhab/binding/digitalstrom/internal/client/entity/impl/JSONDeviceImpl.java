/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.entity.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openhab.binding.digitalstrom.internal.client.constants.DeviceConstants;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONApiResponseKeysEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.OutputModeEnum;
import org.openhab.binding.digitalstrom.internal.client.entity.DSID;
import org.openhab.binding.digitalstrom.internal.client.entity.Device;
import org.openhab.binding.digitalstrom.internal.client.entity.DeviceSceneSpec;
import org.openhab.binding.digitalstrom.internal.client.events.DeviceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 	Alexander Betker
 * @author Alex Maier
 * @since 1.3.0
 */
public class JSONDeviceImpl implements Device {
	
	private static final Logger logger = LoggerFactory.getLogger(JSONDeviceImpl.class);
	

	private DSID dsid = null;
	
	private String name = null;
	
	private int zoneId = 0;
	
	private boolean isPresent = false;
	
	private boolean isOn = false;
	
	private OutputModeEnum outputMode = null;
	
	
	private int outputValue = 0;
	
	private int maxOutputValue = DeviceConstants.DEFAULT_MAX_OUTPUTVALUE;
	
	private int minOutputValue = 0;
	
	
	private int slatPosition = 0;
	
	private int maxSlatPosition = DeviceConstants.MAX_SLAT_POSITION;
	
	private int minSlatPosition = DeviceConstants.MIN_SLAT_POSITION;
	
	
	private int powerConsumption = 0;
	
	private int energyMeterValue = 0;
	
	private int electricMeterValue = 0;
	
	
	private	List<Short> groupList = new LinkedList<Short>();
	
	private List<DeviceListener> deviceListenerList = Collections.synchronizedList(new LinkedList<DeviceListener>());
	
	private Map<Short, DeviceSceneSpec> sceneConfigMap = Collections.synchronizedMap(new HashMap<Short, DeviceSceneSpec>());
	
	private Map<Short, Short> sceneOutputMap = Collections.synchronizedMap(new HashMap<Short, Short>());
	
	
	public JSONDeviceImpl(JSONObject object) {
		
		if (object.get(JSONApiResponseKeysEnum.DEVICE_NAME.getKey()) != null) {
			this.name = object.get(JSONApiResponseKeysEnum.DEVICE_NAME.getKey()).toString();
		}
		
		if (object.get(JSONApiResponseKeysEnum.DEVICE_ID.getKey()) != null) {
			this.dsid = new DSID(object.get(JSONApiResponseKeysEnum.DEVICE_ID.getKey()).toString());
		}
		else if (object.get(JSONApiResponseKeysEnum.DEVICE_ID_QUERY.getKey()) != null) {
			this.dsid = new DSID(object.get(JSONApiResponseKeysEnum.DEVICE_ID_QUERY.getKey()).toString());
		}
		
		if (object.get(JSONApiResponseKeysEnum.DEVICE_ON.getKey()) != null) {
			this.isOn = object.get(JSONApiResponseKeysEnum.DEVICE_ON.getKey()).toString().equals("true");
		}
		
		if (object.get(JSONApiResponseKeysEnum.DEVICE_IS_PRESENT.getKey()) != null) {
			this.isPresent = object.get(JSONApiResponseKeysEnum.DEVICE_IS_PRESENT.getKey()).toString().equals("true");
		}
		else if(object.get(JSONApiResponseKeysEnum.DEVICE_IS_PRESENT_QUERY.getKey()) != null) {
			this.isPresent = object.get(JSONApiResponseKeysEnum.DEVICE_IS_PRESENT_QUERY.getKey()).toString().equals("true");
		}
		
		String zoneStr = null;
		if (object.get(JSONApiResponseKeysEnum.DEVICE_ZONE_ID.getKey()) != null) {
			zoneStr = object.get(JSONApiResponseKeysEnum.DEVICE_ZONE_ID.getKey()).toString();
		}
		else if(object.get(JSONApiResponseKeysEnum.DEVICE_ZONE_ID_QUERY.getKey()) != null) {
			zoneStr = object.get(JSONApiResponseKeysEnum.DEVICE_ZONE_ID_QUERY.getKey()).toString();
		}
		
		if (zoneStr != null) {
			try {
				this.zoneId = Integer.parseInt(zoneStr);
			}
			catch (java.lang.NumberFormatException e) {
				logger.error("NumberFormatException by parsing zoneId: "+zoneStr);
			}
		}
		
		if (object.get(JSONApiResponseKeysEnum.DEVICE_GROUPS.getKey()) instanceof JSONArray) {
			JSONArray array = (JSONArray) object.get(JSONApiResponseKeysEnum.DEVICE_GROUPS.getKey());
			
			for (int i=0; i< array.size(); i++) {
				if (array.get(i) != null) {
					String value = array.get(i).toString();
					short tmp = -1;
					try {
						tmp = Short.parseShort(value);
					} catch (java.lang.NumberFormatException e) {
						logger.error("NumberFormatException by parsing groups: "+value);
					}
					
					if (tmp != -1) {
						this.groupList.add(tmp);
					}
				}
			}
		}
			
		if (object.get(JSONApiResponseKeysEnum.DEVICE_OUTPUT_MODE.getKey()) != null) {
			int tmp = -1;
			try {
				tmp = Integer.parseInt(object.get(JSONApiResponseKeysEnum.DEVICE_OUTPUT_MODE.getKey()).toString());
			} catch (java.lang.NumberFormatException e) {
				logger.error("NumberFormatException by parsing outputmode: "+object.get(JSONApiResponseKeysEnum.DEVICE_OUTPUT_MODE.getKey()).toString());
			}
			
			if (tmp != -1) {
				if (OutputModeEnum.containsMode(tmp)) {
					outputMode = OutputModeEnum.getMode(tmp);
				}
			}
			
		}
		
		init();
		
	}
	
	private void init() {
		if (groupList.contains((short)1)) {
			maxOutputValue = DeviceConstants.MAX_OUTPUT_VALUE_LIGHT;
			if (this.isDimmable()) {
				minOutputValue = DeviceConstants.MIN_DIMM_VALUE;
			}
		}
		else {
			maxOutputValue = DeviceConstants.DEFAULT_MAX_OUTPUTVALUE;
			minOutputValue = 0;
		}
		
		if(isOn)
			setOutputValue(DeviceConstants.DEFAULT_MAX_OUTPUTVALUE);
	}
	
	@Override
	public DSID getDSID() {
		return dsid;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public synchronized void setName(String name) {
		this.name = name;
	}

	@Override
	public List<Short> getGroups() {
		return groupList;
	}

	@Override
	public int getZoneId() {
		return zoneId;
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}

	@Override
	public boolean isOn() {
		return isOn;
	}
	
	@Override
	public void setIsOn(boolean flag) {
		//if device is off set power consumption and energy meter value to 0
		if(flag == false){
			this.powerConsumption=0;
			this.energyMeterValue=0;
		}
		this.isOn = flag;
	}
	
	@Override
	public synchronized void setOutputValue(int value) {
		if (value <= 0) {
			outputValue = 0;
			setIsOn(false);
		}
		else if (value > maxOutputValue) {
			outputValue = maxOutputValue;
			setIsOn(true);
		}
		else {
			
			outputValue = value;
			setIsOn(true);
		}
		notifyDeviceListener(dsid.getValue());
	}

	@Override
	public boolean isDimmable() {
		if (outputMode == null) {
			return false;
		}
		return outputMode.equals(OutputModeEnum.DIMMED);
	}

	@Override
	public OutputModeEnum getOutputMode() {
		return outputMode;
	}

	@Override
	public synchronized void increase() {
		if (isDimmable()) {
			
			if (outputValue == maxOutputValue) {
				return;
			}
			if ((outputValue + getDimmStep()) > maxOutputValue) {
				outputValue = maxOutputValue;
			}
			else {
				outputValue += getDimmStep();
			}
			setIsOn(true);
			notifyDeviceListener(this.dsid.getValue());
		}
	}

	@Override
	public synchronized void decrease() {
		if (isDimmable()) {
			if (outputValue == minOutputValue) {
				if (outputValue == 0) {
					setIsOn(false);
				}
				return;
			}
			
			if ((outputValue - getDimmStep()) <= minOutputValue) {
				if (isOn) {
					System.out.println("Device isOn");
					outputValue = minOutputValue;
				}
				
				if (minOutputValue == 0) {
					setIsOn(false);
				}
				else {
					if (outputValue != 0) {
						setIsOn(true);
					}
				}
			}
			else {
				outputValue -= getDimmStep();
				setIsOn(true);
			}
			notifyDeviceListener(this.dsid.getValue());
		}
	}
	
	@Override
	public int getOutputValue() {
		return outputValue;
	}

	@Override
	public int getMaxOutPutValue() {
		return maxOutputValue;
	}

	@Override
	public boolean isRollershutter() {
		if (outputMode == null) {
			return false;
		}
		return outputMode.equals(OutputModeEnum.UP_DOWN) || outputMode.equals(OutputModeEnum.SLAT);
	}

	@Override
	public int getSlatPosition() {
		return slatPosition;
	}

	@Override
	public synchronized void setSlatPosition(int position) {
		if (position < minSlatPosition) {
			slatPosition = minSlatPosition;
		}
		else if (position > this.maxSlatPosition) {
			slatPosition = this.maxSlatPosition;
		}
		else {
			this.slatPosition = position;
		}
		notifyDeviceListener(this.dsid.getValue());
	}

	@Override
	public int getPowerConsumption() {
		return powerConsumption;
	}

	@Override
	public synchronized void setPowerConsumption(int powerConspumtion) {
		if (powerConspumtion < 0) {
			this.powerConsumption = 0;
		}
		else {
			this.powerConsumption = powerConspumtion;
		}
		
		notifyDeviceListener(this.dsid.getValue());
	}

	@Override
	public int getEnergyMeterValue() {
		return energyMeterValue;
	}

	@Override
	public synchronized void setEnergyMeterValue(int value) {
		if (value < 0) {
			energyMeterValue = 0;
		}
		else {
			energyMeterValue = value;
		}
		
		notifyDeviceListener(this.dsid.getValue());
	}

	@Override
	public void addDeviceListener(DeviceListener listener) {
		if (listener != null) {
			if (!this.deviceListenerList.contains(listener)) {
				this.deviceListenerList.add(listener);
			}
		}
	}

	@Override
	public void removeDeviceListener(DeviceListener listener) {
		if (listener != null) {
			if (this.deviceListenerList.contains(listener)) {
				this.deviceListenerList.remove(listener);
			}
		}
	}

	@Override
	public void notifyDeviceListener(String dsid) {
		for (DeviceListener listener: this.deviceListenerList) {
			listener.deviceUpdated(dsid);
		}
	}

	@Override
	public int getElectricMeterValue() {
		return electricMeterValue;
	}

	@Override
	public synchronized void setElectricMeterValue(int electricMeterValue) {
		if (electricMeterValue < 0) {
			this.electricMeterValue = 0; 
		}
		else {
			this.electricMeterValue = electricMeterValue;
		}
		
		notifyDeviceListener(this.dsid.getValue());
	}

	private short getDimmStep() {
		if (isDimmable()) {
			return DeviceConstants.DIMM_STEP_LIGHT;
		}
		else if (isRollershutter()) {
			return DeviceConstants.MOVE_STEP_ROLLERSHUTTER;
		}
		else {
			return DeviceConstants.DEFAULT_MOVE_STEP;
		}
	}

	@Override
	public int getMaxSlatPosition() {
		return maxSlatPosition;
	}

	@Override
	public int getMinSlatPosition() {
		return minSlatPosition;
	}
	
	@Override
	public short getSceneOutputValue(short sceneId) {
		synchronized(sceneOutputMap) {
			if (sceneOutputMap.containsKey(sceneId)) {
				return sceneOutputMap.get(sceneId);
			}
		}
		return -1;
	}

	@Override
	public void setSceneOutputValue(short sceneId, short value) {
		synchronized(sceneOutputMap) {
			sceneOutputMap.put(sceneId, value);
		}
	}

	@Override
	public void addSceneConfig(short sceneId, DeviceSceneSpec sceneSpec) {
		if (sceneSpec != null) {
			synchronized(sceneConfigMap) {
				sceneConfigMap.put(sceneId, sceneSpec);
			}
		}
	}

	@Override
	public boolean doIgnoreScene(short sceneId) {
		synchronized(sceneConfigMap) {
			if (this.sceneConfigMap.containsKey(sceneId)) {
				return this.sceneConfigMap.get(sceneId).isDontCare();
			}
		}
		return false;
	}

	@Override
	public boolean containsSceneConfig(short sceneId) {
		synchronized(sceneConfigMap) {
			return sceneConfigMap.containsKey(sceneId);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Device) {
			Device device = (Device)obj;
			return device.getDSID().equals(this.getDSID());
		}
		return false;
	}
			
}