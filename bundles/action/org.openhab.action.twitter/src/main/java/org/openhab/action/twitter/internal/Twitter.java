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
     * Check twitter prerequisites. Should be used at the beginning of all public
     * methods
     *
     * @return <code>true</code> if all prerequisites are validated and
     *         <code>false</code> if any prerequisite is not validated.
     */
    private static boolean checkPrerequisites() {
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
     * Internal method for sending a tweet, with or without image
     *
     * @param tweetTxt
     *            text string to be sent as a Tweet
     * @param fileToAttach
     *            the file to attach. May be null if no attached file.
     *
     * @return <code>true</code>, if sending the tweet has been successful and
     *         <code>false</code> in all other cases.
     */
    private static boolean sendTweet(final String tweetTxt, final File fileToAttach) {
        // abbreviate the Tweet to meet the 140 character limit ...
        String abbreviatedTweetTxt = StringUtils.abbreviate(tweetTxt, CHARACTER_LIMIT);
        try {
            // send the Tweet
            StatusUpdate status = new StatusUpdate(abbreviatedTweetTxt);
            if (fileToAttach != null && fileToAttach.isFile()) {
            	status.setMedia(fileToAttach);
            }
            Status updatedStatus = client.updateStatus(status);
            logger.debug("Successfully sent Tweet '{}'", updatedStatus.getText());
            return true;
        } catch (TwitterException e) {
            logger.warn("Failed to send Tweet '{}' because of : {}", abbreviatedTweetTxt, e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * Sends a standard Tweet.
     *
     * @param tweetTxt
     *            text string to be sent as a Tweet
     *
     * @return <code>true</code>, if sending the tweet has been successful and
     *         <code>false</code> in all other cases.
     */
    @ActionDoc(text = "Sends a standard Tweet", returns = "<code>true</code>, if sending the tweet has been successful and <code>false</code> in all other cases.")
    public static boolean sendTweet(
            @ParamDoc(name = "tweetTxt", text = "text string to be sent as a Tweet") String tweetTxt) {
        if (!checkPrerequisites()) {
            return false;
        }
        return sendTweet(tweetTxt, (File)null);
    }

    /**
     * Sends a Tweet with an image
     *
     * @param tweetTxt
     *            text string to be sent as a Tweet
     * @param tweetPicture
     *            the path of the picture that needs to be attached (either an url,
     *            either a path pointing to a local file)
     *
     * @return <code>true</code>, if sending the tweet has been successful and
     *         <code>false</code> in all other cases.
     */
    @ActionDoc(text = "Sends a Tweet with an image", returns = "<code>true</code>, if sending the tweet has been successful and <code>false</code> in all other cases.")
    public static boolean sendTweet(
            @ParamDoc(name = "tweetTxt", text = "text string to be sent as a Tweet") String tweetTxt,
            @ParamDoc(name = "tweetPicture", text = "the picture to attach") String tweetPicture) {
        if (!checkPrerequisites()) {
            return false;
        }

        // prepare the image attachment
        File fileToAttach = null;
        boolean deleteTemporaryFile = false;
        if (StringUtils.startsWith(tweetPicture, "http://") || StringUtils.startsWith(tweetPicture, "https://")) {
            // we have a remote url and need to download the remote file to a temporary location
            String tDir = System.getProperty("java.io.tmpdir");
            String path = tDir + File.separator + "openhab-twitter-remote_attached_file" + "."
                    + FilenameUtils.getExtension(tweetPicture);
            try {
                URL url = new URL(tweetPicture);
                fileToAttach = new File(path);
                deleteTemporaryFile = true;
                FileUtils.copyURLToFile(url, fileToAttach);
            } catch (MalformedURLException e) {
                logger.warn("Can't read file from '{}'", tweetPicture, e);
            } catch (IOException e) {
                logger.warn("Can't save file from '{}' to '{}'", tweetPicture, path, e);
            }
        } else {
            // we have a local file and can just use it directly
            fileToAttach = new File(tweetPicture);
        }

        if (fileToAttach != null && fileToAttach.isFile()) {
            logger.debug("Image '{}' correctly found, will be included in tweet", tweetPicture);
        } else {
            logger.warn("Image '{}' not found, will only tweet text", tweetPicture);
        }
    
        // send the Tweet
        boolean result = sendTweet(tweetTxt, fileToAttach);
        // delete temp file (if needed)
        if (deleteTemporaryFile) {
        	FileUtils.deleteQuietly(fileToAttach);
        }
        return result;
    }

    /**
     * Sends a direct message via Twitter
     *
     * @param recipientId
     *            the receiver of this direct message
     * @param messageTxt
     *            the direct message to send
     *
     * @return <code>true</code>, if sending the direct message has been successful
     *         and <code>false</code> in all other cases.
     */
    @ActionDoc(text = "Sends a direct message via Twitter", returns = "<code>true</code>, if sending the direct message has been successful and <code>false</code> in all other cases.")
    public static boolean sendDirectMessage(
            @ParamDoc(name = "recipientId", text = "the receiver of this direct message") String recipientId,
            @ParamDoc(name = "messageTxt", text = "the direct message to send") String messageTxt) {
        if (!checkPrerequisites()) {
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
            logger.warn("Failed to send Tweet '{}' because of : ", messageTxt, e.getLocalizedMessage());
            return false;
        }
    }
}
