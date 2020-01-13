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

import org.openhab.binding.velux.bridge.common.GetDeviceStatus;
import org.openhab.binding.velux.things.VeluxGwState;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeDeviceStatus} represents a complete set of transactions
 * for querying device status on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link #retrieve} for starting the detection.
 * <LI>{@link #getChannel} for accessing the retrieved information.
 * </UL>
 * Any parameters are controlled by {@link org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration}.
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public class VeluxBridgeDeviceStatus {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeDeviceStatus.class);

    // Type definitions, class-internal variables

    /**
     * Bridge information consisting of:
     * <ul>
     * <li>{@link #isRetrieved} describing the retrieval state,
     * <li>{@link #gwState} containing the brief gateway state,
     * <li>{@link #gwStateDescription} containing the verbose gateway state.
     * </ul>
     */
    public class Channel {
        public boolean isRetrieved = false;
        public StringType gwState;
        public StringType gwStateDescription;
    }

    private Channel channel = null;

    // Constructor methods

    /**
     * Constructor.
     * <P>
     * Initializes the internal data structure {@link #channel} of Velux actuators/products,
     * which is publicly accessible via the method {@link #getChannel()}.
     */
    public VeluxBridgeDeviceStatus() {
        logger.trace("VeluxBridgeDeviceStatus(constructor) called.");
        channel = new Channel();
    }

    // Class access methods

    /**
     * Provide access to the internal structure of the device status.
     *
     * @return a channel describing the overall actual device status.
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Complete workflow for retrieving the firmware version, consisting of Login into bridge, querying the firmware
     * version and logout from bridge based on a well-prepared environment of a {@link VeluxBridgeProvider}, where the
     * results are stored in {@link VeluxBridgeDeviceStatus#channel}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @return <b>channel</b> - or null -
     *         of type {@link VeluxBridgeDeviceStatus.Channel} describing the overall result of this interaction.
     */
    public Channel retrieve(VeluxBridge bridge) {
        logger.trace("retrieve() called. About to query device status.");
        GetDeviceStatus bcp = bridge.bridgeAPI().getDeviceStatus();
        if (bridge.bridgeCommunicate(bcp) && bcp.isCommunicationSuccessful()) {
            VeluxGwState state = bcp.getState();
            this.channel.gwState = new StringType(state.toString());
            this.channel.gwStateDescription = new StringType(state.toDescription());
            this.channel.isRetrieved = true;
            logger.trace("retrieve() finished successfully with result {}.", state.toDescription());
            return channel;
        } else {
            logger.trace("retrieve() finished with failure.");
            return null;
        }
    }

    @Deprecated
    public String retrieve(VeluxBridgeProvider bridge) {
        return null;
    }

}
