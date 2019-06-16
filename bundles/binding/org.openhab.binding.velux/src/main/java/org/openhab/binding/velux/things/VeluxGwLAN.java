/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.velux.things;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Velux</B> product representation.
 * <P>
 * Combined set of information describing a single Velux product.
 *
 * @author Guenther Schreiner - initial contribution.
 * @since 1.13.0
 */
public class VeluxGwLAN {
    private final Logger logger = LoggerFactory.getLogger(VeluxGwLAN.class);

    // Class internal

    private String ipAddress;
    private String subnetMask;
    private String defaultGW;
    private boolean enabledDHCP;

    // Constructor

    public VeluxGwLAN(String ipAddress, String subnetMask, String defaultGW, boolean enabledDHCP) {
        logger.trace("VeluxGwLAN() created.");

        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;
        this.defaultGW = defaultGW;
        this.enabledDHCP = enabledDHCP;
    }

    // Class access methods

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getSubnetMask() {
        return this.subnetMask;
    }

    public String getDefaultGW() {
        return this.defaultGW;
    }

    public boolean getDHCP() {
        return this.enabledDHCP;
    }

    @Override
    public String toString() {
        return String.format("ip %d, nm %d, gw %d, DHCP %s", this.ipAddress, this.subnetMask, this.defaultGW,
                this.enabledDHCP ? "enabled" : "disabled");
    }

}
