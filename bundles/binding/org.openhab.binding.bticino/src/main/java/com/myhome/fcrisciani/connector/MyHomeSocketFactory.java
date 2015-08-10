/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.myhome.fcrisciani.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * The MyHomeSocketFactory is a static class that permits to easily create
 * sockets able to communicate with a MyHome plant
 * 
 * @author Flavio Crisciani
 * @serial 1.0
 * @since 1.7.0
 */
public class MyHomeSocketFactory {
	// ----- TYPES ----- //

	// ---- MEMBERS ---- //

	final static String socketCommand = "*99*0##"; // OpenWebNet command to ask
													// for a command session
	final static String socketMonitor = "*99*1##"; // OpenWebNet command to ask
													// for a monitor session

	// ---- METHODS ---- //

	/**
	 * Reads a well formed message from the input stream passed and return it
	 * back
	 * 
	 * @param inputStream
	 *            steam to read from
	 * @return the message read
	 * @throws IOException
	 *             in case of problem with the input stream, close the stream
	 */
	protected static String readUntilDelimiter(final BufferedReader inputStream)
			throws IOException, SocketTimeoutException {
		StringBuffer response = new StringBuffer();
		int ci = 0;
		char c = ' ';
		Boolean canc = false;

		// Cycle that reads one char each cycle and stop when the sequence ends
		// with ## that is the OpenWebNet delimiter of each message
		do {
			ci = inputStream.read();
			if (ci == -1) {
				System.err.println("Socket already closed by server \n");
				inputStream.close();
				throw new IOException();
			} else {
				c = (char) ci;
				if (c == '#' && canc == false) { // Found first #
					response.append(c);
					canc = true;
				} else if (c == '#') { // Found second # command terminated
										// correctly EXIT
					response.append(c);
					break;
				} else if (c != '#') { // Append char and start again finding
										// the first #
					response.append(c);
					canc = false;
				}
			}
		} while (true);

		return response.toString();
	}

	/**
	 * Reads multiple messages from the input stream and returns them back in an
	 * array
	 * 
	 * @param inputStream
	 *            steam to read from
	 * @return an array of messages
	 * @throws IOException
	 *             in case of problem with the input stream, close the stream
	 */
	protected static String[] readUntilAckNack(final BufferedReader inputStream)
			throws IOException {
		ArrayList<String> result = new ArrayList<String>();
		String commandReceived = null;
		// Call multiple times the previous function to read more messages.
		// A sequence of multiple messages end always with an ACK or NACK so
		// stop this cycle when the message is one of them
		do {
			commandReceived = readUntilDelimiter(inputStream);
			result.add(commandReceived);
		} while (commandReceived != null && isACK(commandReceived) != true
				&& isNACK(commandReceived) != true);

		return result.toArray(new String[result.size()]);
	}

	/**
	 * Is used to select if the response is a positive ACK
	 * 
	 * @param str
	 *            string to be controlled
	 * @return true if the message is an ACK
	 */
	public static Boolean isACK(final String str) {
		return str.contentEquals("*#*1##");
	}

	/**
	 * Is used to select if the response is a negative ACK
	 * 
	 * @param str
	 *            string to be controlled
	 * @return true if the message is an NACK
	 */
	public static Boolean isNACK(final String str) {
		return str.contentEquals("*#*0##");
	}

	/**
	 * Open a command socket with the webserver specified.
	 * 
	 * @param ip
	 *            IP address of the webserver
	 * @param port
	 *            of the webserver
	 * @return the socket ready to be used
	 * @throws IOException
	 *             if there is some problem with the socket opening
	 */
	public static Socket openCommandSession(final String ip, final int port)
			throws IOException {
		Socket sk = new Socket(ip, port);

		BufferedReader inputStream = new BufferedReader(new InputStreamReader(
				sk.getInputStream()));
		PrintWriter outputStream = new PrintWriter(sk.getOutputStream(), true);

		String response = readUntilDelimiter(inputStream);

		outputStream.write(socketCommand);
		outputStream.flush();

		response = readUntilDelimiter(inputStream);

		if (isACK(response) != true) {
			throw new IOException();
		}

		return sk;
	}

	/**
	 * Open a monitor socket with the webserver specified.
	 * 
	 * @param ip
	 *            IP address of the webserver
	 * @param port
	 *            of the webserver
	 * @return the socket ready to be used
	 * @throws IOException
	 *             if there is some problem with the socket opening
	 */
	public static Socket openMonitorSession(final String ip, final int port)
			throws IOException {
		Socket sk = new Socket(ip, port);
		sk.setSoTimeout(45 * 1000);

		BufferedReader inputStream = new BufferedReader(new InputStreamReader(
				sk.getInputStream()));
		PrintWriter outputStream = new PrintWriter(sk.getOutputStream(), true);

		String response = readUntilDelimiter(inputStream);

		outputStream.write(socketMonitor);
		outputStream.flush();

		response = readUntilDelimiter(inputStream);

		if (isACK(response) != true) {
			throw new IOException();
		}

		return sk;
	}

	/**
	 * Close the socket passed
	 * 
	 * @param sk
	 *            socket to be closed
	 * @throws IOException
	 *             if there is some problem with the socket closure
	 */
	public static void disconnect(final Socket sk) throws IOException {
		sk.close();
	}

}
