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
 * Converts an Integer to an OpenClosedValue. The given Integer is considered to
 * be 0 for Open and all others are considered as closed
 * 
 * 
 */
public class IntegerOpenClosedConverter extends StateConverter<Integer, OpenClosedType> {

    @Override
    protected OpenClosedType convertToImpl(Integer source) {
        return (source == 0) ? OpenClosedType.CLOSED : OpenClosedType.OPEN;
    }

    @Override
    protected Integer convertFromImpl(OpenClosedType source) {
        return source.equals(OpenClosedType.CLOSED) ? 0 : 1;
    }

}
