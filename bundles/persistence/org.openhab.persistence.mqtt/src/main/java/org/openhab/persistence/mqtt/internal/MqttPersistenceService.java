/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.mqtt.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.io.transport.mqtt.MqttService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a {@link PersistenceService} implementation using MQTT.
 * 
 * It can be used to persist item states through MQTT messages. The service only
 * supports sending of data, not receiving.
 * 
 * Which items are persisted and when they are persisted can be configured in
 * the mqtt.persist file in the configurations/persistence folder. The
 * mqtt.persist file follows the standard openHAB persistence definitions.
 * 
 * The broker to which the messages are sent is defined by the
 * mqtt-persistence:broker property in the openhab.cfg file.
 * 
 * The topic to which messages are sent is defined by the mqtt-persistence:topic
 * property.
 * 
 * The message payload is created from a template string, which you can define
 * in the property mqtt-persistence:message.
 * 
 * For both the topic and message, the following parameter replacements are made
 * using String.format:
 * 
 * <pre>
 * 	%1 item name 
 * 	%2 alias (as defined in mqtt.persist)
 * 	%3 item state 
 * 	%4 current timestamp
 * </pre>
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class MqttPersistenceService implements PersistenceService, ManagedService {

	private static Logger logger = LoggerFactory.getLogger(MqttPersistenceService.class);

	private MqttService mqttService;

	private String brokerName;

	private String topic;

	private String messageTemplate;

	private MqttPersistencePublisher publisher;

	private boolean configured;

	/**
	 * Start the persistence service.
	 */
	public void activate() {

		if (StringUtils.isBlank(brokerName)) {
			logger.debug("Configuration incomplete. Cannot start yet.");
			return;
		}

		logger.debug("Activating MQTT Persistence");

		// create a new message publisher and register it
		publisher = new MqttPersistencePublisher(topic, messageTemplate);
		mqttService.registerMessageProducer(brokerName, publisher);
	}

	/**
	 * Shut down the persistence service.
	 */
	public void deactivate() {

		logger.debug("Deactivating MQTT Persistence");
		if (StringUtils.isNotBlank(brokerName) && publisher != null) {
			mqttService.unregisterMessageProducer(brokerName, publisher);
		}
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {

		if (properties == null || properties.isEmpty()) {
			logger.trace("No config properties available.");
			return;
		}

		brokerName = getProperty(properties, "broker");
		topic = getProperty(properties, "topic");
		messageTemplate = getProperty(properties, "message");
		configured = true;
		
		logger.debug("Configuration updated for MQTT Persistence.");
		
		deactivate();
		activate();
	}

	/**
	 * Get a value from the given properties.
	 * 
	 * @param properties
	 *            dictionary to load property from.
	 * @param name
	 *            of the property
	 * @return property value
	 * @throws ConfigurationException
	 *             if the property is empty
	 */
	private String getProperty(Dictionary<String, ?> properties, String name) throws ConfigurationException {

		String value = (String) properties.get(name);
		if (StringUtils.isNotBlank(value)) {
			return value.trim();
		} else {
			throw new ConfigurationException("mqtt-persistence:" + name, "Missing or invalid property '" + name + "'");
		}
	}

	@Override
	public String getName() {
		return "mqtt";
	}

	@Override
	public void store(Item item) {
		store(item, null);
	}

	@Override
	public void store(Item item, String alias) {

		if (!configured) {
			logger.trace("MQTT Persistence not configured yet. Cannot store item state for {}", item.getName());
			return;
		}
		try {
			publisher.publish(item, alias);
			logger.debug("Published item state '{}' for item '{}'", item.getState(), item.getName());
		} catch (Exception e) {
			logger.error("Error sending persistency message for item '{}' : {}", item.getName(), e);
		}
	}

	/**
	 * Set MQTT Service from DS.
	 * 
	 * @param mqttService
	 *            to set.
	 */
	public void setMqttService(MqttService mqttService) {
		this.mqttService = mqttService;
	}

	/**
	 * Unset MQTT Service from DS.
	 * 
	 * @param mqttService
	 *            to remove.
	 */
	public void unsetMqttService(MqttService mqttService) {
		this.mqttService = null;
	}

}
