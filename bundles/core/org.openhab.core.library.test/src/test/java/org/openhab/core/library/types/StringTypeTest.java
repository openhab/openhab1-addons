/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.1
 */
public class StringTypeTest {

	@Test
	public void testEquals() {
		StringType empty     = new StringType("");
		StringType expected1 = new StringType("expected1");
		StringType expected2 = new StringType("expected2");
		
		assertEquals(empty.hashCode()    , StringType.EMPTY.hashCode());
		assertEquals(expected1.hashCode(), new StringType("expected1").hashCode());
		assertEquals(expected2.hashCode(), new StringType("expected2").hashCode());
		assertFalse(expected1.hashCode() == new StringType("expected2").hashCode());
		
		assertEquals(empty    , StringType.EMPTY);
		assertEquals(expected1, new StringType("expected1"));
		assertEquals(expected2, new StringType("expected2"));
		assertEquals(false, expected1.equals(new StringType("expected2")));
		assertEquals(false, expected2.equals(new StringType("expected1")));
		assertEquals(false, expected1.equals(StringType.EMPTY));
		assertEquals(false, expected2.equals(StringType.EMPTY));
		
		assertEquals(true, expected1.equals("expected1"));
		assertEquals(false, expected1.equals("expected2"));
	}

}
