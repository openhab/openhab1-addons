package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.IncreaseDecreaseCommand;
import org.creek.mailcontrol.model.types.IncreaseDecreaseDataType;
import org.openhab.core.library.types.IncreaseDecreaseType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class OpenhabIncreaseDecreaseCommand implements OpenhabCommandTransformable {
    private final IncreaseDecreaseDataType data;
    
    public OpenhabIncreaseDecreaseCommand(IncreaseDecreaseCommand command) {
        this.data = (IncreaseDecreaseDataType)command.getData();
    }

    @Override
    public IncreaseDecreaseType getCommandValue() {
        return IncreaseDecreaseType.valueOf(data.name());
    }
}
