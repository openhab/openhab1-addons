/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.openhab.binding.zwave.internal.config.ZWaveDbCommandClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Handles the NodeNaming command class.
 * The indicator command class operates the indicator on the physical device if available. 
 * This can be used to identify a device or use the indicator for special purposes.
 * Example is the Evolve LCD panel that uses the NodeNaming class to toggle the labels 
 * displayed on the LCD. The NodeNaming class is also used to sync multiple panels' labels
 * 
 * @author Pedro Paixao
 * @since 1.8.0
 */

@XStreamAlias("nameLocationCommandClass")
public class ZWaveNodeNamingCommandClass extends ZWaveCommandClass {

	@XStreamOmitField
	private static final Logger logger = LoggerFactory.getLogger(ZWaveNodeNamingCommandClass.class);
	
	private static final int NAME_SET = 0x01;
	private static final int NAME_GET = 0x02;
	private static final int NAME_REPORT = 0x03;

	private static final int LOCATION_SET = 0x04;
	private static final int LOCATION_GET = 0x05;
	private static final int LOCATION_REPORT = 0x06;
	
	private static final int ENCODING_ASCII = 0x00;
	private static final int ENCODING_EXTENDED_ASCII = 0x01;
	private static final int ENCODING_UTF16 = 0x02;

	private static final int MAX_STRING_LENGTH = 16;
	
	private boolean isGetSupported = true;

	/**
	 * Creates a new instance of the ZWaveNameLocationCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveNodeNamingCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.NODE_NAMING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.debug("NODE {}: Received NodeNaming Request", this.getNode().getNodeId());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			
			case NAME_SET:
				logger.debug("NODE {}: Name Set sent to the controller will be processed as Name Report", this.getNode().getNodeId());
				// Process this as if it was a value report.
				processNameReport(serialMessage, offset, endpoint);
				break;
				
			case LOCATION_SET:
				logger.debug("NODE {}: Location Set sent to the controller will be processed as Location Report", this.getNode().getNodeId());
				// Process this as if it was a value report.
				processLocationReport(serialMessage, offset, endpoint);
				break;
				
			case LOCATION_GET:
			case NAME_GET:
				logger.warn(String.format("Command 0x%02X not implemented.", command));
				return;
				
			case NAME_REPORT:
				logger.trace("NODE {}: Process Name Report", this.getNode().getNodeId());
				processNameReport(serialMessage, offset, endpoint);
				break;
				
			case LOCATION_REPORT:
				logger.trace("NODE {}: Process Location Report", this.getNode().getNodeId());
				processLocationReport(serialMessage, offset, endpoint);
				break;
				
			default:
				logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", 
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}
	
	/**
	 * Get a string from the serial message
	 * @param serialMessage
	 * @param offset
	 * @return String
	 */
	protected String getString(SerialMessage serialMessage, int offset) { 
		int charPresentation = serialMessage.getMessagePayloadByte(offset + 1);
		
		// First 5 bits are reserved so 0 them
		charPresentation = 0x07 & charPresentation;
		
		switch (charPresentation) {
			case ENCODING_ASCII:
				logger.info("NODE {} : Node Name is encoded with standard ASCII codes", this.getNode().getNodeId());
				break;
			case ENCODING_EXTENDED_ASCII:
				logger.info("NODE {} : Node Name is encoded with Using standard and OEM Extended ASCII codes", this.getNode().getNodeId());
				break;
			case ENCODING_UTF16:
				logger.info("NODE {} : Node Name is encoded with Unicode UTF-16", this.getNode().getNodeId());
				break;
			default:
				logger.error("NODE {} : Node Name encodeding is unsupported. Encoding code {}", this.getNode().getNodeId(), charPresentation);
				return null;
		}
				
		int numBytes = serialMessage.getMessagePayload().length - 3;
		
		if(numBytes <= 0) {
			logger.error("NODE {} : Node Name report error in message length", this.getNode().getNodeId());
			return null;
		}
		
		// Maximum length is 16 bytes
		if( numBytes > MAX_STRING_LENGTH) {
			logger.warn("NODE {} : Node Name is too big maximum is {} characters {}", this.getNode().getNodeId(), MAX_STRING_LENGTH, numBytes);
			numBytes = MAX_STRING_LENGTH;
		}
		
		byte[] strBuffer = Arrays.copyOfRange(serialMessage.getMessageBuffer(), 3, serialMessage.getMessagePayload().length);
		
		try {
			switch(charPresentation) {
				case ENCODING_ASCII:
				case ENCODING_EXTENDED_ASCII:
					return new String(strBuffer, "ASCII");
					
				case ENCODING_UTF16:
					String sTemp = new String(strBuffer, "UTF-16");
					return new String(sTemp.getBytes("UTF-8"),"UTF-8");
			}
		}
		catch (UnsupportedEncodingException  uee ) {
			System.out.println( "Exception: "  + uee);
		}
		return null;
	}

	/**
	 * Processes a NAME_REPORT / NAME_SET message.
	 * @param serialMessage the incoming message to process.
	 * @param offset the offset position from which to start message processing.
	 * @param endpoint the endpoint or instance number this message is meant for.
	 */
	protected void processNameReport(SerialMessage serialMessage, int offset, int endpoint) {
		String name = getString(serialMessage, offset);
		
		logger.info("NODE {}: Node name: {}", this.getNode().getNodeId(), name);
		ZWaveCommandClassValueEvent zEvent = new ZWaveCommandClassValueEvent(this.getNode().getNodeId(), endpoint, this.getCommandClass(), name);
		this.getController().notifyEventListeners(zEvent);
	}
	
	/**
	 * Processes a LOCATION_REPORT / LOCATION_SET message.
	 * @param serialMessage the incoming message to process.
	 * @param offset the offset position from which to start message processing.
	 * @param endpoint the endpoint or instance number this message is meant for.
	 */
	protected void processLocationReport(SerialMessage serialMessage, int offset, int endpoint) {
		String location = getString(serialMessage, offset);
		
		logger.info("NODE {}: Node location: {}", this.getNode().getNodeId(), location);
		ZWaveCommandClassValueEvent zEvent = new ZWaveCommandClassValueEvent(this.getNode().getNodeId(), endpoint, this.getCommandClass(), location);
		this.getController().notifyEventListeners(zEvent);
	}

	/**
	 * Gets a SerialMessage with the NAME GET command 
	 * @return the serial message
	 */
	public SerialMessage getNameMessage() {
		if(isGetSupported == false) {
			logger.debug("NODE {}: Node doesn't support get requests", this.getNode().getNodeId());
			return null;
		}

		logger.debug("NODE {}: Creating new message for application command NAME_GET", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) NAME_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}

	/**
	 * Gets a SerialMessage with the NAME GET command 
	 * @return the serial message
	 */
	public SerialMessage getLocationMessage() {
		if(isGetSupported == false) {
			logger.debug("NODE {}: Node doesn't support get requests", this.getNode().getNodeId());
			return null;
		}

		logger.debug("NODE {}: Creating new message for application command LOCATION_GET", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) LOCATION_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}

	
	@Override
	public boolean setOptions (ZWaveDbCommandClass options) {
		if(options.isGetSupported != null) {
			isGetSupported = options.isGetSupported;
		}
		
		return true;
	}
	
	/**
	 * Gets a SerialMessage with the Name or Location SET command 
	 * @param the level to set.
	 * @return the serial message
	 */
	private SerialMessage setValueMessage(String str, int command) {
		logger.debug("NODE {}: Creating new message for application command NAME_SET to {}", this.getNode().getNodeId(), str);
		
		byte[] nameBuffer = null;
		try {
			nameBuffer = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
    	int len = nameBuffer.length;
    	if( len > 16 ) {
    		len = 16;
    	}
    	
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							(byte) ((byte) len + 3), 
								(byte) getCommandClass().getKey(), 
								(byte) command,
								(byte) ENCODING_UTF16
								};
    	
    	byte[] msg = new byte[ newPayload.length + len];
    	System.arraycopy(newPayload, 0, msg, 0, newPayload.length);
    	System.arraycopy(nameBuffer, 0, msg, newPayload.length, len);
    	
    	result.setMessagePayload(msg);
    	return result;		
	}
	
	public SerialMessage setNameMessage(String name) {
		return setValueMessage(name, NAME_SET);
	}
	
	public SerialMessage setLocationMessage(String location) {
		return setValueMessage(location, NAME_SET);
	}

}
