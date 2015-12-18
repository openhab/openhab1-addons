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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class return istance of Typical
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class TypicalFactory {
	private static Logger logger = LoggerFactory
			.getLogger(TypicalFactory.class);

	public static SoulissGenericTypical getClass(short soulissType,
			DatagramSocket _datagramsocket, String sSoulissNodeIPAddressOnLAN,
			int iIDNodo, int iSlot, String sOHType, byte iBit, String sUseSlot) {

		SoulissGenericTypical T = null;

		switch (soulissType) {
		case Constants.Souliss_T11:
			T = new SoulissT11(_datagramsocket, sSoulissNodeIPAddressOnLAN,
					iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T12:
			T = new SoulissT12(_datagramsocket, sSoulissNodeIPAddressOnLAN,
					iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T13:
			T = new SoulissT13(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot,
					sOHType);
			break;
		case Constants.Souliss_T14:
			T = new SoulissT14(_datagramsocket, sSoulissNodeIPAddressOnLAN,
					iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T1n_RGB:
			break;
		case Constants.Souliss_T16:
			T = new SoulissT16(_datagramsocket, sSoulissNodeIPAddressOnLAN,
					iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T18:
			T = new SoulissT18(_datagramsocket, sSoulissNodeIPAddressOnLAN,
					iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T19:
			T = new SoulissT19(_datagramsocket, sSoulissNodeIPAddressOnLAN,
					iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T1A:
			T = new SoulissT1A(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot,
					sOHType, iBit);
			break;

		case Constants.Souliss_T21:
			T = new SoulissT21(_datagramsocket, sSoulissNodeIPAddressOnLAN,
					iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T22:
			T = new SoulissT22(_datagramsocket, sSoulissNodeIPAddressOnLAN,
					iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T31:
			T = new SoulissT31(_datagramsocket, sSoulissNodeIPAddressOnLAN,
					iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T52_TemperatureSensor:
			T = new SoulissT52(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot,
					sOHType);
			break;
		case Constants.Souliss_T53_HumiditySensor:
			T = new SoulissT53(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot,
					sOHType);
			break;
		case Constants.Souliss_T54_LuxSensor:
			T = new SoulissT54(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot,
					sOHType);
			break;
		case Constants.Souliss_T55_VoltageSensor:
			T = new SoulissT55(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot,
					sOHType);
			break;
		case Constants.Souliss_T56_CurrentSensor:
			T = new SoulissT56(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot,
					sOHType);
			break;
		case Constants.Souliss_T57_PowerSensor:
			T = new SoulissT57(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot,
					sOHType);
			break;
		case Constants.Souliss_T58_PressureSensor:
			T = new SoulissT58(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot,
					sOHType);
			break;
		case Constants.Souliss_TService_NODE_HEALTY:
			T = new SoulissTServiceNODE_HEALTY(sSoulissNodeIPAddressOnLAN,
					iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_TService_NODE_TIMESTAMP:
			T = new SoulissTServiceNODE_TIMESTAMP(sSoulissNodeIPAddressOnLAN,
					iIDNodo, iSlot, sOHType);
			break;
		default:
			logger.debug("Typical Unknown");
		}
		return T;
	}

}
