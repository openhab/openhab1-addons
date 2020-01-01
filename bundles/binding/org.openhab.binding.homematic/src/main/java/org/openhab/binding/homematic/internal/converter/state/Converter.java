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
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

/**
 * Converter interface for converting between openHab states and Homematic objects.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public interface Converter<T extends State> {

    /**
     * Converts a openHAB type to a Homematic object.
     */
    public Object convertToBinding(Type type, HmValueItem hmValueItem);

    /**
     * Converts a Homematic object to a openHAB type.
     */
    public T convertFromBinding(HmValueItem hmValueItem);

}
