/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.ModifyHouseStatusMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeModifyHouseStatusMonitor} represents a complete set of transactions
 * for executing a scene defined on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeModifyHouseStatusMonitor#modifyHSM} for execution of a scene.
 * </UL>
 * Any parameters are controlled by {@link org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration}.
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBridgeModifyHouseStatusMonitor {
	private final Logger logger = LoggerFactory.getLogger(VeluxBridgeModifyHouseStatusMonitor.class);

	/**
	 * Login into bridge, modify HSM and logout from bridge based
	 * on a well-prepared environment of a {@link VeluxBridgeProvider}.
	 *
	 * @param bridge  Initialized Velux bridge handler.
	 * @param enableService Flag whether the HSM should be activated.
	 * @return <b>success</b>
	 *         of type boolean describing the overall result of this interaction.
	 */
	public boolean modifyHSM(VeluxBridge bridge, boolean enableService) {
		logger.trace("modifyHSM({}) called.", enableService);

		ModifyHouseStatusMonitor bcp = bridge.bridgeAPI().modifyHouseStatusMonitor();
		bcp.serviceActivation(enableService);
		if ((bridge.bridgeCommunicate(bcp)) && (bcp.isCommunicationSuccessful())) {
			logger.debug("modifyHSM() finished successfully.");
			return true;
		}
		else {
			logger.trace("modifyHSM() finished with failure.");
			return false;
		}
	}

}
/**
 * end-of-bridge/VeluxBridgeModifyHouseStatusMonitor.java
 */
