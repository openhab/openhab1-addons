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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
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

	/**
	 * Create new MqttMessageSubscriber from config string.
	 * 
	 * @param configuration
	 *            config string
	 * @throws BindingConfigParseException
	 *             if the config string is invalid
	 */
	public MqttMessageSubscriber(String configuration) throws BindingConfigParseException {

		String[] config = splitConfigurationString(configuration);
		try {

			if (config.length != 4) {
				throw new BindingConfigParseException(
						"Configuration requires 4 parameters separated by ':'");
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

			if (getTransformationServiceName() != null && getTransformationService() == null) {
				initTransformService();
			}

			String value = new String(message);

			if (getTransformationService() != null) {
				value = getTransformationService().transform(
						getTransformationServiceParam(), value);
			} else if (getTransformationRule() != null
					&& !getTransformationRule().equalsIgnoreCase("default")) {
				value = getTransformationRule();
			}

			value = StringUtils.replace(value, "${itemName}", getItemName());

			if (getMessageType().equals(MessageType.COMMAND)) {
				Command command = getCommand(value);
				eventPublisher.postCommand(getItemName(), command);
			} else {
				State state = getState(value);
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
	 * Convert a string representation of a state to an openHAB State.
	 * 
	 * @param value
	 *            string representation of State
	 * @return State
	 */
	protected State getState(String value) {

		List<Class<? extends State>> stateList = new ArrayList<Class<? extends State>>();

		// Not sure if the sequence below is the best one..
		stateList.add(OnOffType.class);
		stateList.add(OpenClosedType.class);
		stateList.add(UpDownType.class);
		stateList.add(HSBType.class);
		stateList.add(PercentType.class);
		stateList.add(DecimalType.class);
		stateList.add(DateTimeType.class);
		stateList.add(StringType.class);

		return TypeParser.parseState(stateList, value);

	}

	/**
	 * Convert a string representation of a command to an openHAB command.
	 * 
	 * @param value
	 *            string representation of command
	 * @return Command
	 */
	protected Command getCommand(String value) {

		List<Class<? extends Command>> commandList = new ArrayList<Class<? extends Command>>();

		commandList.add(OnOffType.class);
		commandList.add(OpenClosedType.class);
		commandList.add(UpDownType.class);
		commandList.add(IncreaseDecreaseType.class);
		commandList.add(StopMoveType.class);
		commandList.add(HSBType.class);
		commandList.add(PercentType.class);
		commandList.add(DecimalType.class);
		commandList.add(StringType.class);

		return TypeParser.parseCommand(commandList, value);

	}

	@Override
	public String getTopic() {
		return StringUtils.replace(super.getTopic(), "${item}", "+");
	}

}
