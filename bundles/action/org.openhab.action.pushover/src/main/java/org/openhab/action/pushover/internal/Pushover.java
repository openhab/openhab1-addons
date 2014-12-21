/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.pushover.internal;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class contains the methods that are made available in scripts and rules
 * for sending messages via the Pushover mobile device push service..
 * 
 * @author Chris Graham
 * @since 1.5.0
 */
public class Pushover {

	private static final Logger logger = 
		LoggerFactory.getLogger(Pushover.class);

	private final static String API_URL = "https://api.pushover.net/1/messages.xml";
	private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";
	private final static String UTF_8_ENCODING = "UTF-8";
	
	private final static String API_RETURN_ROOT_TAG = "hash";
	private final static String API_RETURN_STATUS_TAG = "status";
	private final static String API_RETURN_ERROR_TAG = "error";
	private final static String API_RETURN_STATUS_SUCCESS = "1";

	private final static int API_MAX_MESSAGE_LENGTH = 512;
	private final static int API_MAX_URL_LENGTH = 512;
	private final static int API_MAX_URL_TITLE_LENGTH = 100;
	private final static int[] API_VALID_PRIORITY_LIST = {-2, -1, 0, 1, 2};
	private final static int[] API_HIGH_PRIORITY_LIST = {2};
	private final static int API_MIN_RETRY_SECONDS = 30;
	private final static int API_MAX_EXPIRE_SECONDS = 86400;
	
	public static final String MESSAGE_KEY_API_KEY = "token";
	public static final String MESSAGE_KEY_USER = "user";
	public static final String MESSAGE_KEY_MESSAGE = "message";
	
	public static final String MESSAGE_KEY_DEVICE = "device";
	public static final String MESSAGE_KEY_TITLE = "title";
	public static final String MESSAGE_KEY_URL = "url";
	public static final String MESSAGE_KEY_URL_TITLE = "url_title";
	public static final String MESSAGE_KEY_PRIORITY = "priority";
	public static final String MESSAGE_KEY_TIMESTAMP = "timestamp";
	public static final String MESSAGE_KEY_SOUND = "sound";
	public static final String MESSAGE_KEY_RETRY = "retry";
	public static final String MESSAGE_KEY_EXPIRE = "expire";
	
	public static final String MESSAGE_KEY_CONTENT_TYPE = "content-type";

	static String defaultApiKey;
	static String defaultUser;
	static String defaultDevice;
	static String defaultTitle = "openHAB";
	static String defaultUrl;
	static String defaultUrlTitle;
	static int defaultPriority = 0;
	static String defaultSound;

	static int retry = 300;
	static int expire = 3600;
	static int timeout = 10000;

	@ActionDoc(text = "Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "message", text = "Your message.") String message) {
		return pushover(defaultApiKey, defaultUser, message, defaultDevice, defaultTitle, defaultUrl, defaultUrlTitle, defaultPriority, defaultSound);
	}

	@ActionDoc(text = "Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device) {
		return pushover(defaultApiKey, defaultUser, message, device, defaultTitle, defaultUrl, defaultUrlTitle, defaultPriority, defaultSound);
	}
	
	@ActionDoc(text = "Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority) {
		return pushover(defaultApiKey, defaultUser, message, defaultDevice, defaultTitle, defaultUrl, defaultUrlTitle, priority, defaultSound);
	}
	
	@ActionDoc(text = "Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority,
			@ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url) {
		return pushover(defaultApiKey, defaultUser, message, defaultDevice, defaultTitle, url, defaultUrlTitle, priority, defaultSound);
	}
	
	@ActionDoc(text = "Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority,
			@ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url,
			@ParamDoc(name = "urlTitle", text = "A title for your supplementary URL, otherwise just the URL is shown.") String urlTitle) {
		return pushover(defaultApiKey, defaultUser, message, defaultDevice, defaultTitle, url, urlTitle, priority, defaultSound);
	}

	@ActionDoc(text = "Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority,
			@ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url,
			@ParamDoc(name = "urlTitle", text = "A title for your supplementary URL, otherwise just the URL is shown.") String urlTitle,
			@ParamDoc(name = "sound", text = "The name of one of the sounds supported by device clients to override the user's default sound choice.") String sound) {
		return pushover(defaultApiKey, defaultUser, message, defaultDevice, defaultTitle, url, urlTitle, priority, sound);
	}

	@ActionDoc(text = "Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority) {
		return pushover(defaultApiKey, defaultUser, message, device, defaultTitle, defaultUrl, defaultUrlTitle, priority, defaultSound);
	}
	
	@ActionDoc(text = "Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority,
			@ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url) {
		return pushover(defaultApiKey, defaultUser, message, device, defaultTitle, url, defaultUrlTitle, priority, defaultSound);
	}
	
	@ActionDoc(text = "Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority,
			@ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url,
			@ParamDoc(name = "urlTitle", text = "A title for your supplementary URL, otherwise just the URL is shown.") String urlTitle) {
		return pushover(defaultApiKey, defaultUser, message, device, defaultTitle, url, urlTitle, priority, defaultSound);
	}
	
	@ActionDoc(text = "Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority,
			@ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url,
			@ParamDoc(name = "urlTitle", text = "A title for your supplementary URL, otherwise just the URL is shown.") String urlTitle,
			@ParamDoc(name = "sound", text = "The name of one of the sounds supported by device clients to override the user's default sound choice.") String sound) {
		return pushover(defaultApiKey, defaultUser, message, device, defaultTitle, url, urlTitle, priority, sound);
	}

	@ActionDoc(text = "Send a notification to your mobile device while providing api key and user/group key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "apiKey", text = "Your application's API token.") String apiKey,
			@ParamDoc(name = "user", text = "The user/group key (not e-mail address) of your user (or you), viewable when logged into Pushover dashboard.") String user,
			@ParamDoc(name = "message", text = "Your message.") String message) {
		return pushover(apiKey, user, message, defaultDevice, defaultTitle, defaultUrl, defaultUrlTitle, defaultPriority, defaultSound);
	}

	@ActionDoc(text = "Send a notification to your mobile device while providing api key and user/group key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "apiKey", text = "Your application's API token.") String apiKey,
			@ParamDoc(name = "user", text = "The user/group key (not e-mail address) of your user (or you), viewable when logged into Pushover dashboard.") String user,
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device) {
		return pushover(apiKey, user, message, device, defaultTitle, defaultUrl, defaultUrlTitle, defaultPriority, defaultSound);
	}
	
	@ActionDoc(text = "Send a notification to your mobile device while providing api key and user/group key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "apiKey", text = "Your application's API token.") String apiKey,
			@ParamDoc(name = "user", text = "The user/group key (not e-mail address) of your user (or you), viewable when logged into Pushover dashboard.") String user,
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority) {
		return pushover(apiKey, user, message, defaultDevice, defaultTitle, defaultUrl, defaultUrlTitle, priority, defaultSound);
	}

	@ActionDoc(text = "Send a notification to your mobile device while providing api key and user/group key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "apiKey", text = "Your application's API token.") String apiKey,
			@ParamDoc(name = "user", text = "The user/group key (not e-mail address) of your user (or you), viewable when logged into Pushover dashboard.") String user,
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device,
			@ParamDoc(name = "priority", text = "The priority of the notification") int priority) {
		return pushover(apiKey, user, message, device, defaultTitle, defaultUrl, defaultUrlTitle, priority, defaultSound);
	}

	// Primary method for sending a message to the Pushover API
	@ActionDoc(text = "Send a notification to your Android device. apiKey, user and message are required. All else can effectively be null.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean pushover(
			@ParamDoc(name = "apiKey", text = "Your application's API token.") String apiKey,
			@ParamDoc(name = "user", text = "The user/group key (not e-mail address) of your user (or you), viewable when logged into Pushover dashboard.") String user,
			@ParamDoc(name = "message", text = "Your message.") String message,
			@ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device,
			@ParamDoc(name = "title", text = "Your message's title, otherwise your app's name is used.") String title,
			@ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url,
			@ParamDoc(name = "urlTitle", text = "A title for your supplementary URL, otherwise just the URL is shown.") String urlTitle,
			@ParamDoc(name = "priority", text = "Send as -1 to always send as a quiet notification, 1 to display as high-priority and bypass the user's quiet hours, or 2 to also require confirmation from the user.") int priority,
			@ParamDoc(name = "sound", text = "The name of one of the sounds supported by device clients to override the user's default sound choice.") String sound) {

		StringBuilder data = new StringBuilder();
		
		try {
			
			if (!StringUtils.isEmpty(apiKey)) {
				addEncodedParameter(data, MESSAGE_KEY_API_KEY, apiKey);
			} else {
				logger.error("Application API token not specified.");
				return false;
			}
			
			if (!StringUtils.isEmpty(user)) {
				addEncodedParameter(data, MESSAGE_KEY_USER, user);
			} else {
				logger.error("The user/group key was not specified.");
				return false;
			}

			if (!StringUtils.isEmpty(message)) {
				if ( (message.length() + title.length()) <= API_MAX_MESSAGE_LENGTH ) {
					addEncodedParameter(data, MESSAGE_KEY_MESSAGE, message);
				} else {
					logger.error("Together, the event message and title total more than " + API_MAX_MESSAGE_LENGTH + " characters.");
					return false;
				}
			} else {
				logger.error("The event message is missing.");
				return false;
			}
			
			if (!StringUtils.isEmpty(device)) {
				addEncodedParameter(data, MESSAGE_KEY_DEVICE, device);
			}
			
			if (!StringUtils.isEmpty(title)) {
				addEncodedParameter(data, MESSAGE_KEY_TITLE, title);
			}
			
			if (!StringUtils.isEmpty(url)) {
				if ( url.length() <= API_MAX_URL_LENGTH ) {
					addEncodedParameter(data, MESSAGE_KEY_URL, url);
				} else {
					logger.error("The url is greater than " + API_MAX_URL_LENGTH + " characters.");
					return false;
				}
			}
			
			if (!StringUtils.isEmpty(urlTitle)) {
				if ( urlTitle.length() <= API_MAX_URL_TITLE_LENGTH ) {
					addEncodedParameter(data, MESSAGE_KEY_URL_TITLE, urlTitle);
				} else {
					logger.error("The url title is greater than " + API_MAX_URL_TITLE_LENGTH + " characters.");
					return false;
				}
			}
			
			try {
				if ( isValueInList(API_VALID_PRIORITY_LIST, priority) ) {
					addEncodedParameter(data, MESSAGE_KEY_PRIORITY, String.valueOf(priority));
				} else {
					logger.warn("Invalid priority, skipping. Expected: " + Arrays.toString(API_VALID_PRIORITY_LIST) + ". Got: " + priority + ".");
				}
			} catch (Exception exp) {
				logger.warn("Can't parse the priority value, skipping.");
			}
			
			if (!StringUtils.isEmpty(sound)) {
				addEncodedParameter(data, MESSAGE_KEY_SOUND, sound);
			}
			
			if ( isValueInList(API_HIGH_PRIORITY_LIST, priority) ) {
				if ( retry >= API_MIN_RETRY_SECONDS ) {
					addEncodedParameter(data, MESSAGE_KEY_RETRY, String.valueOf(retry));
				} else {
					logger.warn("Retry value of " + retry + " is too small. Using default value of " + API_MIN_RETRY_SECONDS + ".");
					addEncodedParameter(data, MESSAGE_KEY_RETRY, String.valueOf(API_MIN_RETRY_SECONDS));
				}

				if ( expire <= API_MAX_EXPIRE_SECONDS ) {
					addEncodedParameter(data, MESSAGE_KEY_EXPIRE, String.valueOf(expire));
				} else {
					logger.warn("Expire value of " + expire + " is too large. Using default value of " + API_MAX_EXPIRE_SECONDS + ".");
					addEncodedParameter(data, MESSAGE_KEY_EXPIRE, String.valueOf(API_MAX_EXPIRE_SECONDS));					
				}
			}

			String content = data.toString();
			logger.debug("Executing post to " + API_URL + " with the following content: " + content);
			String response = HttpUtil.executeUrl("POST", API_URL, IOUtils.toInputStream(content), CONTENT_TYPE, timeout);
			logger.debug("Raw response: " + response);
			
			try {
				String responseMessage = parseResponse(response);
				if (StringUtils.isEmpty(responseMessage)) {
					return true;
				} else {
					logger.error("Received error message from Pushover: " + responseMessage);
					return false;
				}
			} catch (Exception e) {
				logger.warn("Can't parse response from Pushover: " + response, e);
				return false;
			}
		} catch (Exception e) {
			logger.error("An error occured while notifying your mobile device.", e);
			return false;
		}
	}

	private static String parseResponse(String response) throws ParserConfigurationException, SAXException, IOException {		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		InputSource inStream = new InputSource();
		inStream.setCharacterStream(new StringReader(response));
		Document doc = db.parse(inStream);

		Element root = doc.getDocumentElement();
		
		if (API_RETURN_ROOT_TAG.equals(root.getTagName())) {
			NodeList statusList = root.getElementsByTagName(API_RETURN_STATUS_TAG);
			
			for (int i = 0; i < statusList.getLength(); i++) {
				Element value = (Element) statusList.item(i);
				if ( API_RETURN_STATUS_SUCCESS.equals(value.getFirstChild().getNodeValue()) ) {
					return null;
				}
			}
			
			NodeList errorList = root.getElementsByTagName(API_RETURN_ERROR_TAG);
			Element value = (Element) errorList.item(0);
			
			return value.getFirstChild().getNodeValue();
		}
		
		return response;
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
	
	private static boolean isValueInList (int[] list, int value) {
		for (int i=0; i < list.length; i++) {
			if ( list[i] == value) {
				return true;
			}
		}
		
		return false;
	}
}
