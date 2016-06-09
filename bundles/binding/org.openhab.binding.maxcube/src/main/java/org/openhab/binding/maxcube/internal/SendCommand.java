/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal;

import org.openhab.binding.maxcube.internal.command.CubeCommand;

/**
 * Class for sending a command.
 *
 * @author Marcel Verpaalen
 *
 */
public final class SendCommand {

    private int id;
    private static int commandId = -1;

    private CubeCommand cubeCommand = null;
    private String serialNumber;
    private String key;
    private String commandText;

    /**
     * Composes a {@link SendCommand} based on a {@link CubeCommand}
     *
     * @param serialNumber of the Cube
     * @param cubeCommand {@link CubeCommand}
     * @param commandText text that will display in the console/log when the command executes
     */
    public SendCommand(String serialNumber, CubeCommand cubeCommand, String commandText) {
        commandId += 1;
        id = commandId;
        this.serialNumber = serialNumber;
        this.cubeCommand = cubeCommand;
        key = getKey(serialNumber, cubeCommand);
        this.setCommandText(commandText);
    }

    /**
     * * Composes a {@link SendCommand} based on a {@link CubeCommand} and identify the channel to remove duplicates in
     * the queue
     *
     * @param serialNumber of the Cube
     * @param cubeCommand {@link CubeCommand}
     * @param channel to correctly remove duplicates from the send queue
     * @param commandText text that will display in the console/log when the command executes
     */
    public SendCommand(String serialNumber, CubeCommand cubeCommand, String channel, String commandText) {
        commandId += 1;
        id = commandId;
        this.serialNumber = serialNumber;
        this.cubeCommand = cubeCommand;
        key = getKey(serialNumber, channel);
        this.setCommandText(commandText);
    }

    /**
     * Get the key based on the serial & command
     * This is can be used to find duplicated commands in the queue
     */
    private static String getKey(String serialNumber, CubeCommand cubeCommand) {
        String key = serialNumber + "-" + cubeCommand.getClass().getSimpleName();
        return key;
    }

    /**
     * @return the key based on the serial and command String
     *         This is can be used to find duplicated commands in the queue
     */
    private static String getKey(String serialNumber, String command) {
        String key = serialNumber + "-" + command;
        return key;
    }

    /**
     * @return the key based on the serial and channel
     *         This is can be used to find duplicated commands in the queue
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the {@link CubeCommand}
     */
    public CubeCommand getCubeCommand() {
        return cubeCommand;
    }

    /**
     * @return the device
     */
    public String getDeviceSerial() {
        return serialNumber;
    }

    /**
     * @return the commandText
     */
    public String getCommandText() {
        return commandText;
    }

    /**
     * @param commandText the commandText to set
     */
    public void setCommandText(String commandText) {
        this.commandText = commandText;
    }

}
