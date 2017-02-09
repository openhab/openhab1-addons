/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.slack.internal;

import java.io.IOException;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

/**
 * This class provides static methods that can be used in automation rules
 * for sending messages to Slack.
 *
 * It uses the Simple Slack Api from https://github.com/Ullink/simple-slack-api
 *
 * @since 1.10.0
 * @author Rino.van.Wijngaarden@gmail.com
 *
 */
public class Slack {

    private static final Logger logger = LoggerFactory.getLogger(Slack.class);

    static String authToken;

    @ActionDoc(text = "Sends a message to a Slack channel using the configured (bot) authToken")
    static public boolean sendToSlackChannel(@ParamDoc(name = "message") String message,
            @ParamDoc(name = "channel") String channel) {
        return sendToSlackChannel(authToken, message, channel);
    }

    @ActionDoc(text = "Sends a message to a Slack user using the configured (bot) authToken")
    static public boolean sendToSlackUser(@ParamDoc(name = "message") String message,
            @ParamDoc(name = "user") String user) {
        return sendToSlackUser(authToken, message, user);
    }

    private static boolean sendToSlackUser(String authToken, String message, String user) {
        boolean success = false;
        logger.debug("sendToSlackUser authToken='{}', message='{}', user='{}'", authToken, message, user);
        SlackSession session = null;
        try {
            session = SlackSessionFactory.createWebSocketSlackSession(authToken);
            session.connect();
            SlackUser slackUser = session.findUserByUserName(user);
            if (user == null) {
                logger.error("Could not find Slack user '{}'", user);
            } else {
                session.sendMessageToUser(slackUser, message, null);
                success = true;
            }
        } catch (IOException e) {
            logger.error("Error sending message to Slack user", e);
        } finally {
            try {
                if (session != null) {
                    session.disconnect();
                }
            } catch (IOException e) {
                logger.error("Error disconnecting Slack session", e);
            }
        }
        return success;
    }

    private static boolean sendToSlackChannel(String authToken, String message, String channel) {
        boolean success = false;
        logger.info("sendToSlackChannel authToken='{}', message='{}', channel='{}'", authToken, message, channel);
        SlackSession session = null;
        try {
            session = SlackSessionFactory.createWebSocketSlackSession(authToken);
            session.connect();
            SlackChannel slackChannel = session.findChannelByName(channel);
            if (slackChannel == null) {
                logger.error("Could not find Slack channel '{}'", channel);
            } else {
                session.sendMessage(slackChannel, message);
                success = true;
            }
        } catch (IOException e) {
            logger.error("Error sending message to Slack channel", e);
        } finally {
            try {
                if (session != null) {
                    session.disconnect();
                }
            } catch (IOException e) {
                logger.error("Error disconnecting Slack session", e);
            }
        }
        return success;
    }

}
