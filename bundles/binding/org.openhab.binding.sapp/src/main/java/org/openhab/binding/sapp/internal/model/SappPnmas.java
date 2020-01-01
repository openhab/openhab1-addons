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
package org.openhab.binding.sapp.internal.model;

/**
 * Class which represents a Pnmas controller
 *
 * @author Paolo Denti
 * @since 1.8.0
 */
public class SappPnmas {

    private String ip;
    private int port;

    /**
     * Constructor
     */
    public SappPnmas(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * ip getter
     */
    public String getIp() {
        return ip;
    }

    /**
     * port getter
     */
    public int getPort() {
        return port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("[ ip: %s - port : %d]", ip, port);
    }
}
