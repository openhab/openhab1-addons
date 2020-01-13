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
package org.openhab.binding.upb.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model for a message sent or received from a UPB modem.
 *
 * @author cvanorman
 * @since 1.9.0
 */
public class UPBMessage {

    /**
     * An enum of possible commands.
     * 
     * @author cvanorman
     *
     */
    public enum Command {
        ACTIVATE,
        DEACTIVATE,
        GOTO,
        START_FADE,
        STOP_FADE,
        BLINK,
        REPORT_STATE,
        STORE_STATE,
        DEVICE_STATE,
        NONE;

        /**
         * Gets the protocol byte code for this Command.
         * 
         * @return
         */
        public byte toByte() {
            for (Entry<Integer, Command> e : commandMap.entrySet()) {
                if (e.getValue() == this) {
                    return e.getKey().byteValue();
                }
            }

            return 0;
        }

        /**
         * Converts a byte value into a Command.
         * 
         * @param value
         *            the byte value.
         * @return the Command that is represented by the given byte value.
         */
        public static Command valueOf(byte value) {
            return commandMap.get(value);
        }
    }

    /**
     * An enum of possible modem response types.
     * 
     * @author cvanorman
     *
     */
    public enum Type {
        ACCEPT,
        BUSY,
        ERROR,
        ACK,
        NAK,
        MESSAGE_REPORT,
        NONE;
    }

    private static final Logger logger = LoggerFactory.getLogger(UPBMessage.class);

    private static HashMap<Integer, Command> commandMap = new HashMap<>();

    static {
        commandMap.put(0x20, Command.ACTIVATE);
        commandMap.put(0x21, Command.DEACTIVATE);
        commandMap.put(0x22, Command.GOTO);
        commandMap.put(0x23, Command.START_FADE);
        commandMap.put(0x24, Command.STOP_FADE);
        commandMap.put(0x25, Command.BLINK);
        commandMap.put(0x30, Command.REPORT_STATE);
        commandMap.put(0x31, Command.STORE_STATE);
        commandMap.put(0x86, Command.DEVICE_STATE);
    }

    private static HashMap<String, Type> typeMap = new HashMap<>();

    static {
        typeMap.put("PA", Type.ACCEPT);
        typeMap.put("PB", Type.BUSY);
        typeMap.put("PE", Type.ERROR);
        typeMap.put("PK", Type.ACK);
        typeMap.put("PN", Type.NAK);
        typeMap.put("PU", Type.MESSAGE_REPORT);
    }

    /**
     * Converts a hex string into a {@link UPBMessage}.
     * 
     * @param commandString
     *            the string as returned by the modem.
     * @return a new UPBMessage.
     */
    public static UPBMessage fromString(String commandString) {
        UPBMessage command = new UPBMessage();

        String typeString = commandString.substring(0, 2);
        Type type = Type.NONE;

        if (typeMap.containsKey(typeString)) {
            type = typeMap.get(typeString);
        }

        command.setType(type);

        try {
            if (commandString.length() > 2) {
                byte[] data = DatatypeConverter.parseHexBinary(commandString.substring(2));
                command.getControlWord().setBytes(data[1], data[0]);
                int index = 2;
                command.setNetwork(data[index++]);
                command.setDestination(data[index++]);
                command.setSource(data[index++]);

                int commandCode = data[index++] & 0xFF;

                if (commandMap.containsKey(commandCode)) {
                    command.setCommand(commandMap.get(commandCode));
                } else {
                    command.setCommand(Command.NONE);
                }

                if (index < data.length - 1) {
                    command.setArguments(Arrays.copyOfRange(data, index, data.length - 1));
                }
            }
        } catch (Exception e) {
            logger.error("Attempted to parse invalid message: {}", commandString, e);
        }

        return command;
    }

    private Type type;
    private ControlWord controlWord = new ControlWord();
    private byte network;
    private byte destination;
    private byte source;

    private Command command = Command.NONE;
    private byte[] arguments;

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * @return the controlWord
     */
    public ControlWord getControlWord() {
        return controlWord;
    }

    /**
     * @param controlWord
     *            the controlWord to set
     */
    public void setControlWord(ControlWord controlWord) {
        this.controlWord = controlWord;
    }

    /**
     * @return the network
     */
    public byte getNetwork() {
        return network;
    }

    /**
     * @param network
     *            the network to set
     */
    public void setNetwork(byte network) {
        this.network = network;
    }

    /**
     * @return the destination
     */
    public byte getDestination() {
        return destination;
    }

    /**
     * @param destination
     *            the destination to set
     */
    public void setDestination(byte destination) {
        this.destination = destination;
    }

    /**
     * @return the source
     */
    public byte getSource() {
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(byte source) {
        this.source = source;
    }

    /**
     * @return the command
     */
    public Command getCommand() {
        return command;
    }

    /**
     * @param command
     *            the command to set
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * @return the arguments
     */
    public byte[] getArguments() {
        return arguments;
    }

    /**
     * @param arguments
     *            the arguments to set
     */
    public void setArguments(byte[] arguments) {
        this.arguments = arguments;
    }
}
