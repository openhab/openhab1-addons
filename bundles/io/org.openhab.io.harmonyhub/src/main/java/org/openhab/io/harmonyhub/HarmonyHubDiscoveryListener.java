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
 * A {@link HarmonyHubDiscoveryListener} listeners for discovered hubs
 *
 * @author Dan Cunningham - Initial contribution
 *
 */
public interface HarmonyHubDiscoveryListener {

    /**
     * The discovery process has finished
     */
    public void hubDiscoveryFinished();

    /**
     * The discovery process has discovered a hub
     *
     * @param result
     */
    public void hubDiscovered(HarmonyHubDiscoveryResult result);
}