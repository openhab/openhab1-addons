/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.dscalarm.internal.protocol;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openhab.binding.dscalarm.internal.DSCAlarmEventListener;
import org.openhab.binding.dscalarm.internal.connector.DSCAlarmConnector;
import org.openhab.binding.dscalarm.internal.connector.DSCAlarmConnectorType;
import org.openhab.binding.dscalarm.internal.connector.SerialConnector;
import org.openhab.binding.dscalarm.internal.connector.TCPConnector;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that utilizes the Third Party Interface () for the EyezOn Envisalink 3/2DS
 * @author Russell Stephens
 * @since 1.6.0
 */
public class API {
	
	private static final Logger logger = LoggerFactory.getLogger(API.class);
       
	private DSCAlarmConnectorType connectorType = null;
	private DSCAlarmConnector dscAlarmConnector = null;
    
    /** API serial port name. **/
    private String apiSerialPort = "";

    /** API IP address. **/
    private String apiIP = "192.168.0.100";

	/** default TCP port. **/
	public static final int DEFAULT_BAUD_RATE = 9600;

	/** default TCP port. **/
	public static final int DEFAULT_TCP_PORT = 4025;

	/** Connection timeout in milliseconds **/
	private static final int CONNECTION_TIMEOUT = 5000;

	/** Default user code **/
	private static final String DEFAULT_USER_CODE = "1234";

	/** Default  password **/
	private static final String DEFAULT_PASSWORD = "user";

   	/** Baud Rate for serial connection - set to default **/
	private int baudRate = DEFAULT_BAUD_RATE;

	/** User password for  network login - set to default **/
	private String password = DEFAULT_PASSWORD;
 
   	/** DSC Alarm user code for some commands - set to default **/
	private String dscAlarmUserCode = DEFAULT_USER_CODE;

	private boolean connected = false;
	private int[] baudRates = {9600,19200,38400,57600,115200};
	
	/**
	 * Constructor for Serial Connection
	 * 
	 * @param serialPort
	 * @param baud
	 */	
	public API(String serialPort, int baud) {
		if (StringUtils.isNotBlank(serialPort)) {
			apiSerialPort = serialPort;
		}

		if(isValidBaudRate(baud))
			baudRate = baud;

		connectorType = DSCAlarmConnectorType.SERIAL;
	}
       
	/**
	 * Constructor for TCP connection
	 * 
	 * @param ip
	 * @param password
	 * @param dscAlarmUserCode
	 */
	public API(String ip, String password, String dscAlarmUserCode) {
		if (StringUtils.isNotBlank(ip)) {
			apiIP = ip;
		}

		if (StringUtils.isNotBlank(password)) {
			this.password = password;
		}

		if (StringUtils.isNotBlank(dscAlarmUserCode)) {
			this.dscAlarmUserCode = dscAlarmUserCode;
		}

		connectorType = DSCAlarmConnectorType.TCP;
	}

	/**
	 * Add event listener
	 * 
	 * @param listener
	 **/
	public void addEventListener(DSCAlarmEventListener listener) {
		dscAlarmConnector.addEventListener(listener);
	}

	/**
	 * Remove event listener
	 * 
	 * @param listener
	 **/
	public synchronized void removeEventListener(DSCAlarmEventListener listener) {
		dscAlarmConnector.removeEventListener(listener);
	}

	/**
	 * Returns Connector Type
	 * 
	 * @return connectorType
	 **/
	public DSCAlarmConnectorType getConnectorType() {
		return connectorType;		
	}

	/**
	 * Method to check if baud rate is valid
	 * 
	 * @return boolean
	 */
	private boolean isValidBaudRate(int baudRate) {
		boolean isValid = false;
		
		for (int i=0; i<baudRates.length; i++) {
			if(baudRate == baudRates[i]) 
				isValid = true;
		}
		
		return isValid;
	}
	
	/**
	 * Return DSC Alarm User Code.
	 **/
	public String getUserCode() {
		return dscAlarmUserCode;
	}
	
	/**
	 * Connect to the DSC Alarm System through the EyezOn Envisalink 3/2DS or the DSC IT-100.
	 **/
    public boolean open() {

    	switch (connectorType) {
	    	case SERIAL:
				if(dscAlarmConnector == null) { 
					dscAlarmConnector = new SerialConnector(apiSerialPort, baudRate);
				}
				break;
	    	case TCP:
	        	if(StringUtils.isNotBlank(apiIP) ) {
					if(dscAlarmConnector == null) { 
						dscAlarmConnector = new TCPConnector(apiIP, DEFAULT_TCP_PORT, CONNECTION_TIMEOUT);
					}
	        	}
	        	else {
	        		logger.error("open(): Unable to Make API TCP Connection!");
	        		connected = false;
	        		return connected;
	        	}
	        	break;
	        default:
	        	break;
    	}
    	
		dscAlarmConnector.open();
		connected = dscAlarmConnector.isConnected();
	
    	if(connected) {
    		if(connectorType == DSCAlarmConnectorType.TCP)
    			sendCommand(APICode.NetworkLogin);
    		
    		if(connected = dscAlarmConnector.isConnected()) {
	    		//Turn off Time Stamp Control
	    		sendCommand(APICode.TimeStampControl,"0");
    		}
    		
    	}

    	if(!connected)
    		logger.error("open(): Unable to Make API Connection!");
    	
		logger.debug("open(): Connected = {}, Connection Type: {}", connected ? true:false, connectorType);

		return connected;
    }
    
    /**
     * Close the connection to the DSC Alarm system.
     */
    public boolean close() {
		logger.debug("close(): Disconnecting from API Connection!");
		dscAlarmConnector.close();
		connected = dscAlarmConnector.isConnected();
    	return connected;
    }

    /**
     * Read a API message received from the DSC Alarm system.
     */
    public String read() {        	
		return dscAlarmConnector.read();
     }
       
    /**
     * Return Connected Status.
     */
     public boolean isConnected() {
         return dscAlarmConnector.isConnected();
     }

     /**
      * Send an API command to the DSC Alarm system.
      * 
      * @param apiCode
      * @param apiData
      * @return
      */
     public boolean sendCommand(APICode apiCode, String... apiData) {
    	boolean successful = false;
    	boolean validCommand = false;
    	
    	String command = apiCode.getCode();
    	String data = "";
 		
 		switch (apiCode) {
			case Poll: /*000*/
			case StatusReport: /*001*/
				validCommand = true;
				break;
 			case LabelsRequest: /*002*/
 				if(!connectorType.equals(DSCAlarmConnectorType.SERIAL)) {
 					break;					
 				}
				validCommand = true;
 				break;
			case NetworkLogin: /*005*/
 				if(!connectorType.equals(DSCAlarmConnectorType.TCP)) {
 					break;					
 				}
 				
 				if (password == null || password.length() < 1 || password.length() > 6) {
 					logger.error("sendCommand(): Password is invalid, must be between 1 and 6 chars", password);
 					break;
 				}
				data = password;
				validCommand = true;
 				break;
 			case DumpZoneTimers: /*008*/
 				if(!connectorType.equals(DSCAlarmConnectorType.TCP)) {
 					break;					
 				}
				validCommand = true;
 				break;
 			case SetTimeDate: /*010*/
 				Date date = new Date();
 				SimpleDateFormat dateTime = new SimpleDateFormat("HHmmMMddYY");
 				data = dateTime.format(date);
				validCommand = true;
 				break;
 			case CommandOutputControl: /*020*/
 				if (apiData[0] == null || !apiData[0].matches("[1-8]")) {
 		    		logger.error("sendCommand(): Partition number must be a single character string from 1 to 8, it was: " + apiData[0]);
 	 				break;
 				}
 
 				if (apiData[1] == null || !apiData[1].matches("[1-4]")) {
 		    		logger.error("sendCommand(): Output number must be a single character string from 1 to 4, it was: " + apiData[1]);
 					break;					
 				}

 				data = apiData[0];
 				validCommand = true;
 				break;
 			case KeepAlive: /*074*/
 				if(!connectorType.equals(DSCAlarmConnectorType.TCP)) {
 					break;					
 				}
 			case PartitionArmControlAway: /*030*/
 			case PartitionArmControlStay: /*031*/
 			case PartitionArmControlZeroEntryDelay: /*032*/
 				if (apiData[0] == null || !apiData[0].matches("[1-8]")) {
 			    	logger.error("sendCommand(): Partition number must be a single character string from 1 to 8, it was: {}", apiData[0]);
 					break;					
 				}
				data = apiData[0];
				validCommand = true;
 				break;
 			case PartitionArmControlWithUserCode: /*033*/
 			case PartitionDisarmControl: /*040*/
 				if (apiData[0] == null || !apiData[0].matches("[1-8]")) {
 			    	logger.error("sendCommand(): Partition number must be a single character string from 1 to 8, it was: {}", apiData[0]);
 					break;
 				}
 				
 				if (dscAlarmUserCode == null || dscAlarmUserCode.length() < 4 || dscAlarmUserCode.length() > 6) {
 					logger.error("sendCommand(): User Code is invalid, must be between 4 and 6 chars: {}", dscAlarmUserCode);
 					break;
 				}
				data = apiData[0] + dscAlarmUserCode;
				validCommand = true;
  				break;
 			case VirtualKeypadControl: /*058*/
 				if(!connectorType.equals(DSCAlarmConnectorType.SERIAL)) {
 					break;					
 				}
 			case TimeStampControl: /*055*/
 			case TimeDateBroadcastControl: /*056*/
 			case TemperatureBroadcastControl: /*057*/
 				if (apiData[0] == null || !apiData[0].matches("[0-1]")) {
 				   	logger.error("sendCommand(): Value must be a single character string of 0 or 1: {}", apiData[0]);
 					break;
 				}
				data = apiData[0];
				validCommand = true;
 				break;
 			case TriggerPanicAlarm: /*060*/
 				if (apiData[0] == null || !apiData[0].matches("[1-8]")) {
 				   	logger.error("sendCommand(): Partition number must be a single character string from 1 to 8, it was: {}", apiData[0]);
 	 				break;
 				}		 
 				 
 				if (apiData[1] == null || !apiData[1].matches("[1-3]")) {
 		    		logger.error("sendCommand(): FAPcode must be a single character string from 1 to 3, it was: {}", apiData[1]);
 					break;
 				}
				data = apiData[0] + apiData[1];
				validCommand = true;
 				break;
 			case KeyStroke: /*070*/
 				if (apiData[0] == null || apiData[0].length() != 1 || !apiData[0].matches("[0-9]|A|#|\\*")) {
 		    		logger.error("sendCommand(): \'keystroke\' must be a single character string from 0 to 9, *, #, or A, it was: {}", apiData[0]);
 					break;
 				}
				data = apiData[0];
				validCommand = true;
 				break;
 			case KeySequence: /*071*/
 				if(!connectorType.equals(DSCAlarmConnectorType.TCP)) {
 					break;
 				}
 				
 				if (apiData[0] == null || apiData[0].length() > 6 || !apiData[0].matches("(\\d|#|\\*)+")) {
 		    		logger.error("sendCommand(): \'keysequence\' must be a string of up to 6 characters consiting of 0 to 9, *, or #, it was: {}", apiData[0]);
 					break;
 				}
				data = apiData[0];
				validCommand = true;
 				break;
 			case CodeSend: /*200*/
 				if (apiData[0] == null || apiData[0].length() < 4 || apiData[0].length() > 6) {
 					logger.error("sendCommand(): Access Code is invalid, must be between 4 and 6 chars: {}", apiData[0]);
 					break;
 				}
				data = apiData[0];
				validCommand = true;
 				break;

 			default:
 				validCommand = false;
				break;

 		} 		

 		if(validCommand) {
 			APICommand apiCommand = new APICommand();
 			apiCommand.setAPICommand(command, data);
    		dscAlarmConnector.write(apiCommand.toString());
    		successful = true;
    		logger.debug("sendCommand(): Command Sent - {}",apiCommand.toString());
    	}
    	else
    		logger.error("sendCommand(): Command Not Sent - Invalid!");
    	 
    	return successful;
    }
}
