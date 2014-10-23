/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarmdecoder.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.openhab.binding.alarmdecoder.AlarmDecoderBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class parses the binding configuration in the items file.
 * 
 * @author Bernd Pfrommer
 * @since 1.6.0
 */
public class AlarmDecoderGenericBindingProvider extends AbstractGenericBindingProvider implements AlarmDecoderBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(AlarmDecoderGenericBindingProvider.class);

	// m_itemMap allows quick retrieval of all items that reference a given message
	// type and address, thus facilitating the lookup when messages arrive
	
	private ArrayList<HashMap<String, ArrayList<AlarmDecoderBindingConfig> >> m_itemMap	=
			new ArrayList<HashMap<String, ArrayList<AlarmDecoderBindingConfig>>>();
	private HashMap<String, AlarmDecoderBindingConfig> m_itemsToConfig =
			new HashMap<String, AlarmDecoderBindingConfig>();
	
	public AlarmDecoderGenericBindingProvider() {
		for (int i = 0; i < ADMsgType.NUMTYPES.getValue(); i++) {
			m_itemMap.add(new HashMap<String, ArrayList<AlarmDecoderBindingConfig>>());
		}
	}
	public AlarmDecoderBindingConfig getBindingConfig(String itemName) {
		return m_itemsToConfig.get(itemName);
	}
	
	
	@Override
	public ArrayList<AlarmDecoderBindingConfig> getConfigurations(ADMsgType mt, String addr,
			String feature) {
		HashMap<String, ArrayList<AlarmDecoderBindingConfig>> a2c = m_itemMap.get(mt.getValue());
		if (addr != null) {
			ArrayList<AlarmDecoderBindingConfig> al = a2c.get(addr);
			return (al == null ? new ArrayList<AlarmDecoderBindingConfig>() : al);
		} else {
			ArrayList<AlarmDecoderBindingConfig> al = new ArrayList<AlarmDecoderBindingConfig>();
			for (Entry<String, ArrayList<AlarmDecoderBindingConfig>> a : a2c.entrySet()) {
				if (feature == null) {
					al.addAll(a.getValue());
				} else {
					for (AlarmDecoderBindingConfig cf : a.getValue()) {
						al.add(cf);
					}
				}
			}
			return (al);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "alarmdecoder";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if ((item instanceof NumberItem) || (item instanceof ContactItem) || (item instanceof StringItem)) {
			return;
		}
		throw new BindingConfigParseException("item '" + item.getName()
			+ "' is of type '" + item.getClass().getSimpleName()
			+ "', only Number, Contact, or String item types are allowed. Check your *.items configuration");
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		HashMap<String, String> params = new HashMap<String, String>();
		String[] parts = s_parseConfigString(bindingConfig, params);
		
		ADMsgType mt = ADMsgType.s_fromString(parts[0]);
		
		HashMap<String, ArrayList<AlarmDecoderBindingConfig>> addrToItemsMap = m_itemMap.get(mt.getValue());
		ArrayList<AlarmDecoderBindingConfig> bcl = addrToItemsMap.get(parts[1]);
		if (bcl == null) {
			// don't have this address mapped to anything yet, start a new item list
			bcl = new ArrayList<AlarmDecoderBindingConfig>();
			addrToItemsMap.put(parts[1], bcl);
		}
		AlarmDecoderBindingConfig bc = new AlarmDecoderBindingConfig(item.getName(), mt, parts[1], parts[2], params);
		bcl.add(bc);
		addBindingConfig(item, bc);

		m_itemsToConfig.put(item.getName(), bc);
		logger.trace("processing item \"{}\" read from .items file with cfg string {}",
				item.getName(), bindingConfig);
	}
	
	/**
	 * Parses binding configuration string
	 * @param bindingConfig
	 * @return array with [type, address, feature + parameters]
	 * @throws BindingConfigParseException
	 */
	
	static private String[] s_parseConfigString(String bindingConfig,
			HashMap<String, String> map) throws BindingConfigParseException {
		//
		String shouldBe = "should be MSGType:ADDRESS#feature,param=foo e.g. RFX:0923844#data,bit=5";
		//
		String[] segments = bindingConfig.split("#");
		if (segments.length != 2)
			throw new BindingConfigParseException("invalid item format: " + bindingConfig + ", " + shouldBe);
		String[] dev = segments[0].split(":");
		
		if (dev.length != 2) {
			throw new BindingConfigParseException("missing colon in item format: "
					+ bindingConfig + ", " + shouldBe);
		}
		String type = dev[0];
		String addr = dev[1];
		ADMsgType mt = ADMsgType.s_fromString(type);
		if (!mt.isValid() || !s_isValidAddress(mt, addr)) {
			throw new BindingConfigParseException("invalid device address for "
					+ type + ": " + addr + " in items file.");
		}
		
		String [] params = segments[1].split(",");
		String feature = params[0];
		for (int i = 1; i < params.length; i++) {
			String [] kv = params[i].split("=");
			if (kv.length == 2) {
				map.put(kv[0],  kv[1]);
			} else {
				logger.error("parameter {} does not have format a=b", params[i]);
			}
		}

		String [] retval = {type, addr, feature};

		return retval;
	}
	/**
	 * Address validator
	 * @param type the known msg type of the configuration
	 * @param addr the address string of the configuration
	 * @return true if valid address for given type
	 */
	static private boolean s_isValidAddress(ADMsgType type, String addr) {
		switch (type) {
		case KPM:
			return (addr.matches("[0-9]+") || addr.equalsIgnoreCase("any"));
		case RFX:
			return (addr.matches("[0-9]+")); // e.g. 0923844
		case EXP:
		case REL:
			return (addr.matches("[0-9][0-9],[0-9][0-9]")); // e.g 14,02
		case INVALID:
		default:
			return (false);
		}
	}
	@Override
	public Boolean autoUpdate(String itemName) {
		return true;
	}
	

}
