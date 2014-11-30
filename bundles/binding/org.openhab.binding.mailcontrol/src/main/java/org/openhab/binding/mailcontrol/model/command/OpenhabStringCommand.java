/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
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
