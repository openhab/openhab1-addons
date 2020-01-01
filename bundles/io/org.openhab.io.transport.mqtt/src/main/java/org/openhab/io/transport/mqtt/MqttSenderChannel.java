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
 * Callback interface for sending a message to the MqttBrokerConnection.
 *
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public interface MqttSenderChannel {

    /**
     * Send a message to the MQTT broker.
     * 
     * @param topic
     *            Topic to publish the message to.
     * @param message
     *            message payload.
     * @throws Exception
     *             if an error occurs during sending.
     */
    public void publish(String topic, byte[] message) throws Exception;

}
