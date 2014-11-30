package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.HSBCommand;
import org.creek.mailcontrol.model.types.HSBDataType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.PercentType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class OpenhabHSBCommand implements OpenhabCommandTransformable {
    private final HSBDataType data;
    
    public OpenhabHSBCommand(HSBCommand command) {
        this.data = (HSBDataType)command.getData();
    }

    @Override
    public HSBType getCommandValue() {
        DecimalType h = new DecimalType(data.getHue());
        PercentType s = new PercentType((int)data.getSaturation());
        PercentType b = new PercentType((int)data.getBrightness());
        return new HSBType(h, s, b);
    }
}
