/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
