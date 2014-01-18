/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.converter;

import org.enocean.java.common.values.ContactState;
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
