/**
 * Copyright (c) 2010-2017, openHAB.org and others.
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
    protected static String[] readUntilAckNack(final BufferedReader inputStream) throws IOException {
        ArrayList<String> result = new ArrayList<String>();
        String commandReceived = null;
        // Call multiple times the previous function to read more messages.
        // A sequence of multiple messages end always with an ACK or NACK so
        // stop this cycle when the message is one of them
        do {
            commandReceived = readUntilDelimiter(inputStream);
            result.add(commandReceived);
        } while (commandReceived != null && isACK(commandReceived) != true && isNACK(commandReceived) != true);

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
     * Encodes the password for OpenWebNet.
     *
     * @param pass
     *            password to encode
     * @param nonce
     *            encoding key received from the gateway
     * @return The encoded password
     */
    public static String calcPass(final String pass, final String nonce) {
        boolean flag = true;
        int num1 = 0x0;
        int num2 = 0x0;
        int password = Integer.parseInt(pass, 10);

        for (int x = 0; x < nonce.length(); x++) {
            char c = nonce.charAt(x);
            if (c != '0') {
                if (flag) {
                    num2 = password;
                }
                flag = false;
            }
            switch (c) {
                case '1':
                    num1 = num2 & 0xFFFFFF80;
                    num1 = num1 >>> 7;
                    num2 = num2 << 25;
                    num1 = num1 + num2;
                    break;
                case '2':
                    num1 = num2 & 0xFFFFFFF0;
                    num1 = num1 >>> 4;
                    num2 = num2 << 28;
                    num1 = num1 + num2;
                    break;
                case '3':
                    num1 = num2 & 0xFFFFFFF8;
                    num1 = num1 >>> 3;
                    num2 = num2 << 29;
                    num1 = num1 + num2;
                    break;
                case '4':
                    num1 = num2 << 1;
                    num2 = num2 >>> 31;
                    num1 = num1 + num2;
                    break;
                case '5':
                    num1 = num2 << 5;
                    num2 = num2 >>> 27;
                    num1 = num1 + num2;
                    break;
                case '6':
                    num1 = num2 << 12;
                    num2 = num2 >>> 20;
                    num1 = num1 + num2;
                    break;
                case '7':
                    num1 = num2 & 0x0000FF00;
                    num1 = num1 + ((num2 & 0x000000FF) << 24);
                    num1 = num1 + ((num2 & 0x00FF0000) >>> 16);
                    num2 = (num2 & 0xFF000000) >>> 8;
                    num1 = num1 + num2;
                    break;
                case '8':
                    num1 = num2 & 0x0000FFFF;
                    num1 = num1 << 16;
                    num1 = num1 + (num2 >>> 24);
                    num2 = num2 & 0x00FF0000;
                    num2 = num2 >>> 8;
                    num1 = num1 + num2;
                    break;
                case '9':
                    num1 = ~num2;
                    break;
                case '0':
                    num1 = num2;
                    break;
            }
            num2 = num1;
        }
        return Integer.toUnsignedString(num1 >>> 0);
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
    public static Socket openCommandSession(final String ip, final int port) throws IOException {
        return openCommandSession(ip, port, "");
    }

    /**
     * Open a command socket with the webserver specified.
     *
     * @param ip
     *            IP address of the webserver
     * @param port
     *            of the webserver
     *
     * @param passwd
     *            of the webserver
     *
     * @return the socket ready to be used
     * @throws IOException
     *             if there is some problem with the socket opening
     */
    public static Socket openCommandSession(final String ip, final int port, final String passwd) throws IOException {
        Socket sk = new Socket(ip, port);

        BufferedReader inputStream = new BufferedReader(new InputStreamReader(sk.getInputStream()));
        PrintWriter outputStream = new PrintWriter(sk.getOutputStream(), true);

        String response = readUntilDelimiter(inputStream);

        outputStream.write(socketCommand);
        outputStream.flush();

        response = readUntilDelimiter(inputStream);

        // If isAck is true, the gateway is configured without password and the function return immediately the socket
        if (!isACK(response)) {

            // If isAck is false: it checks for passwd request
            String nonce = response.substring(2, response.length() - 2);
            String p = calcPass(passwd, nonce);
            outputStream.write("*#" + p + "##");
            outputStream.flush();

            response = readUntilDelimiter(inputStream);

            if (!isACK(response)) {
                throw new IOException("Invalid gateway password");
            }
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
    public static Socket openMonitorSession(final String ip, final int port) throws IOException {
        return openMonitorSession(ip, port, "");
    }

    /**
     * Open a monitor socket with the webserver specified.
     *
     * @param ip
     *            IP address of the webserver
     * @param port
     *            of the webserver
     * @param passwd
     *            of the webserver
     * @return the socket ready to be used
     * @throws IOException
     *             if there is some problem with the socket opening
     */
    public static Socket openMonitorSession(final String ip, final int port, final String passwd) throws IOException {
        Socket sk = new Socket(ip, port);
        sk.setSoTimeout(45 * 1000);

        BufferedReader inputStream = new BufferedReader(new InputStreamReader(sk.getInputStream()));
        PrintWriter outputStream = new PrintWriter(sk.getOutputStream(), true);

        String response = readUntilDelimiter(inputStream);

        outputStream.write(socketMonitor);
        outputStream.flush();

        response = readUntilDelimiter(inputStream);

        if (!isACK(response)) {
            // check for passwd request
            String nonce = response.substring(2, response.length() - 2);
            String p = calcPass(passwd, nonce);
            outputStream.write("*#" + p + "##");
            outputStream.flush();

            response = readUntilDelimiter(inputStream);

            if (!isACK(response)) {
                throw new IOException("Invalid gateway password");
            }
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
