/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
