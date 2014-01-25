/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.mqtt;

import org.openhab.core.events.EventPublisher;

/**
 * All message consumers which want to register as a message consumer to a
 * MqttBrokerConnection should implement this interface.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public interface MqttMessageConsumer {

	/**
	 * Process a received MQTT message.
	 * 
	 * @param topic
	 *            The mqtt topic on which the message was received.
	 * @param payload
	 *            content of the message.
	 */
	public void processMessage(String topic, byte[] payload);

	/**
	 * @return topic to subscribe to. May contain + or # wildcards
	 */
	public String getTopic();

	/**
	 * Set Topic to subscribe to. May contain + or # wildcards
	 * 
	 * @param topic
	 *            to subscribe to.
	 */
	public void setTopic(String topic);

	/**
	 * Set the event publisher to use when broadcasting received messages onto
	 * the openHAB event bus.
	 * 
	 * @param eventPublisher
	 */
	public void setEventPublisher(EventPublisher eventPublisher);

}
