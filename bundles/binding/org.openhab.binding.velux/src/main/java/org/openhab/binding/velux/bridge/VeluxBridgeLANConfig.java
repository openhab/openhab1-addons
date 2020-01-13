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
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.common.GetLANConfig;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeLANConfig} represents a complete set of transactions
 * for retrieving the network configuration of the <B>Velux</B> bridge.
 * <P>
 * It provides the following methods:
 * <UL>
 * <LI>{@link #retrieve} for retrieval of information.
 * <LI>{@link #getChannel} for accessing the retrieved information.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @see VeluxBridgeProvider
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public class VeluxBridgeLANConfig {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeLANConfig.class);

    // Type definitions, class-internal variables

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
        public StringType openHABipAddress;
        public StringType openHABsubnetMask;
        public StringType openHABdefaultGW;
        public OnOffType openHABenabledDHCP;
        @Deprecated
        public String ipAddress;
        @Deprecated
        public String subnetMask;
        @Deprecated
        public String defaultGW;
        @Deprecated
        public boolean enabledDHCP;
    }

    private Channel channel = null;

    // Constructor methods

    /**
     * Constructor.
     * <P>
     * Initializes the internal data structure {@link #channel} of Velux LAN information,
     * which is publicly accessible via the method {@link #getChannel()}.
     */
    public VeluxBridgeLANConfig() {
        logger.trace("VeluxBridgeLANConfig(constructor) called.");
        channel = new Channel();
    }

    // Class access methods

    /**
     * Provide access to the internal structure of LAN information.
     *
     * @return a channel describing the overall actual LAN information.
     */
    public Channel getChannel() {
        return channel;
    }

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
    public Channel retrieve(VeluxBridge bridge) {
        logger.trace("retrieve() called.");

        if (this.channel == null) {
            this.channel = new Channel();
        }

        GetLANConfig bcp = bridge.bridgeAPI().getLANConfig();
        if (bridge.bridgeCommunicate(bcp) && bcp.isCommunicationSuccessful()) {
            logger.trace("retrieve() found successfully configuration {}.", bcp.getLANConfig());
            channel.openHABipAddress = new StringType(bcp.getLANConfig().getIpAddress());
            channel.openHABsubnetMask = new StringType(bcp.getLANConfig().getSubnetMask());
            channel.openHABdefaultGW = new StringType(bcp.getLANConfig().getDefaultGW());
            channel.openHABenabledDHCP = bcp.getLANConfig().getDHCP() ? OnOffType.ON : OnOffType.OFF;
            channel.isRetrieved = true;
            return channel;
        } else {
            logger.trace("retrieve() finished with failure.");
            return null;
        }
    }

    @Deprecated
    public VeluxBridgeLANConfig.Channel retrieve(VeluxBridgeProvider bridge) {
        return null;
    }

}
