/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

import java.net.DatagramSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openhab.binding.souliss.internal.network.udp.SoulissCommGate;

/**
 * This class implements the Souliss commmand HEALTY. The thread send HEALTY
 * every "iRefreshTime" milliseconds
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class RefreshHEALTY {

	DatagramSocket socket = null;
	String SoulissNodeIPAddress = "";
	String soulissNodeIPAddressOnLAN = "";
	int iNodes = 0;
	private static Logger logger = LoggerFactory
			.getLogger(RefreshHEALTY.class);

	public RefreshHEALTY(DatagramSocket datagramsocket,
			String soulissNodeIPAddressOnLAN) {
		this.socket = datagramsocket;
		this.soulissNodeIPAddressOnLAN = soulissNodeIPAddressOnLAN;
		logger.info("Start RefreshDBSTRUCTThread");
	}

	public void tick() {
			logger.info("sendHEALTY_REQUESTframe");
			SoulissCommGate.sendHEALTY_REQUESTframe(socket,
					soulissNodeIPAddressOnLAN,
					SoulissNetworkParameter.nodes);
	}
}
