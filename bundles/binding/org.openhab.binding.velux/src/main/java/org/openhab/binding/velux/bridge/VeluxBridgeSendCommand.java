/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.SendCommand;
import org.openhab.binding.velux.things.VeluxProductPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeSendCommand} represents a complete set of transactions
 * for executing a scene defined on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeSendCommand#sendCommand} for sending a parameter change command.
 * </UL>
 * Any parameters are controlled by {@link org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration}.
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBridgeSendCommand {
	private final Logger logger = LoggerFactory.getLogger(VeluxBridgeSendCommand.class);

	/**
	 * Login into bridge, instruct the bridge to pass a command towards an actuator based
	 * on a well-prepared environment of a {@link VeluxBridgeProvider}.
	 *
	 * @param bridge  Initialized Velux bridge handler.
	 * @param nodeId  Number of Actuator to be modified.
	 * @param value   Target value for Actuator main parameter.
	 * @return <b>success</b>
	 *         of type boolean describing the overall result of this interaction.
	 */
	public boolean sendCommand(VeluxBridge bridge, int nodeId, VeluxProductPosition value) {
		logger.trace("sendCommand(nodeId={},value={}) called.", nodeId, value);

		//ToDo: is this really necessary: ask Velux engineering
		//bridge.bridgeLogout();
		SendCommand bcp = bridge.bridgeAPI().sendCommand();
		int veluxValue = value.getPositionAsVeluxType();
		bcp.setNodeId(nodeId).setMainParameter(veluxValue);
		if ((bridge.bridgeCommunicate(bcp)) && (bcp.isCommunicationSuccessful())) {
			logger.debug("sendCommand() finished successfully.");
			return true;
		}
		else {
			logger.trace("sendCommand() finished with failure.");
			return false;
		}
	}

}
/**
 * end-of-bridge/VeluxBridgeSendCommand.java
 */
