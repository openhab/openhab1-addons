/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecotouch.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Network communication with Waterkotte EcoTouch heat pumps.
 * 
 * The communication protocol was reverse engineered from the Easy-Con Android
 * app. The meaning of the EcoTouch tags was provided by Waterkotte's technical
 * service (by an excerpt of a developer manual).
 * 
 * @author Sebastian Held <sebastian.held@gmx.de>
 * @since 1.5.0
 */

public class EcoTouchConnector {
	private String ip;
	private String username;
	private String password;
	List<String> cookies;
	static Pattern response_pattern = Pattern
			.compile("#(.+)\\s+S_OK[^0-9-]+([0-9-]+)\\s+([0-9-]+)");

	private static final Logger logger = LoggerFactory
			.getLogger(EcoTouchConnector.class);

	/**
	 * Create a network communication without having a current access token.
	 */
	public EcoTouchConnector(String ip, String username, String password) {
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.cookies = null;
	}

	/**
	 * Create a network communication with access token. This speeds up
	 * retrieving values, because the log in step is omitted.
	 */
	public EcoTouchConnector(String ip, String username, String password,
			List<String> cookies) {
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.cookies = cookies;
	}

	private void login() {
		cookies = null;
		String url = null;
		try {
			url = "http://" + ip + "/cgi/login?username="
					+ URLEncoder.encode(username, "UTF-8") + "&password="
					+ URLEncoder.encode(password, "UTF-8");
			URL loginurl = new URL(url);
			URLConnection connection = loginurl.openConnection();
			cookies = connection.getHeaderFields().get("Set-Cookie");
		} catch (MalformedURLException e) {
			logger.debug("The URL '" + url + "' is malformed: " + e.toString());
		} catch (Exception e) {
			logger.debug("Cannot log into Waterkotte EcoTouch: " + e.toString());
		}
	}

	/**
	 * Request a value from the heat pump-
	 * 
	 * @param tag
	 *            The register to query (e.g. "A1")
	 * @return value This value is a 16-bit integer.
	 */
	public int getValue(String tag) throws Exception {
		// request values
		String url = "http://" + ip + "/cgi/readTags?n=1&t1=" + tag;
		StringBuilder body = null;
		int loginAttempt = 0;
		while (loginAttempt < 2) {
			try {
				URLConnection connection = new URL(url).openConnection();
				if (cookies != null) {
					for (String cookie : cookies) {
						connection.addRequestProperty("Cookie",
								cookie.split(";", 2)[0]);
					}
				}
				InputStream response = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(response));
				body = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					body.append(line + "\n");
				}
				if (body.toString().contains("#" + tag)) {
					// succeeded
					break;
				}
				// s.th. went wrong; try to log in
				throw new Exception();
			} catch (Exception e) {
				login();
				loginAttempt++;
			}
		}

		if (body == null || !body.toString().contains("#" + tag)) {
			// failed
			logger.debug("Cannot get value for tag '" + tag
					+ "' from Waterkotte EcoTouch.");
			throw new Exception("invalid response from EcoTouch");
		}

		// ok, the body now contains s.th. like
		// #A30 S_OK
		// 192 223

		Matcher m = response_pattern.matcher(body.toString());
		boolean b = m.find();
		if (!b) {
			// ill formatted response
			logger.debug("ill formatted response: '" + body + "'");
			throw new Exception("invalid response from EcoTouch");
		}

		return Integer.parseInt(m.group(3));
	}

	/**
	 * Authentication token. Store this and use it, when creating the next
	 * instance of this class.
	 * 
	 * @return cookies: This includes the authentication token retrieved during
	 *         log in.
	 */
	public List<String> getCookies() {
		return cookies;
	}
}
