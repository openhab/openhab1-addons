/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.service;

import static org.creek.mailcontrol.model.command.CommandType.DECIMAL;
import static org.creek.mailcontrol.model.command.CommandType.HSB;
import static org.creek.mailcontrol.model.command.CommandType.INCREASE_DECREASE;
import static org.creek.mailcontrol.model.command.CommandType.ON_OFF;
import static org.creek.mailcontrol.model.command.CommandType.OPEN_CLOSED;
import static org.creek.mailcontrol.model.command.CommandType.PERCENT;
import static org.creek.mailcontrol.model.command.CommandType.STOP_MOVE;
import static org.creek.mailcontrol.model.command.CommandType.STRING;

import org.creek.mailcontrol.model.command.CommandTransformable;
import org.creek.mailcontrol.model.command.CommandType;
import org.creek.mailcontrol.model.command.DecimalCommand;
import org.creek.mailcontrol.model.command.HSBCommand;
import org.creek.mailcontrol.model.command.IncreaseDecreaseCommand;
import org.creek.mailcontrol.model.command.ItemCommand;
import org.creek.mailcontrol.model.command.OnOffCommand;
import org.creek.mailcontrol.model.command.OpenClosedCommand;
import org.creek.mailcontrol.model.command.PercentCommand;
import org.creek.mailcontrol.model.command.StopMoveCommand;
import org.creek.mailcontrol.model.command.StringCommand;
import org.creek.mailcontrol.model.command.UpDownCommand;
import org.openhab.binding.mailcontrol.model.command.OpenhabCommandTransformable;
import org.openhab.binding.mailcontrol.model.command.OpenhabDecimalCommand;
import org.openhab.binding.mailcontrol.model.command.OpenhabHSBCommand;
import org.openhab.binding.mailcontrol.model.command.OpenhabIncreaseDecreaseCommand;
import org.openhab.binding.mailcontrol.model.command.OpenhabOnOffCommand;
import org.openhab.binding.mailcontrol.model.command.OpenhabOpenClosedCommand;
import org.openhab.binding.mailcontrol.model.command.OpenhabPercentCommand;
import org.openhab.binding.mailcontrol.model.command.OpenhabStopMoveCommand;
import org.openhab.binding.mailcontrol.model.command.OpenhabStringCommand;
import org.openhab.binding.mailcontrol.model.command.OpenhabUpDownCommand;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class ItemCommandProcessor <T extends Command> {
    private final EventPublisher eventPublisher;

    public ItemCommandProcessor(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void processItemCommand(ItemCommand itemCommand) {
        CommandTransformable command = itemCommand.getCommand();
        
        OpenhabCommandTransformable<T> openhabCommand = getOpenhabCommand(command);
        eventPublisher.postCommand(itemCommand.getItemId(), openhabCommand.getCommandValue());
    }

    @SuppressWarnings("unchecked")
    private OpenhabCommandTransformable<T> getOpenhabCommand(CommandTransformable command) {
        CommandType commandType = command.getCommandType();

        if (commandType == DECIMAL) {
            return (OpenhabCommandTransformable<T>)new OpenhabDecimalCommand((DecimalCommand)command);
        } else if (commandType == HSB) {
            return (OpenhabCommandTransformable<T>)new OpenhabHSBCommand((HSBCommand)command);
        } else if (commandType == INCREASE_DECREASE) {
            return (OpenhabCommandTransformable<T>)new OpenhabIncreaseDecreaseCommand((IncreaseDecreaseCommand)command);
        } else if (commandType == ON_OFF) {
            return (OpenhabCommandTransformable<T>)new OpenhabOnOffCommand((OnOffCommand)command);
        } else if (commandType == OPEN_CLOSED) {
            return (OpenhabCommandTransformable<T>)new OpenhabOpenClosedCommand((OpenClosedCommand)command);
        } else if (commandType == PERCENT) {
            return (OpenhabCommandTransformable<T>)new OpenhabPercentCommand((PercentCommand)command);
        } else if (commandType == STOP_MOVE) {
            return (OpenhabCommandTransformable<T>)new OpenhabStopMoveCommand((StopMoveCommand)command);
        } else if (commandType == STRING) {
            return (OpenhabCommandTransformable<T>)new OpenhabStringCommand((StringCommand)command);
        } else /*if (commandType == UP_DOWN)*/ {
            return (OpenhabCommandTransformable<T>)new OpenhabUpDownCommand((UpDownCommand)command);
        }
    }
}
