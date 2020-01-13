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
package org.openhab.binding.homematic.internal.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to retrieve local network interfaces.
 *
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class LocalNetworkInterface {

    private static final Logger logger = LoggerFactory.getLogger(LocalNetworkInterface.class);

    /**
     * Finds the (non loopback, non localhost) local network interface to be
     * connected to from other machines.
     *
     * @return
     */
    public static String getLocalNetworkInterface() {
        String localInterface = null;
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface current = interfaces.nextElement();
                if (!current.isUp() || current.isLoopback() || current.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = current.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress current_addr = addresses.nextElement();
                    if (current_addr.isLoopbackAddress() || (current_addr instanceof Inet6Address)) {
                        continue;
                    }
                    if (localInterface != null) {
                        logger.warn("Found multiple local interfaces! Replacing " + localInterface + " with "
                                + current_addr.getHostAddress());
                    }
                    localInterface = current_addr.getHostAddress();
                }
            }
        } catch (SocketException e) {
            logger.error("Could not retrieve network interface. ", e);
            return null;
        }
        return localInterface;
    }

}
