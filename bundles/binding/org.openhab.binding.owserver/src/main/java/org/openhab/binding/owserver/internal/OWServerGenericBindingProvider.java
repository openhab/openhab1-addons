/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.owserver.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.owserver.OWServerBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * This class parses the EDS OWServer item binding data. It registers as a 
 * {@link OWServerBindingProvider} service as well.
 * </p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ owserver="<serverId:F90000012A608428:PrimaryValue:2000" }</code></li>
 * 	<li><code>{ owserver="<serverId:53000000224EA612:Temperature:10000" }</code></li>
 * 	<li><code>{ owserver="<serverId:5A0010000021D57E:DewPoint:10000" }</code></li>
* 	<li><code>{ owserver="<serverId:FC00000310120B1D:Counter_B:10000" }</code></li>
 * </ul>
 * 
 * The 'serverId' referenced in the binding string is configured in the openhab.cfg file -:
 * owserver.serverId.host = 192.168.2.1
 * 
 * 'serverId' can be any alphanumeric string as long as it is the same in the binding and
 * configuration file. <b>NOTE</b>: The parameter is case sensitive!
 * 
 * @author Chris Jackson
 * @since 1.3.0
 */
public class OWServerGenericBindingProvider extends AbstractGenericBindingProvider implements OWServerBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(OWServerGenericBindingProvider.class);

	/** 
	 * Artificial command for the owserver-in configuration
	 */
	protected static final Command IN_BINDING_KEY = StringType.valueOf("IN_BINDING");

	/** {@link Pattern} which matches a binding configuration part */
	private static final Pattern BASE_CONFIG_PATTERN =
		Pattern.compile("(<|>)([0-9.a-zA-Z]+:[0-9.a-zA-Z]+:[0-9._a-zA-Z]+:[0-9]+)");

	/** {@link Pattern} which matches an In-Binding */
	private static final Pattern IN_BINDING_PATTERN =
		Pattern.compile("([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9._a-zA-Z]+):([0-9]+)");
	

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "owserver";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig != null) {
			OWServerBindingConfig config = parseBindingConfig(item, bindingConfig);
			addBindingConfig(item, config);
		}
		else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> process bindingConfig aborted!");
		}
	}
	
	/**
	 * Delegates parsing the <code>bindingConfig</code> with respect to the
	 * first character (<code>&lt;</code> or <code>&gt;</code>) to the 
	 * specialized parsing methods
	 * 
	 * @param item
	 * @param bindingConfig
	 * 
	 * @throws BindingConfigParseException
	 */
	protected OWServerBindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		
		OWServerBindingConfig config = new OWServerBindingConfig();
		config.itemType = item.getClass();
		
		Matcher matcher = BASE_CONFIG_PATTERN.matcher(bindingConfig);
		
		if (!matcher.matches()) {
			throw new BindingConfigParseException("bindingConfig '" + bindingConfig + "' doesn't contain a valid binding configuration");
		}
		matcher.reset();
				
		while (matcher.find()) {
			String direction = matcher.group(1);
			String bindingConfigPart = matcher.group(2);
			
			if (direction.equals("<")) {
				config = parseInBindingConfig(item, bindingConfigPart, config);
			}
			else if (direction.equals(">")) {
				// for future use
			}
			else {
				throw new BindingConfigParseException("Unknown command given! Configuration must start with '<' or '>' ");
			}
		}
		
		return config;
	}

	/**
	 * Parses a owserver-in configuration by using the regular expression
	 * <code>([0-9.a-zA-Z]+:[0-9.a-zA-Z]+:[0-9._a-zA-Z]+:[0-9]+)</code>. Where the groups should 
	 * contain the following content:
	 * <ul>
	 * <li>1 - Server ID</li>
	 * <li>2 - One Wire ROM ID</li>
	 * <li>3 - Variable name</li>
	 * <li>4 - Refresh Interval</li>
	 * </ul>
	 * 
	 * @param item 
	 * @param bindingConfig the config string to parse
	 * @param config
	 * 
	 * @return the filled {@link OWServerBindingConfig}
	 * @throws BindingConfigParseException if the regular expression doesn't match
	 * the given <code>bindingConfig</code>
	 */
	protected OWServerBindingConfig parseInBindingConfig(Item item, String bindingConfig, OWServerBindingConfig config) throws BindingConfigParseException {
		Matcher matcher = IN_BINDING_PATTERN.matcher(bindingConfig);
		
		if (!matcher.matches()) {
			throw new BindingConfigParseException("bindingConfig '" + bindingConfig + "' doesn't represent a valid in-binding-configuration. A valid configuration is matched by the RegExp '"+IN_BINDING_PATTERN+"'");
		}
		matcher.reset();
				
		OWServerBindingConfigElement configElement;

		while (matcher.find()) {
			configElement = new OWServerBindingConfigElement();
			configElement.serverId = matcher.group(1);
			configElement.romId = matcher.group(2);
			configElement.name = matcher.group(3);
			configElement.refreshInterval = Integer.valueOf(matcher.group(4)).intValue();

			logger.debug("OWSERVER: "+configElement);
			config.put(IN_BINDING_KEY, configElement);
		}
		
		return config;
	}


	/**
	 * @{inheritDoc}
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		OWServerBindingConfig config = (OWServerBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getServerId(String itemName) {
		OWServerBindingConfig config = (OWServerBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).serverId : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getRomId(String itemName){
		OWServerBindingConfig config = (OWServerBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).romId : null;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public String getName(String itemName) {
		OWServerBindingConfig config = (OWServerBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).name : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRefreshInterval(String itemName) {
		OWServerBindingConfig config = (OWServerBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).refreshInterval : 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<String> getInBindingItemNames() {
		List<String> inBindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			OWServerBindingConfig httpConfig = (OWServerBindingConfig) bindingConfigs.get(itemName);
			if (httpConfig.containsKey(IN_BINDING_KEY)) {
				inBindings.add(itemName);
			}
		}
		return inBindings;
	}

	
	/**
	 * This is an internal data structure to map commands to 
	 * {@link OWServerBindingConfigElement }. There will be map like 
	 * <code>ON->OWServerBindingConfigElement</code>
	 */
	static class OWServerBindingConfig extends HashMap<Command, OWServerBindingConfigElement> implements BindingConfig {
		private static final long serialVersionUID = 946984678609385662L;
		/** generated serialVersion UID */
		Class<? extends Item> itemType;
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the HTTP binding 
	 * provider.
	 */
	static class OWServerBindingConfigElement implements BindingConfig {
		public String serverId;
		public String romId;
		public String name;
		public int refreshInterval;
		
		@Override
		public String toString() {
			return "OWServerBindingConfigElement [serverId=" + serverId
					+ ", romId=" + romId + ", name=" + name + ", refreshInterval=" + refreshInterval + "]";
		}
	}
	
	
}
