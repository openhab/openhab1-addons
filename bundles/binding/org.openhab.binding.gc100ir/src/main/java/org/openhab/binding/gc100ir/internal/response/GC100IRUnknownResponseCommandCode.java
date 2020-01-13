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
 * Represents GC100IR unknown response codes that may be returned by the GC100IR Controller.
 *
 * @author Stephen Liang
 * @since 1.9.0
 *
 */
public enum GC100IRUnknownResponseCommandCode implements GC100IRCommand {
    TIMEOUT_NO_CR_RECEIVED("1",
            "Time out occurred because carriage return <CR> not received. The request was not processed."),
    INVALID_MODULE_ADDRESS("2",
            "Invalid module address (module does not exist) received when attempting to ascertain the version number (getversion)."),
    MODULE_DOES_NOT_EXIST("3", "Invalid module address (module does not exist)."),
    INVALID_CONNECTOR_ADDRESS("4", "Invalid connector address."),
    CONNECTOR_1_INVALID("5", "Connector address 1 is set up as 'sensor in' when attempting to send an IR command."),
    CONNECTOR_2_INVALID("6", "Connector address 2 is set up as 'sensor in' when attempting to send an IR command."),
    CONNECTOR_3_INVALID("7", "Connector address 3 is set up as 'sensor in' when attempting to send an IR command."),
    OFFSET_INVALID("8",
            "Offset is set to an even transition number, but should be set to an odd transition number in the IR command."),
    MAXIMUM_TRANSITIONS_EXCEEDED("9", "Maximum number of transitions exceeded (256 total on/off transitions allowed)."),
    NUMBER_OF_TRANSITIONS_INVALID("10",
            "Number of transitions in the IR command is not even (the same number of on and off transitions is required)."),
    CONTACT_CLOSURE_NOT_RELAY("11", "Contact closure command sent to a module that is not a relay.."),
    MISSING_CARRIAGE_RETURN("12", "Missing carriage return. All commands must end with a carriage return."),
    INVALID_CONNECTOR_STATE("13",
            "State was requested of an invalid connector address, or the connector is programmed as IR out and not sensor in."),
    UNSUPPORTED_COMMAND("14", "Command sent to the unit is not supported by the GC-100."),
    SM_IR_INPROCESS("15", "Maximum number of IR transitions exceeded. (SM_IR_INPROCESS)"),
    INVALID_NUMBER_IR_TRANSITIONS("16", "Invalid number of IR transitions (must be an even number)."),
    SENT_IR_TO_WRONG_MODULE("21", "Attempted to send an IR command to a non-IR module."),
    COMMAND_UNSUPPORTED_BY_MODULE("23", "Command sent is not supported by this type of module.");

    private String unknownCodeId;
    private String errorMessage;

    /**
     * Creates a GC100IRUnknownResponseCommandCode given a responseCode and error message. Because there is an error, it
     * is
     * implied
     * that there is an error or a warning for the given command.
     *
     * @param unknownCodeId An unknown response code received from the GC100IR Device
     * @param errorMessage An insightful error message regarding what happened for this particular response message
     */
    GC100IRUnknownResponseCommandCode(String unknownCodeId, String errorMessage) {
        this.unknownCodeId = unknownCodeId;
        this.errorMessage = errorMessage;
    }

    /**
     * Gets the unknown code identifier
     *
     * @return The unknown code identifier
     */
    public String getUnknownCodeId() {
        return unknownCodeId;
    }

    /**
     * Gets the error message associated with this unknown code
     *
     * @return An error message string
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     *
     * @param unknownCodeId
     * @return
     */
    public static GC100IRUnknownResponseCommandCode getResponseCommandCodeFromCommandString(String commandString) {
        String[] sections = commandString.split(",");

        // unknowncommand, <ID>
        String unknownCodeId = sections[1].trim();

        for (GC100IRUnknownResponseCommandCode candidate : values()) {
            if (candidate.getUnknownCodeId().equals(unknownCodeId)) {
                return candidate;
            }
        }

        throw new IllegalArgumentException(
                "There is no matching unknown code for the command string: " + commandString);
    }

    @Override
    public GC100IRCommandCode getCommandCode() {
        return GC100IRCommandCode.UNKNOWN_COMMAND;
    }

    @Override
    public String toString() {
        return String.format("Unknown Command [unknownCodeId=%s, errorMessage=%s]", unknownCodeId, errorMessage);
    }
}
