package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.UpDownCommand;
import org.creek.mailcontrol.model.types.UpDownDataType;
import org.openhab.core.library.types.UpDownType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class OpenhabUpDownCommand implements OpenhabCommandTransformable {
    private final UpDownDataType data;
    
    public OpenhabUpDownCommand(UpDownCommand command) {
        this.data = (UpDownDataType)command.getData();
    }

    @Override
    public UpDownType getCommandValue() {
        return UpDownType.valueOf(data.name());
    }
}
