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
 * Special converter for homematic brightness. Homematic values are from 0..255, converted to Percentage (0%..100%).
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class BrightnessConverter extends StateConverter<Integer, PercentType> {

    @Override
    protected PercentType convertToImpl(Integer source) {
        return new PercentType(source * 100 / 255);
    }

    @Override
    protected Integer convertFromImpl(PercentType source) {
        return source.intValue() * 255 / 100;
    }


}
