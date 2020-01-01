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

import org.openhab.binding.velux.bridge.common.DetectProducts;
import org.openhab.binding.velux.bridge.common.GetDeviceStatus;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.openhab.binding.velux.things.VeluxGwState;
import org.openhab.binding.velux.things.VeluxGwState.VeluxGatewaySubState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeDetectProducts} represents a complete set of transactions
 * for temporary activation of device detection mode on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method:
 * <UL>
 * <LI>{@link VeluxBridgeDetectProducts#detectProducts} for starting the detection.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 * <P>
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public class VeluxBridgeDetectProducts {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeDetectProducts.class);

    // Type definitions, class-internal variables

    private static long waitMSecs = 2000L;

    // Class access methods

    /**
     * Login into bridge, start process to detect (new) products, loop until bridge is idle again and logout from bridge
     * based on a well-prepared environment of a {@link VeluxBridgeProvider}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @return <b>success</b>
     *         of type boolean describing the overall result of this interaction.
     */
    public boolean detectProducts(VeluxBridge bridge) {
        logger.trace("detectProducts() called.");
        boolean success = false;

        logger.trace("detectProducts() About to activate detection.");
        DetectProducts bcp1 = bridge.bridgeAPI().detectProducts();
        if (!(bridge.bridgeCommunicate(bcp1)) || (bcp1.isCommunicationSuccessful())) {
            while (true) {
                logger.trace("detectProducts() About to query detection status.");
                GetDeviceStatus bcp = bridge.bridgeAPI().getDeviceStatus();
                if (!(bridge.bridgeCommunicate(bcp)) || (bcp.isCommunicationSuccessful())) {
                    logger.trace("detectProducts() finished with failure.");
                    break;
                }
                VeluxGwState deviceStatus = bcp.getState();
                if (deviceStatus.getSubState() == (byte) VeluxGatewaySubState.GW_SS_P1.getStateValue()) {
                    logger.trace("detectProducts() bridge is still busy.");
                } else if (deviceStatus.getSubState() == (byte) VeluxGatewaySubState.GW_SS_IDLE.getStateValue()) {
                    logger.trace("detectProducts() bridge is idle again, now.");
                    success = true;
                    break;
                } else {
                    logger.info("detectProducts() unknown devicestatus ({}) received.", deviceStatus);
                }
                logger.trace("detectProducts() about to wait {} msecs.", waitMSecs);
                try {
                    Thread.sleep(waitMSecs);
                } catch (InterruptedException ie) {
                    logger.trace("detectProducts() wait interrupted.");
                }
            }
        } else {
            logger.trace("detectProducts() activate detection finished with failure.");
        }

        logger.debug("detectProducts() finished {}.", success ? "successfully" : "with failure");
        return success;
    }

    @Deprecated
    public boolean detectProducts(VeluxBridgeProvider bridge) {
        return false;
    }
}
