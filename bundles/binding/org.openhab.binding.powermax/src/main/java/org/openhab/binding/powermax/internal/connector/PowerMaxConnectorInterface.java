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
package org.openhab.binding.powermax.internal.connector;

/**
 * Interface for communication with the Visonic alarm panel
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public interface PowerMaxConnectorInterface {

    /**
     * Method for opening a connection to the Visonic alarm panel.
     */
    void open();

    /**
     * Method for closing a connection to the Visonic alarm panel.
     */
    void close();

    /**
     * Returns connection status
     *
     * @return: true if connected or false if not
     **/
    boolean isConnected();

    /**
     * Method for sending a message to the Visonic alarm panel
     *
     * @param data
     *            the message as a table of bytes
     **/
    void sendMessage(byte[] data);

    /**
     * Method for registering an event listener
     *
     * @param listener
     *            the listener to be registered
     */
    public void addEventListener(PowerMaxEventListener listener);

    /**
     * Method for removing an event listener
     *
     * @param listener
     *            the listener to be removed
     */
    public void removeEventListener(PowerMaxEventListener listener);

}
