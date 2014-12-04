/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.IncreaseDecreaseCommand;
import org.creek.mailcontrol.model.types.IncreaseDecreaseDataType;
import org.openhab.core.library.types.IncreaseDecreaseType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class OpenhabIncreaseDecreaseCommand extends OpenhabCommand<IncreaseDecreaseDataType, IncreaseDecreaseCommand> implements OpenhabCommandTransformable<IncreaseDecreaseType> {
    public OpenhabIncreaseDecreaseCommand(IncreaseDecreaseCommand command) {
        super(command);
    }

    @Override
    public IncreaseDecreaseType getCommandValue() {
        return IncreaseDecreaseType.valueOf(data.name());
    }
}
