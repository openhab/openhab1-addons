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
 * Interface to let others know the the HarmonyHubGatway is configured or not
 * 
 * @author Dan Cunningham
 * @since 1.7.0
 */
public interface HarmonyHubGatewayListener {

    /**
     * Is the HarmonyHubGatway properly configured
     * 
     * @param isConfigured
     */
    public void configured(boolean isConfigured);
}
