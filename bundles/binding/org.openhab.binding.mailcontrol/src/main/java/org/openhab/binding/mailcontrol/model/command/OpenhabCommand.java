/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.AbstractCommand;
import org.creek.mailcontrol.model.types.GenericDataType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public abstract class OpenhabCommand<T extends GenericDataType, U extends AbstractCommand<T>> {
    protected final T data;

    protected OpenhabCommand(U command) {
        this.data = command.getData();
    }
}
