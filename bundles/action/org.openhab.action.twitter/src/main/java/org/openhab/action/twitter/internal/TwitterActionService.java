/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.twitter.internal;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
	

/**
 * This class registers an OSGi service for the Twitter action.
 * 
 * @author Ben Jones
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class TwitterActionService implements ActionService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(TwitterActionService.class);

	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the
	 * action methods before executing code.
	 */
	/* default */ static boolean isProperlyConfigured = false;
	
	/** the configured ConsumerKey (optional, defaults to the official Twitter-App key 'IPqVDkyMvhblm7pobBMYw') */
	static String consumerKey = "IPqVDkyMvhblm7pobBMYw";
	
	/** the configured ConsumerSecret (optional, defaults to official Twitter-App secret '2HatstDfLbz236WCXyf8lKCk985HdaK5zbXFrcJ2BM') */
	static String consumerSecret = "2HatstDfLbz236WCXyf8lKCk985HdaK5zbXFrcJ2BM";

	private static final String TOKEN_PATH = "etc";
	private static final String TOKEN_FILE = "twitter.token";
	private static final File tokenFile = new File(TOKEN_PATH + File.separator + TOKEN_FILE);

	public TwitterActionService() {
	}
	
	public void activate(ComponentContext componentContext) {
	}
	
	public void deactivate(ComponentContext componentContext) {
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	@Override
	public String getActionClassName() {
		return Twitter.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Twitter.class;
	}

	/**
	 * Creates and returns a Twitter4J Twitter client.
	 * 
	 * @return a new instance of a Twitter4J Twitter client.
	 */
	private static twitter4j.Twitter createClient() {
		twitter4j.Twitter client = TwitterFactory.getSingleton();
		client.setOAuthConsumer(consumerKey, consumerSecret);
		return client;
	}

	private static void start() {
		if (!Twitter.isEnabled) {
			return;
		}
		
		Twitter.client = createClient();
		
		AccessToken accessToken = getAccessToken();
		if (accessToken != null) {
			Twitter.client.setOAuthAccessToken(accessToken);
			logger.info("TwitterAction has been successfully authenticated > awaiting your Tweets!");
		} else {
			logger.info("Twitter authentication failed. Please use OSGi "
				+ "console to restart the org.openhab.io.net-Bundle and re-initiate the authorization process!");
		}
	}

	private static AccessToken getAccessToken() {
		try {
			String accessToken = loadToken(tokenFile, "accesstoken");
			String accessTokenSecret = loadToken(tokenFile, "accesstokensecret");

			if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(accessTokenSecret)) {
				RequestToken requestToken = Twitter.client.getOAuthRequestToken();

				// no access token/secret specified so display the authorisation URL in the log
				logger.info("################################################################################################");
				logger.info("# Twitter-Integration: U S E R   I N T E R A C T I O N   R E Q U I R E D !!");
				logger.info("# 1. Open URL '{}'", requestToken.getAuthorizationURL());
				logger.info("# 2. Grant openHAB access to your Twitter account");
				logger.info("# 3. Create an empty file 'twitter.pin' in your openHAB home directory");
				logger.info("# 4. Add the line 'pin=<authpin>' to the twitter.pin file");
				logger.info("# 5. openHAB will automatically detect the file and complete the authentication process");
				logger.info("# NOTE: You will only have 5 mins before openHAB gives up waiting for the pin!!!");
				logger.info("################################################################################################");

				String authPin = null;
				int interval = 5000;
				int waitedFor = 0;
				
				while (StringUtils.isEmpty(authPin)) {
					try {
						Thread.sleep(interval);
						waitedFor += interval;
						// attempt to read the authentication pin from them temp file
					} catch (InterruptedException e) {
						// ignore
					}
					
					File pinFile = new File("twitter.pin");
					authPin = (String) loadToken(pinFile, "pin");

					// if we already waited for more than five minutes then stop
					if (waitedFor > 300000) {
						logger.info("Took too long to enter your Twitter authorisation pin! Please use OSGi "
								+ "console to restart the org.openhab.io.net-Bundle and re-initiate the authorization process!");
						break;
					}
				}

				// if no pin was detected after 5 mins then we can't continue
				if (StringUtils.isEmpty(authPin)) {
					logger.warn("Timed out waiting for the Twitter authorisation pin.");
					return null;
				}

				// attempt to get an access token using the user-entered pin
				AccessToken token = Twitter.client.getOAuthAccessToken(requestToken, authPin);
				accessToken = token.getToken();
				accessTokenSecret = token.getTokenSecret();

				// save the access token details
				saveToken(tokenFile, "accesstoken", accessToken);
				saveToken(tokenFile, "accesstokensecret", accessTokenSecret);
			}
			
			// generate an access token from the token details
			return new AccessToken(accessToken, accessTokenSecret);
		} catch (Exception e) {
			logger.error("Failed to authenticate openHAB against Twitter", e);
			return null;
		}
	}
	
	// Helpers for storing/retrieving tokens from a flat file

	private static String loadToken(File file, String key) throws IOException {
		Properties properties = loadProperties(file);
		String token = (String) properties.get(key);
		if (StringUtils.isEmpty(token))
			return null;
		return token;
	}

	private static Properties loadProperties(File file) throws IOException {
		Properties properties = new Properties();
		try {
			FileInputStream inputStream = new FileInputStream(file);
			try {
				properties.load(inputStream);
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		} catch (FileNotFoundException e) {
			// ignore a missing file exception
		}
		return properties;
	}

	private static void saveToken(File file, String key, String token) throws IOException {
		Properties properties = loadProperties(file);
		if (token == null) {
			token = "";
		}
		properties.setProperty(key, token);
		saveProperties(file, properties);
	}

	private static void saveProperties(File file, Properties properties) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(file);
		try {
			properties.store(outputStream, "Twitter OAuth Authentication Tokens");
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}
	

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {

			String appKeyString = (String) config.get("key");
			if (isNotBlank(appKeyString)) {
				consumerKey = appKeyString;
			}
			
			String appSecretString = (String) config.get("secret");
			if (isNotBlank(appSecretString)) {
				consumerSecret = appSecretString;
			}
			
			if (isBlank(consumerKey) || isBlank(consumerSecret)) {
				throw new ConfigurationException("twitter",
					"The parameters 'key' or 'secret' are missing! Please refer to your 'openhab.cfg'");
			}
			
			String enabledString = (String) config.get("enabled");
			if (StringUtils.isNotBlank(enabledString)) {
				Twitter.isEnabled = Boolean.parseBoolean(enabledString);
			}
			
			isProperlyConfigured = true;
			start();
		}
	}
	
}
