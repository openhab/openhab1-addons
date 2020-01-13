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

import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kai Kreuzer - Initial contribution
 */
public class EventPublisherDelegate implements org.openhab.core.events.EventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(EventPublisherDelegate.class);

    private EventPublisher eventPublisher;

    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void unsetEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = null;
    }

    @Override
    public void sendCommand(String itemName, Command command) {
        // we do not offer synchronous sending of commands anymore
        postCommand(itemName, command);
    }

    @Override
    public void postCommand(String itemName, Command command) {
    }

    @Override
    public void postUpdate(String itemName, State newState) {
    }
}
