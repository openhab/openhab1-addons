/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.LightwaveRfBindingProvider;
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
 * @since 1.7.0
 */
public class LightwaveRfGenericBindingProvider extends
		AbstractGenericBindingProvider implements LightwaveRfBindingProvider {

	private static Logger logger = LoggerFactory
			.getLogger(LightwaveRfGenericBindingProvider.class);

	private static Pattern ROOM_REG_EXP = Pattern.compile(".*room=([0-9]*).*");
	private static Pattern DEVICE_REG_EXP = Pattern
			.compile(".*device=([0-9]*).*");
	private static Pattern POLL_REG_EXP = Pattern.compile(".*poll=([0-9]*).*");
	private static Pattern TYPE_REG_EXP = Pattern.compile(".*type=([^,]*).*");
	private static Pattern SERIAL_REG_EXP = Pattern
			.compile(".*serial=([^,]*).*");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "lightwaverf";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		// if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		// throw new BindingConfigParseException("item '" + item.getName()
		// + "' is of type '" + item.getClass().getSimpleName()
		// +
		// "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		// }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		try {
			String roomId = null;
			String deviceId = null;
			LightwaveRfType type = null;
			int poll = -1;
			String serialId = null;
			LightwaveRfItemDirection direction = LightwaveRfItemDirection.IN_AND_OUT;
			
			if(bindingConfig.startsWith("<")){
				direction = LightwaveRfItemDirection.IN_ONLY;
			} 
			else if (bindingConfig.startsWith(">")) {
				direction = LightwaveRfItemDirection.OUT_ONLY;
			}

			Matcher roomMatcher = ROOM_REG_EXP.matcher(bindingConfig);
			if (roomMatcher.matches()) {
				roomId = roomMatcher.group(1);
			}

			Matcher deviceMatcher = DEVICE_REG_EXP.matcher(bindingConfig);
			if (deviceMatcher.matches()) {
				deviceId = deviceMatcher.group(1);
			}

			Matcher serialMatcher = SERIAL_REG_EXP.matcher(bindingConfig);
			if (serialMatcher.matches()) {
				serialId = serialMatcher.group(1);
			}

			Matcher typeMatcher = TYPE_REG_EXP.matcher(bindingConfig);
			if (typeMatcher.matches()) {
				type = LightwaveRfType.valueOf(typeMatcher.group(1));
			}

			Matcher pollMatcher = POLL_REG_EXP.matcher(bindingConfig);
			if (pollMatcher.matches()) {
				poll = Integer.valueOf(pollMatcher.group(1));
			}

			LightwaveRfBindingConfig config = new LightwaveRfBindingConfig(
					roomId, deviceId, serialId, type, poll, direction);

			logger.info(
					"ConfigString[{}] Room[{}] Device[{}] Serial[{}] Type[{}] Poll[{}]",
					new Object[] {bindingConfig, roomId, deviceId, serialId, type, poll});
			addBindingConfig(item, config);
		} catch (Exception e) {
			throw new BindingConfigParseException(
					"Error parsing binding for Context[" + context + "] Item["
							+ item + "] BindingConfig[" + bindingConfig
							+ "] ErrorMessage: " + e.getMessage());
		}

	}

	@Override
	public List<String> getBindingItemsForRoomDevice(String roomId,
			String deviceId) {
		List<String> bindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			LightwaveRfBindingConfig itemConfig = (LightwaveRfBindingConfig) bindingConfigs
					.get(itemName);
			if (roomId != null && roomId.equals(itemConfig.getRoomId())) {
				if (deviceId != null
						&& deviceId.equals(itemConfig.getDeviceId())) {
					bindings.add(itemName);
				}
			}
		}
		return bindings;
	}

	@Override
	public List<String> getBindingItemsForSerial(String serialId) {
		List<String> bindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			LightwaveRfBindingConfig itemConfig = (LightwaveRfBindingConfig) bindingConfigs
					.get(itemName);
			if (serialId != null && serialId.equals(itemConfig.getSerialId())) {
				bindings.add(itemName);
			}
		}
		return bindings;

	}

	@Override
	public List<String> getBindingItemsForRoom(String roomId) {
		List<String> bindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			LightwaveRfBindingConfig itemConfig = (LightwaveRfBindingConfig) bindingConfigs
					.get(itemName);
			if (roomId != null && roomId.equals(itemConfig.getRoomId())) {
				bindings.add(itemName);
			}
		}
		return bindings;
	}

	@Override
	public List<String> getBindingItemsForType(LightwaveRfType type) {
		List<String> bindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			LightwaveRfBindingConfig itemConfig = (LightwaveRfBindingConfig) bindingConfigs
					.get(itemName);
			if (type.equals(itemConfig.getType())) {
				bindings.add(itemName);
			}
		}
		return bindings;
	}

	@Override
	public int getPollInterval(String itemName) {
		LightwaveRfBindingConfig itemConfig = (LightwaveRfBindingConfig) bindingConfigs
				.get(itemName);
		return itemConfig != null ? itemConfig.getPollTime() : -1;
	}

	@Override
	public LightwaveRfItemDirection getDirection(String itemName) {
		LightwaveRfBindingConfig itemConfig = (LightwaveRfBindingConfig) bindingConfigs
				.get(itemName);
		return itemConfig != null ? itemConfig.getDirection() : LightwaveRfItemDirection.IN_AND_OUT;
	}
	
	@Override
	public LightwaveRfType getTypeForItemName(String itemName) {
		LightwaveRfBindingConfig itemConfig = (LightwaveRfBindingConfig) bindingConfigs
				.get(itemName);
		return itemConfig != null ? itemConfig.getType() : null;
	}

	@Override
	public String getRoomId(String itemString) {
		LightwaveRfBindingConfig itemConfig = (LightwaveRfBindingConfig) bindingConfigs
				.get(itemString);
		return itemConfig != null ? itemConfig.getRoomId() : null;

	}

	@Override
	public String getDeviceId(String itemString) {
		LightwaveRfBindingConfig itemConfig = (LightwaveRfBindingConfig) bindingConfigs
				.get(itemString);
		return itemConfig != null ? itemConfig.getDeviceId() : null;
	}

	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Neil Renaud
	 * @since 1.7.0
	 */
	class LightwaveRfBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		private final String roomId;
		private final String deviceId;
		private final String serialId;
		private final LightwaveRfType type;
		private final int pollTime;
		private final LightwaveRfItemDirection direction;

		public LightwaveRfBindingConfig(String roomId, String deviceId,
				String serialId, LightwaveRfType type, int pollTime, LightwaveRfItemDirection direction) {
			this.roomId = roomId;
			this.deviceId = deviceId;
			this.serialId = serialId;
			this.type = type;
			this.pollTime = pollTime;
			this.direction = direction;
		}

		public String getDeviceId() {
			return deviceId;
		}

		public String getRoomId() {
			return roomId;
		}

		public String getSerialId() {
			return serialId;
		}

		public LightwaveRfType getType() {
			return type;
		}

		public int getPollTime() {
			return pollTime;
		}
		
		public LightwaveRfItemDirection getDirection() {
			return direction;
		}
	}
}
