/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import org.openhab.binding.velux.bridge.comm.GetDeviceStatus;
import org.openhab.binding.velux.things.VeluxGwState;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Get Firmware Version</B>
 * <P>
 * Common Message semantic: Communication with the bridge and (optionally) storing returned information within the class itself.
 * <P>
 * As 3rd level class it defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the interface {@link org.openhab.binding.velux.bridge.comm.slip.SlipBridgeCommunicationProtocol
 * SlipBridgeCommunicationProtocol}.
 * <P>
 * Methods in addition to the mentioned interface:
 * <UL>
 * <LI>{@link SCgetDeviceStatus#getState} to retrieve the Velux gateway status.</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class SCgetDeviceStatus extends GetDeviceStatus implements SlipBridgeCommunicationProtocol  {
	private final Logger logger = LoggerFactory.getLogger(SCgetDeviceStatus.class);

	private final static String description = "get device status";
	private final static Command command = Command.GW_GET_STATE_REQ;

	/*
	 * Message Objects
	 */

	private byte[] requestData;
	private short responseCommand;
	private byte[] responseData;


	/*
	 * ===========================================================
	 * Constructor Method
	 */

	public SCgetDeviceStatus() {
		logger.trace("SCgetDeviceStatus(constructor) called.");
		requestData = new byte[0];
	}


	/*
	 * ===========================================================
	 * Methods required for interface {@link SlipBridgeCommunicationProtocol}.
	 */

	@Override
	public String name() {
		return description;
	}

	@Override
	public CommandNumber getRequestCommand() {
		return command.getCommand();
	}

	public byte[] getRequestDataAsArrayOfBytes() {
		return requestData;
	}

	public void setResponse(short thisResponseCommand, byte[] thisResponseData){
		logger.trace("setResponseCommand({}, {}) called.", thisResponseCommand, new Packet(thisResponseData));
		responseCommand = thisResponseCommand;
		responseData = thisResponseData;
	}

	@Override
	public boolean isCommunicationFinished() {
		return true;
	}
	@Override
	public boolean isCommunicationSuccessful() {
		return (responseCommand == Command.GW_GET_STATE_CFM.getShort());
	}


	/*
	 * ===========================================================
	 * Methods in addition to interface {@link BridgeCommunicationProtocol}.
	 */

	public VeluxGwState getState() {
		byte stateValue =	responseData[0];
		byte subStateValue = responseData[1];
		VeluxGwState thisGwState = new VeluxGwState(stateValue, subStateValue);
		logger.trace("getState() returns {} ({}).", thisGwState, thisGwState.toDescription());
		return thisGwState;
	}

}
/**
 * end-of-bridge/comm/slip/SCgetDeviceStatus.java
 */