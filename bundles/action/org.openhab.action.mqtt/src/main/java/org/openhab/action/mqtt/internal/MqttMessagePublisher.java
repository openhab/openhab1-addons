/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.mqtt.internal;

import org.openhab.io.transport.mqtt.MqttMessageProducer;
import org.openhab.io.transport.mqtt.MqttSenderChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implementing Message Producer from MQTT IO service.
 * 
 * @author Klaudiusz Staniek
 * @since 1.8.0
 */
public class MqttMessagePublisher implements MqttMessageProducer {

	private static final Logger logger = LoggerFactory.getLogger(MqttMessagePublisher.class);

	private MqttSenderChannel senderChannel;
	
	public void publish(String topic, String message) {
		if (senderChannel == null) {
			return;
		}
		try {
			senderChannel.publish(topic, message.getBytes());
		} catch (Exception e) {
			logger.error("Error publishing message: {}", e.getMessage());
		}
	}
	
	@Override
	public void setSenderChannel(MqttSenderChannel channel) {
		senderChannel = channel;
	}
}
