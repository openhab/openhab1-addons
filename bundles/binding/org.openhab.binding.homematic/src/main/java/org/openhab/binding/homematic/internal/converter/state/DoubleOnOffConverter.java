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
 * Converts a double value into a On/Off type. OFF = 0, ON = 1.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class DoubleOnOffConverter extends StateConverter<Double, OnOffType> {

    @Override
    protected OnOffType convertToImpl(Double source) {
        return null;
    }

    @Override
    protected Double convertFromImpl(OnOffType source) {
        return source.equals(OnOffType.OFF) ? new Double(0) : new Double(1);
    }

}
