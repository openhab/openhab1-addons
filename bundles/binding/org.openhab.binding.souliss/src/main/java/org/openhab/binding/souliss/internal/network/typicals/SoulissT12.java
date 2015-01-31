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

/**
 * Typical T12 SWITCH WITH AUTO mode (NOT SUPPORTED BY OPENHAB)
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissT12 extends SoulissT11 {

	public SoulissT12(DatagramSocket _datagramsocket,
			String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot,
			String sOHType) {
		super(_datagramsocket, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot,
				sOHType);
		this.setType(Constants.Souliss_T12);
	}
}
