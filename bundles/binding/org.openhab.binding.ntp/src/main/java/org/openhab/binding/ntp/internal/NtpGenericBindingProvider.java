/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ntp.internal;

import java.util.Locale;
import java.util.TimeZone;

import org.openhab.binding.ntp.NtpBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides NTP binding information from it. It registers as a 
 * {@link NtpBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ ntp="Europe/Berlin:de_DE" }</code>
 * 	<li><code>{ ntp="Europe/Berlin" }</code>
 * 	<li><code>{ ntp="" }</code>
 * </ul>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * 
 * @since 0.8.0
 */
public class NtpGenericBindingProvider extends AbstractGenericBindingProvider implements NtpBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "ntp";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof DateTimeItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only DateTimeItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		
		super.processBindingConfiguration(context, item, bindingConfig);
		
		String[] configParts = bindingConfig.trim().split(":");
		if (configParts.length > 2) {
			throw new BindingConfigParseException("NTP binding configuration must not contain more than two parts");
		}
		
		NTPBindingConfig config = new NTPBindingConfig();
		
		config.timeZone = configParts.length > 0 ? TimeZone.getTimeZone(configParts[0]) : TimeZone.getDefault();
		config.locale = configParts.length > 1 ? new Locale(configParts[1]) : Locale.getDefault();

		addBindingConfig(item, config);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public TimeZone getTimeZone(String itemName) {
		NTPBindingConfig config = (NTPBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.timeZone : TimeZone.getDefault();
	}

	/**
	 * {@inheritDoc}
	 */
	public Locale getLocale(String itemName) {
		NTPBindingConfig config = (NTPBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.locale : Locale.getDefault();
	}
	
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the NTP
	 * binding provider.
	 */
	static private class NTPBindingConfig implements BindingConfig {
		public TimeZone timeZone;
		public Locale locale;
	}

}
