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

import org.openhab.binding.velux.bridge.common.CheckLostNodes;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeDeviceCheckLostNodes} represents a complete set of transactions
 * for querying device status on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link #initiate} for starting the detection.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public class VeluxBridgeDeviceCheckLostNodes {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeDeviceCheckLostNodes.class);

    // Class access methods

    /**
     * Login into bridge, query the bridge for device status and logout from bridge
     * based on a well-prepared environment of a {@link VeluxBridgeProvider}.
     *
     * @param bridge Initialized Velux bridge handler.
     */
    public void initiate(VeluxBridge bridge) {
        logger.trace("initiate() called.");
        CheckLostNodes bcp = bridge.bridgeAPI().checkLostNodes();
        if (bridge.bridgeCommunicate(bcp) && bcp.isCommunicationSuccessful()) {
            logger.trace("initiate() finished successfully.");
        } else {
            logger.trace("initiate() finished with failure.");
        }
        return;
    }

    @Deprecated
    public String retrieve(VeluxBridgeProvider bridge) {
        return null;
    }
}
