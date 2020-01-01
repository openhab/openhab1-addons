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
package org.openhab.binding.em.internal;

import org.junit.Test;
import org.openhab.binding.em.internal.EMBindingConfig.EMType;

import junit.framework.Assert;

/**
 * Class to test the parsing of the received binary messages
 *
 * @author Till Klocke
 * @since 1.4.0
 *
 */
public class ParsingUtilsTest {

    private static String[] EM_DATA = { "E0205BFCB0402000300F7", "E0205C0CD0402000300FA", "E0205C4D5040200030004" };

    @Test
    public void testParsingEMData() throws Exception {

        String address = ParsingUtils.parseAddress(EM_DATA[0]);
        Assert.assertEquals("05", address);

        EMType type = ParsingUtils.parseType(EM_DATA[0]);
        Assert.assertEquals(EMType.EM100EM, type);

        int cumulatedValue = ParsingUtils.parseCumulatedValue(EM_DATA[0]);
        Assert.assertEquals(1227, cumulatedValue);

        int oldCumulatedValue = 0;
        for (String data : EM_DATA) {
            int cumValue = ParsingUtils.parseCumulatedValue(data);
            Assert.assertTrue(cumValue > oldCumulatedValue);
            oldCumulatedValue = cumValue;
        }
    }

}
