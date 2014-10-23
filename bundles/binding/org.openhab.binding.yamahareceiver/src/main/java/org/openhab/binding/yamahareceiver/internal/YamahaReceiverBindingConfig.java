/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahareceiver.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Yamaha Receiver item binding configuration
 * 
 * @author Eric Thill
 * @author Ben Jones
 * @since 1.6.0
 */
public class YamahaReceiverBindingConfig implements BindingConfig {

	public static final String KEY_DEVICE_UID = "uid";
	public static final String KEY_ZONE = "zone";
	public static final String KEY_BINDING_TYPE = "bindingType";

	public enum Zone {
		main, zone2, zone3, zone4;
		
		public String toString() {
			switch (this) {
				case main:
					return "Main_Zone";
				case zone2:
					return "Zone_2";
				case zone3:
					return "Zone_3";
				case zone4:
					return "Zone_4";
					
				default:
					throw new RuntimeException("Unsupported zone: " + this.toString());
			}
		}
	}

	public enum BindingType {
		power, volumePercent, volumeDb, mute, input, surroundProgram, netRadio;
	}

	private final String deviceUid;
	private final Zone zone;
	private final BindingType bindingType;
	private final String itemName;

	public YamahaReceiverBindingConfig(String deviceUid, String zone, String bindingType,
			String itemName) throws BindingConfigParseException {
		this.deviceUid = deviceUid != null ? deviceUid
				: YamahaReceiverBinding.DEFAULT_DEVICE_UID;
		this.zone = parseZone(zone);
		this.bindingType = parseBindingType(bindingType);
		this.itemName = itemName;
	}

	private static Zone parseZone(String str)
			throws BindingConfigParseException {
		if (str == null) {
			throw new BindingConfigParseException(KEY_ZONE);
		}
		try {
			return Zone.valueOf(str);
		} catch (Exception e) {
			throw new BindingConfigParseException("error parsing "
					+ KEY_ZONE);
		}
	}

	private static BindingType parseBindingType(String str)
			throws BindingConfigParseException {
		if (str == null) {
			throw new BindingConfigParseException(KEY_BINDING_TYPE);
		}
		try {
			return BindingType.valueOf(str);
		} catch (Exception e) {
			throw new BindingConfigParseException("error parsing "
					+ KEY_BINDING_TYPE);
		}
	}

	public String getDeviceUid() {
		return deviceUid;
	}

	public Zone getZone() {
		return zone;
	}
	
	public BindingType getBindingType() {
		return bindingType;
	}

	public String getItemName() {
		return itemName;
	}
}
