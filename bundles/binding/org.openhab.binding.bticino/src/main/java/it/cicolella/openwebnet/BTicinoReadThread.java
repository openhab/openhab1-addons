package it.cicolella.openwebnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*****************************************************************
 * BTicinoReadThread.java                                        *
 * Original code:			          -              *
 * date          : Sep 8, 2004                                   *
 * copyright     : (C) 2005 by Bticino S.p.A. Erba (CO) - Italy  *
 *                     Embedded Software Development Laboratory  *
 * license       : GPL                                           *
 * email         : 		             		         *
 * web site      : www.bticino.it; www.myhome-bticino.it         *
 *                                                               *
 * Modified and adapted for Freedomotic project by:              *
 * Mauro Cicolella - Enrico Nicoletti                            *
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
 * Description: Thread for receiving messages from socket monitor
 * 
 */
public class BTicinoReadThread extends Thread {
	private static final Logger logger = LoggerFactory
			.getLogger(BTicinoReadThread.class);
	Socket socketMon = null;
	BufferedReader inputMon = null;
	int num = 0;
	int index = 0;
	boolean outcome = false;
	char response[] = null;
	char c = ' ';
	int ci = 0;
	String responseString = null;
	private OpenWebNet freedomSensor = null;

	BTicinoReadThread(Socket sock, BufferedReader inp, OpenWebNet freedomSensor) {
		socketMon = sock;
		inputMon = inp;
		this.freedomSensor = freedomSensor;
	}

	/**
	 * Start Thread for receiving messages from socket monitor
	 */
	@Override
	public void run() {
		do {
			BTicinoSocketReadManager.responseLineMon = null;
			num = 0;
			index = 0;
			outcome = false;
			response = new char[1024];
			c = ' ';
			ci = 0;
			try {
				do {
					if (socketMon != null && !socketMon.isInputShutdown()) {
						ci = inputMon.read();
						if (ci == -1) {
							num = 0;
							index = 0;
							c = ' ';
							logger.info("Socket closed by server");
							socketMon = null;
							BTicinoSocketReadManager.setStateMonitor(0);
							break;
						} else {
							c = (char) ci;
							if (c == '#' && num == 0) {
								response[index] = c;
								num = index;
								index = index + 1;
							} else if (c == '#' && index == num + 1) {
								response[index] = c;
								outcome = true;
								break;
							} else if (c != '#') {
								response[index] = c;
								num = 0;
								index = index + 1;
							}
						}
					} else {
						// System.out.println("&&&&&   socket nulla");
					}
				} while (outcome != true);
			} catch (IOException e) {
				logger.error("Mon exception: " + e.toString(), e);
			}

			if (outcome == true) {
				responseString = new String(response, 0, index + 1);
				BTicinoSocketReadManager.responseLineMon = responseString;
			} else {
				BTicinoSocketReadManager.responseLineMon = null;
				BTicinoSocketReadManager.setStateMonitor(0);
				break;
			}

			logger.info("Mon: " + BTicinoSocketReadManager.responseLineMon);
			// OWNFrame.writeAreaLog(OWNUtilities.getDateTime() + " Mon"
			// + BTicinoSocketReadManager.responseLineMon);

			// builds a freedomotic event starting from the frame
			// if it is a change state frame the corresponding freedomotic event
			// is notified
			freedomSensor
					.buildEventFromFrame(BTicinoSocketReadManager.responseLineMon);
			response = null;
		} while (BTicinoSocketReadManager.getStateMonitor() == 3);
		logger.info("Thread Monitorize ended!");
	}
}
