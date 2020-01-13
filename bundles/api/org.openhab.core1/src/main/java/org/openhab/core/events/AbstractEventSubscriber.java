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
package org.openhab.core.events;

import static org.openhab.core.events.EventConstants.*;

import org.openhab.core.types.Command;
import org.openhab.core.types.EventType;
import org.openhab.core.types.State;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

/**
 *
 * @author Kai Kreuzer - Initial contribution
 */
abstract public class AbstractEventSubscriber implements EventHandler {
    @Override
    public void handleEvent(Event event) {
        String itemName = (String) event.getProperty("item");

        String topic = event.getTopic();
        String[] topicParts = topic.split(TOPIC_SEPERATOR);

        if (!(topicParts.length > 2) || !topicParts[0].equals(TOPIC_PREFIX)) {
            return; // we have received an event with an invalid topic
        }
        String operation = topicParts[1];

        if (operation.equals(EventType.UPDATE.toString())) {
            State newState = (State) event.getProperty("state");
            if (newState != null) {
                receiveUpdate(itemName, newState);
            }
        }
        if (operation.equals(EventType.COMMAND.toString())) {
            Command command = (Command) event.getProperty("command");
            if (command != null) {
                receiveCommand(itemName, command);
            }
        }
    }

    public void receiveCommand(String itemName, Command command) {
        // default implementation: do nothing
    }

    public void receiveUpdate(String itemName, State newState) {
        // default implementation: do nothing
    }

}
