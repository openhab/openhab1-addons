/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal.messages;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.dsmr.internal.DSMRMeter;
import org.openhab.binding.dsmr.internal.DSMRMeterType;
import org.openhab.binding.dsmr.internal.DSMRVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for constructing OBIS messages from Strings
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public class OBISMsgFactory {
	/* logger */
	private static final Logger logger = LoggerFactory
			.getLogger(OBISMsgFactory.class);

	/* Regular expression for OBIS strings */
	private static final String OBIS_REGEX = "^(\\d{1,3}-\\d{1,3}:\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})(\\(.*\\))+$";
	private static final String OBIS_VALUE_REGEX = "\\(([^\\(\\)]*)\\)";
	private static final String OBIS_CONFIGURABLE_CHANNEL_PATTERN = "(\\d+)-(-1):(\\d+)\\.(\\d+)\\.(\\d+)";

	/* Pattern instances */
	private final Pattern obisPattern;
	private final Pattern obisValuePattern;
	private final Pattern obisChannelPattern;

	/* internal lookup cache */
	private final HashMap<String, OBISMsgType> obisLookupTable;

	/**
	 * Creates a new OBISMsgFactory for the specified DSMRVersion
	 * 
	 * @param version
	 *            {@link DMSRVersion} to use for handling input data
	 * @param dsmrMeters
	 *            available DSMR meters (see {@link DSMRMeter}) in the binding
	 */
	public OBISMsgFactory(DSMRVersion version, List<DSMRMeter> dsmrMeters) {
		obisPattern = Pattern.compile(OBIS_REGEX);
		obisValuePattern = Pattern.compile(OBIS_VALUE_REGEX);
		obisChannelPattern = Pattern.compile(OBIS_CONFIGURABLE_CHANNEL_PATTERN);

		/*
		 * Fill a lookup table with OBIS messages belonging to the specified
		 * DSMR version and channel - MeterType mapping
		 */
		obisLookupTable = new HashMap<String, OBISMsgType>();

		// Create a convenience lookup table for a channel based on meter type
		Map<DSMRMeterType, Integer> meterChannelMapping = new HashMap<DSMRMeterType, Integer>();
		for (DSMRMeter meter : dsmrMeters) {
			meterChannelMapping.put(meter.getMeterType(), meter.getChannel());
		}
		fillLookupTable(version, meterChannelMapping);
	}

	/**
	 * Return OBISMessage from specified string or null if string couldn't be
	 * parsed correctly or no corresponding OBISMessage was found
	 * 
	 * @param obisStr
	 *            a single raw OBIS message string received from the DSMR meter
	 * @return OBISMessage or null if parsing failed
	 */
	public OBISMessage getMessage(String obisStr) {
		OBISMessage msg = null;

		if (obisStr != null) {
			Matcher m = obisPattern.matcher(obisStr);

			if (m.matches()) {
				logger.debug("Received valid OBIS String:" + obisStr);

				List<String> cosemStringValues = new ArrayList<String>();

				// Get identifier and all the values as a single String
				OBISMsgType msgType = getOBISMsgType(m.group(1));

				if (msgType != OBISMsgType.UNKNOWN) {
					// Get the individual COSEM String values
					String allCosemStringValues = m.group(2);
					Matcher valueMatcher = obisValuePattern
							.matcher(allCosemStringValues);

					while (valueMatcher.find()) {
						cosemStringValues.add(valueMatcher.group(1));
					}

					logger.debug("OBIS message type:" + msgType + ", values:"
							+ cosemStringValues);

					msg = new OBISMessage(msgType);

					try {
						msg.parseCosemValues(cosemStringValues);
					} catch (ParseException pe) {
						logger.error("Failed to parse " + obisStr, pe);
					}
				} else {
					logger.warn("Received OBIS unknown message:" + obisStr);
				}
			}
		}

		logger.debug("Converted to:" + msg);

		return msg;
	}

	/**
	 * Returns the OBIS message type (See {@link OBISMsgType}) for the specified
	 * OBIS reduced identifier
	 * 
	 * @param obisId
	 *            the OBIS reduced identifier
	 * @return the {@link OBISMsgType} or UNKNOWN if the OBIS reduced identifier
	 *         is unknown
	 */
	private OBISMsgType getOBISMsgType(String obisId) {
		if (obisLookupTable.containsKey(obisId)) {
			return obisLookupTable.get(obisId);
		} else {
			return OBISMsgType.UNKNOWN;
		}
	}

	/**
	 * This method fills a lookup table in which the applicable OBIS message
	 * types are stored based on the {@link DSMRVersion} and mapping channel -
	 * {@link DSMRMeterType}
	 * <p>
	 * DSMR messages can be interpreted ambiguous if the version or mapping is
	 * not known. The lookup table makes sure that messages are parsed without
	 * ambiguity.
	 * 
	 * @param version
	 *            applicable DSMR version
	 * @param mapping
	 *            DSMRMeterType - channel mapping
	 */
	private void fillLookupTable(DSMRVersion version,
			Map<DSMRMeterType, Integer> mapping) {
		for (OBISMsgType t : OBISMsgType.values()) {
			if (t.applicableVersions.contains(version)) {
				Matcher m = obisChannelPattern.matcher(t.obisId);
				if (m.matches()) {
					DSMRMeterType meterType = t.meterType;
					if (mapping.containsKey(meterType)) {
						/*
						 * OBIS-identifier contains a variable channel Check if
						 * the configuration contains the mapping for this meter
						 * type and then make the OBIS-identifier specific
						 */

						Integer channel = mapping.get(t.meterType);
						logger.debug("Change OBIS-identifier " + t.obisId
								+ " for meter " + t.meterType + " on channel "
								+ channel);

						String obisSpecificIdentifier = m.replaceFirst("$1-"
								+ channel + ":$3.$4.$5");
						obisLookupTable.put(obisSpecificIdentifier, t);
					} else {
						logger.debug("Mapping does not contain a channel for "
								+ meterType);
					}
				} else {
					obisLookupTable.put(t.obisId, t);
				}
			}
		}
	}
}
