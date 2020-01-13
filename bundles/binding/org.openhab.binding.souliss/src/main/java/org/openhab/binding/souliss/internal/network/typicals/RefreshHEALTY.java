/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
	private static Logger logger = LoggerFactory.getLogger(RefreshHEALTY.class);

	public RefreshHEALTY(DatagramSocket datagramsocket,
			String soulissNodeIPAddressOnLAN) {
		this.socket = datagramsocket;
		this.soulissNodeIPAddressOnLAN = soulissNodeIPAddressOnLAN;
		logger.info("Start RefreshDBSTRUCTThread");
	}

	public void tick() {
			logger.debug("sendHEALTY_REQUESTframe");
	SoulissCommGate.sendHEALTY_REQUESTframe(socket,soulissNodeIPAddressOnLAN,SoulissNetworkParameter.nodes);
	}
}
