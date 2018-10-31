/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.GetDeviceStatus;
import org.openhab.binding.velux.things.VeluxGwState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeDeviceStatus} represents a complete set of transactions
 * for querying device status on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeDeviceStatus#retrieve} for starting the detection.
 * </UL>
 * Any parameters are controlled by {@link org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration}.
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBridgeDeviceStatus {
	private final Logger logger = LoggerFactory.getLogger(VeluxBridgeDeviceStatus.class);


    /**
     * Bridge information, (currently only) consisting of:
     * <ul>
     * <li>isRetrieved (boolean flag),
     * <li>VeluxGwState
     * </ul>
     */
    public class Channel {
        public boolean isRetrieved = false;
        public VeluxGwState gwState;   
    }

    private Channel channel = null;

    /**
     * Complete workflow for retrieving the firmware version, consisting of Login into bridge, querying the firmware
     * version and logout from bridge based on a well-prepared environment of a {@link VeluxBridgeProvider}, where the
     * results are stored within as well in {@link VeluxBridgeDeviceStatus#channel}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @return <b>channel</b> - or null -
     *         of type {@link VeluxBridgeDeviceStatus.Channel} describing the overall result of this interaction.
     */
	public Channel retrieve(VeluxBridge bridge) {
		logger.trace("retrieve() called.");


		if (channel == null) {
			channel = new Channel();
		}
		logger.trace("retrieve() About to query device status.");
		GetDeviceStatus bcp = bridge.bridgeAPI().getDeviceStatus();
		if ((bridge.bridgeCommunicate(bcp)) && (bcp.isCommunicationSuccessful())) {
			this.channel.gwState = bcp.getState();
			this.channel.isRetrieved = true;
			logger.trace("retrieve() finished successfully with result {}.", this.channel.gwState);
			return channel;
		}
		else {
			logger.trace("retrieve() finished with failure.");
			return null;
		}
	}
}

/**
 * end-of-bridge/VeluxBridgeDeviceStatus.java
 */
