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
package org.openhab.binding.homematic.internal.converter.state;

import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.core.library.types.OpenClosedType;

/**
 * Converts between openHAB OpenClosedType and Homematic values.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class OpenClosedTypeConverter extends AbstractEnumTypeConverter<OpenClosedType> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected OpenClosedType getFalseType() {
        return OpenClosedType.CLOSED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected OpenClosedType getTrueType() {
        return OpenClosedType.OPEN;
    }

    /**
     * Invert only boolean values which are not from a sensor or a state from
     * some devices.
     */
    @Override
    protected boolean isInvert(HmValueItem hmValueItem) {
        return !isName(hmValueItem, "SENSOR") && !isStateInvertDevice(hmValueItem) && hmValueItem.isBooleanValue();
    }

}
