/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import org.openhab.core.library.types.UpDownType;

/**
 * Converts UpDown Command / State to a Double value. Currently used for roller
 * shutter control.<br>
 * DOWN = 0, UP = 1
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * 
 */
public class InvertedDoubleUpDownConverter extends StateConverter<Double, UpDownType> {

    @Override
    protected UpDownType convertToImpl(Double source) {
        return null;
    }

    @Override
    protected Double convertFromImpl(UpDownType source) {
        return source.equals(UpDownType.UP) ? new Double(1) : new Double(0);
    }

}
