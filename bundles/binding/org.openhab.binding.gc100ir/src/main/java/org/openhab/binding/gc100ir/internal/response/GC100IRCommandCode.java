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
package org.openhab.binding.gc100ir.internal.response;

/**
 * Valid commands that the GC100IR may return
 *
 * @author Stephen Liang
 * @since 1.9.0
 *
 */
public enum GC100IRCommandCode {
    UNKNOWN_COMMAND("unknowncommand"),
    COMPLETED_SUCCESSFULLY("completeir");

    private String command;

    private GC100IRCommandCode(String command) {
        this.command = command;
    }

    /**
     * Gets the command string representation of a command code
     *
     * @return String - The command string representation of a command code
     */
    public String getCommand() {
        return command;
    }

    /**
     * Builds the appropriate command code for the given command string
     *
     * @param commandString A string representation of a GC100IR command
     * @return An enum
     */
    public static GC100IRCommandCode getCommandCodeFromString(String commandString) {
        String[] sections = commandString.split(",");

        if (sections.length == 0) {
            throw new IllegalArgumentException("Invalid command received: " + commandString);
        }

        String command = sections[0];

        for (GC100IRCommandCode candidate : values()) {
            if (candidate.getCommand().equals(command)) {
                return candidate;
            }
        }

        throw new IllegalArgumentException("Unknown command received: " + commandString);
    }
}
