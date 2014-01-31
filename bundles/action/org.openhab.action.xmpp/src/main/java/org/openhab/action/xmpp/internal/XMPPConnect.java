/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.xmpp.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides XMPP access. An account can be configured, which is then
 * used for sending and receiving messages.
 * 
 * @author Kai Kreuzer
 * @since 0.4.0
 */
public class XMPPConnect implements ManagedService {

	static private final Logger logger = 
		LoggerFactory.getLogger(XMPPConnect.class);

	private static String servername;
	private static String proxy;
	private static Integer port;
	private static String username;
	private static String password;
	private static String chatroom;
	private static String chatnickname;
	private static String chatpassword;
	private static String[] consoleUsers;

	private static boolean initialized = false;

	private static XMPPConnection connection;
	private static MultiUserChat chat;

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			XMPPConnect.servername = (String) config.get("servername");
			XMPPConnect.proxy = (String) config.get("proxy");
			String portString = (String) config.get("port");
			if (portString != null) {
				XMPPConnect.port = Integer.valueOf(portString);
			}
			XMPPConnect.username = (String) config.get("username");
			XMPPConnect.password = (String) config.get("password");
			XMPPConnect.chatroom = (String) config.get("chatroom");
			XMPPConnect.chatnickname = (String) config.get("chatnickname");
			XMPPConnect.chatpassword = (String) config.get("chatpassword");

			String users = (String) config.get("consoleusers");

			if (!StringUtils.isEmpty(users)) {
				XMPPConnect.consoleUsers = users.split(",");
			} else {
				XMPPConnect.consoleUsers = new String[0];
			}

			// check mandatory settings
			if (servername == null || servername.isEmpty())
				return;
			if (username == null || username.isEmpty())
				return;
			if (password == null || password.isEmpty())
				return;

			// set defaults for optional settings
			if (port == null) {
				port = 5222;
			}
			if (chatnickname == null || chatnickname.isEmpty()) {
				chatnickname = "openhab-bot";
			}

			establishConnection();
		}
	}

	private static void establishConnection() {
		if (servername != null) {
			ConnectionConfiguration config;
			// Create a connection to the jabber server on the given port
			if (proxy != null) {
				config = new ConnectionConfiguration(servername, port, proxy);
			} else {
				config = new ConnectionConfiguration(servername, port);
			}

			if (connection != null && connection.isConnected()) {
				connection.disconnect();
			}

			connection = new XMPPConnection(config);

			try {
				connection.connect();
				connection.login(username, password);
				if (consoleUsers.length > 0) {
					connection.getChatManager().addChatListener(new XMPPConsole(consoleUsers));
					connection.addConnectionListener(new XMPPConnectionListener());
				}
				logger.info("Connection to XMPP as '{}' has been established.",
						username);
				initialized = true;
			} catch (XMPPException e) {
				logger.error("Could not establish connection to XMPP server '" + servername + ":" + port + "': {}", e.getMessage());
			} catch (NullPointerException e) {
				logger.error("Could not establish connection to XMPP server '" + servername + ":" + port + "'");
			}
		}
	}

	private static void joinChat() throws NotInitializedException {
		if (chatroom != null) {
			if (!initialized) {
				establishConnection();
				if (!initialized) {
					throw new NotInitializedException();
				}
			}	
			
			chat = new MultiUserChat(connection, chatroom);
			
			try {
				if (chatpassword != null) {
					chat.join(chatnickname, chatpassword);
				} else {
					chat.join(chatnickname);
				}
				logger.info("Successfuly joined chat '{}' with nickname '{}'.",
						chatroom, chatnickname);				
			} catch (XMPPException e) {
				logger.error("Could not join chat '{}' with nickname '{}': {}",
						chatroom, chatnickname, e.getMessage());
			}				
		}
	}
	
	/**
	 * returns the active connection which can be used to send messages
	 * 
	 * @return the XMPP connection
	 * @throws NotInitializedException
	 *             if the connection has not been established successfully
	 */
	public static XMPPConnection getConnection() throws NotInitializedException {
		if (!initialized) {
			establishConnection();
			if (!initialized) {
				throw new NotInitializedException();
			}
		}
		return connection;
	}
	
	/**
	 * returns the active chat which can be used to send messages to a room
	 * 
	 * @return the XMPP connection
	 * @throws NotInitializedException
	 *             if the chat has not been successfully joined
	 */
	public static MultiUserChat getChat() throws NotInitializedException {
		if (chat == null) {
			joinChat();	
		}		
		if (!chat.isJoined()) {
			joinChat();
			if (!chat.isJoined()) {
				throw new NotInitializedException();
			}
		}
		return chat;
	}	

	private static class XMPPConnectionListener implements ConnectionListener {

		public void connectionClosed() {
			logger.debug("XMPP connection has been closed.");
			initialized = false;
		}

		public void connectionClosedOnError(Exception e) {
			logger.info("XMPP connection has been closed on error: {}", e.getMessage());
			try {
				if (!connection.isConnected()) {
					initialized = false;
					getConnection();
				}
				logger.info("XMPP re-connection succeeded.");
			} catch (NotInitializedException nie) {
				logger.error("XMPP re-connection failed, giving up: {}", nie.getMessage());
			}
		}

		public void reconnectingIn(int s) {
		}

		public void reconnectionFailed(Exception e) {
			logger.debug("XMPP re-connection failed.", e);
		}

		public void reconnectionSuccessful() {
			try {
				if (!connection.isConnected()) {
					initialized = false;
					getConnection();
				}
				logger.debug("XMPP re-connection succeeded.");
			} catch (NotInitializedException e) {
				logger.debug("XMPP re-connection failed, giving up.");
			}
		}

	}
}
