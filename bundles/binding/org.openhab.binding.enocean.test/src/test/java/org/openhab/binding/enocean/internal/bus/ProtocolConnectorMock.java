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
package org.openhab.binding.enocean.internal.bus;

import org.opencean.core.common.ProtocolConnector;

public class ProtocolConnectorMock implements ProtocolConnector {

    public void connect(String arg0) {
        // TODO Auto-generated method stub

    }

    public void disconnect() {
        // TODO Auto-generated method stub

    }

    public byte get() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void get(byte[] arg0) {
        // TODO Auto-generated method stub

    }

    public short getShort() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void mark() {
        // TODO Auto-generated method stub

    }

    public void reset() {
        // TODO Auto-generated method stub

    }

    public void write(byte[] arg0) {
        // TODO Auto-generated method stub

    }

}
