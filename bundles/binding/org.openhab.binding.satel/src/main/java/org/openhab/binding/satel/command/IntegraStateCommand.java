/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.command;

import java.util.Arrays;

import org.openhab.binding.satel.internal.event.EventDispatcher;
import org.openhab.binding.satel.internal.event.IntegraStateEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.openhab.binding.satel.internal.types.StateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command class for commands that return state of Integra objects, like
 * partitions (armed, alarm, entry time), zones (violation, tamper, alarm),
 * outputs and doors (opened, opened long).
 *
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class IntegraStateCommand extends SatelCommandBase {

    private static final Logger logger = LoggerFactory.getLogger(IntegraStateCommand.class);

    private StateType stateType;

    /**
     * Constructs new command instance for specified type of state.
     *
     * @param stateType
     *            type of state
     * @param extended
     *            if <code>true</code> command will be sent as extended (256
     *            zones or outputs)
     */
    public IntegraStateCommand(StateType stateType, boolean extended) {
        super(stateType.getRefreshCommand(), extended);
        this.stateType = stateType;
    }

    /**
     * @return <code>true</code> if current command is extended (256
     *         zones/outputs)
     */
    public boolean isExtended() {
        return Arrays.equals(EXTENDED_CMD_PAYLOAD, this.getPayload());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleResponse(EventDispatcher eventDispatcher, SatelMessage response) {
        if (super.handleResponse(eventDispatcher, response)) {
            // dispatch event
            eventDispatcher
                    .dispatchEvent(new IntegraStateEvent(response.getCommand(), response.getPayload(), isExtended()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean isResponseValid(SatelMessage response) {
        // validate response
        if (response.getCommand() != this.stateType.getRefreshCommand()) {
            logger.error("Invalid response code: {}", response.getCommand());
            return false;
        }
        if (response.getPayload().length != this.stateType.getPayloadLength(isExtended())) {
            logger.error("Invalid payload length for this state type {}: {}", this.stateType,
                    response.getPayload().length);
            return false;
        }
        return true;
    }

}
