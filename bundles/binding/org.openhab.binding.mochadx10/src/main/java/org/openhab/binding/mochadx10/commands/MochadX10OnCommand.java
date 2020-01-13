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
package org.openhab.binding.mochadx10.commands;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;

/**
 * Implementation of the X10 On command
 *
 * @author Jack Sleuters
 * @since 1.7.0
 *
 */
public class MochadX10OnCommand extends MochadX10Command {
    /**
     * Constructor
     * 
     * @param eventPublisher Required to post the command on the openhab bus
     * @param address The address for which this command was received
     */
    public MochadX10OnCommand(EventPublisher eventPublisher, MochadX10Address address) {
        super(eventPublisher, address);
    }

    @Override
    public void postCommand(String itemName, int currentLevel) {
        eventPublisher.postCommand(itemName, OnOffType.ON);
    }

    @Override
    public int getLevel() {
        return 100;
    }

}
