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
 * @author Antonino-Fazio
 */
public class TypicalFactory {
	private static Logger LOGGER = LoggerFactory.getLogger(TypicalFactory.class);
	
	public static SoulissGenericTypical getClass(short soulissType, DatagramSocket _datagramsocket, String sSoulissNodeIPAddress, String sSoulissNodeIPAddressOnLAN, int iIDNodo ,int iSlot, String sOHType, byte iBit) {
		// TODO Auto-generated method stub
		
		SoulissGenericTypical T=null;
		
		switch (soulissType){
		case Constants.Souliss_T11: 
			T=new SoulissT11( _datagramsocket, sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T12: 
			T=new SoulissT12( _datagramsocket, sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T13:
			T=new SoulissT13(sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T14:
			//T=new SoulissT14(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T1n_RGB:
			//T=new Souliss_T1n_RGB(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T16:
			T=new SoulissT16( _datagramsocket, sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T18:
			//T=new SoulissT18(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T19:
			T=new SoulissT19( _datagramsocket, sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T1A:
			T=new SoulissT1A(sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType, iBit);
			break;
			
		case Constants.Souliss_T21: 
			//T=new SoulissT21(sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot);
			break;
		case Constants.Souliss_T22:
			T=new SoulissT22(_datagramsocket, sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T_TemperatureSensor:
			//T=new Souliss_T_TemperatureSensor(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T_HumiditySensor:
			//T=new Souliss_T_HumiditySensor(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T32_IrCom_AirCon:
			//T=new Souliss_T32(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T41_Antitheft_Main:
			//T=new SoulissT41(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T42_Antitheft_Peer:
			//T=new SoulissT42(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T_related:
			//T=new Souliss_T_related(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T51:
			//T=new SoulissT51(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;

		case Constants.Souliss_T52_TemperatureSensor:
			T=new SoulissT52(sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T53_HumiditySensor:
			T=new SoulissT53(sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_T54_LuxSensor:
			//T=new SoulissT54(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T55_VoltageSensor:
			//T=new SoulissT44(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T56_CurrentSensor:
			//T=new SoulissT56(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T57_PowerSensor:
			T=new SoulissT57(sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_TService_NODE_HEALTY:
			T=new SoulissTServiceNODE_HEALTY(sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
			break;
		case Constants.Souliss_TService_NODE_TIMESTAMP:
			T=new SoulissTServiceNODE_TIMESTAMP(sSoulissNodeIPAddress, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
			break;
		default:
			LOGGER.debug("Typical Unknown");	
		}
		
			
		
		return T;
	}

}