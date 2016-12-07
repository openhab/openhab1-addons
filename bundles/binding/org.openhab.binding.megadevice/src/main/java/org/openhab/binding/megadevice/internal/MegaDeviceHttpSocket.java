/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.megadevice.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for incoming connections from megadevices
 * 
 * @author Petr Shatsillo
 * @since 1.9.0
 */

public class MegaDeviceHttpSocket extends Thread {

	private final Socket s;
	private InputStream is;
	private OutputStream os;
	private static Logger logger = LoggerFactory.getLogger(MegaDeviceHttpSocket.class);

	public MegaDeviceHttpSocket(Socket s) {
		this.s = s;
		try {
			this.is = s.getInputStream();
			this.os = s.getOutputStream();
		} catch (IOException e) {
			logger.error("ERROR: {}. Unable to start socket. ", e.getMessage());
		}
	}

	@Override
	public void run() {
		readInput();
		writeResponse();
	}

	private void readInput() {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while (true) {
			String s;
			String[] getCommands;
			try {
				s = br.readLine();
				if (s == null || s.trim().length() == 0) {
					break;
				}
				if (s.contains("GET") && s.contains("?")) {
					logger.debug("{} {} ", this.s.getInetAddress().getHostAddress(), s);
					String[] CommandParse = s.split("[/ ]");
					String command = CommandParse[2];
					getCommands = command.split("[?&>=]");
					if (s.contains("m=1")) {
						MegaDeviceBinding.updateValues(this.s.getInetAddress().getHostAddress(), getCommands,
								OnOffType.OFF);
					} else if (s.contains("m=2")) {
						// do nothing -- long pressed
					} else {
						MegaDeviceBinding.updateValues(this.s.getInetAddress().getHostAddress(), getCommands,
								OnOffType.ON);
					}
					break;
				} else
					break;
			} catch (IOException e) {
				logger.error("ERROR: MegaDevice input server error. Restart http socket");
				logger.error(e.getLocalizedMessage());
				MegadeviceHttpServer.setRunningState(false);
			}
		}
	}

	private void writeResponse() {
		String result = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + 0 + "\r\n"
				+ "Connection: close\r\n\r\n";
		try {
			os.write(result.getBytes());
			os.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
	}
}
