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
 * Response to an {@link AppCommandRequest}, wraps a list of {@link CommandRx}
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name = "rx")
@XmlAccessorType(XmlAccessType.FIELD)
public class AppCommandResponse {

    @XmlElement(name = "cmd")
    private List<CommandRx> commands = new ArrayList<CommandRx>();

    public AppCommandResponse() {
    }

    public List<CommandRx> getCommands() {
        return commands;
    }

    public void setCommands(List<CommandRx> commands) {
        this.commands = commands;
    }
}
