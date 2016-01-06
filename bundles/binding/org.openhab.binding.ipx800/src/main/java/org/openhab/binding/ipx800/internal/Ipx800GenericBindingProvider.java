/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ipx800.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.openhab.binding.ipx800.Ipx800BindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Seebag
 * @since 1.8.0
 */
public class Ipx800GenericBindingProvider extends AbstractGenericBindingProvider implements Ipx800BindingProvider {
	//private final static Pattern ITEM_PATTERN = Pattern.compile("^(?<device>.+):(?<slot>.+)(>(?<to_slot>.+)?)(?<extra>:.*)*$");
	
	private static final Logger logger = LoggerFactory.getLogger(Ipx800Binding.class);
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "ipx800";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch or Number-Items are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		Ipx800BindingConfig config = new Ipx800BindingConfig(item, bindingConfig);
		
		addBindingConfig(item, config);
	}
	
	public Ipx800BindingConfig getBindingConfig(String itemName) {
		return (Ipx800BindingConfig) bindingConfigs.get(itemName);
	}
	
	/**
	 * Example of configuration :
	 * Switch Output { ipx800="myipx:O01" } <- reflect state of output relay
	 * Switch Input { ipx800="myipx:I01" } <- Switch will reflect state of input
	 * Switch InputPressed { ipx800="myipx:I01:p" } <- Switch will change state each time input switch to 1
	 * Switch InputReleased { ipx800="myipx:I01:r" } <- Switch will change state each time input switch to 0
	 * Switch InputShortPress { ipx800="myipx:I01:l" } <- Switch will change on short press (but not on long)
	 * Switch InputLongPress { ipx800="myipx:I01:L" } <- Switch will change on long press (but not on short)
	 * Switch InputSimpleClic { ipx800="myipx:I08:d" } <- Switch will change on simple clic press (but not on double)
	 * Switch InputDoubleClic { ipx800="myipx:I08:D" } <- Switch will change on double clic press (but not on single)
	 * 
	 * Switch InputToOuput {ipx800="myipx:I01>O01"}
	 * Switch InputToOuputOtherDevice {ipx800="myipx:I01>myipx2:O01"}
	 * 
	 * Number PowerSimple {ipx800="myipx:C01"} <- Counter in simple mode
	 * Number PowerAverage {ipx800="myipx:C01:a:0.25:1h"} <- Counter in average mode with 0.25 unit per tick and one hour period
	 * 
	 * @author Seebag
	 *
	 */
	class Ipx800BindingConfig implements BindingConfig {
		private Item item;
		private String deviceName = "";
		private String slotField = "";
		private String toSlotField = "";
		private String toDeviceName = "";
		private String extra = "";
		private boolean toPulse = false;
		
		public Ipx800BindingConfig(Item item, String config) throws BindingConfigParseException {
			this.item = item;
			try {
				Pattern ITEM_PATTERN = Pattern.compile("^(?<device>[^>:]+?):(?<slot>[^>]+?)(?<extra>:[^>]+)*(>(?<to>[^>]+))?$");
				final Matcher matcher = ITEM_PATTERN.matcher(config);

				if (matcher.matches()) {
					deviceName = matcher.group("device");
					slotField = matcher.group("slot");
					if (matcher.group("to") != null) {
						toSlotField = matcher.group("to");
						if (toSlotField.contains(":")) {
							String[] slots = toSlotField.split(":");
							toDeviceName = slots[0];
							toSlotField = slots[1];
							if (slots.length >= 3) {
								toPulse = slots[2].equals("p");
							}
						}
					}
					String mExtra = matcher.group("extra");
					if (mExtra != null) {
						extra = mExtra.substring(1); // Remove first extra :
					}
				} else {
					throw new BindingConfigParseException("Bad Ipx800 item configuration");
				}
			} catch (PatternSyntaxException e) {
				logger.error(e.getMessage());
				// If 'Look-behind group does not have an obvious maximum length near index' error, please update to jre 1.7
			}
		}
		
		public String getDeviceName() {
			return deviceName;
		}
		
		public String getPortField() {
			return slotField;
		}
		
		public String getToPortField() {
			return toSlotField;
		}
		
		public String getToDeviceName() {
			return toDeviceName;
		}
		
		public String getExtra() {
			return extra;
		}
		
		public String getExtra(int argNumber) {
			String[] tab = extra.split(":");
			if (tab.length > argNumber) {
				return tab[argNumber];
			}
			return "";
		}
		
		public Item getItem() {
			return item;
		}
		
		public boolean isToPulse() {
			return toPulse;
		}
	}
	
	
}
