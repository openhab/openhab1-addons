/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.megadevice.internal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is for bringing up incoming connection
 * 
 * @author Petr Shatsillo
 * @since 1.9.0
 */
public class MegadeviceHttpServer extends Thread {
	private static Logger logger = LoggerFactory.getLogger(MegadeviceHttpServer.class);
	private static int portnumber = 8989;
	public static boolean isRunning = true;
	private static ServerSocket ss = null;

	public void run() {

		logger.info("Starting MegaHttpServer at {} port", portnumber);

		try {
			ss = new ServerSocket(portnumber);
		} catch (IOException e) {
			logger.error("ERROR -> {}. Can't open socket ", e.getMessage());
		}
		while (isRunning) {
			Socket s = null;
			try {
				s = ss != null ? ss.accept() : null;
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			if (!ss.isClosed()) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
				new Thread(new MegaDeviceHttpSocket(s)).start();
			}
		}
	}

	public static void setPort(int port) {
		portnumber = port;
	}

	public static void setRunningState(boolean isRun) {
		isRunning = isRun;
		try {
			logger.debug("closing port");
			ss.close();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	public static boolean getRunningState() {
		return isRunning;
	}
}
