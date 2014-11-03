/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.serial.internal;


import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator
 * 
 * @author Johannes Engelke <info@johannes-engelke.de>
 * @since 1.6.0
 */
public class SerialActionHandler {
	
	private final static Logger log = LoggerFactory.getLogger(SerialActionHandler.class);
	
	private String deviceName;
	private Integer baudRate = 9600;
	private Integer dataBits = SerialPort.DATABITS_8;
	private Integer stopBits = SerialPort.STOPBITS_1;
	private Integer parityMode = SerialPort.PARITY_EVEN;
	
	private SerialPort serialPort;
	private OutputStream os;
	private BufferedWriter bw;
	
	private Boolean lock = false;
	
	public SerialActionHandler(String deviceName) {
		this.deviceName = deviceName; 
	}
	
	public SerialActionHandler(String key, String value) {
		setProperty(key, value);
	}

	public String getDeviceName() {
		return deviceName;
	}
	
	public void setDeviceName(String deviceName){
		if(this.deviceName != deviceName){
			this.deviceName = deviceName;
			this.closeHardware();
		}
	}
	
	public void setProperty(String key, String value){
		
		if(key == "device"){
			this.setDeviceName(value);
		} else if (key == "baudrate") {
			this.setBaudRate(Integer.parseInt(value));
		} else if (key == "databits") {
			this.setDataBits(Integer.parseInt(value));
		} else if (key == "parity") {
			this.setParityMode(Integer.parseInt(value));
		} else if (key == "stopbits") {
			this.setStopBits(Integer.parseInt(value));
		} 
	}
	
	public void writeMessage(String message){
		log.debug("Sending raw message to serial Port (" + deviceName + "): " + message);
		
		if(bw == null){
			log.error("Can't write message, BufferedWrite is NULL");
		}
		synchronized (bw) {
			try{
				bw.write(message);
				bw.flush();
			} catch (IOException e){
				log.error("Can't write to serial port (" + deviceName + ")", e);
			}
		}
	}
	
	public void openHardware() throws SerialActionException {
		if(serialPort == null){
			log.debug("Open serial port " + deviceName);
			try {
				CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(deviceName);
				if(portIdentifier.isCurrentlyOwned()){
					throw new SerialActionException("The port " + deviceName + " is currenty used by "
							+ portIdentifier.getCurrentOwner());
				}
				
				CommPort port = portIdentifier.open(this.getClass().getName(), 2000);
				if(!(port instanceof SerialPort)){
					throw new SerialActionException("The device " + deviceName + " is not a serial port");
				}
				
				serialPort = (SerialPort) port;
				serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parityMode);
				
				os = serialPort.getOutputStream();
				bw = new BufferedWriter(new OutputStreamWriter(os));
	
			} catch (NoSuchPortException e) {
				throw new SerialActionException(e);
			} catch (PortInUseException e) {
				throw new SerialActionException(e);
			} catch (UnsupportedCommOperationException e) {
				throw new SerialActionException(e);
			} catch (IOException e) {
				throw new SerialActionException(e);
			}
		}
	}
	
	public void closeHardware() {
		log.debug("Closing serial device " + deviceName);
		
		try {
			if(bw != null) {
				bw.close();
			}
		} catch (IOException e) {
			log.error("Can't close output stream", e);
		} finally {
			if(serialPort != null){
				serialPort.close();
			}
		}
	}

	public Integer getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(Integer baudRate) {
		if(baudRate != this.baudRate){
			this.baudRate = baudRate;
			this.closeHardware();
		}
	}

	public Integer getDataBits() {
		return dataBits;
	}

	public void setDataBits(Integer dataBits) {
		if(this.dataBits != dataBits){
			this.dataBits = dataBits;
			this.closeHardware();
		}
	}

	public Integer getStopBits() {
		return stopBits;
	}

	public void setStopBits(Integer stopBits) {
		if(this.stopBits != stopBits){
			this.stopBits = stopBits;
			this.closeHardware();
		}
	}

	public Integer getParityMode() {
		return parityMode;
	}

	public void setParityMode(Integer parityMode) {
		if(this.parityMode != parityMode){
			this.parityMode = parityMode;
			this.closeHardware();
		}
	}
	
	public void send(String message) throws IOException{
		log.debug("Sending raw message (' " + message + "') to device: " + this.deviceName);
		if (bw == null) {
			log.error("Can't write message, BufferedWriter is NULL");
		}
		synchronized (bw) {
			bw.write(message);
			bw.flush();
		}
	}

	public boolean lock() {
		if(!this.lock){
			this.lock = true;
			return true;
		}
		return false;
	}
	
	public boolean unlock(){
		this.lock = false;
		return true;
	}

}