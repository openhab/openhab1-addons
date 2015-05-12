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
 * This class implements the Souliss commmand DBSTRUCT. The thread send DBSTRUCT
 * every "iRefreshTime" milliseconds
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class RefreshDBSTRUCTThread extends Thread {

	int REFRESH_TIME;
	DatagramSocket socket = null;
	String SoulissNodeIPAddress = "";
	String soulissNodeIPAddressOnLAN = "";
	private static Logger LOGGER = LoggerFactory
			.getLogger(RefreshDBSTRUCTThread.class);

	public RefreshDBSTRUCTThread(DatagramSocket datagramsocket,
			String soulissNodeIPAddress, String soulissNodeIPAddressOnLAN,
			int iRefreshTime) {
		// TODO Auto-generated constructor stub
		REFRESH_TIME = iRefreshTime;
		this.socket = datagramsocket;
		this.SoulissNodeIPAddress = soulissNodeIPAddress;
		this.soulissNodeIPAddressOnLAN = soulissNodeIPAddressOnLAN;
		LOGGER.info("Avvio RefreshDBSTRUCTThread");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (true) {
			try {
				LOGGER.info("sendDBStructFrame");
				SoulissCommGate.sendDBStructFrame(socket,
						soulissNodeIPAddressOnLAN);
				Thread.sleep(REFRESH_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
			super.run();
		}
	}

}
