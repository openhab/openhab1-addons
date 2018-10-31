/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.GetScenes;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.openhab.binding.velux.things.VeluxExistingScenes;
import org.openhab.binding.velux.things.VeluxScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeGetScenes} represents a complete set of transactions
 * for retrieving of any available scenes into a structure {@link VeluxExistingScenes}
 * defined on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeGetScenes#getScenes} for retrieval of information.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @see VeluxScene
 * @see VeluxExistingScenes
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBridgeGetScenes {
	private final Logger logger = LoggerFactory.getLogger(VeluxBridgeGetScenes.class);

	public boolean getScenes(VeluxBridge bridge, VeluxBridgeInstance thisKLF) {
		logger.trace("getScenes() called.");


		GetScenes bcp = bridge.bridgeAPI().getScenes();
		if ((bridge.bridgeCommunicate(bcp)) && (bcp.isCommunicationSuccessful())) {
			for (VeluxScene scene : bcp.getScenes()) {
				logger.trace("getScenes() found scene {}.", scene.toString());

				VeluxScene veluxScene = new VeluxScene(scene);
				logger.trace("getScenes() storing scene {}.", veluxScene);
				if (!thisKLF.existingScenes().isRegistered(veluxScene)) {
					thisKLF.existingScenes().register(veluxScene);
				}
				logger.trace("getScenes() stored scene {}.", veluxScene);
			}
			logger.debug("getScenes() finally has found scenes {}.", thisKLF.existingScenes());
			return true;
		} else {
			logger.trace("getScenes() finished with failure.");
			return false;
		}
	}

}
/**
 * end-of-bridge/VeluxBridgeGetScenes.java
 */
