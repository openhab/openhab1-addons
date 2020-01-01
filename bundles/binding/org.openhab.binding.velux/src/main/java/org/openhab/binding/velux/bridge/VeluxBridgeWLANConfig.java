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

import org.openhab.binding.velux.bridge.common.GetWLANConfig;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeWLANConfig} represents a complete set of transactions
 * for retrieving the wireless network configuration of the <B>Velux</B> bridge.
 * <P>
 * It provides the following methods:
 * <UL>
 * <LI>{@link #retrieve} for retrieval of information.
 * <LI>{@link #getChannel} for accessing the retrieved information.
 * </UL>
 * <P>
 * Any parameters are controlled by {@link org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration}.
 *
 * @see VeluxBridgeProvider
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public class VeluxBridgeWLANConfig {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeWLANConfig.class);

    // Type definitions, class-internal variables

    /**
     * Wireless network configuration, consisting of:
     * <ul>
     * <li>isRetrieved,
     * <li>wlanSSID,
     * <li>wlanPassword.
     * </ul>
     */
    public class Channel {
        public boolean isRetrieved = false;
        public StringType openHABwlanSSID;
        public StringType openHABwlanPassword;
        @Deprecated
        public String wlanSSID;
        @Deprecated
        public String wlanPassword;
    }

    private Channel channel = null;

    // Constructor methods

    /**
     * Constructor.
     * <P>
     * Initializes the internal data structure {@link #channel} of Velux WLAN information,
     * which is publicly accessible via the method {@link #getChannel()}.
     */
    public VeluxBridgeWLANConfig() {
        logger.trace("VeluxBridgeWLANConfig(constructor) called.");
        channel = new Channel();
    }

    // Class access methods

    /**
     * Provide access to the internal structure of WLAN information.
     *
     * @return a channel describing the overall WLAN situation.
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Complete workflow for retrieving the wireless network configuration, consisting of Login into bridge, querying
     * the network configuration and logout from bridge based on a well-prepared environment of a
     * {@link VeluxBridgeProvider}, where the results are stored within {@link VeluxBridgeWLANConfig#channel}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @return <b>channel</b> - or null -
     *         of type {@link VeluxBridgeWLANConfig.Channel} describing the overall result of this interaction.
     */
    public Channel retrieve(VeluxBridge bridge) {
        logger.trace("retrieve() called.");

        GetWLANConfig bcp = bridge.bridgeAPI().getWLANConfig();
        if (bridge.bridgeCommunicate(bcp) && bcp.isCommunicationSuccessful()) {
            logger.trace("retrieve() found successfully configuration {}.", bcp.getWLANConfig());
            channel.openHABwlanSSID = new StringType(bcp.getWLANConfig().getSSID());
            channel.openHABwlanPassword = new StringType(bcp.getWLANConfig().getPassword());
            channel.isRetrieved = true;
            return channel;
        } else {
            logger.trace("retrieve() finished with failure.");
            return null;
        }
    }

    @Deprecated
    public Channel retrieve(VeluxBridgeProvider bridge) {
        return null;
    }

}
