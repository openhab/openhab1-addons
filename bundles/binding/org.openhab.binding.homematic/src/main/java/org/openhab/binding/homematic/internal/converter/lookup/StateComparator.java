/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.lookup;

import java.util.Comparator;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;

/**
 * Compares two states. The more specific one wins :-). For now all DecimalTypes are preferred upon other types.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class StateComparator implements Comparator<Class<? extends State>> {

    @Override
    public int compare(Class<? extends State> o1, Class<? extends State> o2) {
        if(DecimalType.class.isAssignableFrom(o1)) {
            return 1;
        }
        if(DecimalType.class.isAssignableFrom(o2)) {
            return -1;
        }
        return 0;
    }

}
