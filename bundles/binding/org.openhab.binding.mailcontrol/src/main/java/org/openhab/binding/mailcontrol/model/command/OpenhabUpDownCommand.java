/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
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
