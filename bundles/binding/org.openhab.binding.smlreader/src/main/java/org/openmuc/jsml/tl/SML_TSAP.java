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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

/**
 * This class implements the SML Transport Layer Service Access Point (TSAP) for the use of SML over TCP/IP. It can be
 * used by a client to create SML_TConnections as well as by a Server to listen for SML_TConnections
 */
public class SML_TSAP {

	private ServerThread serverThread;
	private int localPort = -1;
	private boolean started = false;
	private SML_TConnectionListener conListener;
	private ServerSocketFactory serverSocketFactory = null;
	private SocketFactory socketFactory = null;
	private int maxConnections = 100;
	private int messageTimeout = 0;
	private int messageFragmentTimeout = 2000;

	/**
	 * Use this constructor to create a client SML_TSAP that can be used to start a connection to a remote SML server.
	 */
	public SML_TSAP() {
		socketFactory = SocketFactory.getDefault();
	}

	/**
	 * Use this constructor to create a client SML_TSAP that can be used to start a connection to a remote SML server.
	 * You could pass an SSLSocketFactory to enable SSL.
	 */
	public SML_TSAP(SocketFactory socketFactory) {
		this.socketFactory = socketFactory;
	}

	/**
	 * Use this constructor to create a server TSAP that can listen on a port.
	 * 
	 * @param port
	 *            the TCP-port that the ServerSocket will connect to. Should be between 1 and 65535.
	 * @param conListener
	 *            the ConnectionListener that will be notified when remote TSAPs are connecting or the server stopped
	 *            listening.
	 */
	public SML_TSAP(int port, SML_TConnectionListener conListener) {
		this(port, conListener, ServerSocketFactory.getDefault());
	}

	/**
	 * Use this constructor to create a server TSAP that can listen on a port, with a specified ServerSocketFactory.
	 * 
	 * @param port
	 *            the TCP-port that the ServerSocket will connect to. Should be between 1 and 65535.
	 * @param conListener
	 *            the ConnectionListener that will be notified when remote TSAPs are connecting or the server stopped
	 *            listening.
	 * @param serverSocketFactory
	 *            The ServerSocketFactory to be used to create the ServerSocket
	 */
	public SML_TSAP(int port, SML_TConnectionListener conListener, ServerSocketFactory serverSocketFactory) {
		if (port < 1 || port > 65535) {
			throw new RuntimeException("port number is out of bound");
		}
		localPort = port;
		this.conListener = conListener;
		this.serverSocketFactory = serverSocketFactory;
	}

	/**
	 * Starts a new thread that listens on the configured port. This method is non-blocking.
	 * 
	 * @throws IOException
	 */
	public void startListening() throws IOException {
		if (localPort == -1) {
			throw new IllegalStateException("TSAP was not constructed as Server TSAP");
		}
		started = true;
		serverThread = new ServerThread(this, serverSocketFactory.createServerSocket(localPort), maxConnections,
				messageTimeout, messageFragmentTimeout);
		serverThread.start();
	}

	/**
	 * Stop listing on the port. Stops the server thread.
	 */
	public void stopListening() {
		if (serverThread != null) {
			serverThread.stopServer();
		}
		serverThread = null;
		started = false;
	}

	/**
	 * Set the maximum number of connections that are allowed in parallel by the Server SAP.
	 * 
	 * @param maxConnections
	 *            the number of connections allowed (default is 100)
	 */
	public void setMaxConnections(int maxConnections) {
		if (started == true) {
			throw new RuntimeException("Trying to set parameter although server has started.");
		}
		if (maxConnections < 0) {
			throw new IllegalArgumentException("maxConnections is out of bound");
		}
		this.maxConnections = maxConnections;
	}

	/**
	 * Set the TConnection timeout for waiting for the first byte of a new message. Default is 0 (unlimited)
	 * 
	 * @param messageTimeout
	 *            in milliseconds
	 * @throws SocketException
	 */
	public void setMessageTimeout(int messageTimeout) throws SocketException {
		if (started == true) {
			throw new RuntimeException("Message timeout may not be set while the server SAP ist listening.");
		}
		this.messageTimeout = messageTimeout;
	}

	/**
	 * Set the TConnection timeout for receiving data once the beginning of a message has been received. Default is 2000
	 * (2seconds)
	 * 
	 * @param messageFragmentTimeout
	 *            in milliseconds
	 * @throws SocketException
	 */
	public void setMessageFragmentTimeout(int messageFragmentTimeout) throws SocketException {
		if (started == true) {
			throw new RuntimeException("Message fragment timeout may not be set while the server SAP ist listening.");
		}
		this.messageFragmentTimeout = messageFragmentTimeout;
	}

	protected SML_TConnectionListener getConnectionListener() {
		return conListener;
	}

	/**
	 * Connect to a remote TSAP that is listening at the destination address.
	 * 
	 * @param address
	 *            remote InetAddress
	 * @param port
	 *            remote port
	 * @return the Connection Object
	 * @throws IOException
	 *             is thrown if connection was unsuccessful.
	 */
	public SML_TConnection connectTo(InetAddress address, int port, int timeout) throws IOException {
		started = true;
		Socket socket = socketFactory.createSocket();
		socket.connect(new InetSocketAddress(address, port), timeout);
		SML_TConnection myConnection = new SML_TConnection(socket, messageTimeout, messageFragmentTimeout);

		return myConnection;
	}

}
