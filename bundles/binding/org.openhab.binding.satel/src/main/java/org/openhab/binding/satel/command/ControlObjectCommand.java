/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.command;

import java.util.BitSet;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.satel.internal.event.EventDispatcher;
import org.openhab.binding.satel.internal.event.NewStatesEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.openhab.binding.satel.internal.types.ControlType;

/**
 * Command class for commands that control (change) state of Integra objects,
 * like partitions (arm, disarm), zones (bypass, unbypass) outputs (on, off,
 * switch), etc.
 *
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class ControlObjectCommand extends ControlCommand {

    private ControlType controlType;

    /**
     * Creates new command class instance for specified type of control.
     * 
     * @param controlType
     *            type of controlled objects
     * @param objects
     *            bits that represents objects to control
     * @param userCode
     *            code of the user on behalf the control is made
     */
    public ControlObjectCommand(ControlType controlType, byte[] objects, String userCode) {
        super(controlType.getControlCommand(), ArrayUtils.addAll(userCodeToBytes(userCode), objects));
        this.controlType = controlType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleResponse(EventDispatcher eventDispatcher, SatelMessage response) {
        if (super.handleResponse(eventDispatcher, response)) {
            // force refresh states that might have changed
            BitSet newStates = this.controlType.getControlledStates();
            if (newStates != null && !newStates.isEmpty()) {
                eventDispatcher.dispatchEvent(new NewStatesEvent(newStates));
            }
            return true;
        }

        return false;
    }

}
