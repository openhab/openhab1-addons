/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import org.junit.Test;
import static junit.framework.Assert.*;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.1.0
 */
public class DecimalTypeTest {

	@Test
	public void testEquals() {
		DecimalType dt1 = new DecimalType("142.8");
		DecimalType dt2 = new DecimalType("142.8");
		DecimalType dt3 = new DecimalType("99.7");
		PercentType pt = new PercentType("99.7");
		
		assertEquals(true, dt1.equals(dt2));
		assertEquals(false, dt1.equals(dt3));
		assertEquals(true, dt3.equals(pt));
		assertEquals(false, dt1.equals(pt));
	}
	
}
