/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal.hardware;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.openhab.binding.fritzaha.internal.hardware.callbacks.FritzahaCallback;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.State;
import org.openhab.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles requests to a Fritz!OS web interface for interfacing with
 * AVM home automation devices. It manages authentication and wraps commands.
 * 
 * @author Christian Brauers
 * @since 1.3.0
 */
public class FritzahaWebInterface {
	/**
	 * Host name of web interface
	 */
	protected String host;
	/**
	 * Port under which web interface can be accessed
	 */
	protected int port;
	/**
	 * Protocol to use to access web interface (http or https)
	 */
	protected String protocol;
	/**
	 * Username to use for the web interface (if configured)
	 */
	protected String username;
	/**
	 * Password to the web interface
	 */
	protected String password;
	/**
	 * Current session ID
	 */
	protected String sid;
	/**
	 * HTTP client for asynchronous calls
	 */
	protected HttpClient asyncclient;
	/**
	 * Timeout for synchronous HTTP requests to web interface in milliseconds
	 */
	protected int timeout;
	/**
	 * Timeout for asynchronous HTTP requests to web interface in milliseconds
	 */
	protected int asynctimeout;
	/**
	 * Maximum number of simultaneous asynchronous connections
	 */
	protected int asyncmaxconns = 20;
	/**
	 * Event publisher used by binding
	 */
	protected EventPublisher eventPublisher;

	static final Logger logger = LoggerFactory.getLogger(FritzahaWebInterface.class);
	// Uses RegEx to handle bad FritzBox XML
	/**
	 * RegEx Pattern to grab the session ID from a login XML response
	 */
	protected static final Pattern SID_PATTERN = Pattern.compile("<SID>([a-fA-F0-9]*)</SID>");
	/**
	 * RegEx Pattern to grab the challenge from a login XML response
	 */
	protected static final Pattern CHALLENGE_PATTERN = Pattern.compile("<Challenge>(\\w*)</Challenge>");
	/**
	 * RegEx Pattern to grab the access privilege for home automation functions
	 * from a login XML response
	 */
	protected static final Pattern ACCESS_PATTERN = Pattern
			.compile("<Name>HomeAuto</Name>\\s*?<Access>([0-9])</Access>");

	/**
	 * This method authenticates with the Fritz!OS Web Interface and updates the
	 * session ID accordingly
	 * 
	 * @return New session ID
	 */
	public String authenticate() {
		String loginXml = HttpUtil.executeUrl("GET", getURL("login_sid.lua", addSID("")), 10 * timeout);
		if (loginXml == null) {
			logger.error("FritzBox does not respond");
			return null;
		}
		Matcher sidmatch = SID_PATTERN.matcher(loginXml);
		if (!sidmatch.find()) {
			logger.error("FritzBox does not respond with SID");
			logger.debug("Output:\n" + loginXml);
			return null;
		}
		sid = sidmatch.group(1);
		Matcher accmatch = ACCESS_PATTERN.matcher(loginXml);
		if (accmatch.find()) {
			if (accmatch.group(1) == "2")
				logger.info("Resuming FritzBox connection with SID " + sid);
			return sid;
		}
		Matcher challengematch = CHALLENGE_PATTERN.matcher(loginXml);
		if (!challengematch.find()) {
			logger.error("FritzBox does not respond with challenge for authentication");
			return null;
		}
		String challenge = challengematch.group(1);
		String response = createResponse(challenge);
		loginXml = HttpUtil.executeUrl(
				"GET",
				getURL("login_sid.lua", (!username.equals("") ? ("username=" + username + "&") : "") + "response="
						+ response), timeout);
		if (loginXml == null) {
			logger.error("FritzBox does not respond");
			return null;
		}
		sidmatch = SID_PATTERN.matcher(loginXml);
		if (!sidmatch.find()) {
			logger.error("FritzBox does not respond with SID");
			return null;
		}
		sid = sidmatch.group(1);
		accmatch = ACCESS_PATTERN.matcher(loginXml);
		if (accmatch.find()) {
			if (accmatch.group(1) == "2")
				logger.info("Established FritzBox connection with SID " + sid);
			return sid;
		}
		logger.error("User " + username + " has no access to FritzBox home automation functions");
		return null;
	}

	/**
	 * Checks the authentication status of the web interface
	 * 
	 * @return
	 */
	public boolean isAuthenticated() {
		return !(sid == null);
	}

	/**
	 * Creates the proper response to a given challenge based on the password
	 * stored
	 * 
	 * @param challenge
	 *            Challenge string as returned by the Fritz!OS login script
	 * @return Response to the challenge
	 */
	protected String createResponse(String challenge) {
		String handshake = challenge.concat("-").concat(password);
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.error("This version of Java does not support MD5 hashing");
			return "";
		}
		byte[] handshakeHash;
		try {
			handshakeHash = md5.digest(handshake.getBytes("UTF-16LE"));
		} catch (UnsupportedEncodingException e) {
			logger.error("This version of Java does not understand UTF-16LE encoding");
			return "";
		}
		String response = challenge.concat("-");
		for (byte handshakeByte : handshakeHash)
			response = response.concat(String.format("%02x", handshakeByte));
		return response;
	}

	/**
	 * Constructor to set up interface
	 * 
	 * @param host
	 *            Hostname/IP address of Fritzbox
	 * @param port
	 *            Port to use for Fritzbox connection
	 * @param protocol
	 *            Protocol to use (HTTP,HTTPS)
	 * @param username
	 *            Username for login
	 * @param password
	 *            Password for login
	 * @param synctimeout
	 * 			  Timeout for synchronous http-connections
	 * @param asynctimeout
	 * 			  Timeout for asynchronous http-connections
	 */
	public FritzahaWebInterface(String host, int port, String protocol, String username, String password, 
			int synctimeout, int asynctimeout) {
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.username = username;
		this.password = password;
		this.timeout = synctimeout;
		this.asynctimeout = asynctimeout;
		sid = null;
		asyncclient = new HttpClient(new SslContextFactory(true));
		asyncclient.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
		asyncclient.setMaxConnectionsPerAddress(asyncmaxconns);
		asyncclient.setTimeout(asynctimeout);
		try {
			asyncclient.start();
		} catch (Exception e) {
			logger.error("Could not start HTTP Client for " + getURL(""));
		}
		authenticate();
		logger.debug("Starting with SID " + sid);
	}

	/**
	 * Constructs a URL from the stored information and a specified path
	 * 
	 * @param path
	 *            Path to include in URL
	 * @return URL
	 */
	public String getURL(String path) {
		return protocol + "://" + host + ((port != -1) ? (":" + port) : "") + "/" + path;
	}

	/**
	 * Constructs a URL from the stored information, a specified path and a
	 * specified argument string
	 * 
	 * @param path
	 *            Path to include in URL
	 * @param args
	 *            String of arguments, in standard HTTP format
	 *            (arg1=value1&arg2=value2&...)
	 * @return URL
	 */
	public String getURL(String path, String args) {
		return getURL(path + "?" + args);
	}

	public String addSID(String args) {
		if (sid == null)
			return args;
		else
			return ("".equals(args) ? ("sid=") : (args + "&sid=")) + sid;
	}

	/**
	 * Sends an HTTP GET request using the asynchronous client
	 * 
	 * @param Path
	 *            Path of the requested resource
	 * @param Args
	 *            Arguments for the request
	 * @param Callback
	 *            Callback to handle the response with
	 */
	public HttpExchange asyncGet(String path, String args, FritzahaCallback callback) {
		if (!isAuthenticated())
			authenticate();
		HttpExchange getExchange = new FritzahaContentExchange(callback);
		getExchange.setMethod("GET");
		getExchange.setURL(getURL(path, addSID(args)));
		try {
			asyncclient.send(getExchange);
		} catch (IOException e) {
			logger.error("An I/O error occurred while sending the GET request " + getURL(path, addSID(args)));
			return null;
		}
		logger.debug("GETting URL " + getURL(path, addSID(args)));
		return getExchange;
	}

	/**
	 * Sends an HTTP POST request using the asynchronous client
	 * 
	 * @param Path
	 *            Path of the requested resource
	 * @param Args
	 *            Arguments for the request
	 * @param Callback
	 *            Callback to handle the response with
	 */
	public HttpExchange asyncPost(String path, String args, FritzahaCallback callback) {
		if (!isAuthenticated())
			authenticate();
		HttpExchange postExchange = new FritzahaContentExchange(callback);
		postExchange.setMethod("POST");
		postExchange.setURL(getURL(path));
		try {
			postExchange.setRequestContent(new ByteArrayBuffer(addSID(args).getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			logger.error("An encoding error occurred in the POST arguments");
			return null;
		}
		postExchange.setRequestContentType("application/x-www-form-urlencoded;charset=utf-8");
		try {
			asyncclient.send(postExchange);
		} catch (IOException e) {
			logger.error("An I/O error occurred while sending the POST request to " + getURL(path));
			return null;
		}
		return postExchange;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	public void postUpdate(String itemName, State newState) {
		if (eventPublisher != null) {
			logger.debug("Sending update to item " + itemName);
			eventPublisher.postUpdate(itemName, newState);
		} else
			logger.error("No event publisher for " + host);
	}
}
