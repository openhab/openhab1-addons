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
package org.openhab.binding.satel.command;

import java.util.BitSet;
import java.util.Timer;
import java.util.TimerTask;

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

    private static final long REFRESH_DELAY = 1000;

    private static final Timer refreshTimer = new Timer("ControlObjectCommand timer", true);

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
    public boolean handleResponse(final EventDispatcher eventDispatcher, SatelMessage response) {
        if (super.handleResponse(eventDispatcher, response)) {
            // force refresh states that might have changed
            final BitSet newStates = this.controlType.getControlledStates();
            if (newStates != null && !newStates.isEmpty()) {
                // add delay to give a chance to process sent command
                refreshTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        eventDispatcher.dispatchEvent(new NewStatesEvent(newStates));
                    }
                }, REFRESH_DELAY);
            }
            return true;
        }

        return false;
    }

}
