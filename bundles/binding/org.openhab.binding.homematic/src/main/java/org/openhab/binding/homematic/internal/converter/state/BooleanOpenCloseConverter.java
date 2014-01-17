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
 * Converts between a boolean flag and an OpenCloseType.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class BooleanOpenCloseConverter extends StateConverter<Boolean, OpenClosedType> {

    @Override
    protected OpenClosedType convertToImpl(Boolean source) {
        return source ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
    }

    @Override
    protected Boolean convertFromImpl(OpenClosedType source) {
        return source.equals(OpenClosedType.OPEN) ? Boolean.TRUE : Boolean.FALSE;
    }

}
