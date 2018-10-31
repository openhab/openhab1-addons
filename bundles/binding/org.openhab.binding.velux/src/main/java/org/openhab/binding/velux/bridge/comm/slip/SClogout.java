/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import org.openhab.binding.velux.bridge.comm.Logout;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>DeAuthentication</B>
 * <P>
 * Common Message semantic: Communication with the bridge and (optionally) storing returned information within the class itself.
 * <P>
 * As 3rd level class it defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the interface 
 * {@link org.openhab.binding.velux.bridge.comm.slip.SlipBridgeCommunicationProtocol SlipBridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public  class SClogout extends Logout implements SlipBridgeCommunicationProtocol {
	private final Logger logger = LoggerFactory.getLogger(SClogout.class);

	private final static String description = "deauthenticate / logout";
	private final static Command command = Command.GW_OPENHAB_CLOSE;

	private final byte[] emptyPacket = new byte[0];

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
		return emptyPacket;
	}

	public void setResponse(short thisResponseCommand, byte[] thisResponseData){
		logger.trace("setResponseCommand({}, {}) called.", thisResponseCommand);
	}

	@Override
	public boolean isCommunicationFinished() {
		return true;
	}
	@Override
	public boolean isCommunicationSuccessful() {
		return true;
	}

}
/**
 * end-of-bridge/comm/slip/SClogout.java
 */
