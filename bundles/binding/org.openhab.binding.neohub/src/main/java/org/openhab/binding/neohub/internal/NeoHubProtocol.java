/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.neohub.internal;

import org.apache.commons.lang.StringUtils;

/**
 * Implements a subset of the neoHub JSON API version 1.7.
 * 
 * @author Sebastian Prehn
 * @since 1.5.0
 */
public class NeoHubProtocol {

	private final NeoHubConnector neoHubConnector;

	NeoHubProtocol(final NeoHubConnector connector) {
		this.neoHubConnector = connector;
	}

	/**
	 * Retreives all available info on all neo stat devices connected to neohub.
	 * 
	 * @return info response
	 */
	public InfoResponse info() {
		return neoHubConnector.sendMessage("{\"INFO\":0}",
				new ResponseHandler<InfoResponse>() {
					@Override
					public InfoResponse onResponse(String response) {
						return InfoResponse.createInfoResponse(response);
					}
				});
	}

	/**
	 * Retrieve highest and lowest temperatures in last week/month/year, and
	 * preheat stats.
	 */
	public void statistics() {
		neoHubConnector.sendMessage("{\"STATISTICS\":0}", null);
	}

	/**
	 * Away mode shuts down the heating, enables frost protection and turns time
	 * clocks off.
	 * 
	 * @param onOrOff
	 *            <code>true</code> activates away mode,
	 *            <code>false</false> deactivates away mode again.
	 * @param deviceNames
	 *            neo stat device names
	 */
	public void setAway(final boolean onOrOff, final String... deviceNames) {
		neoHubConnector.sendMessage(
				String.format("{\"AWAY_%s\":[\"%s\"]}", onOrOff ? "ON" : "OFF",
						StringUtils.join(deviceNames, "\",\"")), null);
	}

	/**
	 * Standby mode shuts down the heating, enables frost protection.
	 * 
	 * @param onOrOff
	 *            <code>true</code> activates standby mode,
	 *            <code>false</false> deactivates standby mode again.
	 * @param deviceNames
	 *            neo stat device names
	 */
	public void setStandby(final boolean onOrOff, final String... deviceNames) {
		neoHubConnector.sendMessage(
				String.format("{\"FROST_%s\":[\"%s\"]}",
						onOrOff ? "ON" : "OFF",
						StringUtils.join(deviceNames, "\",\"")), null);
	}
	/*
	 * other commands:
	 * BOOST_ON("{\"BOOST_ON\":[{\"hours\":0,\"minutes\":10},<device(s)>]}"),
	 * BOOST_OFF("{\"BOOST_OFF\":[{\"hours\":0,\"minutes\":10},<device(s)>]}"),
	 * why provide duration? SET_TEMP // temp till next switching time HOLD set
	 * temp for a defined period of time READ_COMFORT_LEVELS SET_COMFORT_LEVELS
	 * VIEW_ROC GET_TEMPLOG // 7day history
	 */

}
