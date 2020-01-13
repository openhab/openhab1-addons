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

import org.opencean.core.common.values.ContactState;
import org.openhab.core.library.types.OpenClosedType;

/**
 * Converts a ContactState to an OH OpenClosedType.
 *
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 *
 */
public class ContactStateConverter extends StateConverter<ContactState, OpenClosedType> {

    @Override
    protected OpenClosedType convertToImpl(ContactState source) {
        return source.equals(ContactState.OPEN) ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
    }

    @Override
    protected ContactState convertFromImpl(OpenClosedType source) {
        return null;
    }

}
