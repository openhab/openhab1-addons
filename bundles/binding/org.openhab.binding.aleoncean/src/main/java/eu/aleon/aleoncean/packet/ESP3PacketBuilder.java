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
package eu.aleon.aleoncean.packet;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import eu.aleon.aleoncean.util.CRC8;
import eu.aleon.aleoncean.util.CircularByteBuffer;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class ESP3PacketBuilder {

    public static final String PROPERTY_NAME_NEW_ESP3_PACKET_RECEIVED = "ESP3Packet";

    private final PropertyChangeSupport propertyChangeSupport;

    private final CircularByteBuffer buffer;

    private enum State {

        READ_SYNC_BYTE,
        READ_HEADER_DATA_LENGTH_HIGH,
        READ_HEADER_DATA_LENGTH_LOW,
        READ_OPTIONAL_LENGTH,
        READ_PACKET_TYPE,
        READ_CRC8_HEADER,
        READ_DATA,
        READ_OPTIONAL_DATA,
        READ_CRC8_DATA;

    }

    private enum ProceedByte {

        IN_PROGRESS, /* all other states */
        SYNC_RECEIVED,
        RESTART,
        COMPLETE;

    }

    private int dataLength;
    private int optionalLength;
    private int curLength;
    private final CRC8 crc;
    private State state;

    public ESP3PacketBuilder() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);

        this.buffer = new CircularByteBuffer(ESP3Packet.MAX_RAW_SIZE);
        this.crc = new CRC8();
        this.state = State.READ_SYNC_BYTE;
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private ProceedByte proceedByte(final byte b) {
        ProceedByte progress = ProceedByte.IN_PROGRESS;

        switch (state) {
            case READ_SYNC_BYTE:
                if (b == ESP3Packet.SYNC_BYTE) {
                    crc.reset();
                    state = State.READ_HEADER_DATA_LENGTH_HIGH;
                    progress = ProceedByte.SYNC_RECEIVED;
                }
                break;

            case READ_HEADER_DATA_LENGTH_HIGH:
                dataLength = (((int) b) & 0xFF) << 8;
                crc.update(b);
                state = State.READ_HEADER_DATA_LENGTH_LOW;
                break;

            case READ_HEADER_DATA_LENGTH_LOW:
                int len;
                len = (int) b;
                len = len & 0xFF;
                //dataLength += ((int) b) & 0xFF;
                dataLength = len;
                crc.update(b);
                state = State.READ_OPTIONAL_LENGTH;
                break;

            case READ_OPTIONAL_LENGTH:
                optionalLength = ((int) b) & 0xFF;
                crc.update(b);
                state = State.READ_PACKET_TYPE;
                break;

            case READ_PACKET_TYPE:
                state = State.READ_CRC8_HEADER;
                crc.update(b);
                break;

            case READ_CRC8_HEADER:
                if (b == crc.getValue()) {
                    crc.reset();
                    curLength = 0;
                    state = State.READ_DATA;
                } else {
                    progress = ProceedByte.RESTART;
                    state = State.READ_SYNC_BYTE;
                }
                break;

            case READ_DATA:
                ++curLength;
                crc.update(b);
                if (curLength == dataLength) {
                    if (optionalLength > 0) {
                        curLength = 0;
                        state = State.READ_OPTIONAL_DATA;
                    } else {
                        state = State.READ_CRC8_DATA;
                    }
                }
                break;

            case READ_OPTIONAL_DATA:
                ++curLength;
                crc.update(b);
                if (curLength == optionalLength) {
                    state = State.READ_CRC8_DATA;
                }
                break;

            case READ_CRC8_DATA:
                if (b == crc.getValue()) {
                    progress = ProceedByte.COMPLETE;
                } else {
                    progress = ProceedByte.RESTART;
                }
                state = State.READ_SYNC_BYTE;
                break;

            default:
                break;
        }

        return progress;
    }

    public void add(final byte b) {
        buffer.put(b);
        handleBuffer();
    }

    public void handleBuffer() {
        while (!buffer.isEmpty()) {
            final byte b = buffer.get();
            final ProceedByte progress = proceedByte(b);
            switch (progress) {
                case SYNC_RECEIVED:
                    buffer.setMarkToLastReadPos();
                    break;
                case RESTART:
                    buffer.setReadPosToNextValue(ESP3Packet.SYNC_BYTE);
                    break;
                case COMPLETE:
                    final byte[] raw = buffer.getAllFromMarkToLastRead();
                    propertyChangeSupport.firePropertyChange(PROPERTY_NAME_NEW_ESP3_PACKET_RECEIVED, null, raw);
                    break;
                case IN_PROGRESS:
                    /* handle until all read from buffer. */
                    break;

                default:
                    break;
            }
        }
    }
}
