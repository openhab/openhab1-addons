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

import org.openhab.binding.insteonplm.InsteonPLMActiveBinding;
import org.openhab.core.types.State;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* A DeviceFeatureListener essentially represents an OpenHAB item that
* listens to a particular feature of an Insteon device
* @author Daniel Pfrommer
* @author Bernd Pfrommer
* @since 1.6.0
*/

public class DeviceFeatureListener {
	private static final Logger logger = LoggerFactory.getLogger(DeviceFeatureListener.class);
	public enum StateChangeType {
		ALWAYS,
		CHANGED
	};
	private String			m_itemName = null;
	private EventPublisher 	m_eventPublisher = null;
	private HashMap<String, String>	m_parameters = new HashMap<String, String>();
	private HashMap<Class<?>, State> m_state = new HashMap<Class<?>, State>();
	private ArrayList<InsteonAddress> m_relatedDevices = new ArrayList<InsteonAddress>();
	private InsteonPLMActiveBinding	m_binding = null;
	private final static int TIME_DELAY_POLL_RELATED_MSEC = 5000;
	/**
	 * Constructor
	 * @param item name of the item that is listening 
	 * @param eventPublisher the publisher to use for publishing on the openhab bus
	 */
	public DeviceFeatureListener(InsteonPLMActiveBinding binding, String item, EventPublisher eventPublisher) {
		m_binding = binding;
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
	 * Test if string parameter is present and has a given value
	 * 
	 * @param key		key to match
	 * @param value		value to match
	 * @return			true if key exists and value matches
	 */
	private boolean parameterHasValue(String key, String value) {
		if (m_parameters == null) return false;
		String v = m_parameters.get(key);
		return (v != null &&v.equals(value));
	}

	/**
	 * Set parameters for this feature listener
	 * @param p the parameters to set
	 */
	public void setParameters(HashMap<String,String> p) {
		m_parameters = p;
		updateRelatedDevices();
	}
	
	/**
	 * Publishes a state change on the openhab bus
	 * @param state the new state to publish on the openhab bus
	 * @param changeType whether to always publish or not
	 */
	public void stateChanged(State state, StateChangeType changeType) {
		State oldState = m_state.get(state.getClass());
		if (oldState == null) {
			logger.trace("new state: {}:{}", state.getClass().getSimpleName(), state);
			// state has changed, must publish
			publishState(state);
		} else {
			logger.trace("old state: {}:{}=?{}", state.getClass().getSimpleName(), oldState, state);
			// only publish if state has changed or it is requested explicitly
			if (changeType == StateChangeType.ALWAYS || !oldState.equals(state)) {
				publishState(state);
			}
		}
		m_state.put(state.getClass(), state);
	}
	/**
	 * Call this function to inform about a state change for a given
	 * parameter key and value. If dataKey and dataValue don't match,
	 * the state change will be ignored.
	 * @param state the new state to which the feature has changed
	 * @param changeType how to process the state change (always, or only when changed)
	 * @param dataKey the data key on which to filter
	 * @param dataValue the value that the data key must match for the state to be published
	 */
	public void stateChanged(State state, StateChangeType changeType,
								String dataKey, String dataValue) {
		if (parameterHasValue(dataKey, dataValue)) {
			stateChanged(state, changeType);
		}
	}
    /**
     * Publish the state. In the case of PercentType, if the value is
     * 0, send a OnOffType.OFF and if the value is 100, send a OnOffType.ON.
     * That way an OpenHAB Switch will work properly with a Insteon dimmer,
     * as long it is used like a switch (On/Off). An openHAB DimmerItem will
     * internally convert the ON back to 100% and OFF back to 0, so there is
     * no need to send both 0/OFF and 100/ON.
     * 
     * @param state the new state of the feature
     */
	private void publishState(State state) {
		State publishState = state;
		if (state instanceof PercentType) {
			if (state.equals(PercentType.ZERO)) {
				publishState = OnOffType.OFF;
			} else if (state.equals(PercentType.HUNDRED)) {
				publishState = OnOffType.ON;
			}
		}
		pollRelatedDevices();
		m_eventPublisher.postUpdate(m_itemName, publishState);
	}
	/**
	 * Extracts related devices from the parameter list and
	 * stores them for faster access later.
	 */
	
	private void updateRelatedDevices() {
		String d = m_parameters.get("related");
		if (d == null) return;
		String [] devs = d.split("\\+");
		for (String dev : devs) {
			InsteonAddress a = InsteonAddress.s_parseAddress(dev);
			if (a == null) {
				logger.error("invalid insteon address: {}", a);
				continue;
			}
			m_relatedDevices.add(a);
		}
	}
	/**
	 * polls all devices that are related to this item
	 * by the "related" keyword
	 */
	private void pollRelatedDevices() {
		for (InsteonAddress a : m_relatedDevices) {
			logger.debug("polling related device {} in {} ms",
					a, TIME_DELAY_POLL_RELATED_MSEC);
			InsteonDevice d = m_binding.getDevice(a);
			if (d != null) {
				d.doPoll(TIME_DELAY_POLL_RELATED_MSEC);
			} else {
				logger.warn("device {} related to item {} is not configured!", a, m_itemName);
			}
		}
	}
}
