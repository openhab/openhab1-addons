/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.io.transport.mqtt;

/**
 * All message producers which want to register as a message producer to a
 * MqttBrokerConnection should implement this interface.
 *
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public interface MqttMessageProducer {

    /**
     * Set the sender channel which the message producer should use to publish
     * any message.
     * 
     * @param channel
     *            Sender Channel which will be set by the MqttBrokerConnection.
     */
    public void setSenderChannel(MqttSenderChannel channel);
}
