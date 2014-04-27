/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.command;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.types.State;

/**
 * Converts a Stop/Move command to a ON/ OFF type.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class StopMoveBooleanCommandConverter extends CommandConverter<OnOffType, StopMoveType> {

    @Override
    protected OnOffType convertImpl(State actualState, StopMoveType command) {
        return command.equals(StopMoveType.MOVE) ? OnOffType.ON : OnOffType.OFF;
    }

}
