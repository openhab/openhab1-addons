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
