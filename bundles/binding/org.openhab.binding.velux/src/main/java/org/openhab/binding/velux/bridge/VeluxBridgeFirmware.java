/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.BCgetFirmware;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeFirmware} represents a complete set of transactions
 * for retrieving of firmware version string on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeFirmware#retrieve} for retrieval of information.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @see VeluxBridgeProvider
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBridgeFirmware {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeFirmware.class);

    /**
     * Bridge information, (currently only) consisting of:
     * <ul>
     * <li>isRetrieved (boolean flag),
     * <li>firmwareVersion.
     * </ul>
     */
    public class Channel {
        public boolean isRetrieved = false;
        public String firmwareVersion = "";
    }

    private Channel channel = null;

    /**
     * Complete workflow for retrieving the firmware version, consisting of Login into bridge, querying the firmware
     * version and logout from bridge based on a well-prepared environment of a {@link VeluxBridgeProvider}, where the
     * results are stored within as well in {@link VeluxBridgeFirmware#channel}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @return <b>channel</b> - or null -
     *         of type {@link VeluxBridgeFirmware.Channel} describing the overall result of this interaction.
     */
    public Channel retrieve(VeluxBridgeProvider bridge) {
        logger.trace("retrieve() called.");

        if (this.channel == null) {
            this.channel = new Channel();
        }

        BCgetFirmware.Response response = bridge.bridgeCommunicate(new BCgetFirmware());
        if (response != null) {
            logger.trace("retrieve() found successfully firmware {}.", response.getFirmwareVersion());
            this.channel.firmwareVersion = response.getFirmwareVersion();
            this.channel.isRetrieved = true;
            return channel;
        }
        logger.trace("retrieve() finished with failure.");
        return null;
    }

}
/**
 * end-of-bridge/VeluxBridgeFirmware.java
 */
