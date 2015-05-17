/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import java.util.ArrayList;
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
	private HashMap<String, FeatureGroup> m_featureGroups =
				new HashMap<String, FeatureGroup>();
	
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
	 * Get all feature groups
	 * @return all feature groups of this device type
	 */
	public HashMap<String, FeatureGroup> getFeatureGroups() { return m_featureGroups; }
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
		if (m_features.containsKey(aKey)) return false;
		m_features.put(aKey,  aFeatureName);
		return true;
	}
	/**
	 * Adds feature group to device type
	 * @param aKey name of the feature group, which acts as key for lookup later
	 * @param fg feature group to add
	 * @return true if add succeeded, false if group was already there
	 */
	public boolean addFeatureGroup(String aKey, FeatureGroup fg) {
		if (m_features.containsKey(aKey)) return false;
		m_featureGroups.put(aKey,  fg);
		return true;
	}
	
	public String toString() {
		String s = "pk:" + m_productKey + "|model:" + m_model +
				"|desc:" + m_description + "|features";
		for (Entry<String, String> f : m_features.entrySet()) {
			s += ":" + f.getKey() + "=" + f.getValue();
		}
		s += "|groups";
		for (Entry<String, FeatureGroup> f : m_featureGroups.entrySet()) {
			s += ":" + f.getKey() + "=" + f.getValue();
		}
		return s;
	}
	/**
	 * Class that reflects feature group association
	 * @author Bernd Pfrommer
	 */
	public static class FeatureGroup {
		private	String				m_name = null;
		private	String				m_type = null;
		private	ArrayList<String>	m_features = new ArrayList<String>();
		
		FeatureGroup(String name, String type) {
			m_name = name;
			m_type = type;
		}
		public void addFeature(String f) { m_features.add(f); }
		public ArrayList<String> getFeatures() { return m_features; }
		public String getName() { return m_name; }
		public String getType() { return m_type; }
		public String toString() {
			String s = "";
			for (String g : m_features) {
				s += g + ",";
			}
			return (s.replaceAll(",$",""));
		}
	}


	
}
