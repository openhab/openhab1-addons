/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.action.pushsafer.internal;

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

/**
 * This class contains the methods that are made available in scripts and rules
 * for sending messages via the Pushsafer mobile device push service..
 *
 * @author Chris Graham / Kevin Siml
 * @since 1.9.0
 */
public class Pushsafer {

    private static final Logger logger = LoggerFactory.getLogger(Pushsafer.class);

    private final static String API_URL = "https://www.pushsafer.com/api";
    private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private final static String UTF_8_ENCODING = "UTF-8";

    private final static String API_RETURN_ROOT_TAG = "hash";
    private final static String API_RETURN_STATUS_TAG = "status";
    private final static String API_RETURN_ERROR_TAG = "error";
    private final static String API_RETURN_STATUS_SUCCESS = ":1,";
    private final static int API_MAX_MESSAGE_LENGTH = 4096;

    public static final String MESSAGE_KEY_API_KEY = "k";
    public static final String MESSAGE_KEY_MESSAGE = "m";
    public static final String MESSAGE_KEY_DEVICE = "d";
    public static final String MESSAGE_KEY_TITLE = "t";
    public static final String MESSAGE_KEY_ICON = "i";
    public static final String MESSAGE_KEY_VIBRATION = "v";
    public static final String MESSAGE_KEY_SOUND = "s";

    static int timeout = 10000;

    // Primary method for sending a message to the Pushsafer API
    @ActionDoc(text = "Send a notification to your iOS, Android or Win10 device. Private or Alias Key and message are required. All other can be null. Check the Pushsafer.com API for more information: https://www.pushsafer.com/en/pushapi", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushsafer(@ParamDoc(name = "apiKey", text = "Your Private or Alias Key.") String apiKey,

            @ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "title", text = "Your message's title.") String title,
            @ParamDoc(name = "device", text = "Your device id or device group id to send the message to that device(s), leave empty to send the message to all of your registered devices.") String device,
            @ParamDoc(name = "icon", text = "A icon to show with your message.") String icon,
            @ParamDoc(name = "vibration", text = "How often the device should vibrate (1-3 or leave empty).") String vibration,
            @ParamDoc(name = "sound", text = "The id of one of the sounds to override the default sound.") String sound) {

        StringBuilder data = new StringBuilder();

        try {

            // Private or Alias Key
            if (!StringUtils.isEmpty(apiKey)) {
                addEncodedParameter(data, MESSAGE_KEY_API_KEY, apiKey);
            } else {
                logger.error("Private or Alias key not specified.");
                return false;
            }

            // Message
            if (!StringUtils.isEmpty(message)) {
                if ((message.length() + title.length()) <= API_MAX_MESSAGE_LENGTH) {
                    addEncodedParameter(data, MESSAGE_KEY_MESSAGE, message);
                } else {

                    logger.error("Together, the event message and title total more than {} characters.",
                            API_MAX_MESSAGE_LENGTH);
                    return false;
                }
            } else {
                logger.error("The event message is missing.");
                return false;
            }

            // Device
            if (!StringUtils.isEmpty(device)) {
                addEncodedParameter(data, MESSAGE_KEY_DEVICE, device);
            }

            // Title
            if (!StringUtils.isEmpty(title)) {
                addEncodedParameter(data, MESSAGE_KEY_TITLE, title);
            }

            // Icon
            if (!StringUtils.isEmpty(icon)) {
                addEncodedParameter(data, MESSAGE_KEY_ICON, icon);
            }

            // Vibration
            if (!StringUtils.isEmpty(vibration)) {
                addEncodedParameter(data, MESSAGE_KEY_VIBRATION, vibration);
            }

            // Sound
            if (!StringUtils.isEmpty(sound)) {
                addEncodedParameter(data, MESSAGE_KEY_SOUND, sound);
            }

            String content = data.toString();

            logger.debug("Executing post with the following content: {}", content);
            String response = HttpUtil.executeUrl("POST", API_URL, IOUtils.toInputStream(content), CONTENT_TYPE,
                    timeout);
            logger.debug("Raw response: {}", response);

            try {
                if (StringUtils.isEmpty(response)) {
                    logger.error(
                            "Received an empty response from our Pushsafer API call. This can mean either we are having trouble connecting to the Pushsafer API or the Pushsafer API is actively enforcing rate limits with a connection time-out.");
                    return false;
                }

                if (StringUtils.containsIgnoreCase(response, API_RETURN_STATUS_SUCCESS)) {
                    return true;
                } else {

                    logger.error("Received error message from Pushsafer: {}", response);
                    return false;
                }
            } catch (Exception e) {

                logger.warn("Can't parse response from Pushsafer: {}", response);
                return false;
            }
        } catch (Exception e) {

            logger.error("An error occurred while notifying your mobile device: {}", e);
            return false;
        }
    }

    private static void addEncodedParameter(StringBuilder sb, String name, String value)
            throws UnsupportedEncodingException {
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

    private static boolean isValueInList(int[] list, int value) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] == value) {
                return true;
            }
        }

        return false;
    }
}