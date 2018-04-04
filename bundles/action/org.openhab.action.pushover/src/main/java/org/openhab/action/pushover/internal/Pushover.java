/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.pushover.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This class contains the methods that are made available in scripts and rules
 * for sending messages via the Pushover mobile device push service..
 *
 * @author Chris Graham - Initial contribution
 * @author Christoph Weitkamp - Added Receipts and Callback API for handling of emergency-priority notifications
 * @since 1.5.0
 */
public class Pushover {

    private static final Logger logger = LoggerFactory.getLogger(Pushover.class);

    private static final String JSON_API_URL = "https://api.pushover.net/1/messages.json";
    private static final String JSON_CANCEL_API_URL = "https://api.pushover.net/1/receipts/{receipt}/cancel.json";
    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String DEFAULT_CONTENT_TYPE = "image/jpeg";

    private static final JsonParser parser = new JsonParser();

    private static final String API_RETURN_STATUS_TAG = "status";
    private static final String API_RETURN_INFO_TAG = "info";
    private static final String API_RETURN_ERRORS_TAG = "errors";
    private static final String API_RETURN_RECEIPT_TAG = "receipt";

    private static final int API_MAX_MESSAGE_LENGTH = 512;
    private static final int API_MAX_URL_LENGTH = 512;
    private static final int API_MAX_URL_TITLE_LENGTH = 100;
    private static final int[] API_VALID_PRIORITY_LIST = { -2, -1, 0, 1, 2 };
    private static final int[] API_HIGH_PRIORITY_LIST = { 2 };
    private static final int API_MIN_RETRY_SECONDS = 30;
    private static final int API_MAX_EXPIRE_SECONDS = 86400;

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
    public static final String MESSAGE_KEY_ATTACHMENT = "attachment";

    public static final String MESSAGE_KEY_CONTENT_TYPE = "content-type";

    static String defaultApiKey;
    static String defaultUser;
    static String defaultDevice;
    static String defaultTitle = "openHAB";
    static String defaultUrl;
    static String defaultUrlTitle;
    static int defaultPriority = 0;
    static int emergencyPriority = 2;
    static String defaultSound;

    static int retry = 300;
    static int expire = 3600;
    static int timeout = 10000;

    private String apiKey;
    private String user;
    private String message;
    private String device;
    private String title;
    private String url;
    private String urlTitle;
    private int priority;
    private String sound;
    private String attachment;
    private String contentType;

    public Pushover() {
        apiKey = Pushover.defaultApiKey;
        user = Pushover.defaultUser;
        device = Pushover.defaultDevice;
        title = Pushover.defaultTitle;
        url = Pushover.defaultUrl;
        urlTitle = Pushover.defaultUrlTitle;
        priority = Pushover.defaultPriority;
        sound = Pushover.defaultSound;
    }

    public Pushover withApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public Pushover withUser(String user) {
        this.user = user;
        return this;
    }

    public Pushover withDevice(String device) {
        this.device = device;
        return this;
    }

    public Pushover withTitle(String title) {
        this.title = title;
        return this;
    }

    public Pushover withUrl(String url) {
        this.url = url;
        return this;
    }

    public Pushover withUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
        return this;
    }

    public Pushover withPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public Pushover withEmergencyPriority() {
        this.priority = emergencyPriority;
        return this;
    }

    public Pushover withSound(String sound) {
        this.sound = sound;
        return this;
    }

    public Pushover withAttachment(String attachment) {
        this.attachment = attachment;
        return this;
    }

    public Pushover withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public static Pushover pushoverBuilder(String message) {
        Pushover pushover = new Pushover();
        pushover.message = message;
        return pushover;
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "message", text = "Your message.") String message) {
        return pushover(defaultApiKey, defaultUser, message, defaultDevice, defaultTitle, defaultUrl, defaultUrlTitle,
                defaultPriority, defaultSound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device) {
        return pushover(defaultApiKey, defaultUser, message, device, defaultTitle, defaultUrl, defaultUrlTitle,
                defaultPriority, defaultSound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "priority", text = "The priority of the notification") int priority) {
        return pushover(defaultApiKey, defaultUser, message, defaultDevice, defaultTitle, defaultUrl, defaultUrlTitle,
                priority, defaultSound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "priority", text = "The priority of the notification") int priority,
            @ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url) {
        return pushover(defaultApiKey, defaultUser, message, defaultDevice, defaultTitle, url, defaultUrlTitle,
                priority, defaultSound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "priority", text = "The priority of the notification") int priority,
            @ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url,
            @ParamDoc(name = "urlTitle", text = "A title for your supplementary URL, otherwise just the URL is shown.") String urlTitle) {
        return pushover(defaultApiKey, defaultUser, message, defaultDevice, defaultTitle, url, urlTitle, priority,
                defaultSound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "priority", text = "The priority of the notification") int priority,
            @ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url,
            @ParamDoc(name = "urlTitle", text = "A title for your supplementary URL, otherwise just the URL is shown.") String urlTitle,
            @ParamDoc(name = "sound", text = "The name of one of the sounds supported by device clients to override the user's default sound choice.") String sound) {
        return pushover(defaultApiKey, defaultUser, message, defaultDevice, defaultTitle, url, urlTitle, priority,
                sound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device,
            @ParamDoc(name = "priority", text = "The priority of the notification") int priority) {
        return pushover(defaultApiKey, defaultUser, message, device, defaultTitle, defaultUrl, defaultUrlTitle,
                priority, defaultSound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device,
            @ParamDoc(name = "priority", text = "The priority of the notification") int priority,
            @ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url) {
        return pushover(defaultApiKey, defaultUser, message, device, defaultTitle, url, defaultUrlTitle, priority,
                defaultSound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device,
            @ParamDoc(name = "priority", text = "The priority of the notification") int priority,
            @ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url,
            @ParamDoc(name = "urlTitle", text = "A title for your supplementary URL, otherwise just the URL is shown.") String urlTitle) {
        return pushover(defaultApiKey, defaultUser, message, device, defaultTitle, url, urlTitle, priority,
                defaultSound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device,
            @ParamDoc(name = "priority", text = "The priority of the notification") int priority,
            @ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url,
            @ParamDoc(name = "urlTitle", text = "A title for your supplementary URL, otherwise just the URL is shown.") String urlTitle,
            @ParamDoc(name = "sound", text = "The name of one of the sounds supported by device clients to override the user's default sound choice.") String sound) {
        return pushover(defaultApiKey, defaultUser, message, device, defaultTitle, url, urlTitle, priority, sound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device while providing api key and user/group key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "apiKey", text = "Your application's API token.") String apiKey,
            @ParamDoc(name = "user", text = "The user/group key (not e-mail address) of your user (or you), viewable when logged into Pushover dashboard.") String user,
            @ParamDoc(name = "message", text = "Your message.") String message) {
        return pushover(apiKey, user, message, defaultDevice, defaultTitle, defaultUrl, defaultUrlTitle,
                defaultPriority, defaultSound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device while providing api key and user/group key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "apiKey", text = "Your application's API token.") String apiKey,
            @ParamDoc(name = "user", text = "The user/group key (not e-mail address) of your user (or you), viewable when logged into Pushover dashboard.") String user,
            @ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device) {
        return pushover(apiKey, user, message, device, defaultTitle, defaultUrl, defaultUrlTitle, defaultPriority,
                defaultSound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device while providing api key and user/group key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "apiKey", text = "Your application's API token.") String apiKey,
            @ParamDoc(name = "user", text = "The user/group key (not e-mail address) of your user (or you), viewable when logged into Pushover dashboard.") String user,
            @ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "priority", text = "The priority of the notification") int priority) {
        return pushover(apiKey, user, message, defaultDevice, defaultTitle, defaultUrl, defaultUrlTitle, priority,
                defaultSound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your mobile device while providing api key and user/group key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "apiKey", text = "Your application's API token.") String apiKey,
            @ParamDoc(name = "user", text = "The user/group key (not e-mail address) of your user (or you), viewable when logged into Pushover dashboard.") String user,
            @ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device,
            @ParamDoc(name = "priority", text = "The priority of the notification") int priority) {
        return pushover(apiKey, user, message, device, defaultTitle, defaultUrl, defaultUrlTitle, priority,
                defaultSound);
    }

    @Deprecated
    @ActionDoc(text = "Deprecated. Send a notification to your Android device. apiKey, user and message are required. All else can effectively be null.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean pushover(@ParamDoc(name = "apiKey", text = "Your application's API token.") String apiKey,
            @ParamDoc(name = "user", text = "The user/group key (not e-mail address) of your user (or you), viewable when logged into Pushover dashboard.") String user,
            @ParamDoc(name = "message", text = "Your message.") String message,
            @ParamDoc(name = "device", text = " Your user's device name to send the message directly to that device, rather than all of the user's devices.") String device,
            @ParamDoc(name = "title", text = "Your message's title, otherwise your app's name is used.") String title,
            @ParamDoc(name = "url", text = "A supplementary URL to show with your message.") String url,
            @ParamDoc(name = "urlTitle", text = "A title for your supplementary URL, otherwise just the URL is shown.") String urlTitle,
            @ParamDoc(name = "priority", text = "Send as -1 to always send as a quiet notification, 1 to display as high-priority and bypass the user's quiet hours, or 2 to also require confirmation from the user.") int priority,
            @ParamDoc(name = "sound", text = "The name of one of the sounds supported by device clients to override the user's default sound choice.") String sound) {
        return pushover0(apiKey, user, message, device, title, url, urlTitle, priority, sound, null, null) != null;
    }

    @ActionDoc(text = "Send a notification to your device. apiKey, user and message are required. All else can effectively be null.", returns = "a <code>receipt</code> (30 character string containing the character set [A-Za-z0-9]), if emergency, otherwise empty string or <code>null</code> in case of any error.")
    public static String sendPushoverMessage(
            @ParamDoc(name = "pushover", text = "The Pushover object containing all required parameters.") Pushover p) {
        return pushover0(p.apiKey, p.user, p.message, p.device, p.title, p.url, p.urlTitle, p.priority, p.sound,
                p.attachment, p.contentType);
    }

    // Primary method for sending a message to the Pushover API
    private static String pushover0(String apiKey, String user, String message, String device, String title, String url,
            String urlTitle, int priority, String sound, String attachment, String contentType) {

        List<Part> parts = new ArrayList<>();
        try {

            if (!StringUtils.isEmpty(apiKey)) {
                parts.add(new StringPart(MESSAGE_KEY_API_KEY, apiKey, UTF_8_ENCODING));
            } else if (!StringUtils.isEmpty(defaultApiKey)) {
                parts.add(new StringPart(MESSAGE_KEY_API_KEY, defaultApiKey, UTF_8_ENCODING));
            } else {
                logger.warn("Application API token not specified.");
                return null;
            }

            if (!StringUtils.isEmpty(user)) {
                parts.add(new StringPart(MESSAGE_KEY_USER, user, UTF_8_ENCODING));
            } else if (!StringUtils.isEmpty(defaultUser)) {
                parts.add(new StringPart(MESSAGE_KEY_USER, defaultUser, UTF_8_ENCODING));
            } else {
                logger.warn("The user/group key was not specified.");
                return null;
            }

            if (!StringUtils.isEmpty(message)) {
                if ((message.length() + title.length()) <= API_MAX_MESSAGE_LENGTH) {
                    parts.add(new StringPart(MESSAGE_KEY_MESSAGE, message, UTF_8_ENCODING));
                } else {
                    logger.warn("Together, the event message and title total more than {} characters.",
                            API_MAX_MESSAGE_LENGTH);
                    return null;
                }
            } else {
                logger.warn("The event message is missing.");
                return null;
            }

            if (!StringUtils.isEmpty(device)) {
                parts.add(new StringPart(MESSAGE_KEY_DEVICE, device, UTF_8_ENCODING));
            } else if (!StringUtils.isEmpty(defaultDevice)) {
                parts.add(new StringPart(MESSAGE_KEY_DEVICE, defaultDevice, UTF_8_ENCODING));
            }

            if (!StringUtils.isEmpty(title)) {
                parts.add(new StringPart(MESSAGE_KEY_TITLE, title, UTF_8_ENCODING));
            } else if (!StringUtils.isEmpty(defaultTitle)) {
                parts.add(new StringPart(MESSAGE_KEY_TITLE, defaultTitle, UTF_8_ENCODING));
            }

            if (!StringUtils.isEmpty(url)) {
                if (url.length() <= API_MAX_URL_LENGTH) {
                    parts.add(new StringPart(MESSAGE_KEY_URL, url, UTF_8_ENCODING));
                } else {
                    logger.warn("The url is greater than {} characters.", API_MAX_URL_LENGTH);
                    return null;
                }
            } else if (!StringUtils.isEmpty(defaultUrl)) {
                if (defaultUrl.length() <= API_MAX_URL_LENGTH) {
                    parts.add(new StringPart(MESSAGE_KEY_URL, defaultUrl, UTF_8_ENCODING));
                } else {
                    logger.warn("The url is greater than {} characters.", API_MAX_URL_LENGTH);
                    return null;
                }
            }

            if (!StringUtils.isEmpty(urlTitle)) {
                if (urlTitle.length() <= API_MAX_URL_TITLE_LENGTH) {
                    parts.add(new StringPart(MESSAGE_KEY_URL_TITLE, urlTitle, UTF_8_ENCODING));
                } else {
                    logger.warn("The url title is greater than {} characters.", API_MAX_URL_TITLE_LENGTH);
                    return null;
                }
            } else if (!StringUtils.isEmpty(defaultUrlTitle)) {
                if (defaultUrlTitle.length() <= API_MAX_URL_TITLE_LENGTH) {
                    parts.add(new StringPart(MESSAGE_KEY_URL_TITLE, defaultUrlTitle, UTF_8_ENCODING));
                } else {
                    logger.warn("The url title is greater than {} characters.", API_MAX_URL_TITLE_LENGTH);
                    return null;
                }
            }

            try {
                if (isValueInList(API_VALID_PRIORITY_LIST, priority)) {
                    parts.add(new StringPart(MESSAGE_KEY_PRIORITY, String.valueOf(priority), UTF_8_ENCODING));

                    if (isValueInList(API_HIGH_PRIORITY_LIST, priority)) {
                        if (retry >= API_MIN_RETRY_SECONDS) {
                            parts.add(new StringPart(MESSAGE_KEY_RETRY, String.valueOf(retry), UTF_8_ENCODING));
                        } else {
                            logger.warn("Retry value of {} is too small. Using default value of {}.", retry,
                                    API_MIN_RETRY_SECONDS);
                            parts.add(new StringPart(MESSAGE_KEY_RETRY, String.valueOf(API_MIN_RETRY_SECONDS),
                                    UTF_8_ENCODING));
                        }

                        if (expire <= API_MAX_EXPIRE_SECONDS) {
                            parts.add(new StringPart(MESSAGE_KEY_EXPIRE, String.valueOf(expire), UTF_8_ENCODING));
                        } else {
                            logger.warn("Expire value of {} is too large. Using default value of {}.", expire,
                                    API_MAX_EXPIRE_SECONDS);
                            parts.add(new StringPart(MESSAGE_KEY_EXPIRE, String.valueOf(API_MAX_EXPIRE_SECONDS),
                                    UTF_8_ENCODING));
                        }
                    }
                } else {
                    logger.warn("Invalid priority, skipping. Expected: {}. Got: {}.",
                            Arrays.toString(API_VALID_PRIORITY_LIST), priority);
                }
            } catch (Exception exp) {
                logger.warn("Can't parse the priority value, skipping.");
            }

            if (!StringUtils.isEmpty(sound)) {
                parts.add(new StringPart(MESSAGE_KEY_SOUND, sound, UTF_8_ENCODING));
            } else if (!StringUtils.isEmpty(defaultSound)) {
                parts.add(new StringPart(MESSAGE_KEY_SOUND, defaultSound, UTF_8_ENCODING));
            }

            if (!StringUtils.isEmpty(attachment)) {
                logger.debug("Push the image attachment '{}'.", attachment);
                try {
                    File f = new File(attachment);
                    FilePart fp = new FilePart(MESSAGE_KEY_ATTACHMENT, f.getName(), f);
                    String ct = contentType;
                    if (StringUtils.isEmpty(ct)) {
                        ct = DEFAULT_CONTENT_TYPE;
                    }
                    fp.setContentType(ct);
                    parts.add(fp);
                } catch (IOException e) {
                    logger.warn("Could not process file '{}': {}, will send the message without attachment.",
                            attachment, e.getMessage());
                }
            }
            PostMethod httpPost = new PostMethod(JSON_API_URL);

            httpPost.setRequestEntity(
                    new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), httpPost.getParams()));

            logger.debug("Executing post to {} with the following content: {}", JSON_API_URL, httpPost);
            HttpClient client = new HttpClient();
            String response = "";
            try {
                int statusCode = client.executeMethod(httpPost);
                if (statusCode != HttpStatus.SC_OK) {
                    logger.warn("Method failed: {}.", httpPost.getStatusLine());
                    return null;
                }
                response = IOUtils.toString(httpPost.getResponseBodyAsStream(), UTF_8_ENCODING);
            } catch (IOException e) {
                logger.warn("Fatal transport error. {}", e.getMessage());
                return null;
            } finally {
                // Release the connection.
                httpPost.releaseConnection();
            }
            logger.debug("Raw response: {}", response);

            try {
                if (StringUtils.isEmpty(response)) {
                    logger.warn(
                            "Received an empty response from our Pushover API call. This can mean either we are having trouble connecting to the Pushover API or the Pushover API is actively enforcing rate limits with a connection time-out.");
                    return null;
                }
                JsonObject json = parser.parse(response).getAsJsonObject();
                if (json.has(API_RETURN_INFO_TAG)) {
                    logger.warn("Received info message from Pushover: {}",
                            json.get(API_RETURN_INFO_TAG).getAsJsonArray().getAsString());
                }
                if (json.has(API_RETURN_STATUS_TAG) && json.get(API_RETURN_STATUS_TAG).getAsInt() == 1) {
                    // do not return null, but empty string, since its no error, if the receipt value is not provided
                    // the receipt is provided for emergency-priority only!
                    return json.has(API_RETURN_RECEIPT_TAG) ? json.get(API_RETURN_RECEIPT_TAG).getAsString() : "";
                } else {
                    logger.warn("Received error message from Pushover: {}",
                            json.get(API_RETURN_ERRORS_TAG).getAsJsonArray().getAsString());
                    return null;
                }
            } catch (Exception e) {
                logger.warn("Can't parse response from Pushover: {}", e.getMessage());
                logger.debug("Raw response: {}", response);
                return null;
            }
        } catch (Exception e) {
            logger.warn("An error occurred while notifying your mobile device: {}", e.getMessage());
            return null;
        }
    }

    @ActionDoc(text = "Cancel an emergency-priority notification using the default api key.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean cancelPushoverEmergency(
            @ParamDoc(name = "receipt", text = "Your message's receipt.") String receipt) {
        return cancelPushoverEmergency(defaultApiKey, defaultUser, receipt);
    }

    // Primary method for canceling an emergency-priority message
    @ActionDoc(text = "Cancel an emergency-priority notification. apiKey, user and receipt are required.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean cancelPushoverEmergency(
            @ParamDoc(name = "apiKey", text = "Your application's API token.") String apiKey,
            @ParamDoc(name = "user", text = "The user/group key (not e-mail address) of your user (or you), viewable when logged into Pushover dashboard.") String user,
            @ParamDoc(name = "receipt", text = "Your message's receipt.") String receipt) {

        List<Part> parts = new ArrayList<>();

        try {
            if (!StringUtils.isEmpty(apiKey)) {
                parts.add(new StringPart(MESSAGE_KEY_API_KEY, apiKey, UTF_8_ENCODING));
            } else if (!StringUtils.isEmpty(defaultApiKey)) {
                parts.add(new StringPart(MESSAGE_KEY_API_KEY, defaultApiKey, UTF_8_ENCODING));
            } else {
                logger.warn("Application API token not specified.");
                return false;
            }

            if (!StringUtils.isEmpty(user)) {
                parts.add(new StringPart(MESSAGE_KEY_USER, user, UTF_8_ENCODING));
            } else if (!StringUtils.isEmpty(defaultUser)) {
                parts.add(new StringPart(MESSAGE_KEY_USER, defaultUser, UTF_8_ENCODING));
            } else {
                logger.warn("The user/group key was not specified.");
                return false;
            }

            if (StringUtils.isEmpty(receipt)) {
                logger.warn("The message's receipt is missing.");
                return false;
            }

            String url = JSON_CANCEL_API_URL.replace("{receipt}", receipt);

            PostMethod httpPost = new PostMethod(url);
            httpPost.setRequestEntity(
                    new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), httpPost.getParams()));

            logger.debug("Executing post to {} with the following content: {}", url, httpPost);
            HttpClient client = new HttpClient();
            String response = "";
            try {
                int statusCode = client.executeMethod(httpPost);
                if (statusCode != HttpStatus.SC_OK) {
                    logger.warn("Method failed: {}.", httpPost.getStatusLine());
                    return false;
                }
                response = IOUtils.toString(httpPost.getResponseBodyAsStream(), UTF_8_ENCODING);
            } catch (IOException e) {
                logger.warn("Fatal transport error. {}", e.getMessage());
                return false;
            } finally {
                // Release the connection.
                httpPost.releaseConnection();
            }

            logger.debug("Raw response: {}", response);

            if (StringUtils.isEmpty(response)) {
                logger.warn(
                        "Received an empty response from our Pushover API call. This can mean either we are having trouble connecting to the Pushover API or the Pushover API is actively enforcing rate limits with a connection time-out.");
                return false;
            }
            JsonObject json = parser.parse(response).getAsJsonObject();
            if (json.has(API_RETURN_STATUS_TAG) && json.get(API_RETURN_STATUS_TAG).getAsInt() == 1) {
                return true;
            } else {
                logger.warn("Received error message from Pushover: {}",
                        json.get(API_RETURN_ERRORS_TAG).getAsJsonArray().getAsString());
                return false;
            }
        } catch (Exception e) {
            logger.warn("An error occurred while canceling your notification: {}", e.getMessage());
            return false;
        }
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
