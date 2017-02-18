/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal.messages;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openhab.binding.dsmr.internal.DSMRMeter;
import org.openhab.binding.dsmr.internal.DSMRMeterType;
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
    private static final Logger logger = LoggerFactory.getLogger(OBISMsgFactory.class);

    /* internal lookup cache */
    private final HashMap<OBISIdentifier, List<OBISMsgType>> obisLookupTable;

    /**
     * Creates a new OBISMsgFactory
     *
     * @param dsmrMeters
     *            available DSMR meters (see {@link DSMRMeter}) in the binding
     */
    public OBISMsgFactory(List<DSMRMeter> dsmrMeters) {
        /*
         * Fill a lookup table with OBIS message types based on the specified
         * channel - MeterType mapping
         */
        obisLookupTable = new HashMap<OBISIdentifier, List<OBISMsgType>>();

        // Create a convenience lookup table for a channel based on meter type
        Map<DSMRMeterType, Integer> meterChannelMapping = new HashMap<DSMRMeterType, Integer>();
        for (DSMRMeter meter : dsmrMeters) {
            meterChannelMapping.put(meter.getMeterType(), meter.getChannel());
        }
        fillLookupTable(meterChannelMapping);
    }

    /**
     * Return OBISMessage from specified string or null if string couldn't be
     * parsed correctly or no corresponding OBISMessage was found
     *
     * @param obisIdString
     *            String containing the OBIS message identifier
     * @param cosemStringValues
     *            LinkedList of String containing Cosem values
     * @return OBISMessage or null if parsing failed
     */
    public OBISMessage getMessage(String obisIdString, LinkedList<String> cosemStringValues) {
        OBISIdentifier obisId = null;
        OBISIdentifier reducedObisId = null;

        try {
            obisId = new OBISIdentifier(obisIdString);
            reducedObisId = obisId.getReducedOBISIdentifier();
        } catch (ParseException pe) {
            logger.error("Received invalid OBIS identifier: {}", obisIdString);

            return null;
        }

        logger.debug("Received obisIdString {}, obisId: {}, values: {}", obisIdString, obisId, cosemStringValues);

        if (obisLookupTable.containsKey(reducedObisId)) {
            List<OBISMsgType> compatibleMsgTypes = obisLookupTable.get(reducedObisId);

            OBISMessage msg = null;

            logger.debug("Found {} compatible message type(s)", compatibleMsgTypes.size());
            for (OBISMsgType msgType : compatibleMsgTypes) {
                msg = new OBISMessage(msgType);

                try {
                    logger.debug("Parse values for OBIS Message type: {}", msgType);

                    msg.parseCosemValues(cosemStringValues);

                    return msg;
                } catch (ParseException pe) {
                    logger.debug("Failed to parse OBIS identifier {}, values: {} for type {} ", obisId,
                            cosemStringValues, msgType, pe);
                }
            }
            logger.error("Failed to parse OBIS identifier {}, values: {}", obisId, cosemStringValues);
        } else {
            logger.warn("Received OBIS unknown message: {}", obisId);
        }
        return null;
    }

    /**
     * This method fills a lookup table with the OBIS message types based on the
     * mapping channel - {@link DSMRMeterType}
     * <p>
     *
     * @param mapping
     *            DSMRMeterType - channel mapping
     */
    private void fillLookupTable(Map<DSMRMeterType, Integer> mapping) {
        for (OBISMsgType t : OBISMsgType.values()) {
            OBISIdentifier obisId = t.obisId;

            if (obisId.getGroupB() == null) {
                DSMRMeterType meterType = t.meterType;
                if (mapping.containsKey(meterType)) {
                    /*
                     * OBIS-identifier contains a variable channel Check if the
                     * configuration contains the mapping for this meter type
                     * and then make the OBIS-identifier specific
                     */

                    Integer channel = mapping.get(t.meterType);
                    logger.debug("Change OBIS-identifier {} for meter {} on channel {}", t.obisId, t.meterType,
                            channel);

                    obisId = new OBISIdentifier(obisId.getGroupA(), channel, obisId.getGroupC(), obisId.getGroupD(),
                            obisId.getGroupE(), obisId.getGroupF());
                } else {
                    logger.debug("Mapping does not contain a channel for {}", meterType);

                    obisId = null;
                }
            }
            if (obisId != null) {
                if (!obisLookupTable.containsKey(obisId)) {
                    obisLookupTable.put(obisId, new LinkedList<OBISMsgType>());
                }
                obisLookupTable.get(obisId).add(t);
            }
        }
    }
}
