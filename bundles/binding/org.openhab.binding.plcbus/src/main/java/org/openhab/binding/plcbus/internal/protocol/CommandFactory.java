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
package org.openhab.binding.plcbus.internal.protocol;

/**
 * Factory for PLC Bus Commands
 *
 * @author Robin Lenz
 * @since 1.1.0
 */
public class CommandFactory {

    private static CommandProvider provider = new CommandProvider();

    /**
     * Create the Command for Commandbyte
     * 
     * @param Byte of Command
     * @return created Command
     */
    public static Command createBy(byte commandByte) {
        return provider.getCommandBy(commandByte);
    }

}
