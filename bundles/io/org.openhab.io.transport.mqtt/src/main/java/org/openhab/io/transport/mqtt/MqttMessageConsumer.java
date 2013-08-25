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
