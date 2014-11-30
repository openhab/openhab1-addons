package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.StopMoveCommand;
import org.creek.mailcontrol.model.types.StopMoveDataType;
import org.openhab.core.library.types.StopMoveType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class OpenhabStopMoveCommand implements OpenhabCommandTransformable {
    private final StopMoveDataType data;
    
    public OpenhabStopMoveCommand(StopMoveCommand command) {
        this.data = (StopMoveDataType)command.getData();
    }

    @Override
    public StopMoveType getCommandValue() {
        return StopMoveType.valueOf(data.name());
    }
}
