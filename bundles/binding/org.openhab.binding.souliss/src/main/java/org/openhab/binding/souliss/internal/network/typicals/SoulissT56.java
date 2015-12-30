/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

/**
 * Typical T56 Current Sensor Derived from T51 Analog input, half-precision
 * floating point
 * 
 * @author Stephen Olesen
 * @since 1.8.0
 */
public class SoulissT56 extends SoulissT51 {

	public SoulissT56(String sSoulissNodeIPAddressOnLAN, int iIDNodo,
			int iSlot, String sOHType) {
		super(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
		this.setType(Constants.Souliss_T56_CurrentSensor);
	}
}
