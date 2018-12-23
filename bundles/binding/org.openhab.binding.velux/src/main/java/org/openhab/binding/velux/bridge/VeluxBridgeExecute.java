/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.BCrunScene;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeExecute} represents a complete set of transactions
 * for executing a scene defined on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeExecute#execute} for execution of a scene.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBridgeExecute {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeExecute.class);

    /**
     * Login into bridge, executes a scene and logout from bridge based
     * on a well-prepared environment of a {@link VeluxBridgeProvider}.
     *
     * @param bridge  Initialized Velux bridge handler.
     * @param sceneNo Number of scene to be executed.
     * @return <b>success</b>
     *         of type boolean describing the overall result of this interaction.
     */
    public boolean execute(VeluxBridgeProvider bridge, int sceneNo) {
        logger.trace("execute({}) called.", sceneNo);

        if (!bridge.bridgeLogin()) {
            logger.debug("Velux bridge login sequence failed; expecting bridge is OFFLINE.");
            return false;
        }
        BCrunScene.Response response = bridge.bridgeCommunicate(new BCrunScene(sceneNo));
        if (response != null) {
            if (response.getResult()) {
                logger.debug("execute() finished successfully.");
                return true;
            }
        }
        if (!bridge.bridgeLogout()) {
            logger.debug("Velux bridge logout sequence failed; expecting bridge is OFFLINE.");
            return false;
        }
        logger.trace("execute() finished with failure.");
        return false;
    }
}
/**
 * end-of-bridge/VeluxBridgeExecute.java
 */
