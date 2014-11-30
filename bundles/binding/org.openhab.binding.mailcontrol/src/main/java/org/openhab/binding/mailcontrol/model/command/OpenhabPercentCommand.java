package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.PercentCommand;
import org.creek.mailcontrol.model.types.PercentDataType;
import org.openhab.core.library.types.PercentType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class OpenhabPercentCommand implements OpenhabCommandTransformable {
    private final PercentDataType data;
    
    public OpenhabPercentCommand(PercentCommand command) {
        this.data = (PercentDataType)command.getData();
    }

    @Override
    public PercentType getCommandValue() {
        return new PercentType(data.toString());
    }
}
