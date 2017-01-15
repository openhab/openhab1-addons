/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal.messages;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openhab.binding.dsmr.internal.cosem.CosemDate;
import org.openhab.binding.dsmr.internal.cosem.CosemInteger;
import org.openhab.binding.dsmr.internal.cosem.CosemValue;
import org.openhab.binding.dsmr.internal.cosem.CosemValueDescriptor;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for OBIS Message implementation
 *
 * @author M. Volaart
 * @since 1.7.0
 */
public class OBISMessage {
    // logger
    private static final Logger logger = LoggerFactory.getLogger(OBISMessage.class);

    // Identifier of the first power failure date element
    public static final int FIRST_POWER_FAILURE_DATE = 2;
    // Identifier of the first power failure duration element
    public static final int FIRST_POWER_FAILURE_DURATION = 3;

    // OBIS Message Type
    private final OBISMsgType msgType;

    // List of COSEM value in this message
    private List<CosemValue<? extends State>> cosemValues;

    /**
     * Construct a new OBISMessage with the specified OBIS Message Type
     *
     * @param msgType
     *            {@link OBISMsgType}
     */
    public OBISMessage(OBISMsgType msgType) {
        this.msgType = msgType;

        cosemValues = new ArrayList<CosemValue<? extends State>>();
    }

    /**
     * Return the {@link OBISMsgType} for this OBIS message
     *
     * @return the {@link OBISMsgType} for this OBIS message
     */
    public OBISMsgType getMsgType() {
        return msgType;
    }

    /**
     * Returns string representation of this OBISMessage
     *
     * @return string representation of this OBISMessage
     */
    @Override
    public String toString() {
        return "OBIS Message(type:" + msgType.toString() + ", description:" + msgType.description + ", openHABValues:"
                + cosemValues + ")";
    }

    /**
     * Returns the openHAB values that are part of this OBIS message
     *
     * @return List of {@link CosemValue} that are part of this OBIS message
     */
    public List<? extends CosemValue<? extends Object>> getOpenHABValues() {
        return cosemValues;
    }

    /**
     * Parses the List of COSEM String value to internal openHAB values.
     * <p>
     * When the parser has problems it throws an {@link ParseException}. The
     * already parsed values will still be available. It is up to the caller how
     * to handle a partially parsed message.
     *
     * @param cosemStringValues
     *            the List of COSEM String values
     * @throws ParseException
     *             if parsing fails
     */
    public void parseCosemValues(List<String> cosemStringValues) throws ParseException {
        logger.debug("Received items: {}, Needed items: {}", cosemStringValues.size(),
                msgType.cosemValueDescriptors.size());

        /*
         * It is not necessarily a problem if 'Needed items' > 'Received items'.
         * Since some items have a dynamic number of values (e.g. Power Failure
         * Log).
         *
         * Since the minority of the messages has such features, differences
         * between received and needed could indicate problems
         */
        if (cosemStringValues.size() <= msgType.cosemValueDescriptors.size()) {
            for (int i = 0; i < cosemStringValues.size(); i++) {

                CosemValue<? extends State> cosemValue = getCosemValue(msgType.cosemValueDescriptors.get(i));
                if (cosemValue != null) {
                    cosemValue.setValue(cosemStringValues.get(i));
                    cosemValues.add(cosemValue);
                } else {
                    logger.error("Failed to parse: {} for OBISMsgType: {}", cosemStringValues.get(i), msgType);
                }
            }
        } else {
            throw new ParseException("Received items:" + cosemStringValues.size() + ", Needed items:"
                    + msgType.cosemValueDescriptors.size(), 0);
        }

        /*
         * Here we do a post processing on the values
         */
        switch (msgType) {
            case EMETER_POWER_FAILURE_LOG:
                postProcessKaifaE0003();
                break;
            default:
                break;
        }
    }

    /**
     * Creates an empty CosemValue object
     *
     * @param cosemValueDescriptor
     *            the CosemValueDescriptor object that describes the CosemValue
     * @return the instantiated CosemValue based on the specified
     *         CosemValueDescriptor
     */
    private CosemValue<? extends State> getCosemValue(CosemValueDescriptor cosemValueDescriptor) {
        Class<? extends CosemValue<? extends State>> cosemValueClass = cosemValueDescriptor.getCosemValueClass();

        String unit = cosemValueDescriptor.getUnit();
        String dsmrItemId = cosemValueDescriptor.getDsmrItemId();

        try {
            Constructor<? extends CosemValue<? extends State>> c = cosemValueClass.getConstructor(String.class,
                    String.class);

            return c.newInstance(unit, dsmrItemId);
        } catch (ReflectiveOperationException roe) {
            logger.error("Failed to create {} message", msgType.obisId, roe);
        }
        return null;
    }

    /**
     * On the Kaifa E0003 we have seen power failure entries that occurred at
     * 1-1-1970 and have a 2^32 - 1 duration
     *
     * This method filters the values belonging to this entry
     */
    private void postProcessKaifaE0003() {
        logger.debug("postProcessKaifaE0003");

        /*
         * The list of cosemValues for this OBIS Message is:
         * - [0] Number of entries in the list
         * - [1] Cosem Identifier
         * - [2] power failure date entry 1 [Optional]
         * - [3] power failure duration entry 1 [Optional]
         * - [entry n * 2] power failure date entry n
         * - [entry n * 2 + 1] power failure duration entry n
         */

        // First check of there is at least one entry of a power failure present
        // (i.e. date at idx 2 and duration at idx 3)
        if (cosemValues.size() > FIRST_POWER_FAILURE_DURATION) {
            CosemDate powerFailureDate = (CosemDate) cosemValues.get(FIRST_POWER_FAILURE_DATE);
            CosemInteger powerFailureDuration = (CosemInteger) cosemValues.get(FIRST_POWER_FAILURE_DURATION);

            Calendar epoch = Calendar.getInstance();
            epoch.setTime(new Date(0));

            // Check if the first entry it as epoc and has a 2^32-1 value
            // If so, filter this value, since it has no added value
            if (powerFailureDate.getValue().getCalendar().before(epoch)
                    && powerFailureDuration.getValue().intValue() == Integer.MAX_VALUE) {
                logger.debug("Filter invalid power failure entry");
                cosemValues.remove(FIRST_POWER_FAILURE_DURATION);
                cosemValues.remove(FIRST_POWER_FAILURE_DATE);
            }
        }
    }
}
