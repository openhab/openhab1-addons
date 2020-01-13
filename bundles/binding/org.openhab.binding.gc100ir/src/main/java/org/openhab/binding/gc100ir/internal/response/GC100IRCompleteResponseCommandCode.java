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
 * Represents a complete response code from a GC100IR device.
 *
 * @author Stephen Liang
 * @since 1.9.0
 *
 */
public class GC100IRCompleteResponseCommandCode implements GC100IRCommand {
    private String connectorAddress;
    private String uniqueIdentifier;

    /**
     * Constructor for GC100IRCompleteResponseCode
     *
     * @param connectorAddress The connector address which the complete command is associated with
     * @param uniqueIdentifier The unique identifier for the complete command
     */
    private GC100IRCompleteResponseCommandCode(String connectorAddress, String uniqueIdentifier) {
        this.connectorAddress = connectorAddress;
        this.uniqueIdentifier = uniqueIdentifier;
    }

    /**
     * Returns the connector address that the complete command is associated with
     *
     * @return The connector address
     */
    public String getConnectorAddress() {
        return connectorAddress;
    }

    /**
     * Gets the unique identifier code, this code is associated with the ID used during the sendir command.
     *
     * @return The unique identifier code from the GC100IR device
     */
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    /**
     * Builds a GC100IRCompleteResponseCode given a command string in the format of:
     * <code>completeir,<connectoraddress>,<ID></code>
     *
     * @param commandString
     * @return
     */
    public static GC100IRCompleteResponseCommandCode getResponseCommandCodeFromCommandString(String commandString) {
        if (!commandString.startsWith(GC100IRCommandCode.COMPLETED_SUCCESSFULLY.getCommand())) {
            throw new IllegalArgumentException(
                    "The command string " + commandString + " is not a valid completeir command");
        }

        String[] sections = commandString.split(",");

        // completeir,<connectoraddress>,<ID>
        if (sections.length != 3) {
            throw new IllegalArgumentException(
                    "The command string " + commandString + " is not a valid completeir command");
        }

        return new GC100IRCompleteResponseCommandCode(sections[1], sections[2]);
    }

    @Override
    public String toString() {
        return String.format("Complete IR Command [connectorAddress=%s, uniqueIdentifier=%s]", connectorAddress,
                uniqueIdentifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GC100IRCommandCode getCommandCode() {
        return GC100IRCommandCode.COMPLETED_SUCCESSFULLY;
    }
}
