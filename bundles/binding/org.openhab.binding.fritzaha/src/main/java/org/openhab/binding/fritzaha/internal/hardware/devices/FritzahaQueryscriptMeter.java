/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal.hardware.devices;

import org.openhab.binding.fritzaha.internal.hardware.FritzahaWebInterface;
import org.openhab.binding.fritzaha.internal.hardware.callbacks.FritzahaQueryscriptUpdateNumberCallback;
import org.openhab.binding.fritzaha.internal.hardware.callbacks.FritzahaReauthCallback;
import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaOutletMeter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Meter in outlet addressed via query script
 * 
 * @author Christian Brauers
 * @since 1.3.0
 */
public class FritzahaQueryscriptMeter implements FritzahaOutletMeter {
	/**
	 * Host ID
	 */
	String host;
	/**
	 * Device ID
	 */
	String id;
	/**
	 * Meter type
	 */
	MeterType type;

	/**
	 * {@inheritDoc}
	 */
	public String getHost() {
		return host;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	public MeterType getMeterType() {
		return type;
	}

	static final Logger logger = LoggerFactory.getLogger(FritzahaQueryscriptMeter.class);

	/**
	 * {@inheritDoc}
	 */
	public void updateMeterValue(String itemName, FritzahaWebInterface webIface) {
		if (type == MeterType.ENERGY) {
			return;
		}
		logger.debug("Getting meter data for Device ID " + id);
		String path = "net/home_auto_query.lua";
		String args = "xhr=1&command=MultiMeterState&id=" + id;
		webIface.asyncGet(path, args, new FritzahaQueryscriptUpdateNumberCallback(path, args, type, webIface,
				FritzahaReauthCallback.Method.GET, 1, itemName));
	}

	/**
	 * {@inheritDoc}
	 */
	public FritzahaQueryscriptMeter(String host, String id, MeterType type) {
		this.host = host;
		this.id = id;
		this.type = type;
	}
}
