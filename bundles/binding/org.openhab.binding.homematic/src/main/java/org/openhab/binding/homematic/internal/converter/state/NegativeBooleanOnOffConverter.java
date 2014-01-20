/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import org.openhab.core.library.types.OnOffType;

/**
 * Converts between a boolean flag and an OnOffType, but inverted as the {@link BooleanOnOffConverter}. TRUE <-> OFF, FALSE <-> ON.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class NegativeBooleanOnOffConverter extends StateConverter<Boolean, OnOffType> {

    @Override
    protected OnOffType convertToImpl(Boolean source) {
        return source ? OnOffType.OFF : OnOffType.ON;
    }

    @Override
    protected Boolean convertFromImpl(OnOffType source) {
        return source.equals(OnOffType.OFF) ? Boolean.TRUE : Boolean.FALSE;
    }

}
