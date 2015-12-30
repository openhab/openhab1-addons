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

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.openhab.binding.souliss.internal.network.udp.SoulissCommGate;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

/**
 * Typical T12 SWITCH WITH AUTO mode (NOT SUPPORTED BY OPENHAB)
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */

public class SoulissT12 extends SoulissGenericTypical {

	private String sItemNameSwitchValue;
	private String sItemTypeSwitchValue;
	
	private String sItemNameAutoModeValue;
	private String sItemTypeAutoModeValue;
	 
	/**
	 * Send a command as hexadecimal, e.g.: 
	 * Command recap, using: 
	 * 		-  1(hex) as command, toggle the output
	 * 		-  2(hex) as command, the output move to ON the mode is reset
	 * 		-  4(hex) as command, the output move to OFF the mode is reset
	 * 		-  8(hex) as command, the mode is set to AUTO
	 */

	
	public SoulissT12(DatagramSocket _datagramsocket,
			String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot,
			String sOHType) {
		super();
		this.setSlot(iSlot);
		this.setSoulissNodeID(iIDNodo);
		this.setType(Constants.Souliss_T12);
	}

	public void commandSEND(short command) {
		SoulissCommGate.sendFORCEFrame(SoulissNetworkParameter.datagramsocket,
				SoulissNetworkParameter.IPAddressOnLAN,
				this.getSoulissNodeID(), this.getSlot(), command);
	}
	
	/**
	 * Returns a type used by openHAB to show the actual state of the souliss' typical
	 * @return org.openhab.core.types.State
	 * Output status,
	 * 		-  0(hex) for output OFF and mode not in AUTO,
	 * 		-  1(hex) for output ON and mode not in AUTO,
	 * 		- F0(hex) for output OFF and mode in AUTO,
	 * 		- F1(hex) for output ON and mode in AUTO.
	 */
	public State getOHStateSwitch() {
		String sOHState = StateTraslator.statesSoulissToOH(this.getsItemTypeSwitchValue(),
				this.getType(), (short) this.getState());
		if (sOHState != null) {
			return OnOffType.valueOf(sOHState);
		}
		return null;
	}
	public State getOHStateAutoMode() {
		String sOHState = StateTraslator.statesSoulissToOH(this.getsItemTypeAutoModeValue() + "_" + Constants.Souliss_T12_Use_Of_Slot_AUTOMODE,
				this.getType(), (short) this.getState());
		if (sOHState != null) {
			return OnOffType.valueOf(sOHState);
		}
		return null;
	}

	public String getsItemNameSwitchValue() {
		return sItemNameSwitchValue;
	}

	public void setsItemNameSwitchValue(String sItemNameSwitchValue) {
		this.sItemNameSwitchValue = sItemNameSwitchValue;
	}

	public String getsItemTypeSwitchValue() {
		return sItemTypeSwitchValue;
	}

	public void setsItemTypeSwitchValue(String sItemTypeSwitchValue) {
		this.sItemTypeSwitchValue = sItemTypeSwitchValue;
	}

	public String getsItemNameAutoModeValue() {
		return sItemNameAutoModeValue;
	}

	public void setsItemNameAutoModeValue(String sItemNameAutoModeValue) {
		this.sItemNameAutoModeValue = sItemNameAutoModeValue;
	}

	public String getsItemTypeAutoModeValue() {
		return sItemTypeAutoModeValue;
	}

	public void setsItemTypeAutoModeValue(String sItemTypeAutoModeValue) {
		this.sItemTypeAutoModeValue = sItemTypeAutoModeValue;
	}

	@Override
	public State getOHState() {
		// TODO Auto-generated method stub
		return null;
	}

}
