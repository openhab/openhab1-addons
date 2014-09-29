/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.novelanheatpump.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * With the heatpump connector the internal state of a  Novelan (Siemens) Heatpump can be read. 
 * 
 * @author Jan-Philipp Bolle
 * @since 1.0.0
 */
public class HeatpumpConnector {
	
	static final Logger logger = LoggerFactory.getLogger(HeatpumpConnector.class);
	
	private DataInputStream datain = null;
	private DataOutputStream dataout = null;
	private String serverIp = "";
	private int serverPort = 8888;
	
	
	public HeatpumpConnector(String serverIp){
		this.serverIp = serverIp;
	}

	/**
	 * connects to the heatpump via network
	 * 
	 * @throws UnknownHostException indicate that the IP address of a host could not be determined.
	 * @throws IOException indicate that no data can be read from the heatpump
	 */
	public void connect() throws UnknownHostException, IOException {
		Socket sock = new Socket(serverIp, serverPort);
		
		InputStream in = sock.getInputStream();
		OutputStream out = sock.getOutputStream();
		datain = new DataInputStream(in);
		dataout = new DataOutputStream(out);
		logger.debug("Novelan Heatpump connect");
	}

	/**
	 * read the internal state of the heatpump
	 * @return a array with all internal data of the heatpump
	 * @throws IOException indicate that no data can be read from the heatpump
	 */
	public int[] getValues() throws IOException {
		int[] heatpump_values = null;
		while (datain.available() > 0)
			datain.readByte();
		dataout.writeInt(3004);
		dataout.writeInt(0);
		dataout.flush();
		if (datain.readInt() != 3004) {
			return null;
		}
		int stat = datain.readInt();
		int arraylength = datain.readInt();
		heatpump_values = new int[arraylength];

		
		for (int i = 0; i < arraylength; i++)
			heatpump_values[i] = datain.readInt();
		return heatpump_values;
	}

	/**
	 * disconnect from heatpump
	 */
	public void disconnect() {
		try {
			datain.close();
		} catch (IOException e) {
			logger.error("can't close datain",e);
		}
		try {
			dataout.close();
		} catch (IOException e) {
			logger.error("can't close dataout",e);
		}
	}

}
