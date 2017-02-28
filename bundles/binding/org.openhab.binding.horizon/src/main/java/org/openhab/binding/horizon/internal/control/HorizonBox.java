/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.horizon.internal.control;

/**
 * This class holds the information for the device. It used to make a connection and send key commands to
 *
 * @author Jurgen Kuijpers
 * @since 1.9.0
 */
public class HorizonBox {

    private String host;
    private int port;

    public HorizonBox(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Sends the key commands to the horizonbox. The Integer representing the key is defined in {@link Keys}
     *
     */
    public void sendKey(final Integer key) throws Exception {
        HorizonConnection con = null;
        try {
            con = new HorizonConnection(host, port);
            con.sendKey(key);
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }
}
