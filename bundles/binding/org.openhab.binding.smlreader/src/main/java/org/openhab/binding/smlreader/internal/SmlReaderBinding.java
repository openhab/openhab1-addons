/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import java.util.Map;
import java.util.regex.Matcher;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.smlreader.SmlReaderBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Read SML messages from capable devices in user configured intervalls.
 *
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
public class SmlReaderBinding extends AbstractActiveBinding<SmlReaderBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(SmlReaderBinding.class);

	/** 
	 * the refresh interval which is used to poll values from the SmlReader
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	/** 
	 * Map of SML capable devices from configuration.
	 * */
	private Map<String, SmlDevice> smlDevices = null;
	
	/**
	 * Constructor
	 */
	public SmlReaderBinding() {
		smlDevices = new HashMap<String, SmlDevice>();
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
				String obisValue = null;
				String obisValueTransformed = null;
				String configuredItemObis = provider.getObis(itemName);
				String configuredItemDeviceId = provider.getDeviceNameFromConfiguration(itemName);
				String configuredItemTransformationServiceType = provider.getTransformationType(itemName);
				String configuredItemTransformationServiceFunc = provider.getTransformationFunc(itemName);
				
				SmlDevice smlDevice = smlDevices.get(configuredItemDeviceId);
								
				if(smlDevice == null){
					logger.debug("There is no item with id: {}", configuredItemDeviceId);
					continue;				
				}
				
				smlDevice.readValues();
				
				obisValue = smlDevice.getValue(configuredItemObis);
				
				if(obisValue == null) {
					logger.debug("There is no value for id {} actually", configuredItemObis);
					continue;
				}

				logger.debug("configuredItemTransformationServiceType: {}", configuredItemTransformationServiceType);
				logger.debug("configuredItemTransformationServiceFunc: {}", configuredItemTransformationServiceFunc);
				
				if(configuredItemTransformationServiceType == null || configuredItemTransformationServiceType.isEmpty()) {
					obisValueTransformed = obisValue;
				} else { 
					try {
						TransformationService transformationService = 
							TransformationHelper.getTransformationService(SmlReaderActivator.getContext(), configuredItemTransformationServiceType);
						
						if (transformationService != null) {
							obisValueTransformed = transformationService.transform(configuredItemTransformationServiceFunc, obisValue);
						} else {
							obisValueTransformed = obisValue;
						}
					}
					catch (TransformationException te) {
						logger.error("transformation throws exception transformation={}, obisValue={}, exception={}", configuredItemTransformationServiceType, obisValue, te.getMessage());
						obisValueTransformed = obisValue;
					}
				}
				
				Class<? extends Item> itemType = provider.getItemType(itemName);

				if(itemType.isAssignableFrom(NumberItem.class)){
					eventPublisher.postUpdate(itemName, DecimalType.valueOf(obisValueTransformed));
				} else if(itemType.isAssignableFrom(StringType.class)) {
					eventPublisher.postUpdate(itemName, StringType.valueOf(obisValueTransformed));
				}
			}
		}
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			if (StringUtils.isNotBlank((String) config.get("refresh"))) {
				refreshInterval = Long.parseLong((String) config.get("refresh"));
			}
		}
		
		Enumeration<String> keys = config.keys();
		
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			
			if ("service.pid".equals(key) || "refresh".equals(key)) {
				continue;
			}

			Matcher matcher = SmlReaderConstants.CONFIGURATION_PATTERN.matcher(key);
			
			if(!matcher.matches()) {
				logger.debug("given config key {} doesn't match a valid binding config.", key);
				continue;
			}

			matcher.reset();
			
			while (matcher.find()) {
				
				String deviceId = matcher.group(1);
				String serialPort = (String) config.get(deviceId + "." + SmlReaderConstants.CONFIGURATION_KEY_SERIALPORT);
				
				if(serialPort == null || serialPort.isEmpty())
					serialPort = SmlReaderConstants.DEFAULT_SERIAL_PORT;
				
				smlDevices.put(deviceId, SmlDevice.createInstance(deviceId, serialPort));
			}
		}
		
		setProperlyConfigured(true);
	}

	public void activate() {
	}
	
	public void deactivate() {
		smlDevices.clear();
	}
}
