/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
	public AlarmDecoderBindingConfig(String itemName, ADMsgType type, String address,
				String feature,	HashMap<String, String> params) {
		m_itemName = itemName;
		m_type = type;
		m_address = address;
		m_feature = feature;
		m_params = params;
	}

	private final String		m_itemName;
	private final ADMsgType	m_type;
	private final String		m_address;
	private final String		m_feature;
	private final HashMap<String, String>	m_params;

	public HashMap<String,String> getParameters() {
		return m_params;
	}
	public String getItemName() {
		return m_itemName;
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
		String s = m_itemName + "->" + m_type + ":" + m_address +
				"#" + m_feature;
		for (Entry<String, String> p : m_params.entrySet()) {
			s += ":" + p.getKey() + "=" + p.getValue() + ",";
		}
		return s;
	}
}
