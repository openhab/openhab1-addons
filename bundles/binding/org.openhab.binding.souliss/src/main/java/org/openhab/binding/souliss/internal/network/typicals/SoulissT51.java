/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;

/**
 * Typical T51 Analog input, half-precision floating point
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissT51 extends SoulissGenericTypical {

	public SoulissT51(String sSoulissNodeIPAddressOnLAN, int iIDNodo,
			int iSlot, String sOHType) {
		super();
		this.setSlot(iSlot);
		this.setSoulissNodeID(iIDNodo);
		this.setType(Constants.Souliss_T53_HumiditySensor);
		this.setNote(sOHType);

	}

	@Override
	public State getOHState() {
		String sOHState = StateTraslator.statesSoulissToOH(this.getNote(),
				this.getType(), (short) this.getState());
		if (sOHState == null) {
			if (!Float.isNaN(this.getState())) {
				return DecimalType.valueOf(Float.toString(this.getState()));
			} else
				return null;
		} else
			return DecimalType.valueOf(sOHState);
	}
}
