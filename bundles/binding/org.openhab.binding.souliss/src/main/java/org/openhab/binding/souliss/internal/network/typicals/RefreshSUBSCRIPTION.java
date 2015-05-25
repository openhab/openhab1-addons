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
 * This class implements the Souliss commmand SUBSCRIPTION. The thread send
 * SUBSCRIPTION every "iRefreshTime" milliseconds
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class RefreshSUBSCRIPTION {
	DatagramSocket socket = null;
	String soulissNodeIPAddressOnLAN = "";
	int iNodes = 0;
	private static Logger logger = LoggerFactory
			.getLogger(RefreshSUBSCRIPTION.class);

	public RefreshSUBSCRIPTION(DatagramSocket datagramsocket,
			String soulissNodeIPAddressOnLAN) {
		this.socket = datagramsocket;
		this.soulissNodeIPAddressOnLAN = soulissNodeIPAddressOnLAN;
		logger.info("Start RefreshSUBSCRIPTIONThread");
	}


	public void tick() {
			logger.debug("sendSUBSCRIPTIONframe");
			SoulissCommGate.sendSUBSCRIPTIONframe(socket,
					soulissNodeIPAddressOnLAN,
					SoulissNetworkParameter.nodes);
	}
}
