/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.smlreader.SmlReaderBindingProvider;
import org.openhab.binding.smlreader.conversion.IUnitConverter;
import org.openhab.binding.smlreader.devices.ISmlDevice;
import org.openhab.binding.smlreader.devices.SerialDevice;
import org.openhab.binding.smlreader.devices.TSAPDevice;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
public class SmlReaderBinding extends AbstractActiveBinding<SmlReaderBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(SmlReaderBinding.class);

	/** RegEx to validate a config <code>'^(.*?)\\.(serialPort)$'</code> */
	private static final Pattern SMLDEVICE_CONFIG_PATTERN = Pattern.compile(SmlReaderConstants.Configuration.CONFIGURATION_PATTERN);
	
	/** 
	 * the refresh interval which is used to poll values from the SmlReader
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	/** 
	 * Map of SML capable Devices from configuration
	 * */
	private Map<String, ISmlDevice> smlDevices = null;
	
	/**
	 * Constructor
	 */
	public SmlReaderBinding() {
		smlDevices = new HashMap<String, ISmlDevice>();
	}
	
	private ISmlDevice createTSAPDevice(String configuredDevice, Dictionary<String, ?> config){
		int port = 0;
		String value = (String) config.get(configuredDevice + "." + SmlReaderConstants.Configuration.CONFIGURATION_KEY_TSAP_PORT);
		
		if(value != null){
			port = Integer.parseInt(value);
		}else{
			logger.error("No port specified for " + configuredDevice + "!");
			setProperlyConfigured(false);
		}
		
		String host = null;
		value = (String) config.get(configuredDevice + "." + SmlReaderConstants.Configuration.CONFIGURATION_KEY_TSAP_HOST);
		
		if(value != null){
			host = value;
		}else{
			logger.error("No hostname specified for " + configuredDevice + "!");
			setProperlyConfigured(false);
		}
		
		boolean secure;
		value = (String) config.get(configuredDevice + "." + SmlReaderConstants.Configuration.CONFIGURATION_KEY_TSAP_SECURE);
		
		if(value != null){
			secure = Boolean.valueOf(value);
		}else{
			logger.warn("No valid information specified if the connection has to be secured, assuming 'false' " + configuredDevice + "!");
			secure = false;
		}					

		return new TSAPDevice(configuredDevice, host, port, secure);
	}
	
	private ISmlDevice createSerialDevice(String configuredDevice, Dictionary<String, ?> config){
		String serialPort;
		String value = (String) config.get(configuredDevice + "." + SmlReaderConstants.Configuration.CONFIGURATION_KEY_SERIAL_PORT);
		
		if(value != null){
			serialPort = value;
		}else{
			serialPort = SmlReaderConstants.Configuration.DEFAULT_SERIAL_PORT;
		}
		
		return new SerialDevice(configuredDevice, serialPort);
	}
	
	private Set<String> getConfiguredDevices(Dictionary<String, ?> config) {
		Set<String> configuredDevices = new HashSet<String>();
		
		Enumeration<String> keys = config.keys();
		
		while (keys.hasMoreElements()) {

			String key = (String) keys.nextElement();

			// the config-key enumeration contains additional keys that we
			// don't want to process here ...
			if ("service.pid".equals(key) || "refresh".equals(key)) {
				continue;
			}

			Matcher matcher = SMLDEVICE_CONFIG_PATTERN.matcher(key);
			
			if(!matcher.matches()) {
				logger.debug("given config key '" + key + " " + SMLDEVICE_CONFIG_PATTERN);
				continue;
			}

			matcher.reset();
			matcher.find();

			configuredDevices.add(matcher.group(1));
		}
		
		return configuredDevices;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "SmlReader Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		for(SmlReaderBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				/* get the sml device id from the configured item to read data*/
				String configuredItemDeviceId = provider.getDeviceNameFromConfiguration(itemName);
				ISmlDevice smlDevice = smlDevices.get(configuredItemDeviceId);
								
				if(smlDevice == null){
					logger.debug("There is no item with id: " + configuredItemDeviceId);
					continue;				
				}

				smlDevice.readSmlValuesFromDevice();
				
				/* after the messages are read, try to find the value with the obis code 
				and format it according to the configured converionsType*/
				String configuredItemObis = provider.getObis(itemName);
				@SuppressWarnings("rawtypes")
				Class<? extends IUnitConverter> converterType = provider.getUnitConverter(itemName);
				String itemValue = smlDevice.getObisValue(configuredItemObis, converterType);
				
				if(itemValue != null){
					Class<? extends Item> itemType = provider.getItemType(itemName);
					
					if (itemType.isAssignableFrom(NumberItem.class)) {
						eventPublisher.postUpdate(itemName, new DecimalType(itemValue));
					}
					if (itemType.isAssignableFrom(StringItem.class)) {
						eventPublisher.postUpdate(itemName, new StringType(itemValue));
					}
				}
			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate() is called!");
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		Set<String> configuredDevices = getConfiguredDevices(config);
		
		for(String configuredDevice : configuredDevices){
			ISmlDevice device;
			String value = (String) config.get(configuredDevice + "." + SmlReaderConstants.Configuration.CONFIGURATION_KEY_CONNECTION_TYPE);
			ConnectionType connectionType = ConnectionType.notConfigured;
			
			try{
				 connectionType = ConnectionType.valueOf(value);
			}catch(IllegalArgumentException | NullPointerException e){
				logger.error("The connection type of device '" + configuredDevice + "# is invalid.");
				setProperlyConfigured(false);
				break;
			}
				
			if(connectionType == ConnectionType.serial){
				device = createSerialDevice(configuredDevice, config);
			} else if(connectionType == ConnectionType.tsap){
				device = createTSAPDevice(configuredDevice, config);
			} else{
				device = null;
				logger.info("There is no device configured to read SML messages from.");
				setProperlyConfigured(false);
				break;
			}				

			if (smlDevices.put(configuredDevice, device) != null) {
				logger.info("Recreated reader {}", device.toString());
			} else {
				logger.info("Created reader {}",  device.toString());
			}
		}
		
		if (config != null) {
			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			if (StringUtils.isNotBlank((String) config.get("refresh"))) {
				refreshInterval = Long.parseLong((String) config.get("refresh"));
			}
			
			setProperlyConfigured(true);
		}
	}

	public void activate() {
		logger.debug("activate");
	}
	
	public void deactivate() {
		logger.debug("deactivate");
		smlDevices.clear();
	}
}
