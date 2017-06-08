/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.e131.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * E1.31 node class
 *
 * this is a receiving or sending E1.31 node with address and port
 *
 * @author Jan N. Klug
 * @since 1.9.0
 */

public class E131Node {

    private static final Logger logger = LoggerFactory.getLogger(E131Node.class);
    
    public static final int DEFAULT_PORT = 5568;

    protected int port;
    protected InetAddress address;

    /**
     * default constructor
     */
    public E131Node() {
        this.port = DEFAULT_PORT;
    }

    /**
     * constructor with address
     *
     * @param addrString
     *            domain name or IP address as string representation
     */
    public E131Node(String addrString) {
        this.port = DEFAULT_PORT;
        setInetAddress(addrString);
    }

    /**
     * constructor with address and port
     *
     * @param addrString
     *            domain name or IP address as string representation
     * @param port
     *            UDP port of the receiver node
     */
    public E131Node(String addrString, int port) {
        this.port = port;
        setInetAddress(addrString);
    }

    /**
     * sets the node address
     *
     * @param addrString
     *            domain name or IP address as string representation
     */
    public void setInetAddress(String addrString) {
        try {
            this.address = InetAddress.getByName(addrString);
        } catch (UnknownHostException e) {
            this.address = null;
            logger.warn("could not set address from {}", addrString);
        }
    }

    /**
     * sets the node port
     *
     * @param port
     *            UDP port of the receiver node
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * get this nodes port
     *
     * @return
     *         UDP port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * get this nodes address
     *
     * @return
     *         address as InetAddress
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * convert node to String
     *
     * @return
     *         string representation of this node (address:port)
     */
    @Override
    public String toString() {
        return this.address.toString() + ":" + String.valueOf(this.port);
    }

}
