/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import org.openhab.binding.homematic.internal.converter.command.CommandConverter;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

public class TestOnOffConverter extends CommandConverter<PercentType, OnOffType> {

    @Override
    protected PercentType convertImpl(State actualState, OnOffType command) {
        // TODO Auto-generated method stub
        return null;
    }

}
