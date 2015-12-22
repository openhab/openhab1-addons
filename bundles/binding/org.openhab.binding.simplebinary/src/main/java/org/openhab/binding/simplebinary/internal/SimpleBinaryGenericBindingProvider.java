/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.simplebinary.SimpleBinaryBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
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
 * @author Vita Tucek
 * @since 1.8.0
 */
public class SimpleBinaryGenericBindingProvider extends AbstractGenericBindingProvider implements SimpleBinaryBindingProvider {

	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author vita
	 * @since 1.8.0
	 */
	class SimpleBinaryBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values

		public Item item;
		Class<? extends Item> itemType;

		/**
		 * Contains slave address
		 * 
		 */
		public String device;
		/**
		 * Contains slave address
		 * 
		 */
		public int busAddress;
		public int direction = 0;
		public int address;
		private String datatype = "word";

		/**
		 * Return item data length
		 * 
		 * @return
		 */
		public int getDataLenght() {
			if (getDataType() == SimpleBinaryTypes.ARRAY) {
				Matcher matcher = Pattern.compile("^array\\[(\\d+)\\]$").matcher(datatype);

				return Integer.valueOf(matcher.group(1)).intValue();
			} else
				return 1;
		}

		/**
		 * Return item data type
		 * 
		 * @return
		 */
		public SimpleBinaryTypes getDataType() {
			if (datatype.equals("byte"))
				return SimpleBinaryTypes.BYTE;
			if (datatype.equals("word"))
				return SimpleBinaryTypes.WORD;
			if (datatype.equals("dword"))
				return SimpleBinaryTypes.DWORD;
			if (datatype.equals("float"))
				return SimpleBinaryTypes.FLOAT;
			if (datatype.equals("hsv"))
				return SimpleBinaryTypes.HSB;
			if (datatype.equals("rgb"))
				return SimpleBinaryTypes.RGB;
			if (datatype.equals("rgbw"))
				return SimpleBinaryTypes.RGBW;

			Matcher matcher = Pattern.compile("^array\\[(\\d+)\\]$").matcher(datatype);
			if (matcher.matches())
				return SimpleBinaryTypes.ARRAY;

			logger.debug("getDataType() - unresolved type: " + datatype);
			return SimpleBinaryTypes.UNKNOWN;
		}

		public String toString() {
			return item.getName() + " (Device=" + this.device + " BusAddress=" + this.busAddress + " MemAddress=" + this.address + " DataType=" + this.getDataType()
					+ " Direction=" + this.direction + ")";
		}
	}

	/**
	 * This is a helper class holding binding info configuration details
	 * 
	 * @author vita
	 * @since 1.8.0
	 */
	class SimpleBinaryInfoBindingConfig implements BindingConfig {

		/**
		 * 
		 */
		public Item item;
		/**
		 * Contains device(port) name ex.: port01
		 */
		public String device;
		/**
		 * Contains slave address
		 */
		public int busAddress;
		/**
		 * Requested info type
		 */
		public InfoType infoType;
	}

	public enum InfoType {
		STATE, PREVIOUS_STATE, STATE_CHANGE_TIME, PACKET_LOST
	}

	private static final Logger logger = LoggerFactory.getLogger(SimpleBinaryBinding.class);

	/**
	 * Return all configs map
	 * 
	 * @return
	 */
	public Map<String, BindingConfig> configs() {
		return bindingConfigs;
	}

	public String getBindingType() {
		return "simplebinary";
	}

	/**
	 * Return item config
	 * 
	 * @param itemName
	 *            Item name
	 * @return
	 */
	public BindingConfig getItemConfig(String itemName) {

		return bindingConfigs.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {

		logger.debug("processBindingConfiguration() method is called!");
		logger.debug("Item:" + item + "/Config:" + bindingConfig);

		super.processBindingConfiguration(context, item, bindingConfig);

		BindingConfig commonConfig = null;

		// config
		//
		// device:busAddress:address/ID:type:direction
		//
		// device - target device/port (ex. "port" , "port1", "port2")
		// device:busAddress - bus / device address (0-127)
		// address/ID - number
		// type - byte or word or dword or array[length] or rgb or rgbw or hsb
		// direction - "I" or "O" or "IO"
		//
		// Matcher matcher =
		// Pattern.compile("^(port\\d*):(\\d+):(\\d+):([a-z0-9\\[\\]]+):(I|O|IO)$").matcher(bindingConfig);
		Matcher matcher = Pattern.compile("^(port\\d*):(\\d+):(\\d+)((:[a-zA-Z0-9_]*)*)$").matcher(bindingConfig);

		if (!matcher.matches()) {
			// look for info config
			matcher = Pattern.compile("^(port\\d*)(:(\\d+))*:info:((state)|(previous_state)|(state_change_time)|(packet_lost))$").matcher(bindingConfig);

			if (!matcher.matches()) {
				throw new BindingConfigParseException("Illegal config format: " + bindingConfig
						+ ". Correct format: simplebinary=\"port:deviceAddress:itemAddress:dataType:ioDirection\". Example: simplebinary=\"port:1:1:byte:O\"");
			} else {
				// device info config
				SimpleBinaryInfoBindingConfig config = new SimpleBinaryInfoBindingConfig();
				commonConfig = config;

				config.item = item;
				config.device = matcher.group(1);

				if (matcher.group(3) != null)
					config.busAddress = Integer.valueOf(matcher.group(3)).intValue();
				else
					config.busAddress = -1;

				String param = matcher.group(4);

				if (param.equalsIgnoreCase("state")) {
					config.infoType = InfoType.STATE;
				} else if (param.equalsIgnoreCase("previous_state")) {
					config.infoType = InfoType.PREVIOUS_STATE;
				} else if (param.equalsIgnoreCase("state_change_time")) {
					config.infoType = InfoType.STATE_CHANGE_TIME;
				} else if (param.equalsIgnoreCase("packet_lost")) {
					config.infoType = InfoType.PACKET_LOST;
				} else {
					throw new BindingConfigParseException("Unsupported info parameter " + param);
				}
			}
		} else {
			SimpleBinaryBindingConfig config = new SimpleBinaryBindingConfig();
			commonConfig = config;

			config.item = item;
			config.itemType = item.getClass();
			config.device = matcher.group(1);
			config.busAddress = Integer.valueOf(matcher.group(2)).intValue();
			config.address = Integer.valueOf(matcher.group(3)).intValue();

			boolean dataTypeSpecified = false;

			// check if optional parameters are specified
			if (matcher.group(4).length() > 0) {

				String[] optionalConfigs = matcher.group(4).substring(1).split(":");

				for (int i = 0; i < optionalConfigs.length; i++) {
					String param = optionalConfigs[i].toLowerCase();
					// is direction?
					if (param.equals("i") || param.equals("o") || param.equals("io"))
						config.direction = param.equals("io") ? 0 : param.equals("i") ? 1 : 2;
					else {
						// is datatype?
						matcher = Pattern.compile("^byte|word|dword|float|hsb|rgb|rgbw|array\\[\\d+\\]$").matcher(param);

						if (matcher.matches()) {
							// datatype specified as optional parameter
							dataTypeSpecified = true;

							if (config.itemType.isAssignableFrom(NumberItem.class)) {
								if (!param.equals("byte") && !param.equals("word") && !param.equals("dword") && !param.equals("float")) {
									logger.warn("Item %s supported datatypes: byte, word, dword or float. Type %s is ignored. Setted to word.", item.getName(), param);
									config.datatype = "word";
								} else
									config.datatype = param;

							} else if (config.itemType.isAssignableFrom(SwitchItem.class)) {
								if (!param.equals("byte"))
									logger.warn("Item %s support datatype byte only. Type %s is ignored.", item.getName(), param);
								config.datatype = "byte";

							} else if (config.itemType.isAssignableFrom(DimmerItem.class)) {
								if (!param.equals("byte"))
									logger.warn("Item %s support datatype byte only. Type %s is ignored.", item.getName(), param);
								config.datatype = "byte";

							} else if (config.itemType.isAssignableFrom(ColorItem.class)) {
								if (!param.equals("rgb") && !param.equals("rgbw") && !param.equals("hsb")) {
									logger.warn("Item %s supported datatypes: hsb, rgb or rgbw. Type %s is ignored. Setted to rgb.", item.getName(), param);
									config.datatype = "rgb";
								} else
									config.datatype = param;

							} else if (config.itemType.isAssignableFrom(StringItem.class)) {
								if (!param.startsWith("array"))
									logger.warn("Item %s support datatype array only. Type %s is ignored. Setted to ARRAY with length 32.", item.getName(), param);
								config.datatype = "array[32]";

							} else if (config.itemType.isAssignableFrom(ContactItem.class)) {
								if (!param.equals("byte"))
									logger.warn("Item %s support datatype byte only. Type %s is ignored.", item.getName(), param);
								config.datatype = "byte";

							} else if (config.itemType.isAssignableFrom(RollershutterItem.class)) {
								if (!param.equals("word"))
									logger.warn("Item %s support datatype word only. Type %s is ignored.", item.getName(), param);
								config.datatype = "word";

							} else {
								throw new BindingConfigParseException("Unsupported item type: " + item);
							}
						} else {
							logger.warn("Item %s. Unsupported optional parameter %s", item.getName(), optionalConfigs[i]);
						}
					}
				}
			}

			// datatype not specified as optional parameter -> set default
			if (!dataTypeSpecified) {
				if (config.itemType.isAssignableFrom(NumberItem.class)) {
					logger.warn("Item %s has not specified datatype. Setted to WORD.", item.getName());
					config.datatype = "word";
				} else if (config.itemType.isAssignableFrom(SwitchItem.class))
					config.datatype = "byte";
				else if (config.itemType.isAssignableFrom(DimmerItem.class))
					config.datatype = "byte";
				else if (config.itemType.isAssignableFrom(ColorItem.class)) {
					logger.warn("Item %s has not specified datatype. Setted to RGB.", item.getName());
					config.datatype = "rgb";
				} else if (config.itemType.isAssignableFrom(StringItem.class)) {
					logger.warn("Item %s has not specified datatype with length. Setted to ARRAY with length 32.", item.getName());
					config.datatype = "array[32]";
				} else if (config.itemType.isAssignableFrom(ContactItem.class))
					config.datatype = "byte";
				else if (config.itemType.isAssignableFrom(RollershutterItem.class))
					config.datatype = "word";
				else {
					throw new BindingConfigParseException("Unsupported item type: " + item);
				}
			}
		}

		logger.debug(commonConfig.toString());

		addBindingConfig(item, commonConfig);
	}

	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// all types welcome

		// if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		// throw new BindingConfigParseException("item '" + item.getName()
		// + "' is of type '" + item.getClass().getSimpleName()
		// +
		// "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		// }

		// logger.debug("validateItemType() method is called!");
		// logger.debug("Item:" + item + "/Config:" + bindingConfig);
	}
}
