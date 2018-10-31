/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;


import java.sql.Timestamp;

import org.openhab.binding.velux.bridge.comm.GetWLANConfig;
import org.openhab.binding.velux.things.VeluxGwWLAN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * import org.openhab.binding.velux.bridge.comm.json.BCgetWLANConfig;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 */
/**
 * The {@link VeluxBridgeWLANConfig} represents a complete set of transactions
 * for retrieving the wireless network configuration of the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeWLANConfig#retrieve} for retrieval of information.
 * </UL>
 * <P>
 * Any parameters are controlled by {@link org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration}.
 *
 * @see VeluxBridgeProvider
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBridgeWLANConfig {
	private final Logger logger = LoggerFactory.getLogger(VeluxBridgeWLANConfig.class);

	/**
	 * Wireless network configuration, consisting of:
	 * <ul>
	 * <li>isRetrieved (boolean flag),
	 * <li>wlanSSID, and
	 * <li>wlanPassword.
	 * </ul>
	 */
	public class Channel {
		public boolean isRetrieved = false;
		public Timestamp retrievalTime;
		public VeluxGwWLAN wlan = null;
		public String wlanSSID = "";
		public String wlanPassword = "";
	}

	private Channel channel = null;

	/**
	 * Complete workflow for retrieving the wireless network configuration, consisting of Login into bridge, querying
	 * the network configuration and logout from bridge based on a well-prepared environment of a
	 * {@link VeluxBridgeProvider}, where the results are stored within as well in
	 * {@link VeluxBridgeWLANConfig#channel}.
	 *
	 * @param bridge Initialized Velux bridge handler.
	 * @return <b>channel</b> - or null -
	 *         of type {@link VeluxBridgeWLANConfig.Channel} describing the overall result of this interaction.
	 */
	public Channel retrieve(VeluxBridge bridge) {
		logger.trace("retrieve() called.");

		if (channel == null) {
			channel = new Channel();
		}
		GetWLANConfig bcp = bridge.bridgeAPI().getWLANConfig();
		if ((bridge.bridgeCommunicate(bcp)) && (bcp.isCommunicationSuccessful())) {
			logger.trace("retrieve() found successfully configuration {}.", bcp.getWLANConfig());
			channel.wlanSSID = bcp.getWLANConfig().getSSID();
			channel.wlanPassword = bcp.getWLANConfig().getPassword();
			channel.wlan = new VeluxGwWLAN(bcp.getWLANConfig().getSSID(), bcp.getWLANConfig().getPassword());
			channel.isRetrieved = true;
			return channel;
		} else {
			logger.trace("retrieve() finished with failure.");
			return null;
		}
	}

}
/**
 * end-of-bridge/VeluxBridgeWLANConfig.java
 */
