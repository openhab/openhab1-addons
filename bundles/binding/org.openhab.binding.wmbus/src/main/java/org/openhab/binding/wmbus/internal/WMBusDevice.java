/**Copyright (c) 2010-${year}, openHAB.org and others.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
**/

package org.openhab.binding.wmbus.internal;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.locks.ReentrantLock;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.io.IOUtils;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class represents the WMBus device serially connected. It is linked to one string item, but managing all other string items wearing the devices names.
 * 
 * @author Kai Kreuzer, Christoph Parnitzke
 *
 */
public class WMBusDevice implements FinalValuesEventListener, SerialPortEventListener {

	private static final Logger logger = LoggerFactory.getLogger(WMBusDevice.class);

	private String port;
	private int baud = 9600;
	private String stringItemName;
	
	private static ReentrantLock lock = new ReentrantLock(true);

	private EventPublisher eventPublisher;

	private CommPortIdentifier portId;
	private SerialPort serialPort;

	private InputStream inputStream;

	private OutputStream outputStream;

	private String desiredData;

	public WMBusDevice(String port, String desiredData) {
		this.port = port;
		this.desiredData = desiredData;
	}

	public WMBusDevice(String port, String desiredData, int baud) {
		this.port = port;
		this.desiredData = desiredData;
		this.baud = baud;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	public String getStringItemName() {
		return stringItemName;
	}

	public void setStringItemName(String stringItemName) {
		this.stringItemName = stringItemName;
	}

	public String getPort() {
		return port;
	}

	/**
	 * Initialize this device and open the serial port
	 * 
	 * @throws InitializationException if port can not be opened
	 */
	@SuppressWarnings("rawtypes")
	public void initialize() throws InitializationException {
		// create Listener on WMBus Library
		WMBus.addListener(this);
		// parse ports and if the default port is found, initialized the reader
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
			if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (id.getName().equals(port)) {
					logger.debug("Serial port '{}' has been found.", port);
					portId = id;
				}
			}
		}
		if (portId != null) {
			// initialize serial port
			try {
				serialPort = (SerialPort) portId.open("openHAB", 2000);
			} catch (PortInUseException e) {
				throw new InitializationException(e);
			}

			try {
				inputStream = serialPort.getInputStream();
			} catch (IOException e) {
				throw new InitializationException(e);
			}
			try {
				serialPort.addEventListener(this);
			} catch (TooManyListenersException e) {
				throw new InitializationException(e);
			}

			// activate the DATA_AVAILABLE notifier
			serialPort.notifyOnDataAvailable(true);

			try {
				// set port parameters
				serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
			} catch (UnsupportedCommOperationException e) {
				throw new InitializationException(e);
			}

			try {
				// get the output stream
				outputStream = serialPort.getOutputStream();
			} catch (IOException e) {
				throw new InitializationException(e);
			}
		} else {
			StringBuilder sb = new StringBuilder();
			portList = CommPortIdentifier.getPortIdentifiers();
			while (portList.hasMoreElements()) {
				CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
				if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					sb.append(id.getName() + "\n");
				}
			}
			throw new InitializationException("Serial port '" + port + "' could not be found. Available ports are:\n" + sb.toString());
		}
	}

	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			// we get here if data has been received
			int bufferlength = 0;
			boolean bufferread = false;
			try {
				bufferlength = inputStream.read();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			logger.debug("Bytes to read: " + bufferlength);
			byte[] readBuffer = new byte[bufferlength];
			try {
				do {
					// read data from serial device
					if (inputStream.available() == bufferlength) {
						 logger.debug("Bytes read: " + inputStream.read(readBuffer));
						 bufferread = true;
					}
					try {
						// add wait states around reading the stream, so that interrupted transmissions are merged
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// ignore interruption
					}
				} while (!bufferread);
				// send data
				byte[] newbyte = new byte[bufferlength+1];
				newbyte[0] = (byte)(bufferlength);
										
				for (int i = 1; i < bufferlength+1; i++)
					newbyte[i] = readBuffer[i-1];
				try{
					logger.debug("Base 16:");
					String output="";
					for(int i = 0; i<newbyte.length; i++)
						output += (String.format("0x%x ", newbyte[i]));
					logger.debug(output);
				} catch (Exception e) {
					e.printStackTrace();
				}
				InputStream ips = new ByteArrayInputStream(newbyte);
				DataInputStream result = new DataInputStream(ips);
				lock.lock();
					WMBus.mySerialPort_DataReceived(result);
				} catch (InvalidKeyException e) {
					logger.warn(e.getClass().toString() + " happened in Library! : " + e.getMessage());
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					logger.warn(e.getClass().toString() + " happened in Library! : " + e.getMessage());
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					logger.warn(e.getClass().toString() + " happened in Library! : " + e.getMessage());
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					logger.warn(e.getClass().toString() + " happened in Library! : " + e.getMessage());
					e.printStackTrace();
				} catch (BadPaddingException e) {
					logger.warn(e.getClass().toString() + " happened in Library! : " + e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					logger.warn(e.getClass().toString() + " happened in Library! : " + e.getMessage());
					e.printStackTrace();
				} catch (InvalidAlgorithmParameterException e) {
					logger.warn(e.getClass().toString() + " happened in Library! : " + e.getMessage());
					e.printStackTrace();
				}
				lock.unlock();
			break;
		}
	}
	
	/**
	 * This method processes the given values returned from the WMBus class, depending on the desiredData variable
	 * 
	 * @param the final values received from the WMBus class
	 */
	public void receiveFinalValues(FinalValuesEvent e) {
		String result = "";
		logger.debug("DesiredData: "+ desiredData);
		if(desiredData.contains("TotalVolume")){
			if(e.meterID.toString() == "Corona"){
				result = formatValueString(e.FinalValues.get(0).value);
			}
			else if(e.meterID.toString() == "Uniflo"){
				result = formatValueString(e.FinalValues.get(3).value);
			}
			else {
				result = "Failure: Wrong device name";
			}
		}
		else if(desiredData.contains("VolumeFlow")){
			if(e.meterID.toString() == "Corona"){
				result = formatValueString(e.FinalValues.get(1).value);
			}
			else if(e.meterID.toString() == "Uniflo"){
				result = formatValueString(e.FinalValues.get(1).value);
			}
			else {
				result = "Failure: Wrong device name";
			}
		}
		else{ 
			result = "Failure: Wrong desiredData";
		}
		stringItemName = e.meterID.toString();
		// send data to the bus
		logger.debug("Received message '{}' on serial port {}", new String[] { result, port });
		logger.debug("MeterID: "+ e.meterID.toString());
		if(e.meterID == "Corona"){
			if (eventPublisher != null && stringItemName != null) {
				eventPublisher.postUpdate(stringItemName, new StringType(result));
			}
			else logger.warn("No usable data received on serial port!");
		}
		else if(e.meterID == "Uniflo"){
			if (eventPublisher != null && stringItemName != null) {
				eventPublisher.postUpdate(stringItemName, new StringType(result));
			}
			else if (eventPublisher == null)
				logger.warn("No usable data received on serial port! Error: ePub");
			else if (stringItemName == null)
				logger.warn("No usable data received on serial port! Error: SItN");
			else
				logger.warn("No usable data received on serial port!");
		}
		else logger.warn("Is no registered meter!");
		lock.unlock();
	}

	/**
	 * This method formats given string numbers
	 * 
	 * @param String value
	 * @return formated String ("x.xx")
	 */
	private String formatValueString(String value){
		for(int i = 0; i < value.length(); i++){
			if(value.charAt(i) == '.')
				return value.substring(0, i+3); 
		}
		return value;
	}
	
	/**
	 * Sends a string to the serial port of this device
	 * 
	 * @param msg the string to send
	 */
	public void writeString(String msg) {
		logger.debug("Writing '{}' to serial port {}", new String[] { msg, port });
		try {
			// write string to serial port
			outputStream.write(msg.getBytes());
			outputStream.flush();
		} catch (IOException e) {
			logger.error("Error writing '{}' to serial port {}: {}", new String[] { msg, port, e.getMessage() });
		}
	}

	/**
	 * Close this serial device
	 */
	public void close() {
		serialPort.removeEventListener();
		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(outputStream);
		serialPort.close();
	}
}
