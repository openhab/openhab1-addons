/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.openhab.binding.primare.internal.PrimareEventListener;
import org.openhab.binding.primare.internal.PrimareStatusUpdateEvent;
import org.openhab.binding.primare.internal.protocol.PrimareMessageFactory;
import org.openhab.binding.primare.internal.protocol.PrimareResponseFactory;

import org.openhab.core.types.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract base class for Primare communication. It contains the communication
 * methods and Primare model-specific message factories for converting 
 * OpenHAB commands to Primare messages.
 * 
 * 
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public abstract class PrimareConnector {

	private static final Logger logger = 
		LoggerFactory.getLogger(PrimareTCPConnector.class);

	protected String deviceId;
	protected byte[] buffer = new byte[1024];
	protected int total = 0;
	protected int double_dle_start = -1;

	protected List<PrimareEventListener> _listeners = new ArrayList<PrimareEventListener>();

	// Timestamps for supervising connection, might become useful some day
	protected Date bytesSentAt        = null;
	protected Date messageSentAt      = null;
	protected Date bytesReceivedAt    = null;
	protected Date messageReceivedAt  = null;
	
	// Change to true if we detect a broken connection which we are not
	// able to fix right away
	protected boolean connectionBroken = false;

	protected PrimareMessageFactory messageFactory = null;
	protected PrimareResponseFactory responseFactory = null;

	/**
	 * Add event listener, which will be invoked when status update is received from receiver.
	 **/
	public synchronized void addEventListener(PrimareEventListener listener) {
		_listeners.add(listener);
	}
	
	/**
	 * Remove event listener.
	 **/
	public synchronized void removeEventListener(PrimareEventListener listener) {
		_listeners.remove(listener);
	}

	
	/**
	 * Connect to device
	 * 
	 * @throws Exception
	 */
	public abstract void connect() throws Exception;


	/**
	 * Connect to device and set EventListener;
	 * 
	 * @throws Exception
	 */
	public void connectAndAddEventListener(PrimareEventListener listener) throws Exception {
		connect();
		addEventListener(listener);
	}


	/**
	 * Disconnect from device
	 * 
	 */
	public abstract void disconnect();


	/**
	 * Connect to device and set EventListener;
	 * 
	 * @throws Exception
	 */
	public void disconnectAndRemoveEventListener(PrimareEventListener listener) throws Exception {
		disconnect();
		removeEventListener(listener);
	}


	/**
	 * Are we connected? 
	 * 
	 * @throws Exception
	 */
	public abstract boolean isConnected();



	/**
	 * Send bytes to device (no escaping)
	 * 
	 * @throws Exception
	 */
	public abstract void sendBytes(byte[] msg) throws Exception;

	/* Method sendMessage implements standard Primare escaping:
	   - If command, variable or value is equal to 16 (DLE), it is transmitted twice
	   (double DLE)
	   Override this in concrete class if necessary
	*/
	private void sendMessagePart(byte[] msg) throws Exception {
		sendBytes(PrimareUtils.escapeMessage(msg));
		messageSentAt = new Date();
	}

	private void sendMessage(byte[][] msgs) throws Exception {
		for (byte[] msg : msgs)
			sendMessagePart(msg);
	}
	
	public void sendMessage(PrimareMessage deviceMsg) throws Exception {
		sendMessage(deviceMsg.getMessageParts());
		messageSentAt = new Date();
	}
    
	public void sendMessage(PrimareMessage[] deviceMsgs) throws Exception {
		for (PrimareMessage deviceMsg : deviceMsgs)
			sendMessage(deviceMsg);
	}

	
	public void sendCommand(Command command, String deviceCmdString) throws Exception {
		sendMessage(messageFactory.getMessage(command, deviceCmdString));
	}
	
	
	public PrimareMessageFactory getMessageFactory() {
		return messageFactory;
	}
	
	public PrimareResponseFactory getResponseFactory() {
		return responseFactory;
	}
	
	public void sendPingMessages() throws Exception {
		PrimareMessage[] pms = messageFactory.getPingMessages();
		
		if (pms != null) {
			sendMessage(pms);
		}
	}

	public void sendInitMessages() throws Exception {
		PrimareMessage[] ims = messageFactory.getInitMessages();
		
		if (ims != null) {
			sendMessage(ims);
		}
	}



	protected int parseData(int i) {
		logger.trace("Response from {} parse index:{} containing:{}",
			     PrimareConnector.this.toString(), i, buffer[i]);
			
		if (buffer[i] == (byte)0x10) {  // DLE
			if (double_dle_start == i-1) {
				// seeing double DLE, deal with it later
				double_dle_start = -1;
			} else {
				double_dle_start = i;
			}
		} else if (buffer[i] != (byte)0x03)  { // non-ETX
			double_dle_start = -1;
		}
			
		if (buffer[i] == (byte)0x03 && double_dle_start == i-1) {
				
			logger.trace("Response from {} DLE ETX seen", PrimareConnector.this.toString());
				
			// End-flag received, do some work with the data
			// Increment i since we need to include the end-byte
			i++;
			// Copy the message
			byte[] data = Arrays.copyOf(buffer, i);
			// Copy the buffer onto itself to remove the message from buffer
			System.arraycopy(buffer, i, buffer, 0, total - i);
			total = total - i;
				
			logger.trace("Response from {} received {} bytes: (hex) [{}]",
				     PrimareConnector.this.toString(), data.length, PrimareUtils.byteArrayToHex(data));
				
			if (data[0] == (byte)0x02) {
				logger.trace("Response from {} start consuming command response (hex) {}",
					     PrimareConnector.this.toString(), PrimareUtils.byteArrayToHex(data));
					
				messageReceivedAt = new Date();
					
				// send (unescaped) message to event listeners
				try {
					Iterator<PrimareEventListener> iterator = _listeners.iterator();
						
					while (iterator.hasNext()) {
						((PrimareEventListener) iterator.next())
							.statusUpdateReceived(new PrimareStatusUpdateEvent(this),
									      deviceId,
									      PrimareUtils.unescapeMessage(data));
					}
						
				} catch (Exception e) {
					logger.error("Response from {} event listener invoking error - {}",
						     PrimareConnector.this.toString(), e);
				}

				logger.trace("Response from {} consumed command response (hex) {}",
					     PrimareConnector.this.toString(), PrimareUtils.byteArrayToHex(data));
			} else {
				// If a status message exists, implement handling here
			}
		}
		return i;
	}
		


}
