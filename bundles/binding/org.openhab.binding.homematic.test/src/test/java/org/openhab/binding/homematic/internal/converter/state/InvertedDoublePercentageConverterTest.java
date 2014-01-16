/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openhab.binding.homematic.internal.converter.state.InvertedDoublePercentageConverter;
import org.openhab.core.library.types.PercentType;

public class InvertedDoublePercentageConverterTest {

    private InvertedDoublePercentageConverter underTest = new InvertedDoublePercentageConverter();

    @Test
    public void testConvertTo() {
        assertEquals(new PercentType(0), underTest.convertTo(1.0));
    }

    @Test
    public void testConvertFrom() {
        assertEquals((Double) 1.0, underTest.convertFrom(new PercentType(0)));
    }

}
