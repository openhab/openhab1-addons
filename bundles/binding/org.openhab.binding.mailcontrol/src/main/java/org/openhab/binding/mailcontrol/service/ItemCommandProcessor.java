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
package org.openhab.binding.mailcontrol.service;

import static org.creek.mailcontrol.model.data.DataType.*;

import org.creek.mailcontrol.model.data.CommandTransformable;
import org.creek.mailcontrol.model.data.DataType;
import org.creek.mailcontrol.model.data.DecimalData;
import org.creek.mailcontrol.model.data.HSBData;
import org.creek.mailcontrol.model.data.IncreaseDecreaseData;
import org.creek.mailcontrol.model.data.ItemCommandData;
import org.creek.mailcontrol.model.data.OnOffData;
import org.creek.mailcontrol.model.data.OpenClosedData;
import org.creek.mailcontrol.model.data.PercentData;
import org.creek.mailcontrol.model.data.StopMoveData;
import org.creek.mailcontrol.model.data.StringData;
import org.creek.mailcontrol.model.data.UpDownData;
import org.openhab.binding.mailcontrol.model.data.OpenhabCommandTransformable;
import org.openhab.binding.mailcontrol.model.data.OpenhabDecimalData;
import org.openhab.binding.mailcontrol.model.data.OpenhabHSBData;
import org.openhab.binding.mailcontrol.model.data.OpenhabIncreaseDecreaseData;
import org.openhab.binding.mailcontrol.model.data.OpenhabOnOffData;
import org.openhab.binding.mailcontrol.model.data.OpenhabOpenClosedData;
import org.openhab.binding.mailcontrol.model.data.OpenhabPercentData;
import org.openhab.binding.mailcontrol.model.data.OpenhabStopMoveData;
import org.openhab.binding.mailcontrol.model.data.OpenhabStringData;
import org.openhab.binding.mailcontrol.model.data.OpenhabUpDownData;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class ItemCommandProcessor<T extends Command> {
    private static final Logger logger = LoggerFactory.getLogger(ItemCommandProcessor.class);

    private final EventPublisher eventPublisher;

    public ItemCommandProcessor(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void processItemCommand(ItemCommandData itemData) {
        logger.debug("Processing item state: " + itemData);

        CommandTransformable command = itemData.getCommand();
        logger.debug("Command: " + command);

        OpenhabCommandTransformable<T> openhabCommand = getOpenhabCommand(command);
        logger.debug("openhabCommand: " + openhabCommand);

        T commandValue = openhabCommand.getCommandValue();
        logger.debug("commandValue: " + commandValue);

        eventPublisher.postCommand(itemData.getItemId(), openhabCommand.getCommandValue());
    }

    @SuppressWarnings("unchecked")
    private OpenhabCommandTransformable<T> getOpenhabCommand(CommandTransformable command) {
        DataType dataType = command.getCommandType();

        if (dataType == DECIMAL) {
            return (OpenhabCommandTransformable<T>) new OpenhabDecimalData((DecimalData) command);
        } else if (dataType == HSB) {
            return (OpenhabCommandTransformable<T>) new OpenhabHSBData((HSBData) command);
        } else if (dataType == INCREASE_DECREASE) {
            return (OpenhabCommandTransformable<T>) new OpenhabIncreaseDecreaseData((IncreaseDecreaseData) command);
        } else if (dataType == ON_OFF) {
            return (OpenhabCommandTransformable<T>) new OpenhabOnOffData((OnOffData) command);
        } else if (dataType == OPEN_CLOSED) {
            return (OpenhabCommandTransformable<T>) new OpenhabOpenClosedData((OpenClosedData) command);
        } else if (dataType == PERCENT) {
            return (OpenhabCommandTransformable<T>) new OpenhabPercentData((PercentData) command);
        } else if (dataType == STOP_MOVE) {
            return (OpenhabCommandTransformable<T>) new OpenhabStopMoveData((StopMoveData) command);
        } else if (dataType == STRING) {
            return (OpenhabCommandTransformable<T>) new OpenhabStringData((StringData) command);
        } else /* if (StateType == UP_DOWN) */ {
            return (OpenhabCommandTransformable<T>) new OpenhabUpDownData((UpDownData) command);
        }
    }
}
