/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarmdecoder.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.openhab.binding.alarmdecoder.AlarmDecoderBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
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
		// TODO: should parse, then do type checking based on binding config string!
		if ((item instanceof NumberItem) || (item instanceof ContactItem) || (item instanceof StringItem)
				|| (item instanceof SwitchItem)) {
			return;
		}
		throw new BindingConfigParseException("item '" + item.getName()
			+ "' is of type '" + item.getClass().getSimpleName()
			+ "', only Number, Contact, String, or Switch item types are allowed. Check your *.items configuration");
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		HashMap<String, String> params = new HashMap<String, String>();
		String[] parts = s_parseConfigString(item.getName(), bindingConfig, params);
		AlarmDecoderBindingConfig bc = null;
		if (parts[0].equals("SEND")) {
			// binding for sending commands
			if (!(parts.length == 2)) {
				throw new BindingConfigParseException("invalid SEND item config: " + bindingConfig);
			}
			bc = new AlarmDecoderBindingConfig(item, params);
		} else {
			// binding for receiving messages
			ADMsgType mt = ADMsgType.s_fromString(parts[0]);
			HashMap<String, ArrayList<AlarmDecoderBindingConfig>> addrToItemsMap = m_itemMap.get(mt.getValue());
			ArrayList<AlarmDecoderBindingConfig> bcl = addrToItemsMap.get(parts[1]);
			if (bcl == null) {
				// don't have this address mapped to anything yet, start a new item list
				bcl = new ArrayList<AlarmDecoderBindingConfig>();
				addrToItemsMap.put(parts[1], bcl);
			} else {
				// without this line a new binding configuration is entered whenever
				// the .items file is edited
				removeExisting(bcl, item);
			}
			bc = new AlarmDecoderBindingConfig(item, mt, parts[1], parts[2], params);
			bcl.add(bc);
		}
		addBindingConfig(item, bc);
		m_itemsToConfig.put(item.getName(), bc);
		logger.trace("processing item \"{}\" read from .items file with cfg string {}",
				item.getName(), bindingConfig);
	}
	
	/**
	 * Removes existing item configurations
	 * @param bcl array list of binding configs to be checked
	 * @param item item to be checked for
	 */
	private static void removeExisting(ArrayList<AlarmDecoderBindingConfig> bcl, Item item) {
		for (Iterator<AlarmDecoderBindingConfig> it = bcl.iterator(); it.hasNext();) {
			AlarmDecoderBindingConfig bc = it.next();
			if (bc.getItemName().equals(item.getName())) {
				it.remove();
			}
		}
	}
	
	/**
	 * Parses binding configuration string
	 * @param bindingConfig
	 * @return array with ["SEND", "TEXT"], or [type, address, feature + parameters]
	 * @throws BindingConfigParseException if invalid binding string is found
	 */
	
	static private String[] s_parseConfigString(String itemName, String bindingConfig,
			HashMap<String, String> map) throws BindingConfigParseException {
		String shouldBe = "should be MSGType:ADDRESS#feature,param=foo or SEND#sendstring";
		String[] segments = bindingConfig.split("#");
		if (segments.length != 2)
			throw new BindingConfigParseException("invalid item format: " + bindingConfig + ", " + shouldBe);
		if (segments[0].equals("SEND")) {
			String [] params = segments[1].split(",");
			s_parseParameters(itemName, params, 0, map);
			return (segments);
		} else if (ADMsgType.s_containsValidMsgType(segments[0])) {
			return s_parseMsgConfigString(itemName, bindingConfig, map);
		} 
		throw new BindingConfigParseException("invalid item format: " + bindingConfig + ", " + shouldBe);
	}
	
	static private String[] s_parseMsgConfigString(String itemName, String bindingConfig,
			HashMap<String, String> map) throws BindingConfigParseException {
		//
		String shouldBe = "should be MSGType:ADDRESS#feature,param=foo e.g. RFX:0923844#data,bit=5";
		String[] segments = bindingConfig.split("#");
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
		s_parseParameters(itemName, params, 1, map);
	
		String [] retval = {type, addr, feature};

		return retval;
	
	}
	static private void s_parseParameters(String itemName, String [] params, int offset,
			HashMap<String, String> map) {
		for (int i = offset; i < params.length; i++) {
			String [] kv = params[i].split("=");
			if (kv.length == 2) {
				map.put(kv[0],  kv[1]);
			} else {
				logger.error("{} param {} does not have format a=b", itemName, params[i]);
			}
		}
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
