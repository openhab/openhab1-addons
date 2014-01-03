package it.cicolella.openwebnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*****************************************************************
 * BTicinoReadWebServerThread.java                               *
 * Original code:			          -              *
 * date          : Sep 8, 2004                                   *
 * copyright     : (C) 2005 by Bticino S.p.A. Erba (CO) - Italy  *
 *                     Embedded Software Development Laboratory  *
 * license       : GPL                                           *
 * email         : 		             		         *
 * web site      : www.bticino.it; www.myhome-bticino.it         *
 *                                                               *
 * Modified and adapted for Freedomotic project by:              *
 * Mauro Cicolella - Enrico Nicoletti                            *            *
 * date          : 24/11/2011                                    *
 * web site      : www.freedomotic.com                           *
 *****************************************************************/
/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/

/**
 * Description: Manage by thread received string from WebServer
 * 
 */
public class BTicinoReadWebserverThread extends Thread {
	private static final Logger logger = LoggerFactory
			.getLogger(BTicinoReadWebserverThread.class);

	Socket socket = null;
	BufferedReader input = null;
	int typeSocket;
	int num = 0;
	int index = 0;
	boolean result = false;
	char response[];
	char c = ' ';
	int ci = 0;
	String responseString = null;

	/**
	 * Constructor
	 * 
	 * @param sock
	 *            Socket to analyze
	 * @param inp
	 *            Character-input stream
	 * @param numSocket
	 *            Socket type, 0=command, 1=monitor
	 */
	public BTicinoReadWebserverThread(Socket sock, BufferedReader inp,
			int numSocket) {
		socket = sock;
		input = inp;
		typeSocket = numSocket;
	}

	/**
	 * Start Thread for receiveng string from WebServer
	 */
	@Override
	public void run() {
		if (typeSocket == 0) {
			BTicinoSocketWriteManager.responseLine = null;
		} else {
			BTicinoSocketReadManager.responseLineMon = null;
		}
		num = 0;
		index = 0;
		result = false;
		response = new char[1024];
		c = ' ';
		ci = 0;
		try {
			do {
				if (socket != null && !socket.isInputShutdown()) {
					ci = input.read();
					if (ci == -1) {
						num = 0;
						index = 0;
						c = ' ';
						// Freedomotic.logger.severe("Socket closed by server");
						socket = null;
						if (typeSocket == 0) {
							BTicinoSocketWriteManager.setSocketCommandState(0);
							OpenWebNet.gestSocketCommands.socket = null;
						} else {
							BTicinoSocketReadManager.setStateMonitor(0);
							BTicinoSocketReadManager.socketMon = null;
						}
						break;
					} else {
						c = (char) ci;
						if (c == '#' && num == 0) {
							response[index] = c;
							num = index;
							index = index + 1;
						} else if (c == '#' && index == num + 1) {
							response[index] = c;
							result = true;
							break;
						} else if (c != '#') {
							response[index] = c;
							num = 0;
							index = index + 1;
						} else {
							// Freedomotic.logger.severe("-----ERROR-----");
						}
					}
				} else {
				}
			} while (true); // in this cycle until result=true or webserver
							// closes the socket;
			// escape when timeout and thread interrupted
		} catch (IOException e) {
			logger.error("Exception <ReadThread>: ");
			e.printStackTrace();
		}

		if (result == true) {
			responseString = new String(response, 0, index + 1);
			if (typeSocket == 0) {
				BTicinoSocketWriteManager.responseLine = responseString;
			} else {
				BTicinoSocketReadManager.responseLineMon = responseString;
			}
		} else {
			if (typeSocket == 0) {
				BTicinoSocketWriteManager.responseLine = null;
			} else {
				BTicinoSocketReadManager.responseLineMon = null;
			}
		}

		if (typeSocket == 0) {
			if (BTicinoSocketWriteManager.timeoutThread != null) {
				BTicinoSocketWriteManager.timeoutThread.interrupt();
			}
		} else {
			if (BTicinoSocketReadManager.timeoutThreadMon != null) {
				BTicinoSocketReadManager.timeoutThreadMon.interrupt();
			}
		}

		logger.info("Thread string receiving stopped"); // FOR DEBUG USE
		socket = null;
		input = null;
		response = null;
	}
}
