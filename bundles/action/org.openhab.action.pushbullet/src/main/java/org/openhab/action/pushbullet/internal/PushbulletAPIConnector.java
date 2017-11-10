/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.pushbullet.internal;

import com.google.gson.Gson;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.openhab.action.pushbullet.internal.model.Push;
import org.openhab.action.pushbullet.internal.model.PushResponse;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.openhab.action.pushbullet.internal.PushbulletConstants.API_URL_PUSHES;
import static org.openhab.action.pushbullet.internal.PushbulletConstants.DEFAULT_BOTNAME;
import static org.openhab.action.pushbullet.internal.PushbulletConstants.TIMEOUT;

/**
 * This class contains the methods that are made available in scripts and rules
 * for sending messages via the PushbulletAPIConnector mobile device push service..
 *
 * @author Hakan Tandogan
 * @since 1.11.0
 */
public class PushbulletAPIConnector {

    private static final Logger logger = LoggerFactory.getLogger(PushbulletAPIConnector.class);

    private static final Version VERSION = FrameworkUtil.getBundle(PushbulletAPIConnector.class).getVersion();

    private static final Gson gson = new Gson();

    private static final Map<String, PushbulletBot> bots = new HashMap<String, PushbulletBot>();

    private static final String METHOD = "POST";

    private static final String CONTENT_TYPE = "application/json";

    public static void addBot(String botName, String accessToken) {
        PushbulletBot bot = new PushbulletBot(botName, accessToken);
        bots.put(botName, bot);
    }

    public static int botCount() {
        return bots.size();
    }

    @ActionDoc(text = "Logs bots configured in PushbulletAPIConnector")
    static public boolean logPushbulletBots() {

        logger.info("===========================================================================");

        logger.info("Configured {} bots", botCount());

        for (Map.Entry<String, PushbulletBot> entry : bots.entrySet()) {
            logger.info("  Bot: {}", entry);
        }

        logger.info("===========================================================================");

        return true;
    }

    @ActionDoc(text = "Sends a message to PushbulletAPIConnector")
    public static boolean sendPushbulletNote(
            @ParamDoc(name = "botName", text = "Name of the bot sending the message") String botName,
            @ParamDoc(name = "recipient", text = "recipient of the message") String recipient,
            @ParamDoc(name = "title", text = "title of the message") String title,
            @ParamDoc(name = "message", text = "the message to be sent") String message) {
        logger.debug("Trying to send a note");
        return sendPush(botName, recipient, title, message, "note");
    }

    @ActionDoc(text = "Sends a message to PushbulletAPIConnector from the default bot")
    public static boolean sendPushbulletNote(
            @ParamDoc(name = "recipient", text = "recipient of the message") String recipient,
            @ParamDoc(name = "title", text = "title of the message") String title,
            @ParamDoc(name = "message", text = "the message to be sent") String message) {
        logger.debug("Trying to send a note to the default bot");
        return sendPush(DEFAULT_BOTNAME, recipient, title, message, "note");
    }

    /**
     * Inner method handling all the pushes.
     *
     * @param botName
     * @param recipient
     * @param title
     * @param body
     * @param type
     * @return
     */
    private static boolean sendPush(String botName, String recipient, String title, String body, String type) {
        boolean result = false;

        logger.trace("    Botname is   '{}'", botName);

        PushbulletBot bot = bots.get(botName);
        if (bot == null) {
            logger.warn("Unconfigured pushbullet bot, please check configuration");
            return false;
        }

        logger.trace("    Bot is       '{}'", bot);

        logger.trace("    Recipient is '{}'", recipient);
        logger.trace("    Title is     '{}'", title);
        logger.trace("    Message is   '{}'", body);

        Push push = new Push();
        push.setTitle(title);
        push.setBody(body);
        push.setType(type);
        push.setEmail(recipient);

        logger.trace("     Push: {}", push);

        String request = gson.toJson(push);
        logger.trace("    Packed Request: {}", request);

        try (InputStream stream = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8))) {

            Properties headers = new Properties();
            headers.put("User-Agent", "openHAB / pushbullet action " + VERSION.toString());
            headers.put("Access-Token", bot.getToken());

            String responseString = HttpUtil.executeUrl(METHOD, API_URL_PUSHES, headers, stream, CONTENT_TYPE, TIMEOUT);

            logger.trace("    Got Response: {}", responseString);
            PushResponse response = gson.fromJson(responseString, PushResponse.class);

            logger.trace("    Unpacked Response: {}", response);

            stream.close();

            if ((null != response) && (null == response.getPushError())) {
                result = true;
            }
        } catch (IOException e) {
            logger.warn("IO problems pushing note: {}", e.getMessage());
        }

        return result;

    }
}
