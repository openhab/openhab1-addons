/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.edimax.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Configuration of Edimax device in openhab's items files.
 * 
 * @author Heinz
 *
 */
public class EdimaxBindingConfiguration implements BindingConfig {

	/**
	 * Types that are supported.
	 * 
	 * @author Heinz
	 *
	 */
	enum Type {
		POWER, CURRENT, STATE
	}

	/**
	 * Valid Item types for the Types of this binding.
	 */
	private static Map<Type, Class<?>> VALID_TYPES = new HashMap<Type, Class<?>>();
	static {
		VALID_TYPES.put(Type.STATE, SwitchItem.class);
		VALID_TYPES.put(Type.POWER, NumberItem.class);
		VALID_TYPES.put(Type.CURRENT, NumberItem.class);
	}

	/**
	 * MAC address configured.
	 */
	private String macAddress;

	/**
	 * password for the device.
	 */
	private String password;

	/**
	 * Type to handle.
	 */
	private Type type;

	public void parse(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (bindingConfig == null || "".equals(bindingConfig)) {
			throw new BindingConfigParseException(
					"No Configuration specified in \"" + bindingConfig + "\".");
		}

		String[] configParts = bindingConfig.split(":");
		if (configParts.length < 1) {
			throw new BindingConfigParseException(
					"Edimax configuration must contain macaddress. Optionally the password is to be specified, separated with ':'. And optionally it contains the type (separated from password by a ':'). Available types are: 'POWER', 'CURRENT' and 'STATE' which is default.");
		}

		macAddress = configParts[0];

		if (configParts.length > 1) {
			password = configParts[1];
		}
		type = Type.STATE; // set as default
		if (configParts.length > 2) {
			type = Type.valueOf(configParts[2]);
		}

		Class<?> validClass = VALID_TYPES.get(type);
		if (!item.getClass().equals(validClass)) {
			throw new BindingConfigParseException("Type: '" + type
					+ "' only supports " + validClass.getSimpleName() + ".");
		}
	}

	public String getMacAddress() {
		return macAddress;
	}

	public Type getType() {
		return type;
	}

	public String getPassword() {
		return password;
	}

}
