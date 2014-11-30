package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.StringCommand;
import org.creek.mailcontrol.model.types.StringDataType;
import org.openhab.core.library.types.StringType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class OpenhabStringCommand implements OpenhabCommandTransformable {
    private final StringDataType data;
    
    public OpenhabStringCommand(StringCommand command) {
        this.data = (StringDataType)command.getData();
    }

    @Override
    public StringType getCommandValue() {
        return new StringType(data.toString());
    }
}
