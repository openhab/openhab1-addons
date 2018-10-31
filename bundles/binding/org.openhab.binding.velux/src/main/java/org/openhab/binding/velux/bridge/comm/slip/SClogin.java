/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import org.openhab.binding.velux.bridge.comm.Login;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Authentication</B>
 * <P>
 * Common Message semantic: Communication with the bridge and (optionally) storing returned information within the class itself.
 * <P>
 * As 3rd level class it defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the interface 
 * {@link org.openhab.binding.velux.bridge.comm.slip.SlipBridgeCommunicationProtocol SlipBridgeCommunicationProtocol}.
 * <P>
 * Methods in addition to the mentioned interface:
 * <UL>
 * <LI>{@link SClogin#setPassword} to pass the password for authentication.</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public  class SClogin extends Login implements SlipBridgeCommunicationProtocol {
	private final Logger logger = LoggerFactory.getLogger(SClogin.class);

	private final static String description = "authenticate / login";
	private final static Command command = Command.GW_PASSWORD_ENTER_REQ;

	private String password = "";

	/*
	 * Message Objects
	 */

	private byte[] passwordByteArray;
	private short responseCommand;


	/*
	 * ===========================================================
	 * Constructor Method
	 */

	public SClogin() {
		logger.trace("SClogin(constructor) called.");
		passwordByteArray = new byte[32];
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
		return passwordByteArray;
	}

	public void setResponse(short thisResponseCommand, byte[] thisResponseData){
		logger.trace("setResponseCommand({}, {}) called.", thisResponseCommand);
		responseCommand = thisResponseCommand;
	}

	@Override
	public boolean isCommunicationFinished() {
		return true;
	}
	@Override
	public boolean isCommunicationSuccessful() {
		return (responseCommand == Command.GW_PASSWORD_ENTER_CFM.getShort());
	}


	/*
	 * ===========================================================
	 * Methods in addition to interface {@link BridgeCommunicationProtocol}.
	 */

	public void setPassword(String thisPassword) {
		logger.trace("setPassword({}) called.",thisPassword);
		this.password = thisPassword;
		// Copy the password string into the data array
		byte[] password = thisPassword.getBytes();
		System.arraycopy(password, 0, passwordByteArray, 0, password.length);
		return;
	}

	public String getAuthToken() {
		logger.trace("getAuthToken() called, returning {}.",this.password);
		return this.password;
	}


}
/**
 * end-of-bridge/comm/slip/SClogin.java
 */
