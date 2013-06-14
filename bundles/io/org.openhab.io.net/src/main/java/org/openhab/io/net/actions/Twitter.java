/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * This class provides static methods that can be used in automation rules for
 * sending Tweets via Twitter.
 * 
 * @author Ben Jones
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.3.0
 */
public class Twitter implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(Twitter.class);

	/** the configured ConsumerKey (optional, defaults to the official Twitter-App key 'IPqVDkyMvhblm7pobBMYw') */
	private static String consumerKey = "IPqVDkyMvhblm7pobBMYw";
	/** the configured ConsumerSecret (optional, defaults to official Twitter-App secret '2HatstDfLbz236WCXyf8lKCk985HdaK5zbXFrcJ2BM') */
	private static String consumerSecret = "2HatstDfLbz236WCXyf8lKCk985HdaK5zbXFrcJ2BM";
	/** Flag to enable/disable the Twitter client (optional, defaults to 'false') */
	private static boolean isEnabled = false;

	/** The maximum length of a Tweet or direct message */ 
	private static final int CHARACTER_LIMIT = 140;
	
	private static final String TOKEN_PATH = "etc";
	private static final String TOKEN_FILE = "twitter.token";
	private static final File tokenFile = new File(TOKEN_PATH + File.separator + TOKEN_FILE);

	private static twitter4j.Twitter client = createClient();
		
	
	private static void activate() {
		if (!isEnabled) {
			return;
		}

		AccessToken accessToken = getAccessToken();
		if (accessToken != null) {
			client.setOAuthAccessToken(accessToken);
			logger.info("TwitterAction has been successfully authenticated > awaiting your Tweets!");
		} else {
			logger.info("Twitter authentication failed. Please use OSGi "
				+ "console to restart the org.openhab.io.net-Bundle and re-initiate the authorization process!");
		}
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

	/**
	 * Sends a Tweet via Twitter
	 * 
	 * @param tweetTxt the Tweet to send
	 * 
	 * @return <code>true</code>, if sending the tweet has been successful and
	 *         <code>false</code> in all other cases.
	 */
	public static boolean sendTweet(String tweetTxt) {
		if (!isEnabled) {
			logger.debug("Twitter client is disabled > execution aborted!");
			return false;
		}

		try {
			// abbreviate the Tweet to meet the 140 character limit ...
			tweetTxt = StringUtils.abbreviate(tweetTxt, CHARACTER_LIMIT);
			// send the Tweet
			Status status = client.updateStatus(tweetTxt);
			logger.debug("Successfully sent Tweet '{}'", status.getText());
			return true;
		} catch (TwitterException e) {
			logger.error("Failed to send Tweet '" + tweetTxt + "' because of: " + e.getLocalizedMessage());
			return false;
		}
	}
	
	/**
	 * Sends a direct message via Twitter
	 * 
	 * @param recipientId the receiver of this direct message
	 * @param messageTxt the direct message to send
	 * 
	 * @return <code>true</code>, if sending the direct message has been successful and
	 *         <code>false</code> in all other cases.
	 */
	public static boolean sendDirectMessage(String recipientId, String messageTxt) {
		if (!isEnabled) {
			logger.debug("Twitter client is disabled > execution aborted!");
			return false;
		}

		try {
			// abbreviate the Tweet to meet the 140 character limit ...
			messageTxt = StringUtils.abbreviate(messageTxt, CHARACTER_LIMIT);
			// send the direct message
		    DirectMessage message = client.sendDirectMessage(recipientId, messageTxt);
			logger.debug("Successfully sent direct message '{}' to @", message.getText(), message.getRecipientScreenName());
			return true;
		} catch (TwitterException e) {
			logger.error("Failed to send Tweet '" + messageTxt + "' because of: " + e.getLocalizedMessage());
			return false;
		}
	}
	
	private static AccessToken getAccessToken() {
		try {
			String accessToken = loadToken(tokenFile, "accesstoken");
			String accessTokenSecret = loadToken(tokenFile, "accesstokensecret");

			if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(accessTokenSecret)) {
				RequestToken requestToken = client.getOAuthRequestToken();

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
				AccessToken token = client.getOAuthAccessToken(requestToken, authPin);
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
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {

			String appKeyString = (String) config.get("key");
			if (isNotBlank(appKeyString)) {
				Twitter.consumerKey = appKeyString;
			}
			
			String appSecretString = (String) config.get("secret");
			if (isNotBlank(appSecretString)) {
				Twitter.consumerSecret = appSecretString;
			}
			
			if (isBlank(Twitter.consumerKey) || isBlank(Twitter.consumerSecret)) {
				throw new ConfigurationException("twitter",
					"The parameters 'key' or 'secret' are missing! Please refer to your 'openhab.cfg'");
			}
			
			String enabledString = (String) config.get("enabled");
			if (StringUtils.isNotBlank(enabledString)) {
				isEnabled = Boolean.parseBoolean(enabledString);
			}
			
			activate();
		}
	}
	
}
