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

/**
 * An EventPublisher is used to send commands or status updates to the openHAB event bus.
 *
 * @author Kai Kreuzer
 * @since 0.1.0
 */
public interface EventPublisher {

    /**
     * Initiate synchronous sending of a command.
     * This method does not return to the caller until all subscribers have processed the command.
     *
     * @param itemName name of the item to send the command for
     * @param command the command to send
     */
    public abstract void sendCommand(String itemName, Command command);

    /**
     * Initiate asynchronous sending of a command.
     * This method returns immediately to the caller.
     *
     * @param itemName name of the item to send the command for
     * @param command the command to send
     */
    public abstract void postCommand(String itemName, Command command);

    /**
     * Initiate asynchronous sending of a status update.
     * This method returns immediately to the caller.
     *
     * @param itemName name of the item to send the update for
     * @param newState the new state to send
     */
    public abstract void postUpdate(String itemName, State newState);

}
