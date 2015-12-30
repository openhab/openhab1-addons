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

import org.openhab.core.types.State;

/**
 * Typical T12 SWITCH WITH AUTO mode (NOT SUPPORTED BY OPENHAB)
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissT18 extends SoulissT11 {

	@Override
	public void commandSEND(short command) {
		// TODO Auto-generated method stub
		super.commandSEND(command);
	}

	@Override
	public State getOHState() {
		// TODO Auto-generated method stub
		return super.getOHState();
	}

	public SoulissT18(DatagramSocket _datagramsocket,
			String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot,
			String sOHType) {
		super(_datagramsocket, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot,
				sOHType);
		this.setType(Constants.Souliss_T18);
	}
}
