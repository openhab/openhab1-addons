/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.protocol;

import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gnu.io.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Kreutzer
 * @since 1.4.0
 */
public class StiebelHeatPumpSerialConnector extends StiebelHeatPumpConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(StiebelHeatPumpSerialConnector.class);

	/** the serial port to use for connecting to the heat pump device */
	private final String serialPortName;

	/** baud rate of serial port */
	private final int baudRate;

	/** output stream of serial port */
	private DataOutputStream outStream;
	
	/** input stream of serial port */
	private DataInputStream inStream;

	/** output stream of serial port */
	private SerialPort serialPort;

	private int timeout = 5000;
	private final int SLEEP_INTERVAL = 100;

	private int INPUT_BUFFER_LENGTH = 1024;
	
	public StiebelHeatPumpSerialConnector(String serialPort, int baudRate) {
		this.serialPortName = serialPort;
		this.baudRate = baudRate;
		logger.debug("Stiebel heatpump serial message listener started");
	}

	@Override
	/* connect to serial port of heat pump	 */
	public void connect() throws StiebelHeatPumpException {
		logger.debug("Connecting Stiebel heatpump serial port ...");
		try {
			
			CommPortIdentifier portIdentifier;
			try {
				portIdentifier = CommPortIdentifier.getPortIdentifier(serialPortName);

			} catch (NoSuchPortException e) {
				throw new IOException("Serial port with given name does not exist", e);
			}

			if (portIdentifier.isCurrentlyOwned()) {
				throw new IOException("Serial port is currently in use.");
			}

			RXTXPort commPort;
			try {
				commPort = portIdentifier.open(this.getClass().getName(), 2000);
			} catch (PortInUseException e) {
				throw new IOException("Serial port is currently in use.", e);
			}

			if (!(commPort instanceof SerialPort)) {
				commPort.close();
				throw new IOException("The specified CommPort is not a serial port");
			}

			serialPort = (SerialPort) commPort;
			// Set the parameters of the connection.
			setSerialPortParameters(baudRate);
			
			//serialPort.notifyOnDataAvailable(true);

			try {
				outStream = new DataOutputStream(serialPort.getOutputStream());
				inStream = new DataInputStream(serialPort.getInputStream());
			} catch (IOException e) {
				serialPort.close();
				serialPort = null;
				throw new IOException("Error getting input or output or input stream from serial port", e);
			}
		} catch (IOException e) {
			logger.debug("IOException " +e.toString());
			disconnect();
			serialPort = null;
			throw new StiebelHeatPumpException(e.getMessage());
		}
	}
	
	@Override
	/* disconnect the serial connection to heat pump	 */
	public void disconnect() throws StiebelHeatPumpException {

		if (serialPortName != null) {
			try {
				// close the i/o streams.
				outStream.close();
				inStream.close();
			} catch (IOException ex) {
				// don't care
			}
			// Close the port.
			serialPort.close();
			serialPort = null;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Sets the serial port parameters to 57600bps-8N1
	 * 
	 * @param baudrate
	 *            used to initialize the serial connection
	 */
	protected void setSerialPortParameters(int baudrate) throws IOException {

		try {
			// Set serial port to xxxbps-8N1
			serialPort.setSerialPortParams(baudRate,
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException ex) {
			throw new IOException(
					"Unsupported serial port parameter for serial port");
		}
	}

	/**
	 * Gets data from connected heat pump
	 * 
	 * @param request
	 *            request object with all definition to handle messages to heat
	 *            pump
	 * @return map containing name (record definition) and corresponding value
	 */
	public Map<String, String> getHeatPumpData(Request request)
			throws StiebelHeatPumpException {

		logger.debug("Start request {} on heatpump.", request.getName());
		StiebelHeatPumpDataParser parser = new StiebelHeatPumpDataParser();
		Map<String, String> data = new HashMap<String, String>();
		try {
			// prepare request message 
			short checkSum = parser.calculateChecksum(
					new byte[] { request.getRequestByte()});
			byte[] requestMessage = { StiebelHeatPumpDataParser.HEADERSTART,
					StiebelHeatPumpDataParser.GET,
					(byte) parser.shortToByte(checkSum)[0],
					request.getRequestByte(),
					StiebelHeatPumpDataParser.ESCAPE,
					StiebelHeatPumpDataParser.END };

			logger.debug("Sending request message : {}" , StiebelHeatPumpDataParser.bytesToHex(requestMessage));
			if(!startCommunication()){
				throw new StiebelHeatPumpException("no connection!");
			}
			
			byte[] response = getData(requestMessage);
			
			byte[] readBytes = parser.fixDuplicatedBytes(response);

			logger.debug("Received bytes from heatpump : {}",
					StiebelHeatPumpDataParser.bytesToHex(readBytes));

			try {
				// verify the header
				parser.verifyHeader(readBytes);
			} catch (StiebelHeatPumpException e) {
				logger.warn("Response validation failed ! " + e.toString());
				return data;
			}

			// store data from heat pump
			data.putAll(parser.parseRecords(readBytes, request));

			return data;
		} catch (IOException ex) {
			throw new StiebelHeatPumpException(ex.getMessage());
		}
	}
	
	/**
	 * Gets version information of connected heat pump
	 * 
	 * @param request
	 *            object request object with definition to get version of heat
	 *            pump
	 * @return version of heat pump firmware
	 */
	public String getHeatPumpVersion(Request request) 
			throws StiebelHeatPumpException {
		logger.debug("Getting version from Stiebel heat pump ... ");
		
		Map<String, String> heatPumpVersionData = new HashMap<String, String>();
		int retry = 0;
		while(retry <5){
			try {
				heatPumpVersionData = getHeatPumpData(request);
				version = heatPumpVersionData.get("Version");
				return version;
				
			} catch (StiebelHeatPumpException e) {
				retry++;
			}	
			retry++;
		}
		return version;
	}

	/**
	 * initiate communication protocol to heat pump
	 */
	private boolean startCommunication() throws StiebelHeatPumpException {
		
		byte[] buffer = new byte[INPUT_BUFFER_LENGTH ];
		boolean readSuccessful = false;
		int timeval = 0;
		int numBytesReadTotal = 0;

		try {
			outStream.write(StiebelHeatPumpDataParser.STARTCOMMUNICATION);
			outStream.flush();
			logger.debug(String.format("Sended STARTCOMMUNICATION  %02X", StiebelHeatPumpDataParser.STARTCOMMUNICATION));

			while (timeout == 0 || timeval < timeout) {			
				if (inStream.available() > 0) {
					byte abyte;
					abyte = inStream.readByte();
					logger.debug(String.format("Received %02X", abyte));
					buffer[numBytesReadTotal]=abyte;
					numBytesReadTotal++;	
	
					if (numBytesReadTotal == 1 && abyte == StiebelHeatPumpDataParser.ESCAPE) {
						readSuccessful = true;
						break;
					}
				}else{			
					try {
						Thread.sleep(SLEEP_INTERVAL);
						logger.debug("Re-sending STARTCOMMUNICATION");
						outStream.write(StiebelHeatPumpDataParser.STARTCOMMUNICATION);
						outStream.flush();
					} catch (IOException e) {
						logger.warn("IOException " + e.toString());
					} catch (InterruptedException e) {
						logger.warn("InterruptedException " + e.toString());
					}		
					timeval += SLEEP_INTERVAL;
				}
			} 
		}catch (IOException e) {
			logger.warn("IOException " + e.toString());
			timeval += SLEEP_INTERVAL;
		}

		if (!readSuccessful) {
			throw new StiebelHeatPumpException("Timeout while getting communication to heat pump starting");
		}

		if (buffer[0] == StiebelHeatPumpDataParser.ESCAPE){
			logger.debug("Stiebel heatpump serial port ready for request.");
		}else{
			logger.debug("Stiebel heatpump serial port could not be connected with start communication request!");				
		}
		return readSuccessful;
	}

	/**
	 * Gets data from connected heat pump
	 * 
	 * @param request
	 *            request bytes to send to heat pump
	 * @return response bytes from heat pump
	 */
	private byte[] getData(byte[] request) throws IOException {
		for (byte abyte : request){
			outStream.write(abyte);
			//logger.debug(String.format("Sended %02X", abyte));
		}	
		outStream.flush();		
		
		byte[] buffer = new byte[INPUT_BUFFER_LENGTH];
		boolean readSuccessful = false;
		int numBytesReadTotal = 0;
		int timeval = 0;

		while (timeout == 0 || timeval < timeout) {
			if (inStream.available() > 0) {
				byte abyte = inStream.readByte();
				//logger.debug(String.format("Received %02X", abyte));
				buffer[numBytesReadTotal]=abyte;
				numBytesReadTotal++;	

				if (numBytesReadTotal > 1 &&
						buffer[numBytesReadTotal-1] == StiebelHeatPumpDataParser.STARTCOMMUNICATION &&
						buffer[numBytesReadTotal-2] == StiebelHeatPumpDataParser.ESCAPE) {
					readSuccessful = true;
					break;
				}
			}else{			
				try {
					Thread.sleep(SLEEP_INTERVAL);
				} catch (InterruptedException e) {
				}
	
				timeval += SLEEP_INTERVAL;
			}
		}

		if (!readSuccessful) {
			throw new IOException("Did not receive any data available message !");
		}		
		if (numBytesReadTotal != 2) {
			throw new IOException("Data available message does not have length of 2!");
		}
		
		// send acknowledgment 
		outStream.write(StiebelHeatPumpDataParser.ESCAPE);
		//logger.debug(String.format("Sended %02X", StiebelHeatPumpDataParser.ESCAPE));
		outStream.flush();
	
		readSuccessful = false;
		numBytesReadTotal = 0;
		timeval = 0;
		buffer = new byte[INPUT_BUFFER_LENGTH];
		// receive version information
		while (timeout == 0 || timeval < timeout) {
			if (inStream.available() > 0) {
				byte abyte = inStream.readByte();
				//logger.debug(String.format("Received %02X", abyte));
				buffer[numBytesReadTotal]=abyte;
				numBytesReadTotal++;	

				if (numBytesReadTotal > 4 &&
						buffer[numBytesReadTotal-1] == StiebelHeatPumpDataParser.END &&
						buffer[numBytesReadTotal - 2] == StiebelHeatPumpDataParser.ESCAPE) {
					readSuccessful = true;
					break;
				}
			}else{			
				try {
					Thread.sleep(SLEEP_INTERVAL);
				} catch (InterruptedException e) {
				}
	
				timeval += SLEEP_INTERVAL;
			}
		}

		if (!readSuccessful) {
			throw new IOException("Did not receive any data from heat pump!");
		}
	
		if (buffer[numBytesReadTotal - 1] != StiebelHeatPumpDataParser.END &&
				buffer[numBytesReadTotal - 2] != StiebelHeatPumpDataParser.ESCAPE) {
			throw new IOException("Data message does not have footer!");
		}

		byte[] dataBytes = new byte[numBytesReadTotal];
		System.arraycopy(buffer, 0, dataBytes, 0, numBytesReadTotal);
		return dataBytes;
	}
}
