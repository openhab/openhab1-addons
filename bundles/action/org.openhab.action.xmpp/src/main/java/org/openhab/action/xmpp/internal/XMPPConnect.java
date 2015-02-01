/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.xmpp.internal;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Dictionary;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.util.DNSUtil;
import org.jivesoftware.smack.util.dns.javax.JavaxResolver;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.geekplace.javapinning.JavaPinning;

/**
 * This class provides XMPP access. An account can be configured, which is then
 * used for sending and receiving messages.
 * 
 * @author Kai Kreuzer
 * @since 0.4.0
 */
public class XMPPConnect implements ManagedService {

	static {
		// Workaround for SMACK-635. This can be removed once Smack 4.1 (or higher) is used
		// See https://igniterealtime.org/issues/browse/SMACK-635
		DNSUtil.setDNSResolver(JavaxResolver.getInstance());
	}

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
	private static SecurityMode securityMode = SecurityMode.disabled;
	private static String tlsPin;

	private static boolean initialized = false;

	private static XMPPConnection connection;
	private static MultiUserChat chat;

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config == null) {
			return;
		}
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

		String securityModeString = (String) config.get("securitymode");
		if (securityModeString != null) {
			securityMode = SecurityMode.valueOf(securityModeString);
		}
		XMPPConnect.tlsPin = (String) config.get("tlspin");

		String users = (String) config.get("consoleusers");

		if (!StringUtils.isEmpty(users)) {
			XMPPConnect.consoleUsers = users.split(",");
		} else {
			XMPPConnect.consoleUsers = new String[0];
		}

		// check mandatory settings
		if (StringUtils.isEmpty(servername)) return;
		if (StringUtils.isEmpty(username)) return;
		if (StringUtils.isEmpty(password)) return;

		// set defaults for optional settings
		if (port == null) {
			port = 5222;
		}
		if (StringUtils.isEmpty(chatnickname)) {
			chatnickname = "openhab-bot";
		}

		establishConnection();
	}

	private static void establishConnection() {
		if (servername == null) {
			return;
		}
		ConnectionConfiguration config;
		// Create a connection to the jabber server on the given port
		if (proxy != null) {
			config = new ConnectionConfiguration(servername, port, proxy);
		} else {
			config = new ConnectionConfiguration(servername, port);
		}
		config.setSecurityMode(securityMode);
		if (tlsPin != null) {
			try {
				SSLContext sc = JavaPinning.forPin(tlsPin);
				config.setCustomSSLContext(sc);
			} catch (KeyManagementException | NoSuchAlgorithmException e) {
				logger.error("Could not create TLS Pin for XMPP connection", e);
			}
		}

		if (connection != null && connection.isConnected()) {
			try {
				connection.disconnect();
			} catch (NotConnectedException e) {
				logger.debug("Already disconnected", e);
			}
		}

		connection = new XMPPTCPConnection(config);

		try {
			connection.connect();
			connection.login(username, password, null);
			if (consoleUsers.length > 0) {
				ChatManager.getInstanceFor(connection).addChatListener(
						new XMPPConsole(consoleUsers));
				connection.addConnectionListener(new XMPPConnectionListener());
			}
			logger.info("Connection to XMPP as '{}' has been established. Is secure/encrypted: {}",
					connection.getUser(), connection.isSecureConnection());
			initialized = true;
		} catch (Exception e) {
			logger.error("Could not establish connection to XMPP server '" + servername + ":"
					+ port + "': {}", e.getMessage());
		}
	}

	private static void joinChat() throws NotInitializedException {
		if (chatroom == null) {
			return;
		}
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
			logger.info("Successfuly joined chat '{}' with nickname '{}'.", chatroom, chatnickname);
		} catch (XMPPException e) {
			logger.error("Could not join chat '{}' with nickname '{}': {}", chatroom, chatnickname,
					e.getMessage());
		} catch (SmackException e) {
			logger.error("Could not join chat '{}' with nickname '{}': {}", chatroom, chatnickname,
					e.getMessage());
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

	private static class XMPPConnectionListener extends AbstractConnectionListener {

		public void connectionClosed() {
			logger.debug("XMPP connection has been closed.");
			initialized = false;
		}

		public void connectionClosedOnError(Exception e) {
			// Log a warning and the *full* exception as the stacktrace could be useful to diagnose
			// the issue for uncommon exceptions besides e.g. a broken pipe
			logger.warn("XMPP connection has been closed on error: {}", e);
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

	}
}
