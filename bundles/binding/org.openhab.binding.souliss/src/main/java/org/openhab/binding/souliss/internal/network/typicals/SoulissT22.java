/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

import java.net.DatagramSocket;

import org.openhab.binding.souliss.internal.network.udp.SoulissCommGate;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

/**
 * Typical T22 Motorized devices with limit switches and middle position
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissT22 extends SoulissGenericTypical {

	public SoulissT22(DatagramSocket _datagramsocket,
			String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot,
			String sOHType) {
		super();
		this.setSlot(iSlot);
		this.setSoulissNodeID(iIDNodo);
 		this.setType(Constants.Souliss_T22);
		this.setNote(sOHType);
	}

	/**
	 * Send a command as hexadecimal, e.g.: Souliss_T1n_OnCmd = 0x02; short
	 * Souliss_T1n_OffCmd = 0x04;
	 * 
	 * @param command
	 */
	public void commandSEND(short command) {
		SoulissCommGate.sendFORCEFrame(SoulissNetworkParameter.datagramsocket,
				SoulissNetworkParameter.IPAddressOnLAN,
				this.getSoulissNodeID(), this.getSlot(), command);
	}

	@Override
	public State getOHState() {
		String sOHState = StateTraslator.statesSoulissToOH(this.getNote(),
				this.getType(), (short) this.getState());
		if (sOHState != null)
			return new PercentType(Integer.parseInt(sOHState));
		else
			return null;

	}
}
