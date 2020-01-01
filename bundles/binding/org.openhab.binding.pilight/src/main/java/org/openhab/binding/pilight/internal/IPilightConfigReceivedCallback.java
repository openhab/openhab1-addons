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
package org.openhab.binding.pilight.internal;

/**
 * Callback interface to signal any listeners that the current pilight configuration was received.
 *
 * @author Jeroen Idserda
 * @since 1.0
 */
public interface IPilightConfigReceivedCallback {

    /**
     * Configuration received.
     * 
     * @param connection The connection to pilight that received the configuration
     */
    public void configReceived(PilightConnection connection);

}
