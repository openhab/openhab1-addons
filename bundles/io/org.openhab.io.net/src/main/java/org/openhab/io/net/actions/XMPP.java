/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.io.net.actions;

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
import org.openhab.io.net.internal.jabber.XMPPConnect;
import org.openhab.io.net.internal.jabber.NotInitializedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * This class provides static methods that can be used in automation rules
 * for sending XMPP messages. 
 * 
 * @author Kai Kreuzer
 * @since 0.4.0
 *
 */
public class XMPP {

	static private final Logger logger = LoggerFactory.getLogger(XMPP.class);

	/**
	 * Sends a message to an XMPP user.
	 * 
	 * @param to the XMPP address to send the message to
	 * @param message the message to send
	 */
	static public void send(String to, String message) {
		try {
			XMPPConnection conn = XMPPConnect.getConnection();

			ChatManager chatmanager = conn.getChatManager();
			Chat newChat = chatmanager.createChat(to, null);

			try {
				newChat.sendMessage(message);
				logger.debug("Sent message '{}' to '{}'.", message, to);
			} catch (XMPPException e) {
				System.out.println("Error Delivering block");
			}
		} catch (NotInitializedException e) {
			logger.warn("Could not send XMPP message as connection is not correctly initialized!");
		}
	}

	/**
	 * Sends a message with an attachment to an XMPP user.
	 * 
	 * @param to the XMPP address to send the message to
	 * @param message the message to send
	 * @param attachmentUrl a URL string of which the content should be send to the user
	 */
	static public void send(String to, String message, String attachmentUrl) {
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
				} catch (XMPPException e) {
					System.out.println("Error Delivering block");
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
				} catch (IOException e) {
					logger.error("Could not open url '{}' for sending it via XMPP", attachmentUrl, e);
				}
			}
		} catch (NotInitializedException e) {
			logger.warn("Could not send XMPP message as connection is not correctly initialized!");
		}
	}
}
