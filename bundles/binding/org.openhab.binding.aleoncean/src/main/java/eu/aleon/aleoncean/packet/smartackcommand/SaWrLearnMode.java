/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.packet.smartackcommand;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.aleon.aleoncean.packet.ResponsePacket;
import eu.aleon.aleoncean.packet.ResponseReturnCode;
import eu.aleon.aleoncean.packet.SmartAckCode;
import eu.aleon.aleoncean.packet.SmartAckCommandPacket;
import eu.aleon.aleoncean.packet.response.NoDataResponse;
import eu.aleon.aleoncean.packet.response.Response;
import eu.aleon.aleoncean.packet.response.UnknownResponseException;
import eu.aleon.aleoncean.util.CalculationUtil;
import eu.aleon.aleoncean.values.SmartAckLearnModeExtended;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class SaWrLearnMode extends SmartAckCommandPacket {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaWrLearnMode.class);

    public static final long TIMEOUT_MIN = 0;
    public static final long TIMEOUT_MAX = 0xFFFFFFFFL;
    public static final long TIMEOUT_DFL_IF_ZERO = 60000;

    boolean enabled;

    SmartAckLearnModeExtended extended;

    long timeout;

    public SaWrLearnMode() {
        super(SmartAckCode.SA_WR_LEARNMODE);
    }

    @Override
    public void setSmartAckData(final byte[] smartAckData) {
        final ByteBuffer bb = ByteBuffer.wrap(smartAckData);
        bb.order(ByteOrder.BIG_ENDIAN);

        setEnabled(bb.get() != 0);

        final byte rawEnable = bb.get();
        setEnabled(rawEnable != 0);

        final byte rawExtended = bb.get();
        switch (rawExtended) {
            case 0:
                setExtended(SmartAckLearnModeExtended.SIMPLE_LEARNMODE);
                break;
            case 1:
                setExtended(SmartAckLearnModeExtended.ADVANCE_LEARNMODE);
                break;
            case 2:
                setExtended(SmartAckLearnModeExtended.ADVANCE_LEARNMODE_SELECT_REPEATER);
                break;
            default:
                LOGGER.warn(String.format("The received value (0x%02X) for the Smart Ack learn mode extended field is invalid.", rawExtended));
                break;
        }

        final long rawTimeout = ((long) bb.getInt()) & 0xFFFFFFFFL;
        setTimeout(rawTimeout);
    }

    @Override
    public byte[] getSmartAckData() {
        final int rawSmartAckDataLength = 1 /* enable */
                                          + 1 /* extended */
                                          + 4 /* timeout */;

        final byte[] rawSmartAckData = new byte[rawSmartAckDataLength];
        final ByteBuffer bb = ByteBuffer.wrap(rawSmartAckData);
        bb.order(ByteOrder.BIG_ENDIAN);

        final byte rawEnable = isEnabled() ? (byte) 1 : (byte) 0;
        bb.put(rawEnable);

        byte rawExtended;
        switch (getExtended()) {
            case SIMPLE_LEARNMODE:
                rawExtended = 0;
                break;
            case ADVANCE_LEARNMODE:
                rawExtended = 1;
                break;
            case ADVANCE_LEARNMODE_SELECT_REPEATER:
                rawExtended = 2;
                break;
            default:
                rawExtended = 0; /* Use SIMPLE_LEARNMODE as fallback. */

                LOGGER.warn("Invalid value returned from getExtended().");
                break;
        }
        bb.put(rawExtended);

        bb.putInt((int) getTimeout());

        return rawSmartAckData;
    }

    @Override
    public Response inspectResponsePacket(final ResponsePacket packet) throws UnknownResponseException {
        switch (packet.getReturnCode()) {
            case ResponseReturnCode.RET_OK:
            case ResponseReturnCode.RET_NOT_SUPPORTED:
            case ResponseReturnCode.RET_WRONG_PARAM:
                return new NoDataResponse();

            default:
                throw new UnknownResponseException();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public SmartAckLearnModeExtended getExtended() {
        return extended;
    }

    public void setExtended(final SmartAckLearnModeExtended extended) {
        this.extended = extended;
    }

    /**
     * Get the timeout for the learn mode in milliseconds.
     *
     * If the timeout is 0 a default period is used (should be TIMEOUT_DFL_IF_ZERO ms).
     * The timeout is adjusted so it fits in range from TIMEOUT_MIN to TIMEOUT_MAX.
     *
     * @return Return the timeout for the learn mode in milliseconds.
     */
    public long getTimeout() {
        final long ranged = CalculationUtil.fitInRange(timeout, TIMEOUT_MIN, TIMEOUT_MAX);
        if (ranged == 0) {
            return TIMEOUT_DFL_IF_ZERO;
        } else {
            return ranged;
        }
    }

    /**
     * Set the timeout for the learn mode in milliseconds.
     *
     * If the timeout is 0 a default period is used (should be TIMEOUT_DFL_IF_ZERO ms).
     * The timeout is adjusted so it fits in range from TIMEOUT_MIN to TIMEOUT_MAX.
     *
     * @param timeout The timeout for the learn mode in milliseconds.
     */
    public void setTimeout(final long timeout) {
        final long ranged = CalculationUtil.fitInRange(timeout, TIMEOUT_MIN, TIMEOUT_MAX);
        if (ranged == 0) {
            this.timeout = TIMEOUT_DFL_IF_ZERO;
        } else {
            this.timeout = ranged;
        }
    }

}
