/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.connectors;

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
import org.openmuc.jsml.tl.SMLMessageExtractor;

/**
 * Represents a serial SML device connector.
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
final public class SerialConnector extends ConnectorBase {
	SerialPort serialPort;
	InputStream inputStream;
	DataInputStream is;

	/**
	* The name of the port where the device is connected as defined in openHAB configuration.
	*/
	private String portName;

	/**
	* Contructor to create a serial connector instance.
	*
	* @param portName the port where the device is connected as defined in openHAB configuration.
	*/
	public SerialConnector(String portName) {
		super();
		this.portName = portName;
	}
	
	/** 
	* @throws ConnectorException 
	 * @{inheritDoc} 
	*/ 
	@Override
	protected SML_File getMeterValuesInternal() {
		SML_File smlFile = null;
		
		SMLMessageExtractor extractor;
		
		try {
			extractor = new SMLMessageExtractor(is, 3000);
			DataInputStream is = new DataInputStream(new ByteArrayInputStream(extractor.getSmlMessage()));

			smlFile = new SML_File();
			
			while (is.available() > 0) {
				SML_Message message = new SML_Message();

				if (!message.decode(is)) {
					throw new IOException("Could not decode message");
				}
				else {
					smlFile.add(message);
				}
			}
		} catch (IOException e) {
			logger.error("Error at SerialConnector.closeConnection: {}", e.getMessage());
		}		
		
		return smlFile;
	}

	/** 
	* @{inheritDoc} 
	*/ 
	@Override
	protected void openConnection() {
		CommPortIdentifier portId = getCommPortIdentifier();
		
		if(portId != null){
			try {
				serialPort = (SerialPort) portId.open("SmlReaderBinding", 2000);
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
				serialPort.notifyOnDataAvailable(true);
				is = new DataInputStream(new BufferedInputStream(serialPort.getInputStream()));
			} catch (PortInUseException e) {
				logger.error("Error at SerialConnector.openConnection: port {} is already in use.", this.portName);
			} catch (UnsupportedCommOperationException e) {
				logger.error("Error at SerialConnector.openConnection: params for port {} are not supported.", this.portName);
			} catch (IOException e) {
				logger.error("Error at SerialConnector.openConnection: unable to get inputstream for port {}.", this.portName);
			}
		}
	}

	/** 
	* @{inheritDoc} 
	*/ 
	@Override
	protected void closeConnection() {
		try {
			is.close();
			serialPort.close();
			
		} catch (Exception e) {
			logger.error("Error at SerialConnector.closeConnection: {}", e.toString());
		}
	}
	
	/**
	 * Searches and returns the specified port identifier.
	 * */
	private CommPortIdentifier getCommPortIdentifier(){
		CommPortIdentifier commPort = null;
		Enumeration<?> portList;

		portList = CommPortIdentifier.getPortIdentifiers();
		
		while (portList.hasMoreElements()) {
			commPort = (CommPortIdentifier) portList.nextElement();
			if (commPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (commPort.getName().equals(this.portName)) {
					break;
				}
			}
		}
		
		return commPort;
	}
}
