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
package org.openhab.binding.rwesmarthome.internal.communicator.client;

import java.io.IOException;

import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.RWESmarthomeSessionExpiredException;

/**
 * Interface for the communication with RWE Smarthome.
 *
 * @author ollie-dev
 *
 */
public interface RWEClient {

    /**
     * Executes a request
     * 
     * @param hostname
     * @param clientId
     * @param request
     * @param command
     * @return
     * @throws IOException
     * @throws RWESmarthomeSessionExpiredException
     */
    public String execute(String hostname, String clientId, String request, String command)
            throws IOException, RWESmarthomeSessionExpiredException;
}
