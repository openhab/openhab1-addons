/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.BCcheckLostNodes;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeDeviceCheckLostNodes} represents a complete set of transactions
 * for querying device status on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeDeviceCheckLostNodes#retrieve} for starting the detection.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBridgeDeviceCheckLostNodes {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeDeviceCheckLostNodes.class);

    /**
     * Login into bridge, query the bridge for device status and logout from bridge
     * based on a well-prepared environment of a {@link VeluxBridgeProvider}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @return <b>status</b> of type String describing the status of the device/bridge.
     */

    public String retrieve(VeluxBridgeProvider bridge) {
        logger.trace("retrieve() called.");

        logger.trace("retrieve() About to query device status.");
        BCcheckLostNodes.Response response = bridge.bridgeCommunicate(new BCcheckLostNodes());
        if (response == null) {
            logger.trace("retrieve() finished with failure.");
            return null;
        } else {
            logger.trace("retrieve() finished successfully with result {}.", response.getDeviceStatus());
            return response.getDeviceStatus();
        }
    }
}

/**
 * end-of-bridge/VeluxBridgeDeviceCheckLostNodes.java
 */
