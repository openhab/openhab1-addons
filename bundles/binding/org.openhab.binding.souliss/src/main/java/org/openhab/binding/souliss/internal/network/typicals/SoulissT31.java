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
import org.openhab.core.types.State;

/**
 * Typical T31 Thermostat
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissT31 extends SoulissGenericTypical {

	private String sItemNameSetpointValue;
	private String sItemTypeSetpointValue;
	
	private String sItemNameMeasuredValue;
	private String sItemTypeMeasuredValue;

	private short sRawCommandState;
	private Float TemperatureSetpointValue;
	private Float MeasuredValue;
	
	public SoulissT11 heatingCoolingModeValue;
	public SoulissT11 setAsMeasured;
	public SoulissT11 power;
	public SoulissT13 heating;
	public SoulissT13 cooling;
	public SoulissT11 fanHigh;
	public SoulissT11 fanMed;
	public SoulissT11 fanLow;
	public SoulissT11 fanAutoMode;
	public SoulissT11 fanOff;
	
	/**
	 * Typical T31
	 * 
	 * @param _datagramsocket
	 * @param sSoulissNodeIPAddress
	 * @param sSoulissNodeIPAddressOnLAN
	 * @param iIDNodo
	 * @param iSlot
	 * @param sOHType
	 */

	// Parameters sSoulissNode, iSlot, Type and State are stored in the class
	public SoulissT31(DatagramSocket _datagramsocket,
			String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot,
			String sOHType) {
		super();

		this.setSlot(iSlot);
		this.setSoulissNodeID(iIDNodo);
		this.setType(Constants.Souliss_T31);
		
		power= new SoulissT11(_datagramsocket, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
		heatingCoolingModeValue= new SoulissT11(_datagramsocket, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
		heating= new SoulissT13(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
		cooling= new SoulissT13(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
		fanHigh= new SoulissT11(_datagramsocket, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
		fanMed= new SoulissT11(_datagramsocket, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
		fanLow= new SoulissT11(_datagramsocket, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
		fanAutoMode= new SoulissT11(_datagramsocket, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
		fanOff= new SoulissT11(_datagramsocket, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
		setAsMeasured= new SoulissT11(_datagramsocket, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
		
	}

	/**
	 * Send a command as hexadecimal, e.g.: 
	 * Souliss_T3n_InSetPoint ----- 0x01
	 * Souliss_T3n_OutSetPoint ----- 0x02 
	 * Souliss_T3n_AsMeasured ----- 0x03
	 * Souliss_T3n_Cooling ----- 0x04
	 * Souliss_T3n_Heating ----- 0x05
	 * Souliss_T3n_FanOff ----- 0x06
	 * Souliss_T3n_FanLow ----- 0x07
	 * Souliss_T3n_FanMed ----- 0x08
	 * Souliss_T3n_FanHigh ----- 0x09
	 * Souliss_T3n_FanAuto ----- 0x0A
	 * Souliss_T3n_FanManual ----- 0x0B
	 * Souliss_T3n_SetTemp ----- 0x0C
	 * Souliss_T3n_ShutDown ----- 0x0D
	 * 
	 * @param command
	 */
	public void commandSEND(short command) {
		SoulissCommGate.sendFORCEFrame(SoulissNetworkParameter.datagramsocket,
				SoulissNetworkParameter.IPAddressOnLAN,
				this.getSoulissNodeID(), this.getSlot(), command);
	}

	/**
	 * Send a command with parameter 
	 * 
	 * @param command
	 */
	public void CommandSEND(short command, short B1, short B2) {
		SoulissCommGate.sendFORCEFrameT31SetPoint(SoulissNetworkParameter.datagramsocket,
				SoulissNetworkParameter.IPAddressOnLAN,
				this.getSoulissNodeID(), this.getSlot(), command, B1, B2);
}
//	T31 Commands
//	
//	#define Souliss_T3n_InSetPoint			0x01
//	#define Souliss_T3n_OutSetPoint			0x02
//	#define Souliss_T3n_AsMeasured			0x03
//	#define Souliss_T3n_Cooling				0x04
//	#define Souliss_T3n_Heating				0x05
//	#define Souliss_T3n_FanOff				0x06
//	#define Souliss_T3n_FanLow				0x07
//	#define Souliss_T3n_FanMed				0x08
//	#define Souliss_T3n_FanHigh				0x09
//	#define Souliss_T3n_FanAuto				0x0A
//	#define Souliss_T3n_FanManual			0x0B
//	#define Souliss_T3n_SetTemp				0x0C
//	#define Souliss_T3n_ShutDown			0x0D
	
	public State getOHStateMeasuredValue() {
		String sOHState = StateTraslator.statesSoulissToOH(this.getsItemTypeMeasuredValue(),
				this.getType(), (short) this.getState());
		if (sOHState == null) {
			if (!Float.isNaN(this.getState())) {
				if (this.getTemperatureMeasuredValue() != null)
					return DecimalType.valueOf(Float.toString(this
							.getTemperatureMeasuredValue()));
				else
					return null;
			} else
				return null;
		} else
			return DecimalType.valueOf(sOHState);
	}

	public State getOHStateSetpointValue() {
		String sOHState = StateTraslator.statesSoulissToOH(this.getsItemTypeSetpointValue(),
				this.getType(), (short) this.getState());
		if (sOHState == null) {
			if (!Float.isNaN(this.getState())) {
				if (this.getSetpointValue() != null)
					return DecimalType.valueOf(Float.toString(this
							.getSetpointValue()));
				else
					return null;
			} else
				return null;
		} else
			return DecimalType.valueOf(sOHState);
	}

	public String getsItemNameMeasuredValue() {
		return sItemNameMeasuredValue;
	}

	public void setsItemNameMeasuredValue(
			String sItemNameMeasuredValue) {
		this.sItemNameMeasuredValue = sItemNameMeasuredValue;
	}

	public String getsItemNameSetpointValue() {
		return sItemNameSetpointValue;
	}

	public void setsItemNameSetpointValue(
			String sItemNameSetpointValue) {
		this.sItemNameSetpointValue = sItemNameSetpointValue;
	}

	public Float getTemperatureMeasuredValue() {
		return MeasuredValue;
	}

	public Float getSetpointValue() {
		return TemperatureSetpointValue;
	}

	public short getRawCommandState() {
		return sRawCommandState;
	}

	public void setRawCommandState(short sRawCommandState) {
		this.sRawCommandState = sRawCommandState;
		setUpdatedTrue();
	}

	public void setMeasuredValue(Float MeasuredValue) {
		this.MeasuredValue = MeasuredValue;
		setUpdatedTrue();
	}

	public void setSetpointValue(Float temperatureSetpointValue) {
		TemperatureSetpointValue = temperatureSetpointValue;
		setUpdatedTrue();
	}

	public void setsItemTypeSetpointValue(String sNote) {
		this.sItemTypeSetpointValue=sNote;
	}

	public void setsItemTypeMeasuredValue(String sNote) {
		this.sItemTypeMeasuredValue=sNote;

	}
	public String getsItemTypeMeasuredValue() {
		return sItemTypeMeasuredValue;
	}

	public String getsItemTypeSetpointValue() {
		return sItemTypeSetpointValue;
	}
	
	@Override
	public State getOHState() {
		// TODO Auto-generated method stub
		return null;
	}
	
}