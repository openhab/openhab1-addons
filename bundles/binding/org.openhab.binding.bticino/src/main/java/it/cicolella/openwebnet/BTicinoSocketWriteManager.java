package it.cicolella.openwebnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*****************************************************************
 * BTicinoSocketWriteManager.java                                *
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
 * Description: Handle of socket commands, open/close connection, command
 * sending
 * 
 */
public class BTicinoSocketWriteManager {
	private static final Logger logger = LoggerFactory
			.getLogger(BTicinoSocketWriteManager.class);
	/*
	 * state 0 = not connected state 1 = request sent, waiting for reply state 2
	 * = waiting for ack or nack. If ack chenge to state 3 state 3 = connected
	 */
	static BTicinoReadWebserverThread readTh = null; // thread for receiving
														// chars from gateway
	static SocketTimeoutThread timeoutThread = null; // timeout thread
	static private int socketCommandState = 0; // socket command state
	static String responseLine = null; // receiving string from gateway
	static final String SOCKET_COMMAND = "*99*0##";
	static final String SOCKET_SUPER_COMMAND = "*99*9##";
	Socket socket = null;
	BufferedReader input = null;
	PrintWriter output = null;

	/**
	 * Costructor
	 * 
	 */
	public BTicinoSocketWriteManager() {
	}

	public static void setSocketCommandState(int socketCommandStateValue) {
		socketCommandState = socketCommandStateValue;
	}

	public static int getSocketCommandState() {
		return (socketCommandState);
	}

	/**
	 * state 0 = not connected state 1 = request sent on socket command, waiting
	 * for reply state 2 = waiting for ack or nack. If ack set state to 3 state
	 * 3 = connected
	 * 
	 * @param ip
	 *            gateway
	 * @param port
	 *            gateway
	 * @param passwordOpen
	 *            password open gateway set to
	 * @return true if connection OK, false if connection failed
	 */
	public boolean connect(String ip, int port, long passwordOpen) {
		try {
			logger.info("Trying to connect to ethernet gateway on address "
					+ ip + ':' + port);
			socket = new Socket(ip, port);
			setTimeout(0);
			input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			logger.info("Buffer reader created"); // FOR DEBUG USE
			output = new PrintWriter(socket.getOutputStream(), true);
			logger.info("Print Writer created"); // FOR DEBUG USE
		} catch (IOException e) {
			logger.error("Connection impossible! " + e.toString());
			this.close();
		}
		if (socket != null) {
			while (true) {
				readTh = null;
				readTh = new BTicinoReadWebserverThread(socket, input, 0);
				readTh.start();
				try {
					readTh.join();
				} catch (InterruptedException e1) {
					logger.error("----- ERROR readThread.join() during connection: "
							+ e1.toString());
				}

				if (responseLine != null) {
					if (getSocketCommandState() == 0) { // sent request for
														// connection
						logger.info("----- STATE 0 NOT CONNECTED ----- ");
						logger.info("Rx: " + responseLine);
						if (responseLine.equals(OpenWebNet.MSG_OPEN_ACK)) {
							logger.info("Tx: " + SOCKET_COMMAND);
							output.write(SOCKET_COMMAND); // commands
							output.flush();
							setSocketCommandState(1); // waiting for reply
							setTimeout(0);
						} else {
							// if no connection close the socket
							logger.info("Closing socket to server " + ip);
							this.close();
							break;
						}
					} else if (getSocketCommandState() == 1) { // sent type
																// service
																// request
						logger.info("----- STATE 1 -----");
						logger.info("Rx: " + responseLine);
						if (responseLine.equals(OpenWebNet.MSG_OPEN_ACK)) {
							logger.info("Ack received, state = 3");
							setSocketCommandState(3);
							break;
						} else {
							logger.error("Connection impossible!");
							// if no connection close socket
							logger.error("Closing server socket " + ip);
							this.close();
							break;
						}
						// }
					} else if (getSocketCommandState() == 2) {
						logger.info("----- STATE 2 -----");
						logger.info("Rx: " + responseLine);
						if (responseLine.equals(OpenWebNet.MSG_OPEN_ACK)) {
							logger.info("Connection OK");
							setSocketCommandState(3);
							break;
						} else {
							logger.error("Connection impossible!");
							logger.error("Closing server socket " + ip);
							this.close();
							break;
						}
					} else {
						break;
					}
				} else {
					logger.error("--- NULL server response ---");
					this.close();
					break;// else of if(responseLine != null)
				}
			} // close while(true)
		} else {
		}
		if (getSocketCommandState() == 3) {
			return true;
		} else {
			return false;
		}
	}// close connect()

	/**
	 * Close commands socket and set state = 0
	 * 
	 */
	public void close() {
		if (socket != null) {
			try {
				socket.close();
				socket = null;
				// socketCommandState = 0;
				setSocketCommandState(0);
				logger.info("-----Socket closed correctly-----"); // FOR DEBUG
																	// USE
			} catch (IOException e) {
				logger.error("Socket error: " + e.toString());
			}
		}
	}

	/**
	 * Method for sending own command
	 * 
	 * @param commandOpen
	 * @return 0 command sent, 1 command not sent
	 */
	public int send(String commandOpen) {
		logger.info("Sending frame: " + commandOpen + " to the gateway");
		output.write(commandOpen);
		output.flush();
		do {
			setTimeout(0);
			readTh = null;
			readTh = new BTicinoReadWebserverThread(socket, input, 0);
			readTh.start();
			try {
				readTh.join();
			} catch (InterruptedException e1) {
				logger.error("----- ERROR readThread.join() in sending command: "
						+ e1.toString());
			}
			if (responseLine != null) {
				if (responseLine.equals(OpenWebNet.MSG_OPEN_ACK)) {
					logger.info("Rx: " + responseLine);
					logger.info("Command sent");// FOR DEBUG USE
					this.close();
					return 0;
					// break;
				} else if (responseLine.equals(OpenWebNet.MSG_OPEN_NACK)) {
					logger.info("Rx: " + responseLine);
					logger.error("Command NOT sent");
					this.close();
					return 1;
					// break;
				} else {
					// STATE REQUEST
					logger.info("Rx: " + responseLine);
					if (responseLine == OpenWebNet.MSG_OPEN_ACK) {
						logger.info("Command sent");// FOR DEBUG USE
						this.close();
						return 0;
						// break;
					} else if (responseLine == OpenWebNet.MSG_OPEN_NACK) {
						logger.error("Command NOT sent");
						return 1;
					}
				}
			} else {
				logger.error("Impossible sending command " + responseLine);
				this.close();
				return 1;
			}
		} while (true);
	}// close send(...)

	/**
	 * Timeour thread for webserver reply
	 * 
	 * @param typeSocket
	 *            : 0 = command socket, 1 = monitor socket
	 */
	public void setTimeout(int typeSocket) {
		timeoutThread = null;
		timeoutThread = new SocketTimeoutThread("timeout", typeSocket);
		timeoutThread.start();
	}
}// close class

