/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.test;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

public class EventPublisherMock implements EventPublisher {

    private Command sendCommand;
    private Command postCommand;
    private State updateState;
    private String itemName;

    public void sendCommand(String itemName, Command command) {
        this.itemName = itemName;
        this.sendCommand = command;
    }

    public void postCommand(String itemName, Command command) {
        this.itemName = itemName;
        this.postCommand = command;
    }

    public void postUpdate(String itemName, State newState) {
        this.itemName = itemName;
        this.updateState = newState;
    }

    public Command getPostCommand() {
        return postCommand;
    }

    public Command getSendCommand() {
        return sendCommand;
    }

    public State getUpdateState() {
        return updateState;
    }

    public String getItemName() {
        return itemName;
    }
}
