/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.mqtt.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.transport.mqtt.MqttMessageProducer;
import org.openhab.io.transport.mqtt.MqttSenderChannel;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message publisher configuration for items which send outbound MQTT messages.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class MqttMessagePublisher extends AbstractMqttMessagePubSub implements
		MqttMessageProducer {

	private static final Logger logger = LoggerFactory.getLogger(MqttMessagePublisher.class);

	private MqttSenderChannel senderChannel;

	private String trigger;

	/**
	 * Create new MqttMessagePublisher from config string.
	 * 
	 * @param configuration
	 *            config string
	 * @throws BindingConfigParseException
	 *             if the config string is invalid
	 */
	public MqttMessagePublisher(String configuration) throws BindingConfigParseException {

		String[] config = splitConfigurationString(configuration);
		try {

			if (config.length != 5) {
				throw new BindingConfigParseException(
						"Configuration requires 5 parameters separated by ':'");
			}

			if (StringUtils.isEmpty(config[0])) {
				throw new BindingConfigParseException("Missing broker name.");
			} else {
				setBroker(config[0].trim());
			}

			if (StringUtils.isEmpty(config[1]) || config[1].indexOf('+') != -1
					|| config[1].indexOf('#') != -1) {
				throw new BindingConfigParseException("Invalid topic.");
			} else {
				setTopic(config[1].trim());
			}

			if (StringUtils.isEmpty(config[2])) {
				throw new BindingConfigParseException("Missing type.");
			} else {
				try {
					MessageType t = MessageType.valueOf(config[2].trim().toUpperCase());
					setMessageType(t);
				} catch (IllegalArgumentException e) {
					throw new BindingConfigParseException("Invalid type.");
				}
			}

			if (StringUtils.isEmpty(config[3])) {
				throw new BindingConfigParseException("Missing trigger.");
			} else {
				trigger = config[3].trim();
			}

			if (StringUtils.isEmpty(config[4])) {
				throw new BindingConfigParseException("Missing transformation configuration.");
			} else {
				setTransformationRule(config[4].trim());
			}

		} catch (BindingConfigParseException e) {
			throw new BindingConfigParseException("Configuration '"
					+ configuration
					+ "' is not a valid outbound configuration: "
					+ e.getMessage());
		}
	}

	/**
	 * Check if this configuration supports processing of the given State.
	 * 
	 * @param state
	 *            for which to check if we can process.
	 * @return true if processing is supported.
	 */
	public boolean supportsState(State state) {
		if (getMessageType().equals(MessageType.COMMAND)) {
			return false;
		}
		if (getTrigger().equals("*")) {
			return true;
		}

		return trigger.equalsIgnoreCase(state.toString());
	}

	/**
	 * Check if this configuration supports processing of the given Command.
	 * 
	 * @param command
	 *            for which to check if we can process.
	 * @return true if processing is supported.
	 */
	public boolean supportsCommand(Command command) {
		if (getMessageType().equals(MessageType.STATE)) {
			return false;
		}
		if (getTrigger().equals("*")) {
			return true;
		}

		return trigger.equalsIgnoreCase(command.toString());
	}

	/**
	 * Compose the message to be sent. When a transformation is defined, this
	 * will be applied to the message content. After the transformation is
	 * performed, the default parameters are replaced in the content string.
	 * 
	 * @param value
	 *            command or state in string representation.
	 * @return message content.
	 * @throws Exception
	 */
	private byte[] createMessage(String value) throws Exception {

		if (getTransformationServiceName() != null
				&& getTransformationService() == null) {
			initTransformService();
		}

		String content = value;

		if (getTransformationService() != null) {
			content = getTransformationService().transform(getTransformationServiceParam(), value);
		} else if (getTransformationRule() != null && !getTransformationRule().equalsIgnoreCase("default")) {
			content = getTransformationRule();
		}

		if (getMessageType().equals(MessageType.STATE)) {
			content = StringUtils.replace(content, "${state}", value);
		} else {
			content = StringUtils.replace(content, "${command}", value);
		}
		content = StringUtils.replace(content, "${itemName}", getItemName());
		return content.getBytes();
	}

	/**
	 * Publish a messge to the given topic.
	 * 
	 * @param topic
	 * @param message
	 */
	public void publish(String topic, byte[] message) {
		if (senderChannel == null) {
			return;
		}
		
		try {
			senderChannel.publish(topic, createMessage(new String(message)));
		} catch (Exception e) {
			logger.error("Error publishing...", e);
		}
	}

	@Override
	public void setSenderChannel(MqttSenderChannel channel) {
		senderChannel = channel;
	}

	/**
	 * @return string representation of state or command which triggers the
	 *         sending of a message.
	 */
	public String getTrigger() {
		return trigger;
	}

	/**
	 * @return true if this publisher has been activated by the
	 *         MqttBrokerConnection.
	 */
	public boolean isActived() {
		return senderChannel != null;
	}

	/**
	 * Get the topic and replace ${item} in the topic with the actual name.
	 */
	public String getTopic(String itemName) {
		return StringUtils.replace(getTopic(), "${item}", itemName);
	}
	
}
