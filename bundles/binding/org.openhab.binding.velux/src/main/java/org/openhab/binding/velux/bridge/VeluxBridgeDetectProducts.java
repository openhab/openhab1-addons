/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.BCdetectProducts;
import org.openhab.binding.velux.bridge.comm.BCgetDeviceStatus;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
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
 */
public class VeluxBridgeDetectProducts {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeDetectProducts.class);

    private static long waitMSecs = 2000L;

    /**
     * Login into bridge, start process to detect (new) products, loop until bridge is idle again and logout from bridge
     * based on a well-prepared environment of a {@link VeluxBridgeProvider}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @return <b>success</b>
     *         of type boolean describing the overall result of this interaction.
     */

    public boolean detectProducts(VeluxBridgeProvider bridge) {
        logger.trace("detectProducts() called.");
        boolean success = false;
        if (!bridge.bridgeLogin()) {
            logger.debug("Velux bridge login sequence failed; expecting bridge is OFFLINE.");
            return false;
        }

        logger.trace("detectProducts() About to activate detection.");
        BCdetectProducts.Response detectResponse = bridge.bridgeCommunicate(new BCdetectProducts());
        if (detectResponse != null) {
            while (true) {
                logger.trace("detectProducts() About to query detection status.");
                BCgetDeviceStatus.Response response = bridge.bridgeCommunicate(new BCgetDeviceStatus());
                if ((response == null) || (!response.getResult())) {
                    logger.trace("detectProducts() finished with failure.");
                    break;
                }
                String deviceStatus = response.getDeviceStatus();
                if (deviceStatus.equals("discovering")) {
                    logger.trace("detectProducts() bridge is still busy.");
                } else if (deviceStatus.equals("IDLE")) {
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
        if (!bridge.bridgeLogout()) {
            logger.debug("Velux bridge logout sequence failed; expecting bridge is OFFLINE.");
        }
        logger.debug("detectProducts() finished {}.", success ? "successfully" : "with failure");
        return success;
    }
}

/**
 * end-of-bridge/VeluxBridgeDetectProducts.java
 */
