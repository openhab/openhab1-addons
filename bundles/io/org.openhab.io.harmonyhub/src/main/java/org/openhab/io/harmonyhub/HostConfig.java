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
package org.openhab.io.harmonyhub;

/**
 *
 * Internal class to hold a Harmony Hub configuration
 *
 * @author Dan Cunningham
 * @since 1.7.0
 *
 */
public class HostConfig {
    private String discoveryName;
    private String host;
    private String username;
    private String password;

    /**
     * Gets the Harmony Hub Host
     *
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the Harmony Hub Host
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets the Harmony user name to login with
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the Harmony user name to login with
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the Harmony password to login with
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the harmony user name to login with
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getDiscoveryName() {
        return discoveryName;
    }

    public void setDiscoveryName(String discoveryName) {
        this.discoveryName = discoveryName;
    }

    /**
     * Checks if our host, user name and password are set
     *
     * @return
     */
    public boolean useCredentials() {
        return host != null && username != null && password != null;
    }

    public boolean useDiscovery() {
        return discoveryName != null;
    }
}
