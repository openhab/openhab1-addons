/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.converter;

import org.opencean.core.common.values.OnOffState;
import org.openhab.core.library.types.OnOffType;

/**
 * A converter to convert a OnOffState to a OnOffType
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 * 
 */
public class OnOffStateConverter extends StateConverter<OnOffState, OnOffType> {

    @Override
    protected OnOffType convertToImpl(OnOffState source) {
        return OnOffType.valueOf(source.name());
    }

    @Override
    protected OnOffState convertFromImpl(OnOffType source) {
        return OnOffState.valueOf(source.name());
    }

}
