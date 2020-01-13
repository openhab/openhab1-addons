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
package org.openhab.action.pebble.internal;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.palolem.timeline.Timeline;
import nl.palolem.timeline.api.pin.Icon;
import nl.palolem.timeline.api.pin.Notification;
import nl.palolem.timeline.api.pin.Pin;
import nl.palolem.timeline.api.pin.action.HttpAction;
import nl.palolem.timeline.api.pin.layout.GenericNotification;
import nl.palolem.timeline.api.pin.layout.GenericPin;
import nl.palolem.timeline.util.PebbleException;

/**
 * This class contains the methods that are made available in scripts and rules for pushing pins and notifications
 * to the timeline om Pebble smartwatches.
 *
 * @author Jeroen Idserda
 * @since 1.9.0
 */
public class Pebble {

    private static final Logger logger = LoggerFactory.getLogger(Pebble.class);

    private static final String BACKGROUND_COLOR = "#FFAA00"; // 'Chrome Yellow'

    private static Map<String, PebbleInstance> configuration = new HashMap<String, PebbleInstance>();

    public static boolean pebblePin(
            @ParamDoc(name = "token", text = "Pebble openHAB app token OR instance name") String instanceOrToken,
            @ParamDoc(name = "date", text = "Time to schedule the pin") Date time,
            @ParamDoc(name = "title", text = "Title of the pin") String title,
            @ParamDoc(name = "body", text = "Body text of the pin") String body) {

        // @formatter:off
		Pin pin = new Pin.Builder()
				.id(String.valueOf(new Date().getTime()))
				.time(time)
				.layout(new GenericPin.Builder()
						.title(title)
						.tinyIcon(Icon.SCHEDULED_EVENT)
						.body(body)
						.backgroundColor(BACKGROUND_COLOR)
						.build())
				.build();
		// @formatter:on

        return sendPin(getToken(instanceOrToken), pin);
    }

    public static boolean pebblePin(
            @ParamDoc(name = "token", text = "Pebble openHAB app token OR instance name") String instanceOrToken,
            @ParamDoc(name = "date", text = "Time to schedule the pin") Date time,
            @ParamDoc(name = "pinTitle", text = "Title of the pin") String pinTitle,
            @ParamDoc(name = "actionTitle", text = "Title of the action") String actionTitle,
            @ParamDoc(name = "url", text = "URL to GET") String url) {

        // @formatter:off
		Pin pin = new Pin.Builder()
				.id(String.valueOf(new Date().getTime()))
				.time(time)
				.layout(new GenericPin.Builder()
						.title(pinTitle)
						.tinyIcon(Icon.SCHEDULED_EVENT)
						.backgroundColor(BACKGROUND_COLOR)
						.build())
				.action(new HttpAction.Builder()
						.title(actionTitle)
						.url(url)
						.build())
				.build();
		// @formatter:on

        return sendPin(getToken(instanceOrToken), pin);
    }

    public static boolean pebbleNotification(
            @ParamDoc(name = "token", text = "Pebble openHAB app token OR instance name") String instanceOrToken,
            @ParamDoc(name = "title", text = "Title of the notification") String title,
            @ParamDoc(name = "body", text = "Body of the notification") String body) {

        // @formatter:off
		Pin pin = new Pin.Builder().id(String.valueOf(new Date().getTime()))
				.time(new Date())
				.createNotification(new Notification.Builder()
						.layout(new GenericNotification.Builder()
							.title(title)
							.body(body)
							.tinyIcon(Icon.GENERIC_WARNING)
							.backgroundColor(BACKGROUND_COLOR)
							.build()).build())
				.layout(new GenericPin.Builder()
						.title(title)
						.body(body)
						.backgroundColor(BACKGROUND_COLOR)
						.tinyIcon(Icon.GENERIC_WARNING)
						.build())
				.build();
		// @formatter:on

        sendPin(getToken(instanceOrToken), pin);

        return true;
    }

    public static PebbleInstance getInstance(String name) {
        return configuration.get(name);
    }

    public static void setInstance(PebbleInstance instance) {
        configuration.put(instance.getName(), instance);
    }

    private static String getToken(String instanceOrToken) {
        String token = instanceOrToken;

        PebbleInstance instance = configuration.get(instanceOrToken);
        if (instance != null) {
            token = instance.getToken();
        }

        return token;
    }

    private static boolean sendPin(String token, Pin pin) {
        boolean succes = false;

        try {
            Timeline.sendPin(token, pin);
            succes = true;
        } catch (PebbleException e) {
            logger.error("Error in communication with Pebble\n", e);
        } catch (IOException e) {
            logger.error("Error in communication with Pebble\n", e);
        }

        logger.debug("Sending pin with id {} " + ((succes) ? "succeeded" : "failed"), pin.getId());

        return succes;
    }
}
