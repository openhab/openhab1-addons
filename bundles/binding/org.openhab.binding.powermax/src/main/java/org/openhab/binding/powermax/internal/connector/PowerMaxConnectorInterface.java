/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powermax.internal.connector;

/**
 * Interface for communication with the Visonic alarm panel
 *
 * @author lolodomo
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
