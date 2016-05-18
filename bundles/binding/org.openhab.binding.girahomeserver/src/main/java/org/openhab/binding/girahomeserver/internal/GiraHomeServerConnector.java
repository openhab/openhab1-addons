/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.girahomeserver.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;

import org.openhab.core.types.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gira HomeServer connector
 *
 * @author Jochen Mattes
 * @since 1.0.0
 */
public class GiraHomeServerConnector {

    static final Logger logger = LoggerFactory.getLogger(GiraHomeServerConnector.class);

    private static final String TELEGRAM_RAW = "1|%s|%s"; // + '\0';

    private static final int MAX_READ_RETRIES = 5;
    private static final int RECONNECTION_TIMEOUT = 1000;

    /** socket */
    private Socket socket = null;

    /** socket output buffer */
    private PrintWriter dataout = null;

    /** socket input buffer */
    private BufferedReader datain = null;

    /** ip of girahomeserver */
    private String serverIp;

    /** port of girahomeserver */
    private int serverPort;

    /** password of girahomeserver */
    private String serverPassword;

    public GiraHomeServerConnector(String serverIp, int serverPort, String serverPassword) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.serverPassword = serverPassword;
    }

    public void connect() throws UnknownHostException, IOException {
        // connect
        socket = new Socket(serverIp, serverPort);
        dataout = new PrintWriter(socket.getOutputStream(), false);

        datain = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // send password
        dataout.print(serverPassword + '\0');
        dataout.flush();
    }

    /**
     * update the value of a communication object
     *
     * @param param
     * @param value
     * @return
     * @throws IOException
     */
    public void setParam(String communicationObject, Type value) throws IOException {
        String telegram = String.format(TELEGRAM_RAW, encodeCommunicationObjectAddres(communicationObject), value);
        dataout.print(telegram + '\0');
        dataout.flush();
    }

    /**
     * read the value of communication objects from the girahomeserver.
     * Initialize the number of tries with zero
     *
     * @return a array with updates of the communication objects
     * @throws IOException indicate that no data can be read from the girahomeserver
     */
    public HashMap<String, String> getValues() throws IOException {
        return getValues(0);
    }

    /**
     * read the values of communication objects from the girahomeserver
     *
     * @param tries
     * @return
     * @throws IOException
     */
    public HashMap<String, String> getValues(int tries) throws IOException {

        // fetch the data from the socket
        String data;
        try {
            char[] rawData = new char[1024];
            datain.read(rawData);
            data = new String(rawData);
        } catch (IOException e) {
            if (tries > MAX_READ_RETRIES) {
                throw e;
            } else {
                this.reconnect();
                return getValues(tries + 1);
            }
        }

        // Extract the individual values of the communication objects
        // Each of the fields has the format
        // '2|<CO address as int>|<value as text>'
        // They are separated by 0x0 values. The last field is empty.
        HashMap<String, String> values = new HashMap<String, String>();
        String[] elements = data.split("\0");
        if (elements.length == 0) {
            return values;
        }

        // remove the last element (always empty)
        elements = Arrays.copyOfRange(elements, 0, elements.length - 1);

        // translate elements to values
        for (String e : elements) {
            String[] records = e.split("\\|");

            if (records.length == 2) {
                // ignore uninitialized values
                continue;

            } else if (records.length == 3) {
                // decode initialized values
                String address = decodeCommunicationObjectAddress(records[1]);
                String value = records[2];
                values.put(address, value);

            } else {
                logger.warn("Communication object cannot be parsed correctly: {}", e);
                continue;
            }
        }
        return values;
    }

    /**
     * decode the communication object's group address
     *
     * @param encodedAddress
     * @return
     */
    private String decodeCommunicationObjectAddress(String encodedAddress) {
        int addr = Integer.valueOf(encodedAddress);
        int x = addr / 2048;
        int y = (addr - 2048 * x) / 256;
        int z = addr % 256;
        return String.format("%d/%d/%d", x, y, z);
    }

    /**
     * encode the communication object's group address
     *
     * @param address
     * @return
     */
    private int encodeCommunicationObjectAddres(String address) {
        String[] parts = address.split("/");
        return 2048 * Integer.parseInt(parts[0]) + 256 * Integer.parseInt(parts[1]) + Integer.parseInt(parts[2]);
    }

    /**
     * disconnect from girahomserver
     */
    public void disconnect() {
        try {
            datain.close();
        } catch (IOException e) {
            logger.error("can't close datain", e);
        }
        dataout.close();
    }

    /**
     * reconnect to the girahomeserver
     */
    public void reconnect() {

        try {
            this.disconnect();
            Thread.sleep(RECONNECTION_TIMEOUT);
            this.connect();
        } catch (InterruptedException e) {
            // do nothing
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
