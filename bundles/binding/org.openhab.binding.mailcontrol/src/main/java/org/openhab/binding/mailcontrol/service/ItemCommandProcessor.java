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
public class ItemCommandProcessor {
    private final EventPublisher eventPublisher;

    public ItemCommandProcessor(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public <T extends Command> void processItemCommand(ItemCommand itemCommand) {
        CommandTransformable command = itemCommand.getCommand();
        
        OpenhabCommandTransformable openhabCommand = getOpenhabCommand(command);
        eventPublisher.postCommand(itemCommand.getItemId(), openhabCommand.getCommandValue());
    }

    private OpenhabCommandTransformable getOpenhabCommand(CommandTransformable command) {
        CommandType commandType = command.getCommandType();
        OpenhabCommandTransformable openhabCommand;
        if (commandType == DECIMAL) {
            openhabCommand = new OpenhabDecimalCommand((DecimalCommand)command);
        } else if (commandType == HSB) {
            openhabCommand = new OpenhabHSBCommand((HSBCommand)command);
        } else if (commandType == INCREASE_DECREASE) {
            openhabCommand = new OpenhabIncreaseDecreaseCommand((IncreaseDecreaseCommand)command);
        } else if (commandType == ON_OFF) {
            openhabCommand = new OpenhabOnOffCommand((OnOffCommand)command);
        } else if (commandType == OPEN_CLOSED) {
            openhabCommand = new OpenhabOpenClosedCommand((OpenClosedCommand)command);
        } else if (commandType == PERCENT) {
            openhabCommand = new OpenhabPercentCommand((PercentCommand)command);
        } else if (commandType == STOP_MOVE) {
            openhabCommand = new OpenhabStopMoveCommand((StopMoveCommand)command);
        } else if (commandType == STRING) {
            openhabCommand = new OpenhabStringCommand((StringCommand)command);
        } else /*if (commandType == UP_DOWN)*/ {
            openhabCommand = new OpenhabUpDownCommand((UpDownCommand)command);
        }
        return openhabCommand;
    }
}
