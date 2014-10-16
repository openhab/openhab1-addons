/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.dscalarm.internal.connector;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.dscalarm.internal.DSCAlarmEvent;
import org.openhab.binding.dscalarm.internal.DSCAlarmEventListener;
import org.openhab.binding.dscalarm.internal.protocol.APIMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* A class that establishes a Serial connection to the DSC IT100 interface
* 
* @author Russell Stephens
* @since 1.6.0
*/
public class SerialConnector implements DSCAlarmConnector, SerialPortEventListener{
	private static final Logger logger = LoggerFactory.getLogger(SerialConnector.class);

	private String serialPortName = "";
	private int baudRate;
	private SerialPort serialPort = null;
	private OutputStreamWriter serialOutput = null;
	private BufferedReader serialInput = null;
	private DSCAlarmConnectorType connectorType = DSCAlarmConnectorType.SERIAL;
	private static boolean connected = false;
	private static List<DSCAlarmEventListener> _listeners = new ArrayList<DSCAlarmEventListener>();

	/**
	 * Constructor
	 */
	public SerialConnector(String serialPortName, int baudRate) {
		this.serialPortName = serialPortName;
		this.baudRate = baudRate;
	}

	/**
	 * Returns Connector Type
	 */
	public DSCAlarmConnectorType getConnectorType() {
		return connectorType;		
	}
	
	/**
	 * {@inheritDoc}
	 **/
	public void write(String writeString) {
        try {
        	serialOutput.write(writeString);
            serialOutput.flush();
        }catch (IOException ioException) {
        	logger.error("write(): {}",ioException);
			connected = false;
        } catch (Exception exception) {
        	logger.error("write(): Unable to write to socket: {} ", exception);
			connected = false;
        }
    }
	
	/**
	 * {@inheritDoc}
	 **/
   public String read() {
        String message = "";

        try {
        	message = readLine();
        }
        catch (IOException ioException) {
			logger.error("read(): IO Exception: ", ioException);
			connected = false;
        }
        catch (Exception exception) {
			logger.error("read(): Exception: ", exception);
			connected = false;
        }
        
        return message;

    }

	private String readLine() throws IOException {
		return serialInput.readLine();
	}

	/**
	 * {@inheritDoc}
	 **/
	public void open() {

		try {
			
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialPortName);
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
			
			serialPort = (SerialPort) commPort;
			serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.enableReceiveThreshold(1);
			serialPort.disableReceiveTimeout();

			serialOutput = new OutputStreamWriter(serialPort.getOutputStream(), "US-ASCII");
            serialInput = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            
            setSerialEventHandler(this);
            
            connected = true;
			
		} catch (NoSuchPortException noSuchPortException) {
			logger.error("open(): No Such Port Exception: ", noSuchPortException);
            connected = false;
		} catch (PortInUseException portInUseException) {
			logger.error("open(): Port in Use Exception: ", portInUseException);
            connected = false;
		} catch (UnsupportedCommOperationException unsupportedCommOperationException) {
			logger.error("open(): Unsupported Comm Operation Exception: ", unsupportedCommOperationException);
            connected = false;
		} catch (UnsupportedEncodingException unsupportedEncodingException) {
			logger.error("open(): Unsupported Encoding Exception: ", unsupportedEncodingException);
            connected = false;
		} catch (IOException ioException) {
			logger.error("open(): IO Exception: ", ioException);
            connected = false;
		}
	}
	
	 /**
	  * Handles an incoming message
	  * 
	  * @param incomingMessage
	  */
	 public synchronized void handleIncomingMessage(String incomingMessage) {
		APIMessage Message = new APIMessage(incomingMessage);
		logger.debug("handleIncomingMessage(): Message recieved: {} - {}",incomingMessage,Message.toString());

		DSCAlarmEvent event = new DSCAlarmEvent(this);
		event.dscAlarmEventMessage(Message);
		
		// send message to event listeners
		try {
			Iterator<DSCAlarmEventListener> iterator = _listeners.iterator();

			while (iterator.hasNext()) {
				((DSCAlarmEventListener) iterator.next()).dscAlarmEventRecieved(event);
			}

		} catch (Exception e) {
			logger.error("handleIncomingMessage(): Event listener invoking error", e);
		}
	 }

	 /**
	 * Receives Serial Port Events and reads Serial Port Data
	 */	
	public synchronized void serialEvent(SerialPortEvent serialPortEvent) {
		 if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			 try {
				 String messageLine=serialInput.readLine();
				 handleIncomingMessage(messageLine);
			 } catch (IOException ioException) {
				logger.error("serialEvent(): IO Exception: ", ioException);
			 }
		 }
	 }
	 
	/**
	 * Set the serial event handler
	 */
	private void setSerialEventHandler(SerialPortEventListener serialPortEventListenser) {
	    try {
	        // Add the serial port event listener
	        serialPort.addEventListener(serialPortEventListenser);
	        serialPort.notifyOnDataAvailable(true);
	    } catch (TooManyListenersException tooManyListenersException) {
			logger.error("open(): Too Many Listeners Exception: ", tooManyListenersException);
	    }
	}

	/**
	 * {@inheritDoc}
	 **/
	public boolean isConnected() {
		return connected;
	}
   
	/**
	 * {@inheritDoc}
	 **/
	public void close() {
       	logger.debug("close(): Closing Serial Connection!");

       	if(serialPort == null) {
    		connected = false;
       		return;
       	}

       	serialPort.removeEventListener();

       	if(serialInput != null) {
       		IOUtils.closeQuietly(serialInput);
       		serialInput = null;
		}

       	if(serialOutput != null) {
			IOUtils.closeQuietly(serialOutput);
			serialOutput = null;
		}
		
   		serialPort.close();
   		serialPort = null;
   		
		connected = false;
       	logger.debug("close(): Serial Connection Closed!");
	}
	
	/**
	 * {@inheritDoc}
	 **/
	public synchronized void addEventListener(DSCAlarmEventListener listener) {
		_listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 **/
	public synchronized void removeEventListener(DSCAlarmEventListener listener) {
		_listeners.remove(listener);		
	}
}
