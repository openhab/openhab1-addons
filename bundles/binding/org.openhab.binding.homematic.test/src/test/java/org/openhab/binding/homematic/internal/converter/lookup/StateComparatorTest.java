/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.lookup;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
import org.openhab.binding.homematic.internal.converter.lookup.StateComparator;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.State;

public class StateComparatorTest {

    @Test
    public void testComparePercentageWithUpDown() {
        StateComparator comparator = new StateComparator();
        assertEquals(1, comparator.compare(PercentType.class, UpDownType.class));
    }

    @Test
    public void testSorting() {
        ArrayList<Class<? extends State>> list = new ArrayList<Class<? extends State>>();
        list.add(UpDownType.class);
        list.add(PercentType.class);
        Collections.sort(list, new StateComparator());
        assertEquals("Last", PercentType.class, list.get(list.size()-1));
    }

}
