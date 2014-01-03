package it.cicolella.openwebnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*****************************************************************
 * GestSocketMonitor.java                                        *
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
 * Description: Handle Socket Monitor
 * 
 */
public class BTicinoSocketReadManager {
	private static final Logger logger = LoggerFactory
			.getLogger(BTicinoSocketReadManager.class);

	protected static SocketTimeoutThread timeoutThreadMon = null; // thread for
																	// timeout
	protected static Socket socketMon = null;
	protected static String responseLineMon = null; // received string from
													// webserver
	private static final String socketMonitor = "*99*1##";
	protected static BTicinoReadWebserverThread readThMon = null; // thread for
																	// receiving
																	// chars
																	// from
																	// gateway
	private static int stateMonitor = 0; // socket monitor state
	private BufferedReader inputMon = null;
	private PrintWriter outputMon = null;
	private BTicinoReadThread monThread = null;
	private OpenWebNet freedomSensor = null;

	BTicinoSocketReadManager(OpenWebNet freedomSensor) {
		this.freedomSensor = freedomSensor;
	}

	public static void setStateMonitor(int stateMonitorValue) {
		stateMonitor = stateMonitorValue;
	}

	public static int getStateMonitor() {
		return (stateMonitor);
	}

	public static void setResponseLineMon(String responseLineMonitor) {
		responseLineMon = responseLineMonitor;
	}

	public static String getResponseLineMon() {
		return (responseLineMon);
	}

	/**
	 * Socket monitor
	 * 
	 * @param ip
	 *            - gateway ip
	 * @param port
	 *            - gateway port
	 * @param passwordOpen
	 *            - gateway password open
	 * @return true if connection ok, false if connection ko
	 */
	public boolean connect(String ip, int port, long passwordOpen) {
		try {
			logger.info("Mon: Trying to connect to gateway at " + ip
					+ "  port: " + port);
			// OWNFrame.writeAreaLog(OWNUtilities.getDateTime() +
			// "Mon: Trying to connect to gateway at " + ip + "  port: " +
			// port);
			socketMon = new Socket(ip, port);
			setTimeout(1);
			inputMon = new BufferedReader(new InputStreamReader(
					socketMon.getInputStream()));
			logger.info("Mon: Buffer reader created"); // FOR DEBUG USE
			outputMon = new PrintWriter(socketMon.getOutputStream(), true);
			logger.info("Mon: Print Writer created"); // FOR DEBUG USE
		} catch (IOException e) {
			logger.error("Mon: Impossible connection with host " + ip + "\n"
					+ e.toString());
			this.close();
		}

		if (socketMon != null) {
			while (true) {
				readThMon = null;
				readThMon = new BTicinoReadWebserverThread(socketMon, inputMon,
						1);
				readThMon.start();
				try {
					readThMon.join();
				} catch (InterruptedException e1) {
					logger.info("Mon: ----- ERROR readThread.join() when connecting: "
							+ e1.toString());
				}

				if (responseLineMon != null) {
					if (getStateMonitor() == 0) {
						logger.info("\nMon: ----- STATE 0 ----- ");
						logger.info("Mon: Rx: " + responseLineMon);
						if (responseLineMon.equals(OpenWebNet.MSG_OPEN_ACK)) {
							logger.info("Mon: Tx: " + socketMonitor);
							outputMon.write(socketMonitor); // commands
							outputMon.flush();
							setStateMonitor(1); // set autenthication state
							setTimeout(1);
						} else {
							// if not connected close the socket
							logger.info("Mon: Closing gateway connection ");
							this.close();
							break;
						}
					} else if (getStateMonitor() == 1) {
						logger.info("\nMon: ----- STATE 1 -----");
						logger.info("Mon: Rx: " + responseLineMon);
						if (responseLineMon.equals(OpenWebNet.MSG_OPEN_ACK)) {
							logger.info("Mon: Ack received, stateMonitor = 3");
							setStateMonitor(3);
							logger.info("Mon: Monitor activated");
							// OWNFrame.writeAreaLog(OWNUtilities.getDateTime()
							// + "Mon: Monitor activated");
							break;
						} else {
							logger.error("Mon: Impossible connection!!");
							// if no connected close the socket
							logger.info("Mon: Closing gateway connection " + ip);
							this.close();
							break;
						}
						// }
					} else if (getStateMonitor() == 2) {
						logger.info("\nMon: ----- STATE 2 -----");
						logger.info("Mon: Rx: " + responseLineMon);
						if (responseLineMon.equals(OpenWebNet.MSG_OPEN_ACK)) {
							logger.info("Mon: Monitor activated");
							setStateMonitor(3);
							break;
						} else {
							logger.error("Mon: Impossible starting monitor");
							logger.info("Mon: Closing socket monitor");
							this.close();
							break;
						}
					} else {
						break; // maybe no needed
					}
				} else {
					logger.error("Mon: Gateway response NULL");
					this.close();
					break;
				}
			}// close while(true)
		} else {
		}

		if (getStateMonitor() == 3) {
			monThread = null;
			monThread = new BTicinoReadThread(socketMon, inputMon,
					freedomSensor);
			monThread.start();
		}

		if (getStateMonitor() == 3) {
			return true;
		} else {
			return false;
		}

	}// close connect()

	/**
	 * Close socket monitor and set stateMonitor = 0
	 * 
	 */
	public void close() {
		if (socketMon != null) {
			try {
				socketMon.close();
				socketMon = null;
				setStateMonitor(0);
				logger.info("MON: Socket monitor closed-----\n");// FOR DEBUG
																	// USE
			} catch (IOException e) {
				logger.error("MON: Socket closed with errors: " + e.toString());
			}
		}
	}

	/**
	 * Start thread timeout for receiving reply from WebServer.
	 * 
	 * @param socketType
	 *            : 0 = command, 1 = monitor
	 */
	public void setTimeout(int socketType) {
		timeoutThreadMon = null;
		timeoutThreadMon = new SocketTimeoutThread("timeout", socketType);
		timeoutThreadMon.start();
	}
}