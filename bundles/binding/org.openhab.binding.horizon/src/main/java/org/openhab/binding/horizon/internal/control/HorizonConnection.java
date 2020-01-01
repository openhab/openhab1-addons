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
package org.openhab.binding.horizon.internal.control;

import java.io.Closeable;
import java.io.IOException;

/**
 * Class representing the connection to the HorizonBox
 *
 * @author J Kuijpers
 * @since 1.9.0
 */
class HorizonConnection implements Closeable {

    private RfbProto rfb;

    HorizonConnection(String host, int port) throws Exception {
        rfb = new RfbProto(host, port);
    }

    /**
     * Sends the key commands to the {@link RfbProto}. The Integer representing the
     * key is defined in {@link Keys}
     *
     */
    public void sendKey(final Integer key) throws Exception {
        rfb.writeKeyDown(key);
        rfb.writeKeyUp(key);
        rfb.writeBuffer();
    }

    /**
     * Closes the {@link RfbProto}
     *
     */
    @Override
    public void close() throws IOException {
        rfb.close();
    }
}
