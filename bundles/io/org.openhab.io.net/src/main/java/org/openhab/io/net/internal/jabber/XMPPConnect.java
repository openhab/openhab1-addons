/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.io.net.internal.jabber;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides XMPP access. An account can be configured, which is
 * then used for sending and receiving messages.
 * 
 * @author Kai Kreuzer
 * @since 0.4.0
 *
 */
public class XMPPConnect implements ManagedService {

	static private final Logger logger = LoggerFactory.getLogger(XMPPConnect.class);
	
	private static String servername;
	private static Integer port;
	private static String username;
	private static String password;
	private static String[] consoleUsers;
	
	private static boolean initialized = false;

	private static XMPPConnection connection;

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
		if(config!=null) {
			XMPPConnect.servername = (String) config.get("servername");
			String portString = (String) config.get("port");
			if(portString!=null) {
				XMPPConnect.port = Integer.valueOf(portString);
			}
			XMPPConnect.username = (String) config.get("username");
			XMPPConnect.password = (String) config.get("password");
			
			String users = (String) config.get("consoleusers");
			if(!StringUtils.isEmpty(users)) {
				XMPPConnect.consoleUsers = users.split(",");
			}
						
			// check mandatory settings
			if(servername==null || servername.isEmpty()) return;
			if(username==null || username.isEmpty()) return;
			if(password==null || password.isEmpty()) return;
			
			// set defaults for optional settings
			if(port==null) {
				port = 5222;
			}
			
			establishConnection();			
		}
	}

	private static void establishConnection() {
		if(servername!=null) {
			// Create a connection to the jabber server on the given port
			ConnectionConfiguration config = new ConnectionConfiguration(servername, port);
			if(connection!=null && connection.isConnected()) {
				connection.disconnect();
			}
			connection = new XMPPConnection(config);
			try {
				connection.connect();
				connection.login(username, password);
				if(consoleUsers.length>0) {
					connection.getChatManager().addChatListener(new XMPPConsole(consoleUsers));
					connection.addConnectionListener(new XMPPConnectionListener());
				}
				logger.debug("Connection to XMPP as '{}' has been established.", username);
				initialized = true;
			} catch (XMPPException e) {
				logger.error("Could not establish connection to XMPP server '" + servername + ":" + port +"'", e);
			}
		}
	}
	
	/**
	 * returns the active connection which can be used to send messages
	 * 
	 * @return the XMPP connection
	 * @throws NotInitializedException if the connection has not been established successfully
	 */
	public static XMPPConnection getConnection() throws NotInitializedException {
		if(!initialized) {
			establishConnection();
			if(!initialized) {
				throw new NotInitializedException();
			}
		}
		return connection;
	}

	private static class XMPPConnectionListener implements ConnectionListener {

		@Override
		public void connectionClosed() {
			logger.debug("XMPP connection has been closed.");
		}

		@Override
		public void connectionClosedOnError(Exception e) {
			logger.debug("XMPP connection has been closed on error.", e);
			try {
				if(!connection.isConnected()) {
					initialized = false;
					getConnection();
				}
				logger.debug("XMPP re-connection succeeded.");
			} catch(NotInitializedException nie) {
				logger.debug("XMPP re-connection failed, giving up.", nie);
			}
		}

		@Override
		public void reconnectingIn(int s) {
		}

		@Override
		public void reconnectionFailed(Exception e) {
			logger.debug("XMPP re-connection failed.", e);
		}

		@Override
		public void reconnectionSuccessful() {
			try {
				if(!connection.isConnected()) {
					initialized = false;
					getConnection();
				}
				logger.debug("XMPP re-connection succeeded.");
			} catch(NotInitializedException e) {
				logger.debug("XMPP re-connection failed, giving up.");
			}
		}
		
	}
}
