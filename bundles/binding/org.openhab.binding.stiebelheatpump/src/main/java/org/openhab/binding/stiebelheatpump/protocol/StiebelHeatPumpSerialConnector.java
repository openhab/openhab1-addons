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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import javax.xml.bind.DatatypeConverter;

/**
 * @author Peter Kreutzer
 * @since 1.4.0
 */
public class StiebelHeatPumpSerialConnector extends StiebelHeatPumpConnector {
	
	private static final Logger logger = LoggerFactory.getLogger(StiebelHeatPumpSerialConnector.class);
	
	/** the serial port to use for connecting to the heat pump device */
    private final String serialPort;
   
	/**  baud rate of serial port */
    private final int baudRate;
    
    /**  output stream of serial port */
    private OutputStream outStream;
    
    /**  output stream of serial port */
    private InputStream inStream;
    
    /**  output stream of serial port */
    private SerialPort connectedSerialPort;

	private boolean add;

	public StiebelHeatPumpSerialConnector(String serialPort, int baudRate) {
		logger.debug("Stiebel heatpump serial message listener started");
		this.serialPort = serialPort;
		this.baudRate = baudRate;
	}
	
    /**
    * Get the serial port input stream
    * @return The serial port input stream     */   
	public InputStream getSerialInputStream() {
		return inStream;
		}
	
	/**     
	 * brief Get the serial port output stream     
	 * @return The serial port output stream     */
	public OutputStream getSerialOutputStream() {
		return outStream;
		}
	
	@Override
	public void connect() throws StiebelHeatPumpException {
		try {
            // Obtain a CommPortIdentifier object for the port you want to open
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(serialPort);
            // Get the port's ownership
			connectedSerialPort = (SerialPort) portId.open("Openhab stiebel heat pump binding", 5000);
			// Set the parameters of the connection.
			setSerialPortParameters(baudRate);
			// Open the input and output streams for the connection.
			// If they won't open, close the port before throwing an
			// exception.
			outStream = connectedSerialPort.getOutputStream();
			inStream = connectedSerialPort.getInputStream();
			
			} 
		catch (NoSuchPortException e) {
			throw new StiebelHeatPumpException(e.getMessage());
			} 
		catch (PortInUseException e) {
			throw new StiebelHeatPumpException(e.getMessage());
			} 
		catch (IOException e) { 
			connectedSerialPort.close();
			throw new StiebelHeatPumpException(e.getMessage());
			}
	}

	@Override
	public void disconnect() throws StiebelHeatPumpException {
		
		if (serialPort != null) {
			try {
				// close the i/o streams.
				outStream.close();
				inStream.close();
				} 
			catch (IOException ex) {
				// don't care
				}
			// Close the port.
			connectedSerialPort.close();
			connectedSerialPort = null;
			}
	}
	
    /**
     *  Register listener for data available event 
     *  @param dataAvailableListener The data available listener */
	public void addDataAvailableListener(SerialPortEventListener dataAvailableListener) throws StiebelHeatPumpException {
		// Add the serial port event listener
		try {
			connectedSerialPort.addEventListener(dataAvailableListener);
		} catch (TooManyListenersException e) {
			throw new StiebelHeatPumpException(e.getMessage());
		}
		connectedSerialPort.notifyOnDataAvailable(true);
		}
	
	/** Sets the serial port parameters to 57600bps-8N1     */    
	protected void setSerialPortParameters(int baudrate) throws IOException {
		        
		try {            
			// Set serial port to xxxbps-8N1
			connectedSerialPort.setSerialPortParams(baudRate,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE); 
			connectedSerialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);        
			} 
		catch (UnsupportedCommOperationException ex) {  
			throw new IOException("Unsupported serial port parameter for serial port");
			}
		}
	
	/** Gets version information of connected heat pump*/    
	public Map<String,String> getHeatPumpData(Request request) throws StiebelHeatPumpException {
		
		StiebelHeatPumpDataParser parser = new StiebelHeatPumpDataParser();  
		Map<String,String> data = new HashMap<String,String>();
		try {		
			// send request to heat pump
			short checkSum = parser.calculateChecksum(new byte[]{request.getRequestByte()});
			
			byte[] serialVersionMessage = {
					StiebelHeatPumpDataParser.HEADERSTART ,
					StiebelHeatPumpDataParser.GET,
					parser.shortToByte(checkSum)[0],
					request.getRequestByte() , 
					StiebelHeatPumpDataParser.ESCAPE,
					StiebelHeatPumpDataParser.END};
			
			byte[] response = receive(serialVersionMessage);
			
			if (!parser.dataAvailable(response)){
				logger.warn("No data available for request {} with byte ",
						request.getName(),
						DatatypeConverter.printHexBinary(new byte[] {request.getRequestByte()}));
				return data;
			}

			// acknowledge to heat pump to now send the data
			response = receive(new byte[]{StiebelHeatPumpDataParser.ESCAPE});
			// fix duplicated bytes in response
			response = parser.fixDuplicatedBytes(response);
			
			// verify the header
			try{
				parser.verifyHeader(response);				
			}
			catch (StiebelHeatPumpException e){
				logger.warn("Response validation failed ! " + e.toString());
			}
			
			//get data from heat pump
			data.putAll(parser.parseRecords(response, request));

			return data;
		}
		catch (IOException ex) {
			throw new StiebelHeatPumpException(ex.getMessage());
		} 
		catch (InterruptedException ex) {
			throw new StiebelHeatPumpException(ex.getMessage());
		}
	}
	
	/** Gets version information of connected heat pump*/    
	public String getHeatPumpVersion (Request request) throws StiebelHeatPumpException {
		Map<String,String> data = getHeatPumpData(request);
		version = data.get("Version");
		return version;
	}
	
	/** 
	 * @return array of bytes 
	 * @throws IOException 
	 * @throws InterruptedException */    
	private byte[] receive (byte [] message) throws StiebelHeatPumpException, IOException, InterruptedException {		
		outStream.write(message);
		outStream.flush();
		Thread.sleep(1000);
		
		// wait  for response of heatpump
		int availableBytes = 0;
		int retry = 0;
		int maxRetries = 5;
		
		while ( retry < maxRetries ){
			availableBytes = inStream.available();
			if (availableBytes > 0) {
				byte[] readBuffer = new byte[availableBytes];
				// Read the serial port
				inStream.read(readBuffer, 0, availableBytes);
				return readBuffer;
			}
			else{
				retry++;
				}
		}
	
		throw new StiebelHeatPumpException("Could not connect heatpump, tryed "+maxRetries +" time " );
	}
	
}
