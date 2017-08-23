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
package eu.aleon.aleoncean.packet.radio.userdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.aleon.aleoncean.packet.radio.RadioPacketRPS.NUState;
import eu.aleon.aleoncean.packet.radio.RadioPacketRPS.T21State;
import eu.aleon.aleoncean.util.Bits;
import eu.aleon.aleoncean.values.WindowHandlePosition;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public abstract class UserDataEEPF610T2U extends UserDataRPS {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataEEPF610T2U.class);

    private static final T21State T21STATE = T21State.PTM_TYPE2;
    private static final NUState NUSTATE = NUState.UNASSIGNEDMESSAGE;

    public UserDataEEPF610T2U() {
        super(T21STATE, NUSTATE);
    }

    public UserDataEEPF610T2U(final byte[] data) {
        super(data, T21STATE, NUSTATE);
    }

    public abstract WindowHandlePosition getWindowHandlePosition() throws UserDataScaleValueException;

    public abstract void setWindowHandlePosition(final WindowHandlePosition windowHandlePosition) throws UserDataScaleValueException;

    protected final WindowHandlePosition getWindowHandlePosition(final byte b) throws UserDataScaleValueException {
        try {
            final WindowHandlePosition windowHandlePosition = decodePosition((byte) getDataRange(0, 7, 0, 4));
            return windowHandlePosition;
        } catch (final IllegalArgumentException ex) {
            final String msg = "The window handle position seems to be wrong.";
            LOGGER.warn(msg, ex);
            throw new UserDataScaleValueException(msg);
        }
    }

    protected final byte getWindowHandlePositionByte(final WindowHandlePosition windowHandlePosition) throws UserDataScaleValueException {
        try {
            return encodePosition(windowHandlePosition);
        } catch (final IllegalArgumentException ex) {
            final String msg = "The window handle position seems to be wrong.";
            LOGGER.warn(msg, ex);
            throw new UserDataScaleValueException(msg);
        }
    }

    /**
     * Decode the window handle position.
     *
     * @param b A byte that four last significant bits codes the value position.
     * @return Return the coded window handle position on.
     * @throws IllegalArgumentException This exception is thrown if the value could not be decoded.
     */
    private WindowHandlePosition decodePosition(final byte b) {
        if (Bits.isBitSet(b, 3) && Bits.isBitSet(b, 2)) {
            if (Bits.isBitSet(b, 0)) {
                if (Bits.isBitSet(b, 1)) {
                    return WindowHandlePosition.DOWN;
                } else {
                    return WindowHandlePosition.UP;
                }
            } else {
                return WindowHandlePosition.LEFT_OR_RIGHT;
            }
        } else {
            throw new IllegalArgumentException(String.format("Cannot decode position: %d", b & 0xFF));
        }
    }

    /**
     * Encode the window handle position.
     *
     * @param windowHandlePosition The window handle position that should be encoded.
     * @return Return the encoded window handle position using the four last significant bits.
     * @throws IllegalArgumentException This exception is thrown if the value could not be decoded.
     */
    private byte encodePosition(final WindowHandlePosition windowHandlePosition) {
        byte b = 0;

        b = Bits.setBit(b, 3);
        b = Bits.setBit(b, 2);

        switch (windowHandlePosition) {
            case UP:
                b = Bits.clrBit(b, 1);
                b = Bits.setBit(b, 0);
                break;
            case DOWN:
                b = Bits.setBit(b, 1);
                b = Bits.setBit(b, 0);
                break;
            case LEFT_OR_RIGHT:
                // The value of bit 1 is not defined.
                b = Bits.clrBit(b, 0);
                break;
            default:
                throw new IllegalArgumentException(String.format("Not implemented position to encode: %s", windowHandlePosition.toString()));
        }

        return b;
    }
}
