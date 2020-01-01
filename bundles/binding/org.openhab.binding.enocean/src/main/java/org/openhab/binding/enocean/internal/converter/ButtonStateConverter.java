/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
