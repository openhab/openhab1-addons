/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.io.transport.serial.internal;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.openhab.io.transport.serial.SerialDeviceException;
import org.openhab.io.transport.serial.SerialDeviceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Representation of an serial device.
 * 
 * @author Johannes Engelke <info@johannes-engelke.de>
 * @since 1.7.0
*/
public class SerialDevice {
	protected final static Logger log = LoggerFactory.getLogger(SerialDeviceHandler.class);
	
	private String deviceName;
	private Integer baudRate = 9600;
	private Integer dataBits = SerialPort.DATABITS_8;
	private Integer stopBits = SerialPort.STOPBITS_1;
	private Integer parityMode = SerialPort.PARITY_EVEN;
	
	protected SerialPort serialPort;
	protected OutputStream os;
	protected InputStream is;
	protected BufferedWriter bw;
	protected BufferedReader br;
	
	
	public SerialDevice(String deviceName) {
		this.deviceName = deviceName; 
	}
	
	public SerialDevice(String key, String value) {
		setProperty(key, value);
	}

	public void openHardware() throws SerialDeviceException {
		if(serialPort == null){
			log.debug("Open serial port " + deviceName);
			try {
				CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(deviceName);
				if(portIdentifier.isCurrentlyOwned()){
					throw new SerialDeviceException("The port " + deviceName + " is currenty used by "
							+ portIdentifier.getCurrentOwner());
				}
				
				CommPort port = portIdentifier.open(this.getClass().getName(), 2000);
				if(!(port instanceof SerialPort)){
					throw new SerialDeviceException("The device " + deviceName + " is not a serial port");
				}
				
				serialPort = (SerialPort) port;
				serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parityMode);
				
				os = serialPort.getOutputStream();
				is = serialPort.getInputStream();
				bw = new BufferedWriter(new OutputStreamWriter(os));
				br = new BufferedReader(new InputStreamReader(is));
	
			} catch (NoSuchPortException e) {
				throw new SerialDeviceException(e);
			} catch (PortInUseException e) {
				throw new SerialDeviceException(e);
			} catch (UnsupportedCommOperationException e) {
				throw new SerialDeviceException(e);
			} catch (IOException e) {
				throw new SerialDeviceException(e);
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

	public void setProperty(String key, String value){
		log.debug("SetProperty: " + key + " value: " + value);
		if(key.equals("device")){
			this.setDeviceName(value);
		} else if (key.equals("baudrate")) {
			this.setBaudRate(Integer.parseInt(value));
		} else if (key.equals("databits")) {
			this.setDataBits(Integer.parseInt(value));
		} else if (key.equals("parity")) {
			this.setParityMode(Integer.parseInt(value));
		} else if (key.equals("stopbits")) {
			this.setStopBits(Integer.parseInt(value));
		} 
	}

	public void setDeviceName(String deviceName){
		log.debug("DeviceName: " + deviceName);
		if(this.deviceName != deviceName){
			this.deviceName = deviceName;
			this.closeHardware();
		}
	}

	public String getDeviceName() {
		return deviceName;
	}
	
	public void setBaudRate(Integer baudRate) {
		if(baudRate != this.baudRate){
			this.baudRate = baudRate;
			this.closeHardware();
		}
	}

	public Integer getBaudRate() {
		return baudRate;
	}

	public void setDataBits(Integer dataBits) {
		if(this.dataBits != dataBits){
			this.dataBits = dataBits;
			this.closeHardware();
		}
	}

	public Integer getDataBits() {
		return dataBits;
	}

	public void setStopBits(Integer stopBits) {
		if(this.stopBits != stopBits){
			this.stopBits = stopBits;
			this.closeHardware();
		}
	}

	public Integer getStopBits() {
		return stopBits;
	}

	public void setParityMode(Integer parityMode) {
		if(this.parityMode != parityMode){
			this.parityMode = parityMode;
			this.closeHardware();
		}
	}

	public Integer getParityMode() {
		return parityMode;
	}
}