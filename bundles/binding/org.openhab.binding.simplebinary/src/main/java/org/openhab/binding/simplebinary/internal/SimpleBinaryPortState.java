/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import java.util.Calendar;
import java.util.Map;

import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.InfoType;
import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.SimpleBinaryInfoBindingConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;

/**
 * Status of communication port
 * 
 * @author Vita Tucek
 * @since 1.8.0
 */
public class SimpleBinaryPortState {

	public enum PortStates {
		UNKNOWN, LISTENING, CLOSED, NOT_EXIST, NOT_AVAILABLE
	}

	private PortStates state = PortStates.UNKNOWN;
	private PortStates prevState = PortStates.UNKNOWN;
	private Calendar changedSince;
	private EventPublisher eventPublisher;
	private String itemState = null;
	private String itemPreviousState = null;
	private String itemStateChangeTime = null;

	/**
	 * Return port status
	 * 
	 * @return
	 */
	public PortStates getState() {
		return state;
	}

	/**
	 * Return previous status
	 * 
	 * @return
	 */
	public PortStates getPreviusState() {
		return prevState;
	}

	/**
	 * Return date when last change occurred
	 * 
	 * @return
	 */
	public Calendar getChangeDate() {
		return changedSince;
	}

	/**
	 * Set port state
	 * 
	 * @param state
	 */
	public void setState(PortStates state) {

		// set state only if previous is different
		if (this.state != state) {
			this.prevState = this.state;
			this.state = state;
			this.changedSince = Calendar.getInstance();

			// update event bus
			if (itemState != null)
				eventPublisher.postUpdate(itemState, new DecimalType(this.state.ordinal()));
			if (itemPreviousState != null)
				eventPublisher.postUpdate(itemPreviousState, new DecimalType(this.prevState.ordinal()));
			if (itemStateChangeTime != null)
				eventPublisher.postUpdate(itemStateChangeTime, new DateTimeType(this.changedSince));
		}
	}

	/**
	 * Set binding data for internal use and port item state init
	 * 
	 * @param eventPublisher
	 * @param itemsInfoConfig
	 * @param deviceName
	 */
	public void setBindingData(EventPublisher eventPublisher, Map<String, SimpleBinaryInfoBindingConfig> itemsInfoConfig, String deviceName) {
		this.eventPublisher = eventPublisher;

		for (Map.Entry<String, SimpleBinaryInfoBindingConfig> item : itemsInfoConfig.entrySet()) {

			if (item.getValue().device.equals(deviceName)) {

				// check correct address
				if (item.getValue().busAddress == -1) {
					// find right info type
					if (item.getValue().infoType == InfoType.STATE)
						itemState = item.getValue().item.getName();
					else if (item.getValue().infoType == InfoType.PREVIOUS_STATE)
						itemPreviousState = item.getValue().item.getName();
					else if (item.getValue().infoType == InfoType.STATE_CHANGE_TIME)
						itemStateChangeTime = item.getValue().item.getName();
				}
			}
		}
	}
}
