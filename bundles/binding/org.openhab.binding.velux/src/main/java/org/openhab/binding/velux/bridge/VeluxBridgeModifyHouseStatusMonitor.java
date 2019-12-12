/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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

import org.openhab.binding.velux.bridge.common.ModifyHouseStatusMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeModifyHouseStatusMonitor} represents a complete set of transactions
 * for modifying the service state of the <B>House Status Monitor</B> on the <B>Velux</B> bridge.
 * <P>
 * The HSM is responsible for continuous updates towards the communication initiator
 * about any changes of actuator states.
 * <P>
 * It therefore provides a method {@link VeluxBridgeModifyHouseStatusMonitor#modifyHSM} for modifying the HSM settings.
 * Any parameters are controlled by {@link org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration}.
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public class VeluxBridgeModifyHouseStatusMonitor {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeModifyHouseStatusMonitor.class);

    // Class access methods

    /**
     * Login into bridge, modify HSM and logout from bridge based
     * on a well-prepared environment of a {@link VeluxBridgeProvider}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @param enableService Flag whether the HSM should be activated.
     * @return true if successful or false otherwise.
     */
    public boolean modifyHSM(VeluxBridge bridge, boolean enableService) {
        logger.trace("modifyHSM({}) called.", enableService);

        ModifyHouseStatusMonitor bcp = bridge.bridgeAPI().modifyHouseStatusMonitor();
        bcp.serviceActivation(enableService);
        if (bridge.bridgeCommunicate(bcp) && bcp.isCommunicationSuccessful()) {
            logger.debug("modifyHSM() finished successfully.");
            return true;
        } else {
            logger.trace("modifyHSM() finished with failure.");
            return false;
        }
    }

}
