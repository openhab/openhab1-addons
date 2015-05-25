/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.maxcube.internal.exceptions.IncompleteMessageException;
import org.openhab.binding.maxcube.internal.exceptions.IncorrectMultilineIndexException;
import org.openhab.binding.maxcube.internal.exceptions.MessageIsWaitingException;
import org.openhab.binding.maxcube.internal.exceptions.NoMessageAvailableException;
import org.openhab.binding.maxcube.internal.exceptions.UnprocessableMessageException;
import org.openhab.binding.maxcube.internal.exceptions.UnsupportedMessageTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The message processor was introduced to combine multiple received lines to
 * one single message. There are cases, when the MAX!Cube sends multiple
 * messages (M-Message for example). The message processor acts as stack for
 * received messages. Every received line should be added to the processor.
 * After every added line, the message processor analyses the line. It is not
 * possible to add additional lines when there is a message ready to be
 * processed.
 * 
 * @author Christian Rockrohr <christian@rockrohr.de>
 * @since 1.7.0
 */
public class MessageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
	
	public static final String SEPARATOR = ":";
	
	/**
	 * The message that was created from last line received. (Null if no message
	 * available yet)
	 */
	private Message currentMessage = null;

	/**
	 * <pre>
	 * If more that one single line is required to create a message
	 * 	  	numberOfRequiredLines holds the number of required messages to complete
	 * 		receivedLines holds the lines received so far
	 * 		currentMessageType indicates which message type is currently on stack
	 * </pre>
	 */
	private Integer numberOfRequiredLines = null;
	private List<String> receivedLines = new ArrayList<String>();
	private MessageType currentMessageType = null;

	/**
	 * Resets the current status and processed lines. Should be used after
	 * processing a message
	 */
	public void reset() {
		this.currentMessage = null;
		receivedLines.clear();
		currentMessageType = null;
		numberOfRequiredLines = null;
	}

	/**
	 * Analyses the line and creates a message when possible. If the line
	 * indicates, that additional lines are required to create a complete
	 * message, the message processor keeps the line in memory and awaits
	 * additional lines. If the new line does not fit into current state
	 * (incomplete M: message on stack but L: message line received) a
	 * IncompleteMessageException is thrown.
	 * 
	 * @param line
	 *            is the new line received
	 * @return true if a message could be created by this line, false in any
	 *         other cases (line was stacked, error, ...)
	 * @throws MessageIsWaitingException
	 *             when a line was added without pulling the previous message
	 * @throws IncompleteMessageException
	 *             when a line was added that does not belong to current message
	 *             stack
	 * @throws UnsupportedMessageTypeException
	 *             in case the line starts with an unknown message indicator
	 * @throws UnprocessableMessageException
	 *             is thrown when there was a known message indicator found, but
	 *             message could not be parsed correctly.
	 * @throws IncorrectMultilineIndexException 
	 */
	public Boolean addReceivedLine(String line) throws IncompleteMessageException, MessageIsWaitingException,
			UnsupportedMessageTypeException, UnprocessableMessageException, IncorrectMultilineIndexException {

		if (this.currentMessage != null) {
			throw new MessageIsWaitingException();
		}

		MessageType messageType = getMessageType(line);

		if (messageType == null) {
			throw new UnsupportedMessageTypeException();
		}

		if ((this.currentMessageType != null) && (!messageType.equals(this.currentMessageType))) {
			throw new IncompleteMessageException();
		}

		Boolean result = true;

		switch (messageType) {
		case H:
			this.currentMessage = new H_Message(line);
			break;
		case C:
			this.currentMessage = new C_Message(line);
			break;
		case L:
			this.currentMessage = new L_Message(line);
			break;
		case S:
			this.currentMessage = new S_Message(line);
			break;
		case M:
			result = handle_M_MessageLine(line);
			break;
		default:
		}

		return result;
	}

	private Boolean handle_M_MessageLine(String line) throws UnprocessableMessageException, IncompleteMessageException, IncorrectMultilineIndexException {
		Boolean result = false;

		String[] tokens = line.split(Message.DELIMETER);  //M:00,01,xyz.....

		try {
			Integer index = Integer.valueOf(tokens[0].replaceFirst("M:", ""));  //M:00
			Integer counter = Integer.valueOf(tokens[1]);  //01

			if (this.numberOfRequiredLines == null) {
				switch (counter) {
				case 0:
					throw new UnprocessableMessageException();
				case 1:
					this.currentMessage = new M_Message(line);
					result = true;
					break;
				default:
					this.numberOfRequiredLines = counter;
					this.currentMessageType = MessageType.M;
					if (index == 0) {
						this.receivedLines.add(line);
					} else {
						throw new IncorrectMultilineIndexException();
					}
				}
			} else {
				if ((!counter.equals(this.numberOfRequiredLines)) || (!(index == this.receivedLines.size()))) {
					throw new IncorrectMultilineIndexException();
				}
				
				receivedLines.add(tokens[2]);
				
				if (index+1 == receivedLines.size()) {
					String newLine = "";
					for (String curLine : receivedLines)
						newLine += curLine;
					this.currentMessage = new M_Message(newLine);
					result = true;
				}
			}
		} catch (IncorrectMultilineIndexException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new UnprocessableMessageException();
		}

		return result;
	}
	
	/**
	 * @return true if there is a message waiting to be pulled
	 */
	public boolean isMessageAvailable() {
		return this.currentMessage != null;
	}

	/**
	 * Pulls the message from the stack when there is one available. This needs
	 * to be done before next line can be added into message processor. When
	 * message is pulled, the message processor is reseted and ready to process
	 * next line.
	 * 
	 * @return Message
	 * @throws NoMessageAvailableException
	 *             when there was no message on the stack
	 */
	public Message pull() throws NoMessageAvailableException {
		Message result = null;

		if (this.currentMessage == null) {
			throw new NoMessageAvailableException();
		} else {
			result = this.currentMessage;
			reset();
		}

		return result;
	}

	/**
	 * Processes the raw TCP data read from the MAX protocol, returning the
	 * corresponding MessageType.
	 * 
	 * @param line
	 *            the raw data provided read from the MAX protocol
	 * @return MessageType of the line added
	 */
	private static MessageType getMessageType(String line) {

		for (MessageType msgType : MessageType.values()) {
			if (line.startsWith(msgType.name() + SEPARATOR)) {
				return msgType;
			}
		}

		return null;
	}
}
