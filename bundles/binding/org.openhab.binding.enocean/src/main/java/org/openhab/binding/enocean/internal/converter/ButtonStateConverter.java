/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.converter;

import org.opencean.core.common.values.ButtonState;
import org.openhab.core.library.types.OnOffType;

/**
 * Converts a ButtonState to an OH OnOffType.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 */
public class ButtonStateConverter extends StateConverter<ButtonState, OnOffType> {

    @Override
    protected OnOffType convertToImpl(ButtonState source) {
        return source.equals(ButtonState.PRESSED) ? OnOffType.ON : OnOffType.OFF;
    }

    @Override
    protected ButtonState convertFromImpl(OnOffType source) {
        return null;
    }

}
