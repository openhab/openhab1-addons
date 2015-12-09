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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

/**
 * Typical T19 RGB LED Strip
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissT19 extends SoulissGenericTypical {
	int stateLED;

	public int getStateLED() {
		return stateLED;
	}

	public void setStateLED(int stateLED) {
		this.stateLED = stateLED;
	}

	public SoulissT19(DatagramSocket _datagramsocket,
			String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot,
			String sOHType) {
		super();
		this.setSlot(iSlot);
		this.setSoulissNodeID(iIDNodo);
		this.setType(Constants.Souliss_T19);
		this.setNote(sOHType);
	}

	/**
	 * Send a command
	 * 
	 * @param command
	 */
	public void commandSEND(short command) {
		SoulissCommGate.sendFORCEFrame(SoulissNetworkParameter.datagramsocket,
				SoulissNetworkParameter.IPAddressOnLAN,
				this.getSoulissNodeID(), this.getSlot(), command);
	}

	/**
	 * Send Command with Dimmer Value
	 * 
	 * @param command
	 */
	public void commandSEND(short command, short LDimmer) {
		SoulissCommGate.sendFORCEFrame(SoulissNetworkParameter.datagramsocket,
				SoulissNetworkParameter.IPAddressOnLAN,
				this.getSoulissNodeID(), this.getSlot(), command, LDimmer);
	}

	@Override
	/**
	 * Returns a type used in openHAB to show the actual state of the souliss' typical
	 * @return org.openhab.core.types.State
	 */
	public State getOHState() {
		String sOHState = StateTraslator.statesSoulissToOH(this.getNote(),
				this.getType(), (short) this.getState());
		if (sOHState != null) {
			return OnOffType.valueOf(sOHState);
		} else {
			//return new DecimalType(Math.round((this.getState() / 254) * 100));
			return new PercentType(Math.round((this.getState() / 254) * 100));
		}
	}
}
