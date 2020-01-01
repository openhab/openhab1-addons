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
package org.openhab.binding.homematic.internal.converter;

import java.util.Comparator;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;

/**
 * Compares two States. All DecimalTypes are preferred upon other types.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class TypeComparator implements Comparator<Class<? extends State>> {

    @Override
    public int compare(Class<? extends State> o1, Class<? extends State> o2) {
        if (DecimalType.class.isAssignableFrom(o1)) {
            return DecimalType.class.isAssignableFrom(o2) ? 0 : -1;
        }

        if (DecimalType.class.isAssignableFrom(o2)) {
            return DecimalType.class.isAssignableFrom(o1) ? 0 : 1;
        }

        return 0;
    }

}
