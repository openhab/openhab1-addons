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
 * @since 1.3.0
 */
public class MqttService implements ManagedService {

	private static Logger logger = LoggerFactory.getLogger(MqttService.class);

	private ConcurrentHashMap<String, MqttBrokerConnection> brokerConnections = new ConcurrentHashMap<String, MqttBrokerConnection>();

	private EventPublisher eventPublisher;

	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {

		// load broker configurations from configuration file
		if (properties == null || properties.isEmpty()) {
			return;
		}

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
			String name = subkeys[0].toLowerCase();
			String property = subkeys[1];

			if (StringUtils.isBlank(value)) {
				logger.trace("Property is empty: {}", key);
				continue;
			} else {
				logger.trace("Processing property: {} = {}", key, value);
			}

			MqttBrokerConnection conn = brokerConnections.get(name);
			if (conn == null) {
				conn = new MqttBrokerConnection(name);
				brokerConnections.put(name, conn);
			}

			if (property.equals("url")) {
				conn.setUrl(value);
			} else if (property.equals("user")) {
				conn.setUser(value);
			} else if (property.equals("pwd")) {
				conn.setPassword(value);
			} else if (property.equals("qos")) {
				conn.setQos(Integer.parseInt(value));
			} else if (property.equals("retain")) {
				conn.setRetain(Boolean.parseBoolean(value));
			} else if (property.equals("async")) {
				conn.setAsync(Boolean.parseBoolean(value));
			} else if (property.equals("clientId")) {
				conn.setClientId(value);
			} else {
				logger.warn("Unrecognized property: {}", key);
			}
		}
		logger.info("MQTT Service initialization completed.");

		for (MqttBrokerConnection con : brokerConnections.values()) {
			try {
				con.start();
			} catch (Exception e) {
				logger.error("Error starting broker connection {} : {}",
						con.getName(), e.getMessage());
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

		Enumeration<String> e = brokerConnections.keys();
		while (e.hasMoreElements()) {
			MqttBrokerConnection conn = brokerConnections.get(e.nextElement());
			logger.info("Stopping connection {}", conn.getName());
			conn.close();
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

		MqttBrokerConnection conn = brokerConnections.get(brokerName
				.toLowerCase());
		if (conn == null) {
			conn = new MqttBrokerConnection(brokerName);
			brokerConnections.put(brokerName.toLowerCase(), conn);
		}
		return conn;
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
