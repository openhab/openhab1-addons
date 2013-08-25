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
package org.openhab.persistence.mqtt.internal;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import org.openhab.core.items.Item;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.io.transport.mqtt.MqttMessageProducer;
import org.openhab.io.transport.mqtt.MqttSenderChannel;

/**
 * MQTT Message publisher for composing and sending persistence messages.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class MqttPersistencePublisher implements MqttMessageProducer {

	private MqttSenderChannel channel;

	private String messageTemplate;

	private String topic;
	

	/**
	 * Initialize publisher with a given topic and template.
	 * 
	 * @param topic
	 *            MQTT publish topic.
	 * @param messageTemplate
	 *            message payload template.
	 */
	public MqttPersistencePublisher(String topic, String messageTemplate) {
		this.topic = topic;
		this.messageTemplate = messageTemplate;
	}

	@Override
	public void setSenderChannel(MqttSenderChannel channel) {
		this.channel = channel;
	}

	/**
	 * Publish a persistence message for a given item.
	 * 
	 * Topic and template will be reformatted by String.format using the
	 * following parameters:
	 * 
	 * <pre>
	 * 	%1 item name 
	 * 	%2 alias (as defined in mqtt.persist)
	 * 	%3 item state 
	 * 	%4 current timestamp
	 * </pre>
	 * 
	 * @param item
	 *            item which to persist the state of.
	 * @param alias
	 *            null or as defined in persistence configuration.
	 * @throws Exception
	 *             when no MQTT message could be sent.
	 */
	public void publish(Item item, String alias) throws Exception {

		Object state = item.getState().toString();
		
		if (item.getState() instanceof DecimalType) {
			state = ((DecimalType) item.getState()).toBigDecimal();
		} else if (item.getState() instanceof DateTimeType) {
			state = ((DateTimeType) item.getState()).getCalendar();
		} else if (item.getState() instanceof OnOffType) {
			state = item.getState().equals(OnOffType.ON) ? "1" : "0";
		} else if (item.getState() instanceof OpenClosedType) {
			state = item.getState().equals(OpenClosedType.OPEN) ? "1" : "0";
		} else if (item.getState() instanceof UpDownType) {
			state = item.getState().equals(UpDownType.UP) ? "1" : "0";
		}

		String message = format(messageTemplate, item.getName(), trimToEmpty(alias), state, currentTimeMillis());
		String destination = format(topic, item.getName(), trimToEmpty(alias), state, currentTimeMillis());
		
		channel.publish(destination, message.getBytes());
	}
	
}
