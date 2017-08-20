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
package eu.aleon.aleoncean.packet.response.smartackcommand;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.aleon.aleoncean.packet.response.Response;
import eu.aleon.aleoncean.values.SmartAckLearnModeExtended;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class SaRdLearnModeResponseOk extends Response {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaRdLearnModeResponseOk.class);

    private boolean enabled;

    SmartAckLearnModeExtended extended;

    @Override
    public byte[] getResponseData() {
        final int rawResponseDataLength = 1 /* enable */
                                          + 1 /* extended */;

        final byte[] rawResponseData = new byte[rawResponseDataLength];
        final ByteBuffer bb = ByteBuffer.wrap(rawResponseData);
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

        return rawResponseData;
    }

    @Override
    public void setResponseData(final byte[] responseData) {
        final ByteBuffer bb = ByteBuffer.wrap(responseData);
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
                LOGGER.warn(String.format("The received value (0x%02X) for the Smart Ack get learn mode response extended field is invalid.", rawExtended));
                break;
        }
    }

    @Override
    public byte[] getOptionalData() {
        return null;
    }

    @Override
    public void setOptionalData(final byte[] optionalData) {
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

}
