/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.DecimalCommand;
import org.creek.mailcontrol.model.types.DecimalDataType;
import org.openhab.core.library.types.DecimalType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class OpenhabDecimalCommand extends OpenhabCommand<DecimalDataType, DecimalCommand> implements OpenhabCommandTransformable<DecimalType> {
    public OpenhabDecimalCommand(DecimalCommand command) {
        super(command);
    }

    @Override
    public DecimalType getCommandValue() {
        return new DecimalType(data.toString());
    }
}
