/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hdanywhere.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.hdanywhere.HDanywhereBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation class of the HDanywhere Binding Provider
 * 
 * hdanywhere="[192.168.0.88:1:15]" - Number item where input/output value corresponds to a 
 * 			source port number that is/to be attached to given destination port. The format is 
 * 			"hdanywhere ip address":"destination port":"refresh interval". The refresh interval is used
 * 			to poll the hdanywhere device for the current source/destination mapping
 * 
 * hdanywhere="[192.168.0.88:1:15], [192.168.0.88:2:15]" - when the Number item is updated, both output
 * 			ports 1 and 2 will be switched to the given source port number. If the bindings polls for
 * 			the current source/destination map, then the value of the Number item can oscillate if the
 * 			the destination ports are connected to the same source port
 * 
 * 
 * @author Karel Goderis
 * @since 1.4.0
 */
public class HDanywhereGenericBindingProvider extends AbstractGenericBindingProvider implements HDanywhereBindingProvider {

	static final Logger logger = 
			LoggerFactory.getLogger(HDanywhereGenericBindingProvider.class);

	static int counter = 0;

	/** {@link Pattern} which matches a binding configuration part */
	private static final Pattern STATUS_CONFIG_PATTERN = Pattern.compile("\\[(.*):(.*):(.*)\\]");

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "hdanywhere";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only NumberItems are allowed - please check your *.items configuration");
		}		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			parseAndAddBindingConfig(item, bindingConfig);
		} else {
			logger.warn(getBindingType()+" bindingConfig is NULL (item=" + item
					+ ") -> processing bindingConfig aborted!");
		}
	}

	private void parseAndAddBindingConfig(Item item,
			String bindingConfigs) throws BindingConfigParseException {

		String bindingConfig = StringUtils.substringBefore(bindingConfigs, ",");
		String bindingConfigTail = StringUtils.substringAfter(bindingConfigs, ",");

		HDanywhereBindingConfig newConfig = new HDanywhereBindingConfig();
		parseBindingConfig(newConfig,item,bindingConfig);
		addBindingConfig(item, newConfig);              

		while (StringUtils.isNotBlank(bindingConfigTail)) {
			bindingConfig = StringUtils.substringBefore(bindingConfigTail, ",");
			bindingConfig = StringUtils.strip(bindingConfig);
			bindingConfigTail = StringUtils.substringAfter(bindingConfig, ",");
			parseBindingConfig(newConfig,item, bindingConfig);
			addBindingConfig(item, newConfig);      
		}

	}

	/**
	 * Parses the configuration string and update the provided config
	 * 
	 * @param config
	 * @param item
	 * @param bindingConfig
	 * @throws BindingConfigParseException
	 */
	private void parseBindingConfig(HDanywhereBindingConfig config,Item item,
			String bindingConfig) throws BindingConfigParseException {

		String host = null;
		int port = 0;
		int interval = 60;

		if(bindingConfig != null){

			Matcher statusMatcher = STATUS_CONFIG_PATTERN.matcher(bindingConfig);

			if (!statusMatcher.matches()) {
				throw new BindingConfigParseException(
						"HDanywhere binding configuration must consist of either three [config="
								+ statusMatcher + "] parts");
			} else {	
				host = statusMatcher.group(1);
				port = Integer.valueOf(statusMatcher.group(2));
				interval = Integer.valueOf(statusMatcher.group(3));

				HDanywhereBindingConfigElement newElement = new HDanywhereBindingConfigElement(host,port,interval);
				config.add(newElement);						
			}
		}
	}

	/**
	 * This is an internal data structure to track 
	 * {@link ProtocolBindingConfigElement }.
	 */
	static class HDanywhereBindingConfig extends ArrayList<HDanywhereBindingConfigElement> implements BindingConfig {

		private static final long serialVersionUID = -7252828812548386063L;
	}

	public static class HDanywhereBindingConfigElement implements BindingConfig {

		final private String host;
		final private int port;
		final private int interval;

		/**
		 * @return the hostname of the HDanwyhere matrix
		 */
		public String getHost() {
			return host;
		}

		/**
		 * @return the matrix output port number
		 */
		public int getPort() {
			return port;
		}

		/**
		 * @return the interval to poll the HDanywhere matrix for status updates
		 */
		public int getInterval() {
			return interval;
		}

		public HDanywhereBindingConfigElement(String host, int port ,int interval) {
			this.host = host;
			this.port = port;
			this.interval = interval;
		}

		@Override
		public String toString() {
			return "HDanywhereBindingConfigElement [host=" +host
					+ ", port=" + port
					+ ", interval=" + interval +"]";
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean autoUpdate(String itemName) {
		return false;
	}

	@Override
	public List<String> getHosts(String itemName) {
		List<String> hosts = new ArrayList<String>();
		for(String anItem : bindingConfigs.keySet()) {
			HDanywhereBindingConfig aConfig = (HDanywhereBindingConfig) bindingConfigs.get(anItem);
			Iterator<HDanywhereBindingConfigElement> iterator = aConfig.iterator();
			while(iterator.hasNext()) {
				HDanywhereBindingConfigElement anElement = iterator.next();
				if(!hosts.contains(anElement.getHost())) {
					hosts.add(anElement.getHost());
				}
			}
		}
		return hosts;
	}

	@Override
	public List<Integer> getPorts(String host, String itemName) {
		List<Integer> ports = new ArrayList<Integer>();
		HDanywhereBindingConfig aConfig = (HDanywhereBindingConfig) bindingConfigs.get(itemName);
		Iterator<HDanywhereBindingConfigElement> iterator = aConfig.iterator();
		while(iterator.hasNext()) {
			HDanywhereBindingConfigElement anElement = iterator.next();
			if(anElement.getHost().equals(host)) {
				if(!ports.contains(anElement.getPort())) {
					ports.add(anElement.getPort());
				}
			}
		}

		return ports;
	}

	@Override
	public HashMap<String, Integer> getIntervalList() {
		HashMap<String, Integer> result = new HashMap<String, Integer>();

		Collection<String> items = getItemNames();

		Iterator<String> itemIterator = items.iterator();
		while(itemIterator.hasNext()) {
			String anItem = itemIterator.next();

			HDanywhereBindingConfig aConfig = (HDanywhereBindingConfig) bindingConfigs.get(anItem);
			Iterator<HDanywhereBindingConfigElement> iterator = aConfig.iterator();
			while(iterator.hasNext()) {
				HDanywhereBindingConfigElement anElement = iterator.next();
				boolean found = false;

				if(result.containsKey(anElement.getHost())) {
					found = true;
					if(result.get(anElement.getHost()) > anElement.getInterval()) {
						result.put(anElement.getHost(), anElement.getInterval());
						break;
					}
				}

				if(!found) {
					result.put(anElement.getHost(),anElement.getInterval());
				}
			}
		}	
		
		if(result.size() == 0) {
			return null;
		} else {
			return result;	
		}
	}

}
