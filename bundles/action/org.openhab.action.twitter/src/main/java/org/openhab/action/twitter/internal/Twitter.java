/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.twitter.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * This class provides static methods that can be used in automation rules for
 * sending Tweets via Twitter.
 * 
 * @author Ben Jones
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class Twitter {

	private static final Logger logger = LoggerFactory.getLogger(Twitter.class);

	/** Flag to enable/disable the Twitter client (optional, defaults to 'false') */
	static boolean isEnabled = false;

	/** The maximum length of a Tweet or direct message */ 
	private static final int CHARACTER_LIMIT = 140;

	static twitter4j.Twitter client = null;

	/**
	 * Sends a Tweet via Twitter
	 * 
	 * @param tweetTxt the Tweet to send
	 * 
	 * @return <code>true</code>, if sending the tweet has been successful and
	 *         <code>false</code> in all other cases.
	 */
	@ActionDoc(text="Sends a Tweet via Twitter", 
			returns="<code>true</code>, if sending the tweet has been successful and <code>false</code> in all other cases.")
	public static boolean sendTweet(@ParamDoc(name="tweetTxt", text="the Tweet to send") String tweetTxt) {
		if (!TwitterActionService.isProperlyConfigured) {
			logger.debug("Twitter client is not yet configured > execution aborted!");
			return false;
		}
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
	@ActionDoc(text="Sends a direct message via Twitter", 
			returns="<code>true</code>, if sending the direct message has been successful and <code>false</code> in all other cases.")
	public static boolean sendDirectMessage(
			@ParamDoc(name="recipientId", text="the receiver of this direct message") String recipientId, 
			@ParamDoc(name="messageTxt", text="the direct message to send") String messageTxt) {
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
	
}
