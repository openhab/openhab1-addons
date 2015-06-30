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
 * Typical D98 Service Typical HEALTY
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissTServiceNODE_HEALTY extends SoulissGenericTypical {

	public SoulissTServiceNODE_HEALTY(String sSoulissNodeIPAddressOnLAN,
			int iIDNodo, int iSlot, String sOHType) {
		super();
		this.setSlot(iSlot);
		this.setSoulissNodeID(iIDNodo);
		this.setType(Constants.Souliss_TService_NODE_HEALTY);
		this.setNote(sOHType);
	}

	/**
	 * Returns the souliss' typical state as numerical value
	 */
	@Override
	public State getOHState() {
		String sOHState = StateTraslator.statesSoulissToOH(this.getNote(),
				this.getType(), (short) this.getState());
		if (sOHState == null)
			return DecimalType.valueOf(Float.toString(this.getState()));
		else
			return DecimalType.valueOf(sOHState);
	}

}
