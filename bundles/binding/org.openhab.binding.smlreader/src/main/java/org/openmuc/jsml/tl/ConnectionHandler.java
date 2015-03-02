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

import java.net.Socket;

/*
 * The class extends Thread and handles SML connections. Once a connection has been initiated
 * it gives the connection in form of the Connection class to the ConnectionListener and the thread is closed.
 */
public class ConnectionHandler extends Thread {

	private final Socket socket;
	private final ServerThread serverThread;

	ConnectionHandler(Socket socket, ServerThread serverThread) {
		this.socket = socket;
		this.serverThread = serverThread;
	}

	@Override
	public void run() {
		try {
			SML_TConnection tConnection;
			try {
				tConnection = new SML_TConnection(socket, serverThread.messageTimeout,
						serverThread.messageFragmentTimeout);
			} catch (Throwable e) {
				System.err.println("Exception occured when someone tried to connect.");
				return;
			}

			if (serverThread.isAlive() == true) {
				serverThread.connectionIndication(tConnection);
			}

			if (tConnection != null) {
				tConnection.close();
			}

		} finally {
			serverThread.removeHandler(this);
		}
	}
}

// public class ConnectionHandler extends Thread {
//
// private Socket socket;
// private ServerThread serverThread;
//
// ConnectionHandler(Socket socket, ServerThread serverThread) {
// this.socket = socket;
// this.serverThread = serverThread;
// }
//
// @Override
// public void run() {
// try {
// TConnection tConnection = null;
// try {
// tConnection = new TConnection(socket, serverThread.maxTPDUSizeParam,
// serverThread.messageTimeout,
// serverThread.messageFragmentTimeout);
// } catch (Throwable e) {
// System.err.println("Exception occured when someone tried to connect.");
// return;
// }
// try {
// tConnection.listenForCR();
// } catch (Throwable e) {
// System.err
// .println("Exception occured when someone tried to connect. Server was listening for ISO Transport CR packet.");
// tConnection.close();
// return;
// }
// if (serverThread.isAlive() == true) {
// serverThread.connectionIndication(tConnection);
// }
//
// if (tConnection != null) {
// tConnection.close();
// }
//
// } finally {
// serverThread.removeHandler(this);
// }
// }
// }

