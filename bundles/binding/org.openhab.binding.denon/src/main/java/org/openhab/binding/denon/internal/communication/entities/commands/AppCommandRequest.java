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
package org.openhab.binding.denon.internal.communication.entities.commands;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper for a list of {@link CommandTx}
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name = "tx")
@XmlAccessorType(XmlAccessType.FIELD)
public class AppCommandRequest {

    @XmlElement(name = "cmd")
    private List<CommandTx> commands = new ArrayList<CommandTx>();

    public AppCommandRequest() {
    }

    public List<CommandTx> getCommands() {
        return commands;
    }

    public void setCommands(List<CommandTx> commands) {
        this.commands = commands;
    }

    public AppCommandRequest add(CommandTx command) {
        commands.add(command);
        return this;
    }

    public static AppCommandRequest of(CommandTx command) {
        AppCommandRequest tx = new AppCommandRequest();
        return tx.add(command);
    }
}
