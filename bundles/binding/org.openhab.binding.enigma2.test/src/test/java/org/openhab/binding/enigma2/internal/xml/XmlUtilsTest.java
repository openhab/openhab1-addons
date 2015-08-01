/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enigma2.internal.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test class for XMLUtils
 * 
 * @author Sebastian Kutschbach
 * @since 1.6.0
 */
public class XmlUtilsTest {

	private final static String VALID_CONTENT = "<e2powerstate>"
			+ "<e2instandby>false</e2instandby>" + "</e2powerstate>";

	private final static String EMPTY_CONTENT = "<e2powerstate>"
			+ "<e2instandby></e2instandby>" + "</e2powerstate>";

	private final static String MISSING_CONTENT = "<e2powerstate></e2powerstate>";

	@Test
	public void testGetContentOfElementValidContent() {
		assertEquals("false",
				XmlUtils.getContentOfElement(VALID_CONTENT, "e2instandby"));
	}

	@Test
	public void testGetContentOfElementEmptyContent() {
		assertEquals("",
				XmlUtils.getContentOfElement(EMPTY_CONTENT, "e2instandby"));
	}

	@Test
	public void testGetContentOfElementMissingContent() {
		assertNull("false",
				XmlUtils.getContentOfElement(MISSING_CONTENT, "e2instandby"));
	}

}
