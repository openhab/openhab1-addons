/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

import java.awt.Color;
import java.net.DatagramSocket;

import org.openhab.binding.souliss.internal.network.udp.SoulissCommGate;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

/**
 * Typical T16 RGB LED Strip
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissT16 extends SoulissGenericTypical {

	int stateRED;
	int stateGREEN;
	int stateBLU;

	public int getStateRED() {
		return stateRED;
	}

	public void setStateRED(int stateRED) {
		this.stateRED = stateRED;
		setUpdatedTrue();
	}

	public int getStateGREEN() {
		return stateGREEN;
	}

	public void setStateGREEN(int stateGREEN) {
		this.stateGREEN = stateGREEN;
		setUpdatedTrue();
	}

	public int getStateBLU() {
		return stateBLU;
	}

	public void setStateBLU(int stateBLU) {
		this.stateBLU = stateBLU;
		setUpdatedTrue();
	}

	public SoulissT16(DatagramSocket _datagramsocket,
			String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot,
			String sOHType) {
		super();
		this.setSlot(iSlot);
		this.setSoulissNodeID(iIDNodo);
		this.setType(Constants.Souliss_T16);
		this.setNote(sOHType);
	}

	/**
	 * Send a command to the souliss' typical
	 * 
	 * @param command
	 */
	public void commandSEND(short command) {
		SoulissCommGate.sendFORCEFrame(SoulissNetworkParameter.datagramsocket,
				SoulissNetworkParameter.IPAddressOnLAN,
				this.getSoulissNodeID(), this.getSlot(), command);
	}

	/**
	 * Send a command with RGB color values to the souliss' typical
	 * 
	 * @param command
	 */
	public void commandSEND(short command, short R, short G, short B) {
		SoulissCommGate.sendFORCEFrame(SoulissNetworkParameter.datagramsocket,
				SoulissNetworkParameter.IPAddressOnLAN,
				this.getSoulissNodeID(), this.getSlot(), command, R, G, B);
	}

	@Override
	/**
	 * Return the actual value in percent
	 */
	public State getOHState() {
		String sOHState = StateTraslator.statesSoulissToOH(this.getNote(),
				this.getType(), (short) this.getState());
		if (sOHState != null)
			return PercentType.valueOf(sOHState);
		else
			return null;

	}

	/**
	 * Returns the values in HSB, use RGB as input
	 * 
	 * @return org.openhab.core.types.State
	 */
	public org.openhab.core.types.State getOHStateRGB() {
		Color colr = new Color(this.stateRED, this.stateGREEN, this.stateBLU);
		HSBType hsb = new HSBType(colr);
		return hsb;
	}

}
