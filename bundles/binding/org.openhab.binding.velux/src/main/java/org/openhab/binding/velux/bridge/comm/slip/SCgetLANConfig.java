/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import org.openhab.binding.velux.bridge.comm.GetLANConfig;
import org.openhab.binding.velux.things.VeluxGwLAN;
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
 * <LI>{@link SCgetLANConfig#getLANConfig} to retrieve the current LAN configuration.</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class SCgetLANConfig extends GetLANConfig implements SlipBridgeCommunicationProtocol  {
	private final Logger logger = LoggerFactory.getLogger(SCgetLANConfig.class);

	private final static String description = "retrieve LAN configuration";
	private final static Command command = Command.GW_GET_NETWORK_SETUP_REQ;

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

	public SCgetLANConfig() {
		logger.trace("SCgetWLANConfig(constructor) called.");
		requestData = new byte[1];
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
		return (responseCommand == Command.GW_GET_NETWORK_SETUP_CFM.getShort());
	}

	/*
	 * ===========================================================
	 * Methods in addition to interface {@link BridgeCommunicationProtocol}.
	 */

	public VeluxGwLAN getLANConfig() {
		logger.trace("getLANConfig() called.");
		String ipAddress = String.format("%d.%d.%d.%d",
				responseData[0],
				responseData[1],
				responseData[2],
				responseData[3]);
		String subnetMask = String.format("%d.%d.%d.%d",
				responseData[4],
				responseData[5],
				responseData[6],
				responseData[7]);
		String defaultGW = String.format("%d.%d.%d.%d",
				responseData[8],
				responseData[9],
				responseData[10],
				responseData[11]);
		boolean enabledDHCP = (responseData[11] == 0) ? false : true;
		VeluxGwLAN result =	new VeluxGwLAN( ipAddress,  subnetMask,  defaultGW,  enabledDHCP);
		logger.trace("getLANConfig() returns {}.", result);
		return result;
	}

}
/**
 * end-of-bridge/comm/slip/SCGetLANConfig.java
 */
