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
package org.openhab.binding.omnilink.internal;

/**
 * This class is represents a command sent to a omni syste,
 *
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class OmniLinkControllerCommand {
    private int command;
    private int parameter1;
    private int parameter2;

    /**
     * 
     * @param command number see {@code OmniLinkCmd}
     * @param parameter1
     * @param parameter2
     */
    public OmniLinkControllerCommand(int command, int parameter1, int parameter2) {
        super();
        this.command = command;
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int getParameter1() {
        return parameter1;
    }

    public void setParameter1(int parameter1) {
        this.parameter1 = parameter1;
    }

    public int getParameter2() {
        return parameter2;
    }

    public void setParameter2(int parameter2) {
        this.parameter2 = parameter2;
    }
}
