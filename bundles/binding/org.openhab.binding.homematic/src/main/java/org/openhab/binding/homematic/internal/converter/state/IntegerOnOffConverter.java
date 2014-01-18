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
 * in a range of 0 or higher. Only a value 0 is converted to OFF. 
 * 
 * 
 */
public class IntegerOnOffConverter extends StateConverter<Integer, OnOffType> {

    @Override
    protected OnOffType convertToImpl(Integer source) {
        return (source == 0) ? OnOffType.OFF : OnOffType.ON;
    }

    @Override
    protected Integer convertFromImpl(OnOffType source) {
        return source.equals(OnOffType.OFF) ? 0 : 1;
    }

}
