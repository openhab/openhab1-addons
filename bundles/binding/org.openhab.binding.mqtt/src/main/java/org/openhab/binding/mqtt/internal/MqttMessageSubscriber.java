/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mqtt.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.io.transport.mqtt.MqttMessageConsumer;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message subscriber configuration for items which receive inbound MQTT
 * messages.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class MqttMessageSubscriber extends AbstractMqttMessagePubSub implements
		MqttMessageConsumer {

	private static Logger logger = LoggerFactory
			.getLogger(MqttMessageSubscriber.class);

	private EventPublisher eventPublisher;

	private String msgFilter = null;

	private List<Class<? extends State>> acceptedDataTypes = null;
	private List<Class<? extends Command>> acceptedCommandTypes = null;

	/**
	 * Create new MqttMessageSubscriber from config string.
	 * 
	 * @param configuration
	 *            config string
	 * @throws BindingConfigParseException
	 *             if the config string is invalid
	 */
	public MqttMessageSubscriber(String configuration)
			throws BindingConfigParseException {
		this(configuration, null);
	}

	/**
	 * Create new MqttMessageSubscriber from config string and specific item we will be updating.
	 * 
	 * @param configuration
	 *            config string
	 * @param item
	 *            the item to which we will later post updates and send commands
	 * @throws BindingConfigParseException
	 *             if the config string is invalid
	 */
	public MqttMessageSubscriber(String configuration, Item item)
			throws BindingConfigParseException {

		if (item != null) {
			// copy the accepted data types and commands from the specific item we will be updating
			this.acceptedDataTypes = new ArrayList<Class<? extends State>>(item.getAcceptedDataTypes());
			this.acceptedCommandTypes = new ArrayList<Class<? extends Command>>(item.getAcceptedCommandTypes());
		}

		String[] config = splitConfigurationString(configuration);
		try {

			if (config.length != 4 && config.length != 5) {
				throw new BindingConfigParseException(
						"Configuration requires 4 or 5 parameters separated by ':'");
			}

			if (StringUtils.isEmpty(config[0])) {
				throw new BindingConfigParseException("Missing broker name.");
			} else {
				setBroker(config[0].trim());
			}

			if (StringUtils.isEmpty(config[1])) {
				throw new BindingConfigParseException("Invalid topic.");
			} else {
				setTopic(config[1].trim());
			}

			if (StringUtils.isEmpty(config[2])) {
				throw new BindingConfigParseException("Missing type.");
			} else {
				try {
					MessageType t = MessageType.valueOf(config[2].trim()
							.toUpperCase());
					setMessageType(t);
				} catch (IllegalArgumentException e) {
					throw new BindingConfigParseException("Invalid type.");
				}
			}

			if (StringUtils.isEmpty(config[3])) {
				throw new BindingConfigParseException(
						"Missing transformation configuration.");
			} else {
				setTransformationRule(config[3].trim());
				initTransformService();
			}
			if (config.length > 4) {
				setMsgFilter(config[4].trim());
			}

		} catch (BindingConfigParseException e) {
			throw new BindingConfigParseException("Configuration '"
					+ configuration
					+ "' is not a valid inbound configuration: "
					+ e.getMessage());
		}

	}

	@Override
	public void processMessage(String topic, byte[] message) {

		try {

			if (getTransformationServiceName() != null
					&& getTransformationService() == null) {
				logger.debug("Received message before transformation service '{}' was initialized.");
				initTransformService();
			}

			String value = new String(message);

			if (!msgFilterApplies(value)) {
				logger.debug(
						"Skipped message '{}' because Message Filter '{}' does not apply.",
						value, msgFilter);
				return;
			}

			if (getTransformationService() != null) {
				value = getTransformationService().transform(
						getTransformationServiceParam(), value);
			} else if (getTransformationRule() != null
					&& !getTransformationRule().equalsIgnoreCase("default")) {
				value = getTransformationRule();
			}

			value = StringUtils.replace(value, "${itemName}", getItemName());

			if (getMessageType().equals(MessageType.COMMAND)) {
				Command command = getCommand(value, this.acceptedCommandTypes);
				eventPublisher.postCommand(getItemName(), command);
			} else {
				State state = getState(value, this.acceptedDataTypes);
				eventPublisher.postUpdate(getItemName(), state);
			}
		} catch (Exception e) {
			logger.error("Error processing MQTT message.", e);
		}

	}

	/**
	 * Set the publisher to use for publishing openHAB updates.
	 * 
	 * @param eventPublisher
	 *            EventPublisher
	 */
	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	/**
	 * Set a Msg filter to the Subscriber. All Messages that do not match the
	 * filter will be ignored. The filter will be interpreted as regular
	 * expression. Set null to remove filter
	 * 
	 * @param filter
	 *            Regular Expression String
	 */
	public void setMsgFilter(String filter) {
		this.msgFilter = filter;
	}

	public String getMsgFilter() {
		return this.msgFilter;
	}

	/**
	 * Checks whether an incoming message matches a predefined regular
	 * expression filter
	 * 
	 * @param msg
	 * @return true if the msg matches the filter specified, or if no filter is
	 *         specified
	 */
	private boolean msgFilterApplies(String msg) {
		if (msg == null) {
			return false;
		} else if (msgFilter == null) {
			return true;
		} else {
			return msg.matches(msgFilter);
		}
	}

	/**
	 * Convert a string representation of a state to an openHAB State.
	 * 
	 * @param value
	 *            string representation of State
	 * @param acceptedDataTypes
	 *            list of accepted data types for converting value
	 * @return State
	 */
	protected State getState(String value, List<Class<? extends State>> acceptedDataTypes) {

		return TypeParser.parseState(acceptedDataTypes, value);
	}

	/**
	 * Convert a string representation of a command to an openHAB command.
	 * 
	 * @param value
	 *            string representation of command
	 * @param acceptedCommands
	 *            list of accepted commands for converting value
	 * @return Command
	 */
	protected Command getCommand(String value, List<Class<? extends Command>> acceptedCommands) {

		return TypeParser.parseCommand(acceptedCommands, value);
	}

	@Override
	public String getTopic() {
		return StringUtils.replace(super.getTopic(), "${item}", "+");
	}

}
