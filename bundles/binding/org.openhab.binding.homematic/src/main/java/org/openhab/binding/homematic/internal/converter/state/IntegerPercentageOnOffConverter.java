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
 * Converts an Integer to an OnOffValue. The given Integer is considered to be
 * in a range of 0..100 (like percent). Only a vlaue of 100 is converted to ON.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * 
 */
public class IntegerPercentageOnOffConverter extends StateConverter<Integer, OnOffType> {

    private static final int MAX_VALUE = 100;
    private static final int MIN_VALUE = 0;

    @Override
    protected OnOffType convertToImpl(Integer source) {
        return (source == MAX_VALUE) ? OnOffType.ON : OnOffType.OFF;
    }

    @Override
    protected Integer convertFromImpl(OnOffType source) {
        return source.equals(OnOffType.ON) ? MAX_VALUE : MIN_VALUE;
    }

}
