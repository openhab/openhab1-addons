/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.BCgetLANConfig;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeLANConfig} represents a complete set of transactions
 * for retrieving the network configuration of the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeLANConfig#retrieve} for retrieval of information.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @see VeluxBridgeProvider
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBridgeLANConfig {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeLANConfig.class);

    /**
     * IP Network configuration, consisting of:
     * <ul>
     * <li>isRetrieved (boolean flag),
     * <li>ipAddress,
     * <li>subnetMask,
     * <li>defaultGW and
     * <li>enabledDHCP.
     * </ul>
     */
    public class Channel {
        public boolean isRetrieved = false;
        public String ipAddress = "";
        public String subnetMask = "";
        public String defaultGW = "";
        public boolean enabledDHCP = false;
    }

    private Channel channel = null;

    /**
     * Complete workflow for retrieving the network configuration, consisting of Login into bridge, querying
     * the network configuration and logout from bridge based on a well-prepared environment of a
     * {@link VeluxBridgeProvider}, where the results are stored within as well in
     * {@link VeluxBridgeLANConfig#channel}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @return <b>channel</b> - or null - of type {@link VeluxBridgeLANConfig.Channel} describing the overall result of
     *         this interaction.
     */
    public Channel retrieve(VeluxBridgeProvider bridge) {
        logger.trace("retrieve() called.");

        if (this.channel == null) {
            this.channel = new Channel();
        }

        BCgetLANConfig.Response response = bridge.bridgeCommunicate(new BCgetLANConfig());
        if (response != null) {
            logger.trace("retrieve() found successfully configuration {}.", response.getLANConfig());
            this.channel.ipAddress = response.getLANConfig().getIPAddress();
            this.channel.subnetMask = response.getLANConfig().getSubnetMask();
            this.channel.defaultGW = response.getLANConfig().getDefaultGateway();
            this.channel.enabledDHCP = response.getLANConfig().getDHCP();
            this.channel.isRetrieved = true;
            return channel;
        } else {
            logger.trace("retrieve() finished with failure.");
            return null;
        }
    }

}
/**
 * end-of-bridge/VeluxBridgeLANConfig.java
 */
