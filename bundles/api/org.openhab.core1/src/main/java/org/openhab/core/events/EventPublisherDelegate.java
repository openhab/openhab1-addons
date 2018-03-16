/**
 * Copyright (c) 2015-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
