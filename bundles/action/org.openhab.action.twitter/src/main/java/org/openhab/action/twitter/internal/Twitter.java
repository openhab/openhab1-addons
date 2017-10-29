/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.twitter.internal;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.io.FileUtils;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.StatusUpdate;
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
    private static final int CHARACTER_LIMIT = 280;

    static twitter4j.Twitter client = null;

    /**
     * Check twitter prerequesites. Should be used at the beginning of all public methods
     * 
     * @return <code>true</code>, all prerequesites are validated and 
     *         <code>false</code> one prerequesite is not validated.
     */
    private static boolean checkPrerequesites() {
        if (!TwitterActionService.isProperlyConfigured) {
            logger.debug("Twitter client is not yet configured > execution aborted!");
            return false;
        }
        if (!isEnabled) {
            logger.debug("Twitter client is disabled > execution aborted!");
            return false;
        }
        
        return true;
    }
    
    /**
     * Internal method about sending a tweet, with or without image
     * 
     * @param tweetTxt the Tweet to send
     * @param fileToAttach the file to attach. May be null if no attached file.
     * 
     * @return <code>true</code>, if sending the tweet has been successful and
     *         <code>false</code> in all other cases.
     */
    private static boolean sendTweet(final String tweetTxt, final File fileToAttach) {
        // abbreviate the Tweet to meet the 140 character limit ...
        final String abbreviatedTweetTxt = StringUtils.abbreviate(tweetTxt, CHARACTER_LIMIT);
        try {
            // abbreviate the Tweet to meet the allowed character limit ...
            tweetTxt = StringUtils.abbreviate(tweetTxt, CHARACTER_LIMIT);

            // send the Tweet
            final StatusUpdate status = new StatusUpdate(abbreviatedTweetTxt);
            if (fileToAttach != null && fileToAttach.isFile()) {
            	status.setMedia(fileToAttach);
            }
            final Status updatedStatus = client.updateStatus(status);
            logger.debug("Successfully sent Tweet '{}'", updatedStatus.getText());
            return true;
        } catch (TwitterException e) {
            logger.error("Failed to send Tweet '" + abbreviatedTweetTxt + "' because of: " + e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * Sends a Tweet via Twitter
     * 
     * @param tweetTxt the Tweet to send
     * 
     * @return <code>true</code>, if sending the tweet has been successful and
     *         <code>false</code> in all other cases.
     */
    @ActionDoc(text = "Sends a Tweet via Twitter", returns = "<code>true</code>, if sending the tweet has been successful and <code>false</code> in all other cases.")
    public static boolean sendTweet(@ParamDoc(name = "tweetTxt", text = "the Tweet to send") String tweetTxt) {
    	if (! checkPrerequesites()) {
    		return false;
    	}
        return sendTweet(tweetTxt, null);
    }

    /**
     * Sends a Tweet via Twitter
     * 
     * @param tweetTxt the Tweet to send
     * 
     * @return <code>true</code>, if sending the tweet has been successful and
     *         <code>false</code> in all other cases.
     */
    @ActionDoc(text = "Sends a Tweet via Twitter", returns = "<code>true</code>, if sending the tweet has been successful and <code>false</code> in all other cases.")
    public static boolean sendPicture(@ParamDoc(name = "tweetTxt", text = "the Tweet to send") String tweetTxt, @ParamDoc(name = "tweetPicture", text = "the picture to attach") String tweetPicture) {
    	if (! checkPrerequesites()) {
    		return false;
    	}

        // prepare the image attachment
        File fileToAttach = null;
        boolean deleteTemporaryFile = false;
        if (StringUtils.startsWith(tweetPicture, "http")) {
    		final String tDir = System.getProperty("java.io.tmpdir"); 
    		final String path = tDir + File.separator + "openhab-twitter-remote_attached_file" + "." + FilenameUtils.getExtension(tweetPicture); 
        	try {
        		final URL url = new URL(tweetPicture); 
        		fileToAttach = new File(path); 
        		// fileToAttach.deleteOnExit();  // good idea ? could lead to temporary files staying around until JVM is down
        		deleteTemporaryFile = true;
        		FileUtils.copyURLToFile(url, fileToAttach);
        	} catch (final MalformedURLException e) {
        		logger.error("Can't read file from '" + tweetPicture + "'", e);
        	} catch (final IOException e) {
        		logger.error("Can't save file from '" + tweetPicture + "' to '" + path + "'", e);
        	}
        } else {
            fileToAttach = new File(tweetPicture);
        }

        if (fileToAttach != null && fileToAttach.isFile()) {
            logger.info("Image '{}' correctly found, will be included in tweet", tweetPicture);
        } else {
            logger.warn("Image '{}' not found, will only tweet text", tweetPicture);
        }
    
        // send the Tweet
        final boolean result = sendTweet(tweetTxt, fileToAttach);
        // delete temp file (if needed)
        if (deleteTemporaryFile) {
        	FileUtils.deleteQuietly(fileToAttach);
        }
        return result;
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
    @ActionDoc(text = "Sends a direct message via Twitter", returns = "<code>true</code>, if sending the direct message has been successful and <code>false</code> in all other cases.")
    public static boolean sendDirectMessage(
            @ParamDoc(name = "recipientId", text = "the receiver of this direct message") String recipientId,
            @ParamDoc(name = "messageTxt", text = "the direct message to send") String messageTxt) {
    	if (! checkPrerequesites()) {
    		return false;
    	}

        try {
            // abbreviate the Tweet to meet the allowed character limit ...
            messageTxt = StringUtils.abbreviate(messageTxt, CHARACTER_LIMIT);
            // send the direct message
            DirectMessage message = client.sendDirectMessage(recipientId, messageTxt);
            logger.debug("Successfully sent direct message '{}' to @", message.getText(),
                    message.getRecipientScreenName());
            return true;
        } catch (TwitterException e) {
            logger.error("Failed to send Tweet '" + messageTxt + "' because of: " + e.getLocalizedMessage());
            return false;
        }
    }

}
