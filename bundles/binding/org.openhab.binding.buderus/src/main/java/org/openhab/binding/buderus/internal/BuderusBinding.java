/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.buderus.internal;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.util.Dictionary;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.buderus.BuderusBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.StringType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rkjava.serial.j3964r.J3964R;
import de.rkjava.serial.j3964r.J3964REvent;
import de.rkjava.serial.j3964r.J3964RListener;

/**
 * A binding for the Buderus RS232 Gateway, connected via serial port via the 3964R procedure.
 * The Buderus RS232 Gateway Connector polls requests according to item configurations with 
 * a globally configurable serial port and posts received replies from the gateway as ({@link StringType} 
 * to the event bus. The polling interval is 10 seconds by default and can be changed via openhab.cfg. 
 * 
 * This binding uses the gui.io library from org.openhab.io.transport.serial for RS232 communication and
 * the rkjava library of Gerd Limbeck, modified for use with gnu.io, for implementing the 3964R procedure.
 * The rkjava libary is licensed under GNU Lesser General Public License and is open source.
 * 
 * Examples for openhab.cfg:
 * ############################# Buderus RS232 Gateway Connector #############################
 * 
 * # mandatory parameter:
 * # buderus:port=/dev/ttyUSB0
 * 
 * # optional parameters (with default values):
 * 
 * # Baud rate for serial connection to the gateway
 * # buderus:baud=9600 
 * 
 * # refresh interval for execution interval in milliseconds
 * # buderus:refresh=10000
 * 
 * # delayed writing of requests to gateway in wrt. the refresh interval and the number of items
 * # buderus:delayed=false 
 * 
 * # 3964r parameters: 
 * # buderus:rzv=3000 
 * # buderus:qzv=2000
 * # buderus:zzv=180
 * # buderus:trace=false
 * # buderus:STXafterNAK=true
 * 
 * @author Lukas Maertin
 * @since 1.7.0
 */
public class BuderusBinding extends AbstractActiveBinding<BuderusBindingProvider> implements ManagedService, J3964RListener {

	private static final Logger logger = LoggerFactory.getLogger(BuderusBinding.class);

	private J3964R proc3964R = null;

	/** Default refresh interval of 10 seconds */
	private long refreshInterval = 10000L;

	/** Delay writing of requests to the gateway **/
	private boolean delayedWriting = false;

	@Override
	protected String getName() {
		return "Buderus RS232 Gateway Connector";
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void execute() {
		if (!bindingsExist()) {
			logger.debug("There is no existing Buderus RS232 Gateway binding configuration; refresh cycle aborted!");
			return;
		}

		for (BuderusBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				String request = provider.getRequest(itemName);				
				//send request to gateway
				if(request != null) {
					writeRequest(request);
				}
				else{
					logger.debug("No request for " + itemName + " defined");
				}

				//Delay the writing of the next request to relax the communication load of the gateway
				if(delayedWriting){
					logger.debug("Delay writing of next request by " + refreshInterval/(provider.getItemNames().size()+1) + " ms");
					try {
						Thread.sleep(refreshInterval/(provider.getItemNames().size()+1));
					} catch (InterruptedException e) {
						logger.error("InterruptedException" + e.getMessage());
					}
				}
			}
		}

	}	

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {	
		if (config != null) {
			logger.debug("Update config of Buderus RS232 Gateway binding");

			//mandatory parameter
			String port = null;
			String portString = (String)config.get("port");
			if (StringUtils.isNotBlank(portString)) {
				port = portString;
			}
			logger.debug("Port: " + port);

			//optional parameters
			int baud = 9600;
			String baudString = (String)config.get("baud");
			if (StringUtils.isNotBlank(baudString)) {
				baud = Integer.parseInt(baudString);
			}
			logger.debug("Baud: " + baud);

			String refreshIntervalString = (String)config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			logger.debug("Refresh: " + refreshInterval);

			String delayedWritingString = (String)config.get("delayed");
			if (StringUtils.isNotBlank(delayedWritingString) && delayedWritingString.equals("true")) {
				delayedWriting = true;
			}
			else if (StringUtils.isNotBlank(delayedWritingString) && delayedWritingString.equals("false")) {
				delayedWriting = false;
			}
			logger.debug("delayedWriting: " + delayedWriting);

			String rvzString = (String)config.get("rvz");
			if (StringUtils.isNotBlank(rvzString)) {
				J3964R.RVZ = Long.parseLong(rvzString);
			}
			logger.debug("J3964R.RVZ: " + J3964R.RVZ);

			String qvzString = (String)config.get("qvz");
			if (StringUtils.isNotBlank(qvzString)) {
				J3964R.QVZ = Long.parseLong(qvzString);
			}
			logger.debug("J3964R.QVZ: " + J3964R.QVZ);

			String zvzString = (String)config.get("zvz");
			if (StringUtils.isNotBlank(zvzString)) {
				J3964R.ZVZ = Long.parseLong(zvzString);
			}
			logger.debug("J3964R.ZVZ: " + J3964R.ZVZ);

			String traceString = (String)config.get("trace");
			if (StringUtils.isNotBlank(traceString) && traceString.equals("true")) {
				J3964R.trace = true;
			}
			else if (StringUtils.isNotBlank(traceString) && traceString.equals("false")) {
				J3964R.trace = false;
			}
			logger.debug("J3964R.trace: " + J3964R.trace);

			String stxafterNakString = (String)config.get("STXafterNAK");
			if (StringUtils.isNotBlank(stxafterNakString) && stxafterNakString.equals("true")) {
				J3964R.STXafterNAK = true;
			}
			else if (StringUtils.isNotBlank(stxafterNakString) && stxafterNakString.equals("false")) {
				J3964R.STXafterNAK = false;
			}
			logger.debug("J3964R.STXafterNAK: " + J3964R.STXafterNAK);

			
			if(port != null && initialize(port,baud))
				setProperlyConfigured(true);	
			else{
				logger.error("Could not initialize connection to gateway on serial port " + port);
				throw new ConfigurationException(port, "Could not initialize connection to gateway on serial port");
			}
		}
	}

	/**
	 * Initialize the 3964r procedure for the connection to the gateway via serial port
	 * 
	 * @param port serial port to the gateway
	 * @param baud baud rate for the connection
	 * @return success state of the initialization
	 */
	@SuppressWarnings("rawtypes")
	public boolean initialize(String port, int baud) {
		boolean success;
		// parse ports and if the default port is found, initialized the reader
		CommPortIdentifier portId = null;
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
			if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (id.getName().equals(port)) {
					logger.debug("Serial port " + port + " has been found");
					portId = id;
				}
			}
		}
		if (portId != null) {
			// initialize 3964R procedure via serial port
			try {
				proc3964R = new J3964R(port, baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); 
				//Thread starts automatically, no start() necessary
				//do not use the default constructor due to changed PARITY!
			} catch (PortInUseException e) {
				logger.error("PortInUseException" + e.getMessage());
			} 
			logger.debug("Procedure 3964R initailized at port " + port);     

			//Set speed correctly
			int mySpeed = (baud / 2400) * 2400;
			if (mySpeed <= 2400){
				mySpeed = 2400;
			}
			try {
				proc3964R.setSpeed(mySpeed);
			} catch (UnsupportedCommOperationException e) {
				logger.error("UnsupportedCommOperationException" + e.getMessage());
			}
			proc3964R.addJ3964RListener(this);
			success = true;
		} else {
			StringBuilder sb = new StringBuilder();
			portList = CommPortIdentifier.getPortIdentifiers();
			while (portList.hasMoreElements()) {
				CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
				if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					sb.append(id.getName() + "\n");
				}
			}
			logger.error("Serial port " + port + " could not be found. Available ports are:\n-----" + sb.toString() + "\n-----");
			success = false;
		}
		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dataReceived(J3964REvent e) {
		if (e.getBytes() != null) {
			Byte[] receivedFrame = new Byte[e.getBytes().length];
			receivedFrame = e.getBytes();
			String receiveString = "";
			for (int byteLoop = 0; byteLoop < (e.getBytes().length); byteLoop++) {
				receiveString += J3964R.byte2Hex(receivedFrame[byteLoop]) + " ".toUpperCase();
			}
			logger.debug("Received message " + receiveString + " on serial port");

			if(receiveString.length() >= 8 && providers != null && eventPublisher != null){
				String requestFromReceive = receiveString.substring(0, 8);			
				outerloop:
					for (BuderusBindingProvider provider : providers) {
						for (String itemName : provider.getItemNames()) {
							if(requestFromReceive.equals(provider.getRequest(itemName))){
								//logger.debug("Match for request " + requestFromReceive + " at item " + itemName);
								//send received data to bus
								eventPublisher.postUpdate(itemName, new StringType(receiveString));
								logger.debug("Message " + receiveString + " send to item " + itemName);
								break outerloop;
							}
						}
					}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dataSend(J3964REvent e) {
		String errorString = "";
		switch (e.getErrorNo()) {
		case 0:
			errorString = "success";
			break;
		case -1:
			errorString = "error: no DLE after STX";
			break;
		case -2:
			errorString = "error: timeout - no DLE after STX";
			break;
		case -3:
			errorString = "error: canceled after six tries";
			break;
		case -4:
			errorString = "error: canceled after three tries";
			break;
		default:
			errorString = "<unknown error>";
		}
		logger.debug(errorString + " while writing to serial port");
	}

	/**
	 * Sends a request via the 3964R procedure to the gateway
	 * 
	 * @param req the request to send
	 */
	public void writeRequest(String req) {
		// write string to serial port via R3964R procedure
		String trimmedText = req.replaceAll(" ", "");
		int hexBytes = trimmedText.length()/2;
		if (hexBytes > 0) {
			Byte[] bytes = new Byte[hexBytes];  // prepare data for send
			for (int hexByteLoop = 0; hexByteLoop < hexBytes; hexByteLoop++) {
				bytes[hexByteLoop] = new Byte((byte) (Integer.parseInt(trimmedText.substring(hexByteLoop * 2, hexByteLoop * 2 + 2), 16)));        
			}
			//send data to the gateway
			proc3964R.sendData(bytes);
			logger.debug("Wrote " + req + " to gateway");
		}
		else {
			logger.error("Error writing " + req + " to gateway");
		}
	}

}
