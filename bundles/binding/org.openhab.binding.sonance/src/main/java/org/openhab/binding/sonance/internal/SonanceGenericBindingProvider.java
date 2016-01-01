/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sonance.internal;

import org.openhab.binding.sonance.SonanceBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration. Binding
 * configuration format: ip:port:(mute|volume|power)(:00)? Where 00 is the group
 * number (for example for the DSP8-130 IP Codes group codes are 00 (group A) to
 * 07 (group H))
 * 
 * Examples:
 * <ul>
 * <li>10.0.0.100:52000:mute:00 - mute zone A
 * <li>10.0.0.100:52000:volume:01 - volume of zone B
 * <li>10.0.0.200:52000:volume:07 - volume of zone H of another amplifier
 * <li>10.0.0.100:52000:power - sleep mode of amplifier
 * </ul>
 * 
 * @author Laurens Van Acker
 * @since 1.8.0
 */
public class SonanceGenericBindingProvider extends
		AbstractGenericBindingProvider implements SonanceBindingProvider {

	public String getBindingType() {
		return "sonance";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Switch- and NumberItems are allowed - please check your *.items configuration");
		}
	}

	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		SonanceBindingConfig config = new SonanceBindingConfig();

		String[] configParts = bindingConfig.trim().split(":");

		if (configParts.length != 4 && configParts.length != 3) {
			throw new BindingConfigParseException(
					"Sonance binding must contain three or four parts separated by ':'");
		}

		config.ip = configParts[0].trim();
		config.port = Integer.parseInt(configParts[1].trim());

		if (configParts.length == 4)
			config.group = configParts[3].trim();

		if (configParts[2].trim().equals("mute"))
			config.type = Type.MUTE;
		if (configParts[2].trim().equals("volume"))
			config.type = Type.VOLUME;
		if (configParts[2].trim().equals("power"))
			config.type = Type.POWER;

		if (config.type == null)
			throw new BindingConfigParseException(
					"Third part most be mute, volume or power");

		// parse bindingconfig here ...

		addBindingConfig(item, config);
	}

	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Laurens Van Acker
	 * @since 1.8.0
	 */

	public enum Type {
		MUTE, VOLUME, POWER
	}

	/**
	 * Every item has an ip and port to connect to and a group (music zone) to
	 * modify. Possible changes are muting a zone, changing the volume of a zone
	 * or waking up or putting the amplifier to sleep.
	 * 
	 * @author Laurens Van Acker
	 * 
	 */
	class SonanceBindingConfig implements BindingConfig {
		String ip;
		int port;
		Type type;
		String group;

		public Boolean isMute() {
			return type.equals(Type.MUTE);
		}

		public Boolean isVolume() {
			return type.equals(Type.VOLUME);
		}

		public Boolean isPower() {
			return type.equals(Type.POWER);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.binding.sonance.SonanceBindingProvider#getIP(java.lang.String
	 * )
	 */
	public String getIP(String itemName) {
		SonanceBindingConfig config = (SonanceBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.ip : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.binding.sonance.SonanceBindingProvider#getPort(java.lang.
	 * String)
	 */
	public int getPort(String itemName) {
		SonanceBindingConfig config = (SonanceBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.port : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.binding.sonance.SonanceBindingProvider#getGroup(java.lang
	 * .String)
	 */
	public String getGroup(String itemName) {
		SonanceBindingConfig config = (SonanceBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.group : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.binding.sonance.SonanceBindingProvider#isMute(java.lang.String
	 * )
	 */
	public Boolean isMute(String itemName) {
		SonanceBindingConfig config = (SonanceBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.isMute() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.binding.sonance.SonanceBindingProvider#isVolume(java.lang
	 * .String)
	 */
	public Boolean isVolume(String itemName) {
		SonanceBindingConfig config = (SonanceBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.isVolume() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.binding.sonance.SonanceBindingProvider#isPower(java.lang.
	 * String)
	 */
	public Boolean isPower(String itemName) {
		SonanceBindingConfig config = (SonanceBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.isPower() : null;
	}
}
