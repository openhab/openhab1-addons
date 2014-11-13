/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.dscalarm.internal.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openhab.binding.dscalarm.internal.DSCAlarmEvent;
import org.openhab.binding.dscalarm.internal.DSCAlarmEventListener;
import org.openhab.binding.dscalarm.internal.protocol.APIMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that establishes a TCP Socket connection to the EyezOn Envisalink 3/2DS interface
 * 
 * @author Russell Stephens
 * @since 1.6.0
 */
public class TCPConnector implements DSCAlarmConnector {
	private static final Logger logger = LoggerFactory.getLogger(TCPConnector.class);

	String ipAddress = "";
	int tcpPort;
	int connectTimeout;
	private Socket tcpSocket = null;
	private OutputStreamWriter tcpOutput = null;
	private BufferedReader tcpInput = null;
	private TCPListener TCPListener = null;
	private DSCAlarmConnectorType connectorType = DSCAlarmConnectorType.TCP;
	private static boolean connected = false;
	private static List<DSCAlarmEventListener> _listeners = new ArrayList<DSCAlarmEventListener>();
	
	/**
	 * Constructor.
	 **/
	public TCPConnector(String ip, int port, int timeout) {
		ipAddress = ip;
		tcpPort = port;
		connectTimeout = timeout;
	}
	
	/**
	 * Returns Connector Type
	 **/
	public DSCAlarmConnectorType getConnectorType() {
		return connectorType;		
	}
	
	/**
	 * {@inheritDoc}
	 **/
	public void write(String writeString) {
        try {
        	tcpOutput.write(writeString);
            tcpOutput.flush();
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
        	message = tcpInput.readLine();
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
    
	/**
	 * {@inheritDoc}
	 **/
   public void open() {
        try {
        	tcpSocket = new Socket();
            SocketAddress TPIsocketAddress = new InetSocketAddress(ipAddress, tcpPort);
            tcpSocket.connect(TPIsocketAddress, connectTimeout);
			tcpOutput = new OutputStreamWriter(tcpSocket.getOutputStream(), "US-ASCII");
            tcpInput = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            connected = true;
            
			//Start the TCP Listener
	    	TCPListener = new TCPListener();
	    	TCPListener.start();
        }
        catch (UnknownHostException exception) {
			logger.error("open(): Unknown Host Exception: ", exception);
            connected = false;
        }
		catch (SocketException socketException) {
			logger.error("open(): Socket Exception: ", socketException);
            connected = false;
        }
		catch (IOException ioException) {
			logger.error("open(): IO Exception: ", ioException);
            connected = false;
        }
        catch (Exception exception) {
			logger.error("open(): Exception: ", exception);
            connected = false;
        }
    }

	 /**
	  * Handles an incoming  message
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
	 * {@inheritDoc}
	 **/
	 public boolean isConnected() {
		 return connected;
	 }
   
	/**
	 * {@inheritDoc}
	 **/
	 public void close() {
		try {
			if (tcpSocket != null) {
				tcpSocket.close();
				tcpSocket = null;
			}
			if (tcpInput != null) {
				tcpInput.close();
				tcpInput = null;
			}
			if (tcpOutput != null) {
				tcpOutput.close();
				tcpOutput = null;
			}
			connected = false;
			logger.debug("close(): Closed TCP Connection!");
		}
		catch (IOException ioException) {
			logger.error("close(): Unable to close connection - " + ioException.getMessage());
		}
        catch (Exception exception) {
        	logger.error("close(): Error closing connection - " + exception.getMessage());
        }
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

	/**
	 * TCPMessageListener Thread. Receives  messages from the DSC Alarm Panel API.
	 */	
	private class TCPListener extends Thread {
		private final Logger logger = LoggerFactory.getLogger(TCPListener.class);
	
		public TCPListener() {
		}
		
		/**
		 * Run method. Runs the MessageListener thread
		 */
		@Override
		public void run() {
			String messageLine;
			
			try {
				while(connected) {
					if((messageLine = read()) != null) {
						handleIncomingMessage(messageLine);
					}
				}
			}
			catch(Exception e) {
				logger.error("TCPListener(): Unable to read message: ", e);
			}
		}
	}
}
