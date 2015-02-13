/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.openhab.binding.heatmiser.internal.HeatmiserGenericBindingProvider.HeatmiserBindingConfig;
import org.openhab.binding.lightwaverf.LightwaveRFBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Neil Renaud
 * @since 1.6
 */
public class LightwaveRFGenericBindingProvider extends AbstractGenericBindingProvider implements LightwaveRFBindingProvider {

	private static Logger logger = LoggerFactory.getLogger(LightwaveRFGenericBindingProvider.class);
	
	private static Pattern ROOM_REG_EXP = Pattern.compile(".*room=([0-9]*).*");
	private static Pattern DEVICE_REG_EXP = Pattern.compile(".*device=([0-9]*).*");
	private static Pattern POLL_REG_EXP = Pattern.compile(".*poll_interval=([0-9]*).*");
	private static Pattern TYPE_REG_EXP = Pattern.compile(".*type=([^,]*).*");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "lightwaverf";
	}

	/**
	 * @{inheritDoc}
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
	}

	
	public List<String> getBindingItemsForRoomDevice(String roomId, String deviceId) {
		List<String> bindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			LightwaveRFBindingConfig itemConfig = (LightwaveRFBindingConfig) bindingConfigs.get(itemName);
			if (itemConfig.getRoomId().equals(roomId) && itemConfig.getDeviceId().equals(deviceId)) {
				bindings.add(itemName);
			}
		}
		return bindings;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		LightwaveRFBindingConfig config = new LightwaveRFBindingConfig();

		//parse binding config here ...
		String[] splitConfig = bindingConfig.split(":");
		if(splitConfig.length != 2){
			throw new BindingConfigParseException("Error parsing LightwaveRF Binding Config: " + bindingConfig);
		}
		config.code = splitConfig[0];
		config.type = splitConfig[1];
		logger.info(bindingConfig + "Code["+ config.code + "] Type[" + config.type + "]");
		addBindingConfig(item, config);
	}

	class LightwaveRFBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		public String roomId;
		public String deviceId;
		public LightwaveRfType type;
		
		public String getDeviceId() {
			return deviceId;
		}
		public String getRoomId() {
			return roomId;
		}
	}
}
