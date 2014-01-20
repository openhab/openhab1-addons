/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.profiles;

import org.enocean.java.common.Parameter;
import org.enocean.java.common.ParameterAddress;
import org.enocean.java.common.values.Value;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;

/**
 * Bridge class to transform normal rocket switch button behavior to a single
 * switch "profile". A press to "I" switched the switch to ON, a press to "O"
 * switches it to OFF.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 * 
 */
public class SwitchOnOffProfile extends BasicProfile {

    public SwitchOnOffProfile(Item item, EventPublisher eventPublisher) {
        super(item, eventPublisher);
    }

    @Override
    public void valueChanged(ParameterAddress parameterAddress, Value valueObject) {
        Command command = null;
        if (buttonIPressed(parameterAddress)) {
            command = OnOffType.ON;
        } else if (buttonOPressed(parameterAddress)) {
            command = OnOffType.OFF;
        }
        postCommand(command);
    }

    private boolean buttonIPressed(ParameterAddress parameterAddress) {
        return Parameter.I.name().equals(parameterAddress.getParameterId());
    }

    private boolean buttonOPressed(ParameterAddress parameterAddress) {
        return Parameter.O.name().equals(parameterAddress.getParameterId());
    }

}
