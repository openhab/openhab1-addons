/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.onkyo.internal.eiscp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openhab.binding.onkyo.internal.OnkyoEventListener;
import org.openhab.binding.onkyo.internal.OnkyoStatusUpdateEvent;

/**
 * <p>
 * <b>Note:</b>Onkyo Event notifier for Serial port communications.
 * </p>
 * <br>
 *
 * @author Sriram Balakrishnan
 * @since 1.9.0
 */
public class EiscpEventNotifier {
    String deviceSerialorIp;
    private List<OnkyoEventListener> _listeners = new ArrayList<OnkyoEventListener>();

    public EiscpEventNotifier(String deviceSerialOrIp) {
        this.deviceSerialorIp = deviceSerialOrIp;
    }

    /**
     * Add event listener, which will be invoked when status upadte is received
     * from receiver.
     **/
    public synchronized void addEventListener(OnkyoEventListener listener) {
        _listeners.add(listener);
    }

    /**
     * Remove event listener.
     **/
    public synchronized void removeEventListener(OnkyoEventListener listener) {
        _listeners.remove(listener);
    }

    public void notifyMessage(OnkyoStatusUpdateEvent event, byte[] data, int dataSize) throws EiscpException {
        // unit type
        @SuppressWarnings("unused")
        final byte unitType = data[1];

        // data should be end to "[EOF]" or "[EOF][CR]" or
        // "[EOF][CR][LF]" characters depend on model
        // [EOF] End of File ASCII Code 0x1A
        // [CR] Carriage Return ASCII Code 0x0D (\r)
        // [LF] Line Feed ASCII Code 0x0A (\n)

        int endBytes = 0;

        if (data[dataSize - 4] == (byte) 0x1A && data[dataSize - 3] == '\r' && data[dataSize - 2] == '\n'
                && data[dataSize - 1] == 0x00) {

            // skip "[EOF][CR][LF][NULL]"
            endBytes = 4;

        } else if (data[dataSize - 3] == (byte) 0x1A && data[dataSize - 2] == '\r' && data[dataSize - 1] == '\n') {

            // skip "[EOF][CR][LF]"
            endBytes = 3;

        } else if (data[dataSize - 2] == (byte) 0x1A && data[dataSize - 1] == '\r') {

            // "[EOF][CR]"
            endBytes = 2;

        } else if (data[dataSize - 1] == (byte) 0x1A) {

            // "[EOF]"
            endBytes = 1;

        } else {
            throw new EiscpException("Illegal end of message");
        }

        int bytesToCopy = dataSize - 2 - endBytes;

        byte[] message = new byte[bytesToCopy];

        // skip 2 first bytes and copy all bytes before end bytes
        System.arraycopy(data, 2, message, 0, bytesToCopy);

        // send message to event listeners
        try {
            Iterator<OnkyoEventListener> iterator = _listeners.iterator();

            while (iterator.hasNext()) {
                iterator.next().statusUpdateReceived(event, deviceSerialorIp, new String(message));
            }

        } catch (Exception e) {
            throw new EiscpException("Event listener invoking error", e);
        }

    }

}
