/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.xmpp.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class contains the methods that are made available in scripts and rules for XMPP.
 * 
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class XMPP {

	private static final Logger logger = LoggerFactory.getLogger(XMPP.class);

	// provide public static methods here
	
	/**
	 * Sends a message to an XMPP user.
	 * 
	 * @param to the XMPP address to send the message to
	 * @param message the message to send
	 * 
	 * @return <code>true</code>, if sending the message has been successful and 
	 * <code>false</code> in all other cases.
	 */
	@ActionDoc(text="Sends a message to an XMPP user.")
	static public boolean sendXMPP(
			@ParamDoc(name="to") String to, 
			@ParamDoc(name="message") String message) {
		boolean success = false;
		
		try {
			XMPPConnection conn = XMPPConnect.getConnection();

			ChatManager chatmanager = conn.getChatManager();
			Chat newChat = chatmanager.createChat(to, null);

			try {
				while(message.length()>=2000) {
					newChat.sendMessage(message.substring(0, 2000));
					message = message.substring(2000);
				}
				newChat.sendMessage(message);
				logger.debug("Sent message '{}' to '{}'.", message, to);
				success = true;
			} catch (XMPPException e) {
				System.out.println("Error Delivering block");
			}
		} catch (NotInitializedException e) {
			logger.warn("Could not send XMPP message as connection is not correctly initialized!");
		}
		
		return success;
	}

	/**
	 * Sends a message with an attachment to an XMPP user.
	 * 
	 * @param to the XMPP address to send the message to
	 * @param message the message to send
	 * @param attachmentUrl a URL string of which the content should be send to the user
	 * 
	 * @return <code>true</code>, if sending the message has been successful and 
	 * <code>false</code> in all other cases.
	 */
	@ActionDoc(text="Sends a message with an attachment to an XMPP user.")
	static public boolean sendXMPP(
			@ParamDoc(name="to") String to, 
			@ParamDoc(name="message") String message,
			@ParamDoc(name="attachmentUrl") String attachmentUrl) {
		boolean success = false;
		
		try {
			XMPPConnection conn = XMPPConnect.getConnection();

			if (attachmentUrl == null) {
				// send a normal message without an attachment
				ChatManager chatmanager = conn.getChatManager();
				Chat newChat = chatmanager.createChat(to, new MessageListener() {
					public void processMessage(Chat chat, Message message) {
						logger.debug("Received message on XMPP: {}", message.getBody());
					}
				});
				try {
					newChat.sendMessage(message);
					logger.debug("Sent message '{}' to '{}'.", message, to);
					success = true;
				} catch (XMPPException e) {
					logger.error("Error sending message '{}'", message, e);
				}
			} else {
				// Create the file transfer manager
				FileTransferManager manager = new FileTransferManager(conn);

				// Create the outgoing file transfer
				OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(to);

				try {
					URL url = new URL(attachmentUrl);
					// Send the file
					InputStream is = url.openStream();
					OutgoingFileTransfer.setResponseTimeout(10000);
					transfer.sendStream(is, url.getFile(), is.available(), message);
					logger.debug("Sent message '{}' with attachment '{}' to '{}'.", new String[] { message, attachmentUrl, to });
					is.close();
					success = true;
				} catch (IOException e) {
					logger.error("Could not open url '{}' for sending it via XMPP", attachmentUrl, e);
				}
			}
		} catch (NotInitializedException e) {
			logger.warn("Could not send XMPP message as connection is not correctly initialized!");
		}
		
		return success;
	}
	
	/**
	 * Sends a message to an XMPP multi user chat.
	 * 
	 * @param message the message to send
	 * 
	 * @return <code>true</code>, if sending the message has been successful and 
	 * <code>false</code> in all other cases.
	 */
	@ActionDoc(text="Sends a message to an XMPP multi user chat.")
	static public boolean chatXMPP(
			@ParamDoc(name="message") String message) {
		boolean success = false;
		
		try {
			MultiUserChat chat = XMPPConnect.getChat();

			try {
				while(message.length()>=2000) {
					chat.sendMessage(message.substring(0, 2000));
					message = message.substring(2000);
				}
				chat.sendMessage(message);
				logger.debug("Sent message '{}' to multi user chat.", message);
				success = true;
			} catch (XMPPException e) {
				System.out.println("Error Delivering block");
			}
		} catch (NotInitializedException e) {
			logger.warn("Could not send XMPP message as connection is not correctly initialized!");
		}
		
		return success;
	}	
	
}
