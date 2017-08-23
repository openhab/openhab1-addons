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
package eu.aleon.aleoncean.rxtx;

import java.util.concurrent.TimeUnit;
import eu.aleon.aleoncean.packet.ESP3Packet;
import eu.aleon.aleoncean.packet.ResponsePacket;

/**
 * Interface for a ESP3 based EnOcean (read / write) hardware connection.
 *
 * This interface should describe how we abstract the hardware communication e.g. with a EnOcean stick.
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public interface ESP3Connector {

    /**
     * Establish connection to a ESP3 device.
     *
     * @param device An identifier for the device.
     *               This must be an identifier so the specific implementation knows which device to use.
     * @return Return true if the connection could be established, otherwise false.
     */
    public boolean connect(final String device);

    /**
     * Shut down the connection to the ESP3 device.
     *
     */
    public void disconnect();

    /**
     * Transmit an ESP3 packet to the hardware device.
     *
     * @param packet The ESP3 packet that should be transmitted.
     * @return Return the response packet received from the hardware device.
     *         If a timeout occurs null is returned.
     */
    public ResponsePacket write(final ESP3Packet packet);

    /**
     * Read an ESP3 packet from the hardware device.
     *
     * Read an ESP3 packet, waiting up to the specified wait time if necessary for a packet to become available.
     *
     * @param timeout How long to wait before giving up, in units of unit.
     * @param unit    A TimeUnit determining how to interpret the timeout parameter.
     * @return Return the next ESP3Packet that was read. Null if no packet was received in given timeout.
     * @throws eu.aleon.aleoncean.rxtx.ReaderShutdownException This exception is thrown, if the read end was shut down.
     */
    public ESP3Packet read(long timeout, TimeUnit unit) throws ReaderShutdownException;
}
