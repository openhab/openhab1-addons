package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.DecimalCommand;
import org.creek.mailcontrol.model.types.DecimalDataType;
import org.openhab.core.library.types.DecimalType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class OpenhabDecimalCommand implements OpenhabCommandTransformable {
    private final DecimalDataType data;

    public OpenhabDecimalCommand(DecimalCommand command) {
        this.data = (DecimalDataType)command.getData();
    }

    @Override
    public DecimalType getCommandValue() {
        return new DecimalType(data.toString());
    }
}
