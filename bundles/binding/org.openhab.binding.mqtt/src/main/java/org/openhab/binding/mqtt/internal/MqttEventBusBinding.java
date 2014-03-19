/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mqtt.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.mqtt.MqttBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.transport.mqtt.MqttService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding to broadcasts all commands and/or states received on the OpenHab
 * event bus to predefined topics on an MQTT Broker. The binding can also
 * subscribe to state or command topics and publish all of these to the openHAB
 * event bus.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class MqttEventBusBinding extends AbstractBinding<MqttBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(MqttEventBusBinding.class);

	/** MqttService for sending/receiving messages **/
	private MqttService mqttService;

	/** Message producer for sending state messages to MQTT **/
	private MqttMessagePublisher statePublisher;

	/** Message producer for sending command messages to MQTT **/
	private MqttMessagePublisher commandPublisher;

	/** Message consumer for receiving state messages from MQTT **/
	private MqttMessageSubscriber stateSubscriber;

	/** Message consumer for receiving state messages from MQTT **/
	private MqttMessageSubscriber commandSubscriber;

	/**
	 * Name of the broker defined in the openhab.cfg to use for sending messages
	 * to
	 **/
	private String brokerName;

	@Override
	public void activate() {
		super.activate();
		logger.debug("MQTT: Activating event bus binding.");
	}

	@Override
	public void deactivate() {

		if (StringUtils.isBlank(brokerName)) {
			return;
		}

		if (commandPublisher != null) {
			mqttService.unregisterMessageProducer(brokerName, commandPublisher);
			commandPublisher = null;
		}
		if (statePublisher != null) {
			mqttService.unregisterMessageProducer(brokerName, statePublisher);
			statePublisher = null;
		}
		if (commandSubscriber != null) {
			mqttService.unregisterMessageConsumer(brokerName, commandSubscriber);
			commandSubscriber = null;
		}
		if (stateSubscriber != null) {
			mqttService.unregisterMessageConsumer(brokerName, stateSubscriber);
			stateSubscriber = null;
		}
	}

	/**
	 * Extract the item name from the topic. This should be the last part of the
	 * topic string, as the topics are in the format of
	 * /openHab/myItemName/command
	 * 
	 * @param topic
	 *            from which to parse the item name.
	 * @return item name or "unknown".
	 */
	private String getItemNameFromTopic(String topicDefinition,
			String actualTopic) {

		String itemName = "error-parsing-name-from-topic";
		if (StringUtils.isEmpty(actualTopic) || actualTopic.indexOf('/') == -1) {
			return itemName;
		}

		String[] definitionParts = topicDefinition.split("/");
		String[] actualParts = actualTopic.split("/");

		for (int i = 0; i < definitionParts.length; i++) {
			if (definitionParts[i].equalsIgnoreCase("+")) {
				itemName = actualParts[i];
				break;
			}
		}
		return itemName;
	}

	@Override
	public void receiveUpdate(String itemName, State newState) {
		if (newState == null || statePublisher == null
				|| !statePublisher.isActived()) {
			return;
		}
		statePublisher.publish(statePublisher.getTopic(itemName), newState
				.toString().getBytes());
	}

	@Override
	public void receiveCommand(String itemName, Command command) {
		if (commandPublisher == null || command == null || !commandPublisher.isActived()) {
			return;
		}
		commandPublisher.publish(
			commandPublisher.getTopic(itemName), command.toString().getBytes());
	}

	/**
	 * Setter for Declarative Services. Adds the MqttService instance.
	 * 
	 * @param mqttService
	 *            Service.
	 */
	public void setMqttService(MqttService mqttService) {
		this.mqttService = mqttService;
	}

	/**
	 * Unsetter for Declarative Services.
	 * 
	 * @param mqttService
	 *            MqttService to remove.
	 */
	public void unsetMqttService(MqttService mqttService) {
		this.mqttService = null;
	}

	/**
	 * Initialize publisher which publishes all openHAB commands to the given
	 * MQTT topic.
	 * 
	 * @param topic
	 *            to subscribe to.
	 */
	private void setupEventBusStatePublisher(String topic) {

		if (StringUtils.isBlank(topic)) {
			logger.trace("No topic defined for Event Bus State Publisher");
			return;
		}

		try {
			logger.debug("Setting up Event Bus State Publisher for topic {}", topic);
			statePublisher = new MqttMessagePublisher(brokerName + ":" + topic
					+ ":state:*:default");
			mqttService.registerMessageProducer(brokerName, statePublisher);

		} catch (Exception e) {
			logger.error("Could not create event bus state publisher: {}", e.getMessage());
		}

	}

	/**
	 * Initialize subscriber which broadcasts all received command events onto
	 * the openHAB event bus.
	 * 
	 * @param topic
	 *            to subscribe to.
	 */
	private void setupEventBusCommandSubscriber(String topic) {

		if (StringUtils.isBlank(topic)) {
			logger.trace("No topic defined for Event Bus Command Subsriber");
			return;
		}

		try {
			topic = StringUtils.replace(topic, "${item}", "+");
			logger.debug("Setting up Event Bus Command Subscriber for topic {}", topic);
			commandSubscriber = new MqttMessageSubscriber(brokerName + ":"
					+ topic + ":command:default") {

				@Override
				public void processMessage(String topic, byte[] message) {
					Command command;
					try {
						command = getCommand(new String(message));
					} catch (Exception e) {
						logger.error("Error parsing command from message.", e);
						return;
					}
					eventPublisher.postCommand(
							getItemNameFromTopic(getTopic(), topic), command);
				}

			};
			mqttService.registerMessageConsumer(brokerName, commandSubscriber);

		} catch (Exception e) {
			logger.error("Could not create event bus command subscriber: {}",
					e.getMessage());
		}

	}

	/**
	 * Initialize subscriber which broadcasts all received state events onto the
	 * openHAB event bus.
	 * 
	 * @param topic
	 *            to subscribe to.
	 */
	private void setupEventBusStateSubscriber(String topic) {

		if (StringUtils.isBlank(topic)) {
			logger.trace("No topic defined for Event Bus State Subsriber");
			return;
		}

		try {
			topic = StringUtils.replace(topic, "${item}", "+");
			logger.debug("Setting up Event Bus State Subscriber for topic {}", topic);
			stateSubscriber = new MqttMessageSubscriber(brokerName + ":"
					+ topic + ":state:default") {

				@Override
				public void processMessage(String topic, byte[] message) {
					State state;
					try {
						state = getState(new String(message));
					} catch (Exception e) {
						logger.error("Error parsing state from message.", e);
						return;
					}
					eventPublisher.postUpdate(
							getItemNameFromTopic(getTopic(), topic), state);
				}
			};
			mqttService.registerMessageConsumer(brokerName, stateSubscriber);

		} catch (Exception e) {
			logger.error("Could not create event bus state subscriber: {}",
					e.getMessage());
		}

	}

	/**
	 * Initialize publisher which publishes all openHAB commands to the given
	 * MQTT topic.
	 * 
	 * @param topic
	 *            to subscribe to.
	 */
	private void setupEventBusCommandPublisher(String topic) {

		if (StringUtils.isBlank(topic)) {
			logger.trace("No topic defined for Event Bus Command Publisher");
			return;
		}

		try {
			logger.debug("Setting up Event Bus Command Publisher for topic {}", topic);
			commandPublisher = new MqttMessagePublisher(brokerName + ":"
					+ topic + ":command:*:default");
			mqttService.registerMessageProducer(brokerName, commandPublisher);

		} catch (Exception e) {
			logger.error("Could not create event bus command publisher: {}", e.getMessage());
		}
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {

		// load event bus pubish/subscribe configuration from configuration file
		if (properties == null || properties.isEmpty()) {
			logger.trace("No mqtt-eventbus properties configured.");
			return;
		}

		logger.debug("Initializing MQTT Event Bus Binding");
		
		// stop existing publishers/subscribers
		deactivate();

		brokerName = (String) properties.get("broker");
		if (StringUtils.isEmpty(brokerName)) {
			logger.debug("No broker name configured for MQTT EventBusBinding");
			return;
		}

		setupEventBusStatePublisher((String) properties.get("statePublishTopic"));
		setupEventBusStateSubscriber((String) properties.get("stateSubscribeTopic"));
		setupEventBusCommandPublisher((String) properties.get("commandPublishTopic"));
		setupEventBusCommandSubscriber((String) properties.get("commandSubscribeTopic"));

		logger.debug("MQTT Event Bus Binding initialization completed.");
	}
	
}
