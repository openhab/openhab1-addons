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

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.openmuc.jsml.structures.SML_File;
import org.openmuc.jsml.structures.SML_Message;

/**
 * This class can be used to read SML Messages over a serial interface
 * 
 */
public class SML_SerialReceiver extends Thread {

	SerialPort serialPort;
	InputStream inputStream;
	DataInputStream is;

	public void setupComPort(String serialPortName) throws IOException, PortInUseException,
			UnsupportedCommOperationException {

		boolean portFound = false;
		Enumeration<?> portList;
		CommPortIdentifier portId = null;

		// parse ports and if the default port is found, initialized the reader
		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(serialPortName)) {
					portFound = true;
					break;
				}
			}

		}

		if (!portFound) {
			throw new IOException("Port not found: " + serialPortName + '!');
		}

		/* initalize serial port */
		serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);

		/* set port parameters */
		serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);

		serialPort.notifyOnDataAvailable(true);

		is = new DataInputStream(new BufferedInputStream(serialPort.getInputStream()));

	}

	public SML_File getSMLFile() throws IOException {
		SMLMessageExtractor extractor = new SMLMessageExtractor(is, 3000);
		return handleSMLStream(extractor.getSmlMessage());
	}

	SML_File handleSMLStream(byte[] smlPacket) throws IOException {
		DataInputStream is = new DataInputStream(new ByteArrayInputStream(smlPacket));

		SML_File smlFile = new SML_File();
		;

		while (is.available() > 0) {
			SML_Message message = new SML_Message();

			if (!message.decode(is)) {
				throw new IOException("Could not decode message");
			}
			else {

				smlFile.add(message);
			}

		}
		return smlFile;
	}

	public void close() throws IOException {
		is.close();
		serialPort.close();
	}
}
