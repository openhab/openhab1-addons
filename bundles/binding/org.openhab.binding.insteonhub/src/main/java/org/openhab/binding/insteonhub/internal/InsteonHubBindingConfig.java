/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openhab.core.binding.BindingConfig;

/**
 * InsteonHub Binding Configuration
 * 
 * @author Eric Thill
 *
 * @since 1.4.0
 */
public class InsteonHubBindingConfig implements BindingConfig {

	public static final String KEY_BINDING_TYPE = "bindingType";
	public static final String KEY_HUB_ID = "hubid";
	public static final String KEY_DEVICE = "device";
	public static final String KEY_ON_VALUE = "onValue";
	public static final String KEY_OFF_VALUE = "offValue";
	public static final String KEY_OPEN_VALUE = "openValue";
	public static final String KEY_CLOSED_VALUE = "closedValue";

	public enum BindingType {
		DIMMER, SWITCH, INPUT_UBYTE, INPUT_PERCENT, INPUT_ON_OFF, INPUT_OPEN_CLOSED;
		public static BindingType parseIgnoreCase(String str) {
			for (BindingType type : values()) {
				if (type.toString().equalsIgnoreCase(str)) {
					return type;
				}
			}
			return null;
		}
	}

	public static InsteonHubBindingConfig parse(String itemName,
			String configStr) {
		Map<String, String> configMap = stringToMap(configStr);

		// parse hubId (default used if not present)
		String hubId = configMap.get(KEY_HUB_ID);
		if (hubId == null) {
			// no hubid defined => use default
			hubId = InsteonHubBinding.DEFAULT_HUB_ID;
		}

		// parse required device key
		String device = configMap.get(KEY_DEVICE);
		if (device == null) {
			throw new IllegalArgumentException(KEY_DEVICE
					+ " is not defined in " + configMap);
		}
		device = device.replace(".", "");

		// parse required bindingType key
		String bindingTypeStr = configMap.get(KEY_BINDING_TYPE);
		if (bindingTypeStr == null) {
			throw new IllegalArgumentException(KEY_BINDING_TYPE
					+ " is not defined in " + configMap);
		}
		BindingType bindingType = BindingType.parseIgnoreCase(bindingTypeStr);
		if (bindingType == null) {
			throw new IllegalArgumentException("Unknown value for "
					+ KEY_BINDING_TYPE + " '" + bindingTypeStr + "'");
		}

		// parse all optional keys
		String onValueStr = configMap.get(KEY_ON_VALUE);
		Integer onValue = onValueStr == null ? null : Integer
				.parseInt(onValueStr);
		String offValueStr = configMap.get(KEY_OFF_VALUE);
		Integer offValue = offValueStr == null ? null : Integer
				.parseInt(offValueStr);
		String openValueStr = configMap.get(KEY_OPEN_VALUE);
		Integer openValue = openValueStr == null ? null : Integer
				.parseInt(openValueStr);
		String closedValueStr = configMap.get(KEY_CLOSED_VALUE);
		Integer closedValue = closedValueStr == null ? null : Integer
				.parseInt(closedValueStr);

		InsteonHubBindingDeviceInfo deviceInfo = new InsteonHubBindingDeviceInfo(
				hubId, device);
		return new InsteonHubBindingConfig(itemName, deviceInfo, bindingType,
				onValue, offValue, openValue, closedValue);
	}

	private static Map<String, String> stringToMap(String str) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		String[] keyValuePairs = str.split(",");
		for (String keyValuePair : keyValuePairs) {
			String key;
			String value;
			if (keyValuePair.contains("=")) {
				// parse the key and value
				String[] split = keyValuePair.split("=");
				key = split[0].trim();
				value = split[1].trim();
			} else {
				// treat this as a true/false flag to enable
				key = keyValuePair.trim();
				value = "true";
			}
			map.put(key, value);
		}
		return map;
	}

	private final String itemName;
	private final InsteonHubBindingDeviceInfo deviceInfo;
	private final BindingType bindingType;
	private final Integer onValue;
	private final Integer offValue;
	private final Integer openValue;
	private final Integer closedValue;

	public InsteonHubBindingConfig(String itemName,
			InsteonHubBindingDeviceInfo deviceInfo, BindingType bindingType,
			Integer onValue, Integer offValue, Integer openValue,
			Integer closedValue) {
		this.itemName = itemName;
		this.deviceInfo = deviceInfo;
		this.bindingType = bindingType;
		this.onValue = onValue;
		this.offValue = offValue;
		this.openValue = openValue;
		this.closedValue = closedValue;
	}

	public String getItemName() {
		return itemName;
	}

	public InsteonHubBindingDeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public BindingType getBindingType() {
		return bindingType;
	}

	public Integer getOnValue() {
		return onValue;
	}

	public Integer getOffValue() {
		return offValue;
	}

	public Integer getOpenValue() {
		return openValue;
	}

	public Integer getClosedValue() {
		return closedValue;
	}

}
