package it.cicolella.openwebnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*****************************************************************
 * SocketTimeoutThread.java                                      *
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
 * Description: Timeout handling during connection to the gateway for sending
 * open commands
 * 
 */
public class SocketTimeoutThread extends Thread {
	private static final Logger logger = LoggerFactory
			.getLogger(SocketTimeoutThread.class);

	String name;
	int time = 0; // thread sleep time (15 or 30 sec)
	int inputState = 0;
	int socketType; // 0 = command socket, 1 = monitor socket

	/**
	 * Constructor
	 * 
	 * @param threadName
	 * @param numSocket
	 *            Tipo di socket che richiama il costruttore, 0 se è socket
	 *            comandi, 1 se è monitor
	 */
	public SocketTimeoutThread(String threadName, int numSocket) {
		name = threadName;
		socketType = numSocket;
		inputState = BTicinoSocketReadManager.getStateMonitor();
		logger.info("Timeout thread activated!"); // FOR DEBUG USE
	}

	/**
	 * Start timeout Thread
	 */
	@Override
	public void run() {
		do {
			time = 30000;
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				logger.info("Timeout thread stopped!");
				// +e.toString()); FOR DEBUG USE
				break;
			}

			logger.error("Thread timeout SCADUTO!");
			// close thread for chars reading
			if (socketType == 0) {
				if (BTicinoSocketWriteManager.readTh != null) {
					BTicinoSocketWriteManager.readTh.interrupt();
				}
			} else {
				if (BTicinoSocketReadManager.readThMon != null) {
					BTicinoSocketReadManager.readThMon.interrupt();
				}
			}
			break;
		} while (true);
	}
}
