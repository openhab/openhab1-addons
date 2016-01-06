/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.lang.StringUtils;

import org.openhab.binding.rwesmarthome.RWESmarthomeBindingProvider;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * Examples of valid item configurations (replace '2951a048-1d21-5caf-d866-b63bc00280f4' to your specific device id):
 * 
 * Contact rweContact "Window livingroom [MAP(de.map):%s]" <contact> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=contact"}
 * Number rweHumidity "Humidity livingroom [%.1f %%]" <temperature> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=humidity"}
 * Number rweLuminance "Luminance corridor [%d %%]" <slider> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=luminance"}
 * Number rweSettemp "Settemp living [%.1f °C]" <temperature> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=settemperature"}
 * Number rweTemp "Temp living [%.1f °C]" <temperature> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=temperature"}
 * Switch rweAlarm "Alarm corridor" <siren> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=alarm"}
 * Switch rweSettempOpMode "Settemp living auto" <temperature> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=operationmodeauto"}
 * Switch rweSmokeDetector "Smokedetector corridor" <fire> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=smokedetector"}
 * Switch rweSwitch "Light corridor" <switch> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=switch"}
 * Switch rweVariable "Variable TEST" <switch> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=variable"}
 * Rollershutter rweRollershutter "Rollershutter living [%d %%]" <rollershutter> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=rollershutterinverted"}
 * Dimmer rweDimmer "Light [%d %%]" <slider> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=dimmer"}
 * 
 * @author ollie-dev
 * @since 1.8.0
 */
public class RWESmarthomeGenericBindingProvider extends AbstractGenericBindingProvider implements RWESmarthomeBindingProvider, BindingChangeListener {

	private static final Logger logger = LoggerFactory.getLogger(RWESmarthomeGenericBindingProvider.class);
	private Map<String, Item> items = new HashMap<String, Item>();
	private RWESmarthomeContext context = RWESmarthomeContext.getInstance();
	private Map<String, Item> itemMapById = new HashMap<String, Item>();
	private MultiKeyMap itemMapByIdAndParam = new MultiKeyMap();
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return context.getBindingType();
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(
				item instanceof ContactItem || 
				item instanceof DimmerItem ||
				item instanceof NumberItem ||
				item instanceof RollershutterItem ||
				item instanceof StringItem ||
				item instanceof SwitchItem)
			) {
			logger.debug("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only the following item types are allowed: "
					+ "Contact, Dimmer, Number, Rollershutter, String, Switch - please check your *.items configuration");
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only the following item types are allowed: "
					+ "Contact, Dimmer, Number, Rollershutter, String, Switch - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);		
		RWESmarthomeBindingConfig config = new RWESmarthomeBindingConfig();
				
		// parse config
		bindingConfig = StringUtils.trimToEmpty(bindingConfig);
		String[] configstrings = bindingConfig.split("[,]");

		Pattern patternForId = Pattern.compile("^([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})$");
		Pattern patternForParam = Pattern.compile("^(temperature|humidity|settemperature|variable|contact|switch|operationmodeauto|luminance|smokedetector|dimmer|dimmerinverted|rollershutter|rollershutterinverted|alarm)$");

		for (String configstring : configstrings) {
			String[] configParts = StringUtils.trimToEmpty(configstring).split("[=]");
			if (configParts.length != 2) {
				throw new BindingConfigParseException("Each entry must have a key and a value");
			}
			
			String key = StringUtils.trim(configParts[0]);
			String value = StringUtils.trim(configParts[1]);
			
			// id
			if("id".equalsIgnoreCase(key)) {
				if(!patternForId.matcher(value).matches()) {
					throw new BindingConfigParseException("id '" + value + "' is not a valid logicalDeviceId. Valid example: 12345a67-890b-1c23-de45-f67890123456");
				}
				config.setDeviceId(value);
				
			// param
			} else if("param".equalsIgnoreCase(key)){
				if(!patternForParam.matcher(value).matches()) {
					throw new BindingConfigParseException("Invalid configuration: 'param' must be one of the following: temperature|humidity|settemperature|variable|contact|switch|operationmodeauto|luminance|smokedetector|dimmer|dimmerinverted|rollershutter|rollershutterinverted|alarm");
				}
				config.setDeviceParam(value);
		
			// unknown configuration key
			} else {
				logger.warn("Invalid configuration key '%s' - only 'id' and 'param' are allowed!", key, value);
				throw new BindingConfigParseException("Invalid configuration key '" + key + "'");
			}
		}
		
		if(config.getDeviceId() == null) {
			throw new BindingConfigParseException("Invalid configuration: id is missing!");
		}
		if(config.getDeviceParam() == null) {
			throw new BindingConfigParseException("Invalid configuration: param is missing!");
		}
		
		logger.info("Adding item {} with {}", item.getName(), config.toString());
		items.put(item.getName(), item);
		
		itemMapById.put(config.getDeviceId(), item);
		itemMapByIdAndParam.put(config.getDeviceId(), config.getDeviceParam(), item);
		
		addBindingConfig(item, config);
		this.context.setBindingChanged(true);
	}
	
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author ollie-dev
	 * @since 1.8.0
	 */
	public class RWESmarthomeBindingConfig implements BindingConfig {
		private String deviceId = null;
		private String deviceParam = null;
		
		/**
		 * Returns the id of the device.
		 * 
		 * @return the device id
		 */
		public String getDeviceId() {
			return deviceId;
		}

		/**
		 * Sets the id of the device.
		 * 
		 * @param deviceId
		 */
		public void setDeviceId(String deviceId) {
			this.deviceId = deviceId;
		}		
		
		/**
		 * Returns the device parameter.
		 * 
		 * @return device parameter
		 */
		public String getDeviceParam() {
			return deviceParam;
		}

		/**
		 * Sets the device parameter.
		 * 
		 * @param deviceParam
		 */
		public void setDeviceParam(String deviceParam) {
			this.deviceParam = deviceParam;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			String configString = "RWESmarthomeBindingConfig [deviceId=" + deviceId + ", param=" + deviceParam + "]";
			return configString;
		}

	}


	/* (non-Javadoc)
	 * @see org.openhab.binding.rwesmarthome.RWESmarthomeBindingProvider#getItem(java.lang.String)
	 */
	@Override
	public Item getItem(String itemName) {
		return items.get(itemName);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.rwesmarthome.RWESmarthomeBindingProvider#getItemById(java.lang.String)
	 */
	@Override
	public String getItemNameById(String id) {
		Item item = itemMapById.get(id);
		if(item != null) {
			return item.getName();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.rwesmarthome.RWESmarthomeBindingProvider#getItemByIdAndParam(java.lang.String, java.lang.String)
	 */
	@Override
	public String getItemNameByIdAndParam(String id, String param) {
		Item item = (Item) itemMapByIdAndParam.get(id, param);
		if(item != null) {
			return item.getName();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.binding.BindingChangeListener#bindingChanged(org.openhab.core.binding.BindingProvider, java.lang.String)
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		logger.debug("BINDING CHANGED! item='{}', provider='{}'", itemName, provider);
		context.setBindingChanged(true);
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.binding.BindingChangeListener#allBindingsChanged(org.openhab.core.binding.BindingProvider)
	 */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		logger.debug("ALL BINDINGS CHANGED! provider='{}'", provider);
		context.setBindingChanged(true);
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.binding.rwesmarthome.RWESmarthomeBindingProvider#getBindingFor(java.lang.String)
	 */
	public RWESmarthomeBindingConfig getBindingConfigFor(String itemName) {
		return (RWESmarthomeBindingConfig) bindingConfigs.get(itemName);
	}
}
