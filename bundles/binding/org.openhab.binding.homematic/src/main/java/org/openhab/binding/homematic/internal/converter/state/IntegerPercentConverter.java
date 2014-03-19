/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import org.openhab.core.library.types.PercentType;


/**
 * Converts an Integer to an {@link PercentType}. The given Integer is considered to be
 * in a range of 0..100 (like percent). The {@link PercentType} contains the same value as the integer.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * 
 */
public class IntegerPercentConverter extends StateConverter<Integer, PercentType> {

    @Override
    protected PercentType convertToImpl(Integer source) {
        return new PercentType(source);
    }

    @Override
    protected Integer convertFromImpl(PercentType source) {
        return source.intValue();
    }

}
