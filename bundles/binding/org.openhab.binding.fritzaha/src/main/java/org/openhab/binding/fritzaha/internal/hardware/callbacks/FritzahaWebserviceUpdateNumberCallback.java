/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal.hardware.callbacks;

import java.math.BigDecimal;

import org.openhab.binding.fritzaha.internal.hardware.FritzahaWebInterface;
import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaOutletMeter.MeterType;
import org.openhab.core.library.types.DecimalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Callback implementation for updating numbers Supports reauthorization
 * 
 * @author Christian Brauers
 * @since 1.3.0
 */
public class FritzahaWebserviceUpdateNumberCallback extends FritzahaReauthCallback {
	static final Logger logger = LoggerFactory.getLogger(FritzahaWebserviceUpdateNumberCallback.class);
	/**
	 * Item to update
	 */
	private String itemName;
	/**
	 * Meter type to update
	 */
	private MeterType type;

	/**
	 * Constructor for retriable authentication and state updating
	 * 
	 * @param path
	 *            Path to HTTP interface
	 * @param args
	 *            Arguments to use
	 * @param webIface
	 *            Web interface to use
	 * @param httpMethod
	 *            Method used
	 * @param retries
	 *            Number of retries
	 * @param itemName
	 *            Name of item to update
	 */
	public FritzahaWebserviceUpdateNumberCallback(String path, String args, MeterType type,
			FritzahaWebInterface webIface, Method httpMethod, int retries, String itemName) {
		super(path, args, webIface, httpMethod, retries);
		this.itemName = itemName;
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute(int status, String response) {
		super.execute(status, response);
		if (validRequest) {
			logger.debug("Received State response " + response + " for item " + itemName);
			BigDecimal meterValue = new BigDecimal(response.trim());
			BigDecimal meterValueScaled;
			switch (type) {
			case POWER:
				meterValueScaled = meterValue.scaleByPowerOfTen(-3);
				break;
			case ENERGY:
			default:
				meterValueScaled = meterValue;
			}
			webIface.postUpdate(itemName, new DecimalType(meterValueScaled));
		}
	}

}
