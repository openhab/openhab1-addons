/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.entity.impl;

import org.json.simple.JSONObject;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONApiResponseKeysEnum;
import org.openhab.binding.digitalstrom.internal.client.entity.DeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public class JSONDeviceConfigImpl implements DeviceConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(JSONDeviceConfigImpl.class);
	
	private int class_	= -1;
	private int	index	= -1;
	private int	value	= -1;
	
	public JSONDeviceConfigImpl(JSONObject object) {
		if (object.get(JSONApiResponseKeysEnum.DEVICE_GET_CONFIG_CLASS.getKey()) != null) {
			try {
				class_ = Integer.parseInt(object.get(JSONApiResponseKeysEnum.DEVICE_GET_CONFIG_CLASS.getKey()).toString());
			}
			catch (java.lang.NumberFormatException e) {
				logger.error("NumberFormatException by getting class: "+object.get(JSONApiResponseKeysEnum.DEVICE_GET_CONFIG_CLASS.getKey()).toString());
			}
		}
		
		if (object.get(JSONApiResponseKeysEnum.DEVICE_GET_CONFIG_INDEX.getKey()) != null) {
			try {
				index = Integer.parseInt(object.get(JSONApiResponseKeysEnum.DEVICE_GET_CONFIG_INDEX.getKey()).toString());
			}
			catch (java.lang.NumberFormatException e) {
				logger.error("NumberFormatException by getting index: "+object.get(JSONApiResponseKeysEnum.DEVICE_GET_CONFIG_INDEX.getKey()).toString());
			}
		}

		if (object.get(JSONApiResponseKeysEnum.DEVICE_GET_CONFIG_VALUE.getKey()) != null) {
			try {
				value = Integer.parseInt(object.get(JSONApiResponseKeysEnum.DEVICE_GET_CONFIG_VALUE.getKey()).toString());
			}
			catch (java.lang.NumberFormatException e) {
				logger.error("NumberFormatException by getting value: "+object.get(JSONApiResponseKeysEnum.DEVICE_GET_CONFIG_VALUE.getKey()).toString());
			}
		}
		
	}

	@Override
	public int getClass_() {
		return class_;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "class: "+this.class_+", "+"index: "+this.index+", "+"value: "+this.value;
	}

}
