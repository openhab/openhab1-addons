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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

/**
 * @author Peter Kreutzer
 * @since 1.4.0
 */
public class StiebelHeatPumpSerialConnector extends StiebelHeatPumpConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(StiebelHeatPumpSerialConnector.class);

	/** the serial port to use for connecting to the heat pump device */
	private final String serialPort;

	/** baud rate of serial port */
	private final int baudRate;

	/** output stream of serial port */
	private OutputStream outStream;
	
	/** input stream of serial port */
	private InputStream inStream;

	/** output stream of serial port */
	private SerialPort connectedSerialPort;

	private int timeout = 5000;
	private final int SLEEP_INTERVAL = 1000;
	
	private boolean add;

	public StiebelHeatPumpSerialConnector(String serialPort, int baudRate) {
		this.serialPort = serialPort;
		this.baudRate = baudRate;
		logger.debug("Stiebel heatpump serial message listener started");
	}

	@Override
	/* connect to serial port of heat pump	 */
	public void connect() throws StiebelHeatPumpException {
		logger.debug("Connecting Stiebel heatpump serial port ...");
		try {
			
			// Obtain a CommPortIdentifier object for the port you want to open
			CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier(serialPort);
			
			// Get the port's ownership
			connectedSerialPort = (SerialPort) portId.open(
					"Openhab stiebel heat pump binding", 5000);
			
			// Set the parameters of the connection.
			setSerialPortParameters(baudRate);
			
			// Open the input and output streams for the connection.
			// If they won't open, close the port before throwing an
			// exception.
			outStream = connectedSerialPort.getOutputStream();
			inStream = connectedSerialPort.getInputStream();
			
			logger.debug("Sending inital request message : " ,
					DatatypeConverter.printHexBinary(new byte[] {StiebelHeatPumpDataParser.STARTCOMMUNICATION}));		
			outStream.write(StiebelHeatPumpDataParser.STARTCOMMUNICATION);
			outStream.flush();
			byte[] readBuffer = new byte[1];
			inStream.read(readBuffer);

			if (readBuffer[0] == StiebelHeatPumpDataParser.ESCAPE){
				logger.debug("Stiebel heatpump serial port connected.");
			}else{
				logger.warn("Stiebel heatpump serial port could not be connected!");				
			}
		} catch (NoSuchPortException e) {
			logger.debug("NoSuchPortException " +e.toString());
			throw new StiebelHeatPumpException(e.getMessage());
		} catch (PortInUseException e) {
			logger.debug("PortInUseException " +e.toString());
			throw new StiebelHeatPumpException(e.getMessage());
		} catch (IOException e) {
			logger.debug("IOException " +e.toString());
			disconnect();
			connectedSerialPort = null;
			throw new StiebelHeatPumpException(e.getMessage());
		}
	}
	
	@Override
	/* disconnect the serial connection to heat pump	 */
	public void disconnect() throws StiebelHeatPumpException {

		if (serialPort != null) {
			try {
				// close the i/o streams.
				outStream.close();
				inStream.close();
			} catch (IOException ex) {
				// don't care
			}
			// Close the port.
			connectedSerialPort.close();
			connectedSerialPort = null;
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
			connectedSerialPort.setSerialPortParams(baudRate,
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException ex) {
			throw new IOException(
					"Unsupported serial port parameter for serial port");
		}
	}

	/**
	 * Gets version information of connected heat pump
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
					parser.shortToByte(checkSum)[0], request.getRequestByte(),
					StiebelHeatPumpDataParser.ESCAPE,
					StiebelHeatPumpDataParser.END };

			logger.debug("Sending request message : {}" ,
					DatatypeConverter.printHexBinary(requestMessage));
						
			// wait for response of heat pump
			int availableBytes = 0;
			int timeval = 0;
			byte[] readBuffer = null;
			
			while (timeout == 0 || timeval < timeout) {
				outStream.write(requestMessage);
				outStream.flush();
				readBuffer = new byte[2];
				inStream.read(readBuffer, 0, 2);
				
				if (!parser.dataAvailable(readBuffer)) {
					try {
						logger.debug("Retry {} receiving response ...", timeout);
						Thread.sleep(SLEEP_INTERVAL);
					} catch (InterruptedException e) {
					}
					this.disconnect();
					this.connect();
				}
				timeval += SLEEP_INTERVAL;
			}
			
			if (!parser.dataAvailable(readBuffer)) {
				logger.warn("No data available for request {} with byte ",
						request.getName(), DatatypeConverter
								.printHexBinary(new byte[] { request
										.getRequestByte() }));
				return data;
			}

			// acknowledge to heat pump to now send the data
			outStream.write(StiebelHeatPumpDataParser.ESCAPE);
			outStream.flush();
			
			// wait for response of heat pump
			availableBytes = 0;
			timeval = 0;
			readBuffer = null;
			
			while (timeout == 0 || timeval < timeout) {
				availableBytes = inStream.available();
				if (availableBytes > 0) {
					readBuffer = new byte[availableBytes];
					// Read the serial port
					inStream.read(readBuffer, 0, availableBytes);
					logger.debug("Response message : " ,
							DatatypeConverter
							.printHexBinary(readBuffer));	
				} 
				try {
					logger.debug("Retry {} receiving response ...", timeout);
					Thread.sleep(SLEEP_INTERVAL);
				} catch (InterruptedException e) {
				}

				timeval += SLEEP_INTERVAL;
			}

			if(readBuffer == null){
				logger.warn("Could not get data from heatpump, timeout ");
				return data;				
			}

			// fix duplicated bytes in response
			readBuffer = parser.fixDuplicatedBytes(readBuffer);

			logger.debug("Received bytes from from heatpump : ",
					DatatypeConverter.printHexBinary(readBuffer));

			// verify the header
			try {
				parser.verifyHeader(readBuffer);
			} catch (StiebelHeatPumpException e) {
				logger.warn("Response validation failed ! " + e.toString());
			}

			// get data from heat pump
			data.putAll(parser.parseRecords(readBuffer, request));

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
		Map<String, String> data = getHeatPumpData(request);
		version = data.get("Version");
		return version;
	}
}
