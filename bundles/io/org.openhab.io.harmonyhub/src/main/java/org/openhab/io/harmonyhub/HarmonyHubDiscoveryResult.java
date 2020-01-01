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
 * The {@link HarmonyHubDiscoveryResult} class represents a discovery result obtained from network discovery of a
 * Harmony Hub
 *
 * @author Dan Cunningham - Initial contribution
 *
 */
public class HarmonyHubDiscoveryResult {
    private String host;
    private String accountId;
    private String sessionID;
    private String id;
    private String friendlyName;

    public HarmonyHubDiscoveryResult(String host, String accountId, String sessionID, String id, String friendlyName) {
        super();
        this.host = host;
        this.accountId = accountId;
        this.sessionID = sessionID;
        this.id = id;
        this.friendlyName = friendlyName;
    }

    public String getHost() {
        return host;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getId() {
        return id;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

}