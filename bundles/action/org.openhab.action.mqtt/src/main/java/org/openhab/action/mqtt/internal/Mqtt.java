/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.mqtt.internal;

import org.openhab.action.mqtt.internal.MqttMessagePublisher;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;

import java.util.concurrent.ConcurrentHashMap;

import org.openhab.io.transport.mqtt.MqttService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class contains the methods that are made available in scripts and rules for MQTT Action.
 * 
 * @author Klaudiusz Staniek
 * @since 1.8.0
 */
public class Mqtt {

	private static final Logger logger = LoggerFactory.getLogger(Mqtt.class);
	public static MqttService mqttTransportService;
	private static ConcurrentHashMap<String, MqttMessagePublisher> messagePublishers = new ConcurrentHashMap<String, MqttMessagePublisher>();
	

	@ActionDoc(text = "Publishes the message to topic using specified MQTT broker")
	public static boolean publish(
			@ParamDoc(name = "brokerName", text = "The name of the MQTT broker defined in configuration") String brokerName,
			@ParamDoc(name = "topic", text = "The MQTT topic") String topic,
			@ParamDoc(name = "message", text = "The message to be published") String message) {
		
		if (!MqttActionService.isProperlyConfigured) {
			logger.debug("Mqtt action is not yet configured - execution aborted!");
			return false;
		}
		logger.debug("Message to broker: {}, topic: {}", brokerName, topic);
		
		MqttMessagePublisher publisher = messagePublishers.get(brokerName);
		if (publisher == null) {
			publisher = new MqttMessagePublisher();
			messagePublishers.put(brokerName, publisher);
			mqttTransportService.registerMessageProducer(brokerName, publisher);
		}
		
		publisher.publish(topic, message);

		return true;
	}

}
