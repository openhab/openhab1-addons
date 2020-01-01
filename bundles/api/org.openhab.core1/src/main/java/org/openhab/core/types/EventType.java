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
package org.openhab.core.types;

/**
 * Due to the duality of some types (which can be states and commands at the
 * same time), we need to be able to differentiate what the meaning of a
 * message on the bus is - does "item ON" mean that its state has changed to
 * ON or that it should turn itself ON? To decide this, we send the event
 * type as an additional information on the event bus for each message.
 *
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public enum EventType {
    COMMAND,
    UPDATE;

    @Override
    public String toString() {
        switch (this) {
            case COMMAND:
                return "command";
            case UPDATE:
                return "update";
        }
        return "";
    }

}
