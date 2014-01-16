/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import org.openhab.core.library.types.OpenClosedType;

/**
 * Converts an Double to an OpenCloseType. The given Double is considered to be
 * in a range of 0..1 (like percent). Only a value of 1.0 is converted to OPEN.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3
 * 
 */
public class InvertedDoubleOpenClosedConverter extends StateConverter<Double, OpenClosedType> {

    private static final Double MAX_VALUE = 1d;
    private static final Double MIN_VALUE = 0d;

    @Override
    protected OpenClosedType convertToImpl(Double source) {
        return (source == MAX_VALUE) ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
    }

    @Override
    protected Double convertFromImpl(OpenClosedType source) {
        return source.equals(OpenClosedType.OPEN) ? MAX_VALUE : MIN_VALUE;
    }

}
