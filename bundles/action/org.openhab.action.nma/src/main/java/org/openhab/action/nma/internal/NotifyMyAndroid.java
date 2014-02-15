/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.nma.internal;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.openhab.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class contains the methods that are made available in scripts and rules
 * for NotifyMyAndroid.
 * 
 * @author Till Klocke
 * @since 1.3.0
 */
public class NotifyMyAndroid {

	private static final Logger logger = 
		LoggerFactory.getLogger(NotifyMyAndroid.class);

	private final static String API_URL = "https://www.notifymyandroid.com/publicapi/notify";
	private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";
	private final static String UTF_8_ENCODING = "UTF-8";

	public static final String MESSAGE_KEY_API_KEY = "apikey";
	public static final String MESSAGE_KEY_APP = "application";
	public static final String MESSAGE_KEY_EVENT = "event";
	public static final String MESSAGE_KEY_DESC = "description";
	public static final String MESSAGE_KEY_PRIORITY = "priority";
	public static final String MESSAGE_KEY_DEV_KEY = "developerkey";
	public static final String MESSAGE_KEY_URL = "url";
	public static final String MESSAGE_KEY_CONTENT_TYPE = "content-type";

	static String appName = "openHAB";
	static String apiKey;
	static String url;
	static String developerKey;
	static int timeout = 10000;
	static int defaultPriotiy = 0;
	static String defaultUrl;
	

	@ActionDoc(text = "Send a notification to your Android device using the default api key", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean notifyMyAndroid(
			@ParamDoc(name = "event", text = "The event to notify") String event,
			@ParamDoc(name = "description", text = "A description of the event to notify") String description) {
		return notifyMyAndroid(apiKey, event, description);
	}

	@ActionDoc(text = "Send a notification to your Android device using the default api key", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean notifyMyAndroid(
			@ParamDoc(name = "event", text = "The event to notify") String event,
			@ParamDoc(name = "description", text = "A description of the event to notify") String description,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority) {
		return notifyMyAndroid(apiKey, event, description, priority);
	}

	@ActionDoc(text = "Send a notification to your Android device", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean notifyMyAndroid(
			@ParamDoc(name = "apiKey", text = "apiKey to use for the notification") String apiKey,
			@ParamDoc(name = "event", text = "The event to notify") String event,
			@ParamDoc(name = "description", text = "A description of the event to notify") String description) {
		return notifyMyAndroid(apiKey, event, description, defaultPriotiy);
	}

	@ActionDoc(text = "Send a notification to your Android device", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean notifyMyAndroid(
			@ParamDoc(name = "apiKey", text = "apiKey to use for the notification") String apiKey,
			@ParamDoc(name = "event", text = "The event to notify") String event,
			@ParamDoc(name = "description", text = "A description of the event to notify") String description,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority) {
		return notifyMyAndroid(apiKey, event, description, priority, null);
	}

	@ActionDoc(text = "Send a notification to your Android device", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean notifyMyAndroid(
			@ParamDoc(name = "apiKey", text = "apiKey to use for the notification") String apiKey,
			@ParamDoc(name = "event", text = "The event to notify") String event,
			@ParamDoc(name = "description", text = "A description of the event to notify") String description,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority,
			@ParamDoc(name = "url", text = "An URL representing this event") String url) {
		return notifyMyAndroid(apiKey, event, description, priority, url, false);
	}

	// Example
	@ActionDoc(text = "Send a notification to your Android device", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean notifyMyAndroid(
			@ParamDoc(name = "apiKey", text = "apiKey to use for the notification") String apiKey,
			@ParamDoc(name = "event", text = "The event to notify") String event,
			@ParamDoc(name = "description", text = "A description of the event to notify") String description,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority,
			@ParamDoc(name = "url", text = "An URL representing this event") String url,
			@ParamDoc(name = "html", text = "Should the description of the event be interpreted as html") boolean html) {
		StringBuilder data = new StringBuilder();
		try {
			
			if (!StringUtils.isEmpty(apiKey)) {
				addEncodedParameter(data, MESSAGE_KEY_API_KEY, apiKey);
			} else if (!StringUtils.isEmpty(NotifyMyAndroid.apiKey)) {
				addEncodedParameter(data, MESSAGE_KEY_API_KEY, NotifyMyAndroid.apiKey);
			} else {
				logger.error("No api key specified");
				return false;
			}
			
			addEncodedParameter(data, MESSAGE_KEY_APP, appName);
			
			if (!StringUtils.isEmpty(description)) {
				addEncodedParameter(data, MESSAGE_KEY_DESC, description);
			} else {
				logger.error("Description of event is missing");
				return false;
			}
			
			addEncodedParameter(data, MESSAGE_KEY_DEV_KEY, developerKey);
			
			if (!StringUtils.isEmpty(event)) {
				addEncodedParameter(data, MESSAGE_KEY_EVENT, event);
			} else {
				logger.error("Event is empty");
				return false;
			}
			
			addEncodedParameter(data, MESSAGE_KEY_PRIORITY, String.valueOf(priority));
			
			if (!StringUtils.isEmpty(url)) {
				addEncodedParameter(data, MESSAGE_KEY_URL, url);
			} else if (!StringUtils.isEmpty(defaultUrl)) {
				addEncodedParameter(data, MESSAGE_KEY_URL, defaultUrl);
			}
			
			if (html) {
				addEncodedParameter(data, MESSAGE_KEY_CONTENT_TYPE, "text/html");
			}

			String content = data.toString();
			logger.debug("Executing post to " + API_URL + " with the following content: " + content);
			String response = HttpUtil.executeUrl("POST", API_URL,
					IOUtils.toInputStream(content), CONTENT_TYPE, timeout);
			logger.debug("Raw response: " + response);
			
			try {
				String responseMessage = parseResponse(response);
				if (StringUtils.isEmpty(responseMessage)) {
					return true;
				} else {
					logger.error("Received error message from NMA: " + responseMessage);
					return false;
				}
			} catch (Exception e) {
				logger.warn("Can't parse response from NMA: " + response, e);
				return false;
			}
		} catch (Exception e) {
			logger.error("An error occured while notifying your android", e);
			return false;
		}
	}

	private static String parseResponse(String response)
			throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		InputSource inStream = new InputSource();
		inStream.setCharacterStream(new StringReader(response));
		Document doc = db.parse(inStream);

		Element root = doc.getDocumentElement();
		String lastError = null;
		if (root.getTagName().equals("nma")) {
			Node item = root.getFirstChild();
			String childName = item.getNodeName();
			if (!childName.equals("success")) {
				lastError = item.getFirstChild().getNodeValue();
			}
		}
		return lastError;
	}

	private static void addEncodedParameter(StringBuilder sb, String name, String value) throws UnsupportedEncodingException {
		if (StringUtils.isEmpty(value)) {
			return;
		}
		if (sb.length() > 0) {
			sb.append("&");
		}
		sb.append(URLEncoder.encode(name, UTF_8_ENCODING));
		sb.append("=");
		sb.append(URLEncoder.encode(value, UTF_8_ENCODING));
	}

}
