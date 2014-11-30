package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.OnOffCommand;
import org.creek.mailcontrol.model.types.OnOffDataType;

import org.openhab.core.library.types.OnOffType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class OpenhabOnOffCommand implements OpenhabCommandTransformable {
    private final OnOffDataType data;
    
    public OpenhabOnOffCommand(OnOffCommand command) {
        this.data = (OnOffDataType)command.getData();
    }

    @Override
    public OnOffType getCommandValue() {
        return OnOffType.valueOf(data.name());
    }
}
