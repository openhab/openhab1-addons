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

import org.openhab.binding.velux.bridge.common.SendCommand;
import org.openhab.binding.velux.things.VeluxProductPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeSendCommand} represents a complete set of transactions
 * for executing a scene defined on the <B>Velux</B> bridge.
 * <P>
 * It provides a method {@link VeluxBridgeSendCommand#sendCommand} for sending a parameter change command.
 * Any parameters are controlled by {@link org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration}.
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public class VeluxBridgeSendCommand {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeSendCommand.class);

    // Class access methods

    /**
     * Login into bridge, instruct the bridge to pass a command towards an actuator based
     * on a well-prepared environment of a {@link VeluxBridgeProvider}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @param nodeId Number of Actuator to be modified.
     * @param value Target value for Actuator main parameter.
     * @return true if successful, and false otherwise.
     */
    public boolean sendCommand(VeluxBridge bridge, int nodeId, VeluxProductPosition value) {
        logger.trace("sendCommand(nodeId={},value={}) called.", nodeId, value);

        SendCommand bcp = bridge.bridgeAPI().sendCommand();
        int veluxValue = value.getPositionAsVeluxType();
        bcp.setNodeAndMainParameter(nodeId, veluxValue);
        if (bridge.bridgeCommunicate(bcp) && bcp.isCommunicationSuccessful()) {
            logger.debug("sendCommand() finished successfully.");
            return true;
        } else {
            logger.trace("sendCommand() finished with failure.");
            return false;
        }
    }

}
