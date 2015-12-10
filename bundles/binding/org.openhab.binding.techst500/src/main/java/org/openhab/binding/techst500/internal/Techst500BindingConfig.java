/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.techst500.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Techst 500 item binding configuration
 * 
 * @author Stephan Noerenberg
 * @since 1.7.0
 */
public class Techst500BindingConfig implements BindingConfig {

	public static final String KEY_DEVICE_UID = "uid";
	public static final String KEY_BINDING_TYPE = "bindingType";

	public enum BindingType {
		chTemperature, chTemperatureShould, fanSpeed, fanOperates, feederOperates, chPumpOperates, htwTemperature, currentTime, currentDay, wwTemperature, outsideTemperature, feederTemperature, wwPumpOperates;
	}

	private final String deviceUid;
	private final BindingType bindingType;
	private final String itemName;

	public Techst500BindingConfig(String deviceUid, String bindingType,
			String itemName) throws BindingConfigParseException {
		this.deviceUid = deviceUid != null ? deviceUid
				: Techst500Binding.DEFAULT_DEVICE_UID;
		this.bindingType = parseBindingType(bindingType);
		this.itemName = itemName;
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
	
	public BindingType getBindingType() {
		return bindingType;
	}

	public String getItemName() {
		return itemName;
	}
}
