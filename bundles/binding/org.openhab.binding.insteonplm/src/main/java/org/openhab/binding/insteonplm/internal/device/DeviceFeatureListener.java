/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
 package org.openhab.binding.insteonplm.internal.device;

import java.util.HashMap;
import org.openhab.core.types.State;
import org.openhab.core.events.EventPublisher;

/**
* A DeviceFeatureListener essentially represents an OpenHAB item that
* listens to a particular feature of an Insteon device
* @author Daniel Pfrommer
* @author Bernd Pfrommer
* @since 1.6.0
*/

public class DeviceFeatureListener {
	private String			m_itemName = null;
	private EventPublisher 	m_eventPublisher = null;
	private HashMap<String, String>	m_parameters = new HashMap<String, String>();
	/**
	 * Constructor
	 * @param item name of the item that is listening 
	 * @param eventPublisher the publisher to use for publishing on the openhab bus
	 */
	public DeviceFeatureListener(String item, EventPublisher eventPublisher) {
		m_itemName = item;
		m_eventPublisher = eventPublisher;
	}
	/**
	 * Gets item name
	 * @return item name
	 */
	public String getItemName() {
		return m_itemName;
	}

	/**
	 * Test is given parameter is configured
	 * @param key parameter to test for
	 * @return
	 */
	public boolean hasParameter(String key) {
		return m_parameters != null && m_parameters.get(key) != null;
	}
	/**
	 * Set parameters for this feature listener
	 * @param p the parameters to set
	 */
	public void setParameters(HashMap<String,String> p) {
		m_parameters = p;
	}
	/**
	 * Gets integer parameter or -1 if not found
	 * @param key name of parameter to get
	 * @return value of parameter or -1 if not found
	 */
	public int getIntParameter(String key) {
		return (getIntParameter(key, -1));
	}
	/**
	 * Gets integer parameter or default value
	 * @param key the parameter key to look for
	 * @param def the default if key is not found
	 * @return the value (or default if key is not found)
	 */
	public int getIntParameter(String key, int def) {
		if (m_parameters == null) return def;
		String s = m_parameters.get(key);
		if (s == null) return def;
		return (Integer.parseInt(s));
	}
	/**
	 * Publishes a state change on the openhab bus
	 * @param state the new state to publish on the openhab bus
	 */
	public void stateChanged(State state) {
		m_eventPublisher.postUpdate(m_itemName, state);
	}
}
