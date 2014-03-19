/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal.hardware.callbacks;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
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
public class FritzahaQueryscriptUpdateNumberCallback extends FritzahaReauthCallback {
	static final Logger logger = LoggerFactory.getLogger(FritzahaQueryscriptUpdateNumberCallback.class);
	/**
	 * Item to update
	 */
	private String itemName;
	/**
	 * Meter type to look for
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
	public FritzahaQueryscriptUpdateNumberCallback(String path, String args, MeterType type,
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
			String valueType;
			if (type == MeterType.VOLTAGE) {
				valueType = "MM_Value_Volt";
			} else if (type == MeterType.CURRENT) {
				valueType = "MM_Value_Amp";
			} else if (type == MeterType.POWER) {
				valueType = "MM_Value_Power";
			} else
				return;
			ObjectMapper jsonReader = new ObjectMapper();
			Map<String, String> deviceData;
			try {
				deviceData = jsonReader.readValue(response, Map.class);
			} catch (JsonParseException e) {
				logger.error("Error parsing JSON:\n" + response);
				return;
			} catch (JsonMappingException e) {
				logger.error("Error mapping JSON:\n" + response);
				return;
			} catch (IOException e) {
				logger.error("An I/O error occured while decoding JSON:\n" + response);
				return;
			}
			if (deviceData.containsKey(valueType)) {
				BigDecimal meterValue = new BigDecimal(deviceData.get(valueType));
				BigDecimal meterValueScaled;
				switch (type) {
				case VOLTAGE:
					meterValueScaled = meterValue.scaleByPowerOfTen(-3);
					break;
				case CURRENT:
					meterValueScaled = meterValue.scaleByPowerOfTen(-4);
					break;
				case POWER:
					meterValueScaled = meterValue.scaleByPowerOfTen(-2);
					break;
				default:
					meterValueScaled = meterValue;
				}
				webIface.postUpdate(itemName, new DecimalType(meterValueScaled));
			} else
				logger.error("Response did not contain " + valueType);
		}
	}

}
