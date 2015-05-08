/*
 * Copyright 2009-14 Fraunhofer ISE
 *
 * This file is part of jSML.
 * For more information visit http://www.openmuc.org
 *
 * jSML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jSML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSML.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jsml.tl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class extends Thread. It is started by SML_TSAP and listens on a socket for connections and hands them to the
 * ConnectionHandler class. It notifies ConnectionListener if the socket is closed.
 * 
 * @author Stefan Feuerhahn
 * 
 */
class ServerThread extends Thread {

	private final ServerSocket serverSocket;
	private int numConnections = 0;
	private final SML_TSAP tSAP;
	protected int maxTPDUSizeParam;
	private final int maxConnections;
	protected int messageTimeout;
	protected int messageFragmentTimeout;

	protected ServerThread(SML_TSAP tSAP, ServerSocket socket, int maxConnections, int messageTimeout,
			int messageFragmentTimeout) {
		this.tSAP = tSAP;
		serverSocket = socket;
		maxTPDUSizeParam = maxTPDUSizeParam;
		this.maxConnections = maxConnections;
		this.messageTimeout = messageTimeout;
		this.messageFragmentTimeout = messageFragmentTimeout;
	}

	@Override
	public void run() {

		Socket clientSocket = null;

		while (true) {
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				tSAP.getConnectionListener().serverStoppedListeningIndication(e);

				return;
			}

			if (numConnections < maxConnections) {
				numConnections++;
				ConnectionHandler myConnectionHandler = new ConnectionHandler(clientSocket, this);
				myConnectionHandler.start();
			}
			else {
				System.err
						.println("Transport Layer Server: Maximum number of connections reached. Ignoring connection request.");
			}

		}

	}

	protected void connectionIndication(SML_TConnection tConnection) {
		tSAP.getConnectionListener().connectionIndication(tConnection);
	}

	protected void removeHandler(ConnectionHandler handler) {
		numConnections--;
	}

	/**
	 * stops listening on the port but does not close all connections
	 */
	public void stopServer() {
		if (serverSocket != null && serverSocket.isBound()) {
			try {
				serverSocket.close();
			} catch (IOException e) {
			}
		}
	}

}
