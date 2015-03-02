/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.devices;

import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

import org.openmuc.jsml.structures.SML_File;
import org.openmuc.jsml.tl.SML_SerialReceiver;

/**
 * Represents a configured - SML capable device with a serial connection.
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
final public class SerialDevice extends SmlDeviceBase {	
	private SML_SerialReceiver receiver;
	private String serialPort;

	/**
	 * Constructs a SmlDevice Object.
	 * @param id of the SmlDevice from openHAB configuration.
	 * @param serialPort on which the device connection is established.
	 */
	public SerialDevice(String id, String serialPort){
		super(id);
		this.receiver = new SML_SerialReceiver();
		this.serialPort =  serialPort;
	}
	
	/**
	* Open the serial connection.
	* @throws IOException	
	* @throws PortInUseException	
	* @throws UnsupportedCommOperationException	
	* @throws UnsupportedOperationException	
	*/
	private void openSerialPort() throws IOException, PortInUseException, UnsupportedCommOperationException, UnsupportedOperationException {
		if(this.receiver == null){
			throw new UnsupportedOperationException("'" + this.getDeviceId() + "': the serial receiver isn't initialized!");
		}

		this.receiver.setupComPort(this.serialPort);
	}	
	
	/**
	* Close the serial connection
	*/
	private void closeSerialPort(){
		if(this.receiver != null){
			try {
				this.receiver.close();
			} catch (IOException e) {
				logger.error("Can't close the serial connection of " + this.toString() + " due to exception!");
				logger.error(e.getMessage());
			}
		}
	}

	/**
	 * Gets the configured serial port.
	 * @return port of the SmlDevice from openHAB configuration.
	 */
	private String getPort(){
		return this.serialPort;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public String toString(){
		return "Device-Id: '" + this.getDeviceId() + "' Port: '" + this.getPort() + "'";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected boolean openConnection() {
		boolean connectionEstablished = true;
		
		try{
			openSerialPort();
		}catch (IOException | PortInUseException | UnsupportedCommOperationException | UnsupportedOperationException e) {
			logger.error("Can't open the serial connection for : " + this.toString() + "!");
			connectionEstablished = false;
		}
		
		return connectionEstablished;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void closeConnection() {
		closeSerialPort();
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected SML_File getSmlFile() {
		SML_File smlFile = null;
		
		if(receiver != null){
			try {
				smlFile = receiver.getSMLFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return smlFile;
	}
}