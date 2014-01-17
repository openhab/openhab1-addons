/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import org.openhab.core.library.types.DecimalType;

/**
 * Converts an Integer to a {@link DecimalType}.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class IntegerDecimalConverter extends StateConverter<Integer, DecimalType> {

    @Override
    protected DecimalType convertToImpl(Integer source) {
        return new DecimalType(source);
    }

    @Override
    protected Integer convertFromImpl(DecimalType source) {
        return source.intValue();
    }

}
