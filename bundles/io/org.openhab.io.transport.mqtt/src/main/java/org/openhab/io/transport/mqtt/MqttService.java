/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.mqtt;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.events.EventPublisher;
import org.openhab.io.transport.mqtt.internal.MqttBrokerConnection;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQTT Service for creating new connections to MQTT brokers from the openHAB
 * configuration file and registering message publishers and subscribers. This
 * service is the main entry point for all bundles wanting to use the MQTT
 * transport.
 * 
 * @author Davy Vanherbergen
 * @author Ben Jones
 * @since 1.3.0
 */
public class MqttService implements ManagedService {

	private static Logger logger = LoggerFactory.getLogger(MqttService.class);

	private final Map<String, MqttBrokerConnection> brokerConnections = new ConcurrentHashMap<String, MqttBrokerConnection>();
	private final Object lock = new Object();

	private EventPublisher eventPublisher;

	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {

		// load broker configurations from configuration file
		if (properties == null || properties.isEmpty()) {
			return;
		}

		synchronized (lock) {
			Enumeration<String> keys = properties.keys();
			while (keys.hasMoreElements()) {
	
				String key = keys.nextElement();
	
				if (key.equals("service.pid")) {
					// ignore the only non-broker property..
					continue;
				}
	
				String[] subkeys = key.split("\\.");
				if (subkeys.length != 2) {
					logger.debug(
							"MQTT Broker property '{}' should have the format 'broker.propertykey'",
							key);
					continue;
				}
	
				String value = (String) properties.get(key);
				String brokerName = subkeys[0];
				String property = subkeys[1];
	
				if (StringUtils.isBlank(value)) {
					logger.trace("Property is empty: {}", key);
					continue;
				} else {
					logger.trace("Processing property: {} = {}", key, value);
				}
	
				MqttBrokerConnection connection = getConnection(brokerName);

				if (property.equals("url")) {
					connection.setUrl(value);
				} else if (property.equals("user")) {
					connection.setUser(value);
				} else if (property.equals("pwd")) {
					connection.setPassword(value);
				} else if (property.equals("qos")) {
					connection.setQos(Integer.parseInt(value));
				} else if (property.equals("retain")) {
					connection.setRetain(Boolean.parseBoolean(value));
				} else if (property.equals("async")) {
					connection.setAsync(Boolean.parseBoolean(value));
				} else if (property.equals("clientId")) {
					connection.setClientId(value);
				} else {
					logger.warn("Unrecognized property: {}", key);
				}
			}
			logger.info("MQTT Service initialization completed.");
	
			for (MqttBrokerConnection connection : brokerConnections.values()) {
				try {
					connection.start();
				} catch (Exception e) {
					logger.error("Error starting broker connection", e);
				}
			}
		}
	}

	/**
	 * Start service.
	 */
	public void activate() {
		logger.debug("Starting MQTT Service...");
	}

	/**
	 * Stop service.
	 */
	public void deactivate() {
		logger.debug("Stopping MQTT Service...");

		synchronized (lock) {
			for (MqttBrokerConnection connection : brokerConnections.values()) {
				logger.info("Stopping broker connection '{}'", connection.getName());
				connection.close();
			}
		}

		logger.debug("MQTT Service stopped.");
	}

	/**
	 * Lookup an broker connection by name.
	 * 
	 * @param brokerName
	 *            to look for.
	 * @return existing connection or new one if it didn't exist yet.
	 */
	private MqttBrokerConnection getConnection(String brokerName) {	
		synchronized (lock) {
			String brokerKey = brokerName.toLowerCase();
			if (!brokerConnections.containsKey(brokerKey)) {
				brokerConnections.put(brokerKey, new MqttBrokerConnection(brokerName));
			}
			return brokerConnections.get(brokerKey);
		}
	}

	/**
	 * Register a new message consumer which can process messages received on
	 * 
	 * @param brokerName
	 *            Name of the broker on which to listen for messages.
	 * @param mqttMessageConsumer
	 *            Consumer which will process any received message.
	 */
	public void registerMessageConsumer(String brokerName,
			MqttMessageConsumer mqttMessageConsumer) {

		mqttMessageConsumer.setEventPublisher(eventPublisher);
		getConnection(brokerName).addConsumer(mqttMessageConsumer);
	}

	/**
	 * Unregisters an existing message.
	 * 
	 * @param mqttMessageConsumer
	 *            Consumer which needs to be unregistered.
	 */
	public void unregisterMessageConsumer(String brokerName,
			MqttMessageConsumer mqttMessageConsumer) {

		getConnection(brokerName).removeConsumer(mqttMessageConsumer);
	}

	public void registerMessageProducer(String brokerName,
			MqttMessageProducer commandPublisher) {

		getConnection(brokerName).addProducer(commandPublisher);
	}

	/**
	 * Register a new message producer which can send messages to the given
	 * broker.
	 * 
	 * @param brokerName
	 *            Name of the broker to which messages can be sent.
	 * @param mqttMessageProducer
	 *            Producer which generates the messages.
	 */
	public void unregisterMessageProducer(String brokerName,
			MqttMessageProducer commandPublisher) {

		getConnection(brokerName).removeProducer(commandPublisher);
	}

	/**
	 * Set the publisher to use for publishing openHAB updates.
	 * 
	 * @param eventPublisher
	 *            EventPublisher
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	/**
	 * Remove the publisher to use for publishing openHAB updates.
	 * 
	 * @param eventPublisher
	 *            EventPublisher
	 */
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}
}
