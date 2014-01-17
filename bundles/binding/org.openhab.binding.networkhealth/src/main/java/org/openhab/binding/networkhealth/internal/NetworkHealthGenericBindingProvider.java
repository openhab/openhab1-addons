/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.networkhealth.internal;

import org.openhab.binding.networkhealth.NetworkHealthBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides NetworkHealth binding information from it. It registers as a 
 * {@link NetworkHealthBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ nh="192.168.1.100" }</code> - which checks if the given host allows connections on port 80 with a default timeout of 5000ms</li>
 * 	<li><code>{ nh="imap.email.com:993" }</code> - which checks if the given host allows connections on port 993 with a default timeout of 5000ms</li>
 * 	<li><code>{ nh="ssh.secureserver.com:22:10000" } -  - which checks if the given host allows connections on port 22 with a timeout of 10000ms</code></li>
 * </ul>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * 
 * @since 0.6.0
 */
public class NetworkHealthGenericBindingProvider extends AbstractGenericBindingProvider implements NetworkHealthBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "nh";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch- and StringItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		
		super.processBindingConfiguration(context, item, bindingConfig);
		
		String[] configParts = bindingConfig.trim().split(":");
		if (configParts.length > 3) {
			throw new BindingConfigParseException("NetworkHealth configuration can contain three parts at max");
		}
		
		NhBindingConfig config = new NhBindingConfig();
		
		item.getName();
		config.hostname = configParts[0];
		if (configParts.length > 1) {
			config.port = Integer.valueOf(configParts[1]);
		}
		if (configParts.length > 2) {
			config.timeout = Integer.valueOf(configParts[2]);
		}
		addBindingConfig(item, config);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getHostname(String itemName) {
		NhBindingConfig config = (NhBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.hostname : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getPort(String itemName) {
		NhBindingConfig config = (NhBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.port : 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTimeout(String itemName) {
		NhBindingConfig config = (NhBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.timeout : 0;
	}
	
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the NetworkHealth
	 * binding provider.
	 */
	static private class NhBindingConfig implements BindingConfig {
		public String hostname;
		public int port;
		public int timeout;
	}


}
