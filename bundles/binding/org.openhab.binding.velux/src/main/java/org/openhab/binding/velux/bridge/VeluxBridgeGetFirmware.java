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

import org.openhab.binding.velux.bridge.common.GetFirmware;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeGetFirmware} represents a complete set of transactions
 * for retrieving of firmware version string on the <B>Velux</B> bridge.
 * <P>
 * It provides the following methods:
 * <UL>
 * <LI>{@link #retrieve} for retrieval of information.
 * <LI>{@link #getChannel} for accessing the retrieved information.
 * </UL>
 * Any parameters are controlled by {@link org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration}.
 *
 * @see VeluxBridgeProvider
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public class VeluxBridgeGetFirmware {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeGetFirmware.class);

    // Type definitions, class-internal variables

    /**
     * Bridge information consisting of:
     * <ul>
     * <li>isRetrieved (boolean flag),
     * <li>firmwareVersion (human readable String).
     * </ul>
     */
    public class Channel {
        public boolean isRetrieved = false;
        public StringType firmwareVersion;
    }

    private Channel channel = null;

    // Constructor methods

    /**
     * Constructor.
     * <P>
     * Initializes the internal data structure {@link #channel} of Velux firmware information,
     * which is publicly accessible via the method {@link #getChannel()}.
     */
    public VeluxBridgeGetFirmware() {
        logger.trace("VeluxBridgeGetFirmware(constructor) called.");
        channel = new Channel();
    }

    // Class access methods

    /**
     * Provide access to the internal structure of actuators/products.
     *
     * @return {@link Channel} describing the overall actuator situation.
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Complete workflow for retrieving the firmware version, consisting of Login into bridge, querying the firmware
     * version and logout from bridge based on a well-prepared environment of a {@link VeluxBridgeProvider}, where the
     * results are stored in {@link VeluxBridgeGetFirmware#channel}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @return <b>channel</b> - or null -
     *         of type {@link VeluxBridgeGetFirmware.Channel} describing the overall result of this interaction.
     */
    public Channel retrieve(VeluxBridge bridge) {
        logger.trace("retrieve() called.");

        GetFirmware bcp = bridge.bridgeAPI().getFirmware();
        if (bridge.bridgeCommunicate(bcp) && bcp.isCommunicationSuccessful()) {
            this.channel.firmwareVersion = new StringType(bcp.getFirmware().getfirmwareVersion());
            this.channel.isRetrieved = true;
            logger.trace("retrieve() found successfully firmware {}.", this.channel.firmwareVersion);
            return channel;
        }
        logger.trace("retrieve() finished with failure.");
        return null;
    }

}
