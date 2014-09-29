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
import org.openhab.binding.fritzaha.internal.hardware.callbacks.FritzahaReauthCallback;
import org.openhab.binding.fritzaha.internal.hardware.callbacks.FritzahaWebserviceUpdateNumberCallback;
import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaOutletMeter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Meter in outlet addressed via webservice
 * 
 * @author Christian Brauers
 * @since 1.3.0
 */
public class FritzahaWebserviceMeter implements FritzahaOutletMeter {
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

	static final Logger logger = LoggerFactory.getLogger(FritzahaWebserviceMeter.class);

	/**
	 * {@inheritDoc}
	 */
	public void updateMeterValue(String itemName, FritzahaWebInterface webIface) {
		String valueType;
		if (type == MeterType.POWER) {
			valueType = "power";
		} else if (type == MeterType.ENERGY) {
			valueType = "energy";
		} else
			return;
		logger.debug("Getting " + valueType + " value for AIN " + id);
		String path = "webservices/homeautoswitch.lua";
		String args = "switchcmd=getswitch" + valueType + "&ain=" + id;
		webIface.asyncGet(path, args, new FritzahaWebserviceUpdateNumberCallback(path, args, type, webIface,
				FritzahaReauthCallback.Method.GET, 1, itemName));
	}

	/**
	 * {@inheritDoc}
	 */
	public FritzahaWebserviceMeter(String host, String id, MeterType type) {
		this.host = host;
		this.id = id;
		this.type = type;
	}
}
