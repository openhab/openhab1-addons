/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
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
public class OpenhabHSBCommand extends OpenhabCommand<HSBDataType, HSBCommand> implements OpenhabCommandTransformable<HSBType> {
    public OpenhabHSBCommand(HSBCommand command) {
        super(command);
    }

    @Override
    public HSBType getCommandValue() {
        DecimalType h = new DecimalType(data.getHue());
        PercentType s = new PercentType((int)data.getSaturation());
        PercentType b = new PercentType((int)data.getBrightness());
        return new HSBType(h, s, b);
    }
}
