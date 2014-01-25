/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.xmpp.internal;

import org.apache.commons.lang.ArrayUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.openhab.io.console.Console;
import org.openhab.io.console.ConsoleInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This console allows to access openHAB remotely through XMPP with
 * tools like e.g. GoogleTalk.
 * The standard console commands are supported, such as sending item updates,
 * commands or querying for items or their current statuses.
 * A user must be listed in the configuration in order to be permitted to send
 * messages to openHAB.
 * 
 * @author Kai Kreuzer
 * @since 0.4.0
 *
 */
public class XMPPConsole implements ChatManagerListener, MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(XMPPConsole.class);
	
	private String[] allowedUsers;

	public XMPPConsole(String[] userArray) {
		this.allowedUsers = userArray;
	}

	public void chatCreated(Chat chat, boolean arg1) {
		String chatUser = chat.getParticipant();
		if(chatUser.contains("/")) chatUser = chatUser.substring(0, chatUser.indexOf("/"));
		if(ArrayUtils.contains(allowedUsers, chatUser)) {
			chat.addMessageListener(this);
		} else {
			try {
				chat.sendMessage("Sorry, you are not allowed to send messages.");
				logger.warn("Received chat request from the unknown user '{}'.", chatUser);
			} catch (XMPPException e) {
				logger.warn("Error sending XMPP message: {}", e.getMessage());
			}
		}
	}

	public void processMessage(Chat chat, Message msg) {
		logger.debug("Received XMPP message: {} of type {}", msg.getBody(), msg.getType());
		if (msg.getType() != Message.Type.error) {
			String cmd = msg.getBody();
			String[] args = cmd.split(" ");
			ConsoleInterpreter.handleRequest(args, new ChatConsole(chat));
		}
	}
	
	/**
	 * An implementation of the {@link Console} interface, which sends
	 * all console output directly to the chat participant.
	 * 
	 * @author Kai Kreuzer
	 * @since 0.4.0
	 *
	 */
	private static class ChatConsole implements Console {
		
		private Chat chat;
		
		/* used to store strings for simply print commands until a println is sent */
		private StringBuffer sb;

		public ChatConsole(Chat chat) {
			this.chat = chat;
			this.sb = new StringBuffer();
		}

		public void print(String s) {
			sb.append(s);
		}

		public void println(String s) {
			String msg = sb.toString() + s;
			try {
				chat.sendMessage(msg);
			} catch (XMPPException e) {
				logger.error("Error sending message '{}': {}", msg, e.getMessage());
			}
			sb = new StringBuffer();
		}

		public void printUsage(String s) {
			try {
				chat.sendMessage("Usage: \n" + s);
			} catch (XMPPException e) {
				logger.error("Error sending message '{}': {}", s, e.getMessage());
			}
		}
	}
}
