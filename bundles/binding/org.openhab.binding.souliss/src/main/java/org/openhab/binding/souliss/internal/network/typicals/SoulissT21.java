/**
Hardware Limit Switches :
		
			Using bistable switches for identification of open and close position			
				#define Souliss_T2n_LimSwitch_Close	0x08
				#define Souliss_T2n_LimSwitch_Open	0x10

		Hardware and/or Software Command:
			
			Using a monostable wall switch (press and spring return) or a 
			software command from user interface, each press will toogle 
			the output status.			
				#define Souliss_T2n_CloseCmd_Local	0x08
				#define Souliss_T2n_OpenCmd_Local		0x10
				#define Souliss_T2n_StopCmd					0x04
				
			Following constant are defined for sketch source compatibility with versions < A6.1.1
			and their use is now deprecated.
				#define Souliss_T2n_CloseCmd		0x01
				#define Souliss_T2n_OpenCmd			0x02

			This commands are designed to be used by an application (Souliss App, OpenHAB binding, user applications)
			in order to support software "scenario". When the Open/Close command is recevived it is always excuted;
			if the motor is running opposite direction it stops for 4 cycles then it revert motion.
				#define Souliss_T2n_CloseCmd_SW			0x01
				#define Souliss_T2n_OpenCmd_SW			0x02
				#define Souliss_T2n_StopCmd					0x03
				
		Command recap, using: 
		-  0x01(hex) as command, Software CLOSE request (stop 4 cycles if opening) 
		-  0x02(hex) as command, Software OPEN request (stop 4 cycles if closing)
		-  0x04(hex) as command, STOP request
		-  0x08(hex) as command, CLOSE request (stop if opening) 
		-  0x10(hex) as command, OPEN request (stop if closing)
		
		Output status:
		- 1(hex) for CLOSE,
		- 2(hex) for OPEN,
		- 3(hex) for STOP. * Copyright (c) 2010-2015, openHAB.org and others.
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
 * Typical T22 Motorized devices with limit switches and middle position
 * floating point
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissT21 extends SoulissT22 {

	@Override
	public void commandSEND(short command) {
		super.commandSEND(command);
	}

	@Override
	public State getOHState() {
		return super.getOHState();
	}

	public SoulissT21(DatagramSocket _datagramsocket,
			String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot,
			String sOHType) {
		super(_datagramsocket, sSoulissNodeIPAddressOnLAN,  iIDNodo,  iSlot, sOHType);
		this.setType(Constants.Souliss_T21);
	}
}
