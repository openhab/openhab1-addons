/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarmdecoder.internal;

import java.util.HashMap;
import java.util.Map.Entry;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds binding configuration
 * 
 * @author Bernd Pfrommer
 * @since 1.6.0
 */
public class AlarmDecoderBindingConfig implements BindingConfig {
	private static final Logger logger = LoggerFactory.getLogger(AlarmDecoderBindingConfig.class);
	/**
	 * Class that holds binding configuration information
	 * @param type
	 * @param address
	 * @param feature
	 * @param params
	 */
	public AlarmDecoderBindingConfig(Item item, ADMsgType type, String address,
			String feature,	HashMap<String, String> params) {
		m_item 		= item;
		m_type		= type;
		m_address	= address;
		m_feature	= feature;
		m_params	= params;
		m_itemState	= item.getState();
	}

	public AlarmDecoderBindingConfig(Item item, HashMap<String, String> params) {
		m_item 			= item;
		m_itemState		= item.getState();
		m_type			= ADMsgType.INVALID;
		m_params		= params;
	}

	private final Item	m_item;
	private ADMsgType	m_type = ADMsgType.INVALID;
	private String		m_feature = null;
	private String		m_address = null;
	private HashMap<String, String>	m_params = new HashMap<String,String>();
	// Caching the item state in the binding config, ugh :(
	// But somehow the item.getState() always returns Unintialized
	// so it cannot be used to eliminate duplicate status updates
	private State				m_itemState;

	public HashMap<String,String> getParameters() {
		return m_params;
	}
	public Item getItem() {
		return m_item;
	}
	public String getItemName() {
		return m_item.getName();
	}
	public String getFeature() {
		return m_feature;
	}
	public String getAddress() {
		return m_address;
	}
	public ADMsgType getMsgType() {
		return m_type;
	}
	public State getState() {
		return m_itemState;
	}
	public void setState(State s) {
		m_itemState = s;
	}
	/**
	 * Gets integer parameter from binding config. If parameter is not found, or
	 * the value is outside of min <= x <= max, the default value is returned. 
	 * @param key 
	 * @param min	
	 * @param max
	 * @param deflt	  default value in case not found
	 * @return the integer parameter value, or deflt if not found or out of range
	 */
	public int getIntParameter(String key, int min, int max, int deflt) {
		int i = deflt;
		String v = m_params.get(key);
		if (v != null) {
			try {
				int tmp = Integer.decode(v);
				if (tmp >= min && tmp <= max) {
					i = tmp;
				}
			} catch (NumberFormatException e) {
				logger.error("bad number in int parameter configuration: {} = {}", key, v);
			}
		}
		return i;
	}
	
	public boolean hasFeature(String f) {
		return (m_feature.equals(f));
	}
	
	public String toString() {
		String s = m_item.getName() + "->";
		if (m_type.equals(ADMsgType.INVALID)) {
			s = s + "SEND#";
			for (Entry<String, String> p : m_params.entrySet()) {
				s += ":" + p.getKey() + "=" + p.getValue() + ",";
			}
		} else {
			s = s + m_type + ":" + m_address + "#" + m_feature;
			for (Entry<String, String> p : m_params.entrySet()) {
				s += ":" + p.getKey() + "=" + p.getValue() + ",";
			}
		}
		return s;
	}
}
