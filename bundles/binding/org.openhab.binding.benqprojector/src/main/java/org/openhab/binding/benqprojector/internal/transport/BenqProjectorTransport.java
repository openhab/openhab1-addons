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
package org.openhab.binding.benqprojector.internal.transport;

/**
 * Interface for BenQ projector transport classes.
 *
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public interface BenqProjectorTransport {

    /**
     * Setup the transport connection
     * 
     * @param connectionParams
     *            String containing all the information to setup the connection
     * @return true if connection setup successfully
     */
    public boolean setupConnection(String connectionParams);

    /**
     * Close down the connection
     */
    public void closeConnection();

    /**
     * Send command to projector via transport and get a response
     * 
     * @param cmd
     *            command to send
     * @return response to command
     */
    public String sendCommandExpectResponse(String cmd);

}
