/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openhab.binding.maxcube.internal.message.Battery.Charge;
import org.openhab.core.library.types.StringType;

/**
 * Tests for {@link Battery} of MAX! binding.
 * 
 * @author Dominic Lerbs
 * @since 1.7.0
 */
public class BatteryTest {

	@Test
	public void testBatteryCharge() {
		Battery battery = new Battery();
		assertEquals("Battery Charge should be unknown",
				stringTypeFor(Charge.UNKNOWN), battery.getCharge());
		battery.setCharge(Charge.LOW);
		assertEquals("Battery Charge should be low", stringTypeFor(Charge.LOW),
				battery.getCharge());
		battery.setCharge(Charge.OK);
		assertEquals("Battery Charge should be ok", stringTypeFor(Charge.OK),
				battery.getCharge());
	}

	@Test
	public void testBatteryChargeUpdated() {
		Battery battery = new Battery();
		assertFalse("Battery charge should not be set to updated intitially",
				battery.isChargeUpdated());
		battery.setCharge(Charge.LOW);
		assertTrue("Battery charge should be set to updated "
				+ "after change to low", battery.isChargeUpdated());
		battery.setCharge(Charge.LOW);
		assertFalse("Battery charge should not be set to updated "
				+ "after setting same charge again", battery.isChargeUpdated());
		battery.setCharge(Charge.OK);
		assertTrue("Battery charge should be set to updated "
				+ "after change to ok", battery.isChargeUpdated());
	}

	private StringType stringTypeFor(Charge charge) {
		return new StringType(charge.getText());
	}
}