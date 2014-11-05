/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import java.util.HashMap;
import java.util.Map.Entry;

/*
 * The DeviceType class holds device type definitions that are read from
 * an xml file.
 *
 * @author Bernd Pfrommer
 * @since 1.6.0
 */
public class DeviceType {
	private String m_productKey		= null;
	private String m_model			= "";
	private String m_description 	= "";
	private HashMap<String, String> m_features = new HashMap<String, String>();
	
	/**
	 * Constructor
	 * @param aProductKey  the product key for this device type
	 */
	public DeviceType(String aProductKey) {
		m_productKey = aProductKey;
	}
	/**
	 * Get supported features
	 * @return all features that this device type supports
	 */
	public HashMap<String, String> getFeatures() { return m_features; }
	/**
	 * Sets the descriptive model string
	 * @param aModel descriptive model string
	 */
	public void setModel(String aModel) { m_model = aModel; }
	/**
	 * Sets free text description
	 * @param aDesc free text description
	 */
	public void setDescription(String aDesc) { m_description = aDesc; }
	/**
	 * Adds feature to this device type
	 * @param aKey    the key (e.g. "switch") under which this feature can be referenced in the item binding config
	 * @param aFeatureName  the name (e.g. "GenericSwitch") under which the feature has been defined 
	 * @return false if feature was already there
	 */
	public boolean addFeature(String aKey, String aFeatureName) {
		if (m_features.containsKey(aFeatureName)) return false;
		m_features.put(aKey,  aFeatureName);
		return true;
	}
	public String toString() {
		String s = "pk:" + m_productKey + "|model:" + m_model +
				"|desc:" + m_description + "|features";
		for (Entry<String, String> f : m_features.entrySet()) {
			s += ":" + f.getKey() + "=" + f.getValue();
		}
		return s;
	}
	
}
