/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
