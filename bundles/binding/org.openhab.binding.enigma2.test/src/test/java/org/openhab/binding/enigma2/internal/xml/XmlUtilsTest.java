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
package org.openhab.binding.enigma2.internal.xml;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for XMLUtils
 *
 * @author Sebastian Kutschbach
 * @since 1.6.0
 */
public class XmlUtilsTest {

    private final static String VALID_CONTENT = "<e2powerstate>" + "<e2instandby>false</e2instandby>"
            + "</e2powerstate>";

    private final static String EMPTY_CONTENT = "<e2powerstate>" + "<e2instandby></e2instandby>" + "</e2powerstate>";

    private final static String MISSING_CONTENT = "<e2powerstate></e2powerstate>";

    @Test
    public void testGetContentOfElementValidContent() {
        assertEquals("false", XmlUtils.getContentOfElement(VALID_CONTENT, "e2instandby"));
    }

    @Test
    public void testGetContentOfElementEmptyContent() {
        assertEquals("", XmlUtils.getContentOfElement(EMPTY_CONTENT, "e2instandby"));
    }

    @Test
    public void testGetContentOfElementMissingContent() {
        assertNull("false", XmlUtils.getContentOfElement(MISSING_CONTENT, "e2instandby"));
    }

}
