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
 * Converts between a boolean flag and an OnOffType.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class BooleanOnOffConverter extends StateConverter<Boolean, OnOffType> {

    @Override
    protected OnOffType convertToImpl(Boolean source) {
        return source ? OnOffType.ON : OnOffType.OFF;
    }

    @Override
    protected Boolean convertFromImpl(OnOffType source) {
        return source.equals(OnOffType.ON) ? Boolean.TRUE : Boolean.FALSE;
    }

}
