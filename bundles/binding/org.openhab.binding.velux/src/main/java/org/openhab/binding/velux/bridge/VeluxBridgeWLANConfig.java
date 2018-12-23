/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.BCgetWLANConfig;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeWLANConfig} represents a complete set of transactions
 * for retrieving the wireless network configuration of the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeWLANConfig#retrieve} for retrieval of information.
 * </UL>
 * <P>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @see VeluxBridgeProvider
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBridgeWLANConfig {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeWLANConfig.class);

    /**
     * Wireless network configuration, consisting of:
     * <ul>
     * <li>isRetrieved (boolean flag),
     * <li>wlanSSID, and
     * <li>wlanPassword.
     * </ul>
     */
    public class Channel {
        public boolean isRetrieved = false;
        public String wlanSSID = "";
        public String wlanPassword = "";
    }

    private Channel channel = null;

    /**
     * Complete workflow for retrieving the wireless network configuration, consisting of Login into bridge, querying
     * the network configuration and logout from bridge based on a well-prepared environment of a
     * {@link VeluxBridgeProvider}, where the results are stored within as well in
     * {@link VeluxBridgeWLANConfig#channel}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @return <b>channel</b> - or null -
     *         of type {@link VeluxBridgeWLANConfig.Channel} describing the overall result of this interaction.
     */
    public Channel retrieve(VeluxBridgeProvider bridge) {
        logger.trace("retrieve() called.");

        if (channel == null) {
            channel = new Channel();
        }
        BCgetWLANConfig.Response response = bridge.bridgeCommunicate(new BCgetWLANConfig());
        if (response != null) {
            logger.trace("retrieve() found successfully configuration {}.", response.getWLANConfig());
            channel.wlanSSID = response.getWLANConfig().getSSID();
            channel.wlanPassword = response.getWLANConfig().getPassword();
            channel.isRetrieved = true;
            return channel;
        } else {
            logger.trace("retrieve() finished with failure.");
            return null;
        }
    }

}
/**
 * end-of-bridge/VeluxBridgeWLANConfig.java
 */
