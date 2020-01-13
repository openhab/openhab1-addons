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
package org.openhab.binding.enocean.internal.bus;

import java.util.ArrayList;
import java.util.List;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

public class EventPublisherMock implements EventPublisher {

    private Command sendCommand;
    private Command postCommand;
    private State updateState;
    private String itemName;

    private List<Command> commands = new ArrayList<Command>();

    public void sendCommand(String itemName, Command command) {
        this.itemName = itemName;
        this.sendCommand = command;
        commands.add(command);
    }

    public void postCommand(String itemName, Command command) {
        this.itemName = itemName;
        this.postCommand = command;
        commands.add(command);
    }

    public void postUpdate(String itemName, State newState) {
        this.itemName = itemName;
        this.updateState = newState;
    }

    public Command popLastCommand() {
        if (commands.isEmpty()) {
            return null;
        }
        return commands.remove(0);
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
