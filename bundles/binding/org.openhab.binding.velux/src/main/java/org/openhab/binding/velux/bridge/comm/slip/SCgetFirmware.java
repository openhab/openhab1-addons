/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import org.openhab.binding.velux.bridge.comm.GetFirmware;
import org.openhab.binding.velux.things.VeluxGwFirmware;
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
 * as described by the interface {@link org.openhab.binding.velux.bridge.comm.json.JsonBridgeCommunicationProtocol
 * SlipBridgeCommunicationProtocol}.
 * <P>
 * Methods in addition to the mentioned interface:
 * <UL>
 * <LI>{@link SCgetFirmware#getFirmware} to retrieve the Velux firmware version.</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class SCgetFirmware extends GetFirmware implements SlipBridgeCommunicationProtocol  {
	private final Logger logger = LoggerFactory.getLogger(SCgetFirmware.class);

	private final static String description = "retrieve firmware version";
	private final static Command command = Command.GW_GET_VERSION_REQ;

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

	public SCgetFirmware() {
		logger.trace("SCgetFirmware(constructor) called.");
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
		return (responseCommand == Command.GW_GET_VERSION_CFM.getShort());
	}

	/*
	 * ===========================================================
	 * Methods in addition to interface {@link BridgeCommunicationProtocol}.
	 */

	public VeluxGwFirmware getFirmware() {
		String result = String.format("Software version %d.%d.%d.%d.%d.%d, Hardware version %d.%d.%d",
				responseData[0],
				responseData[1],
				responseData[2],
				responseData[3],
				responseData[4],
				responseData[5],
				responseData[6],
				responseData[7],
				responseData[8]
						);
		logger.trace("getFirmware() returns {}.", result);
		return new VeluxGwFirmware(result);
	}

}
/**
 * end-of-bridge/comm/slip/SCgetFirmware.java
 */
