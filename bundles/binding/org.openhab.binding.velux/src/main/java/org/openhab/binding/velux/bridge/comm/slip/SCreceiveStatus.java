/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol;
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
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class SCreceiveStatus implements BridgeCommunicationProtocol,SlipBridgeCommunicationProtocol  {
	private final Logger logger = LoggerFactory.getLogger(SCreceiveStatus.class);

	private final static String description = "retrieve house status";
	private final static Command command = Command.GW_UNDEFCOMMAND;

	/* ===========================================================
	 * Message Objects
	 */

	@SuppressWarnings("unused")
	private byte[] requestData;

	/* ===========================================================
	 * Result Objects
	 */

	private boolean success = false;
	private boolean finished = false;

	private int ntfNodeID;
	private int ntfState;
	private int ntfCurrentPosition;
	private int ntfTarget;

	/*
	 * ===========================================================
	 * Constructor Method
	 */

	public SCreceiveStatus() {
		logger.trace("SCreceiveStatus(constructor) called.");
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
		return null;
	}

	public void setResponse(short responseCommand, byte[] thisResponseData){
		logger.trace("setResponse({} with {} bytes of data) called.", Command.get(responseCommand).toString(), thisResponseData.length);
		logger.trace("setResponse(): handling response {} ({}).",
				Command.get(responseCommand).toString(), new CommandNumber(responseCommand).toString());
		Packet responseData = new Packet(thisResponseData);
		success = false;
		finished = true;
		switch (Command.get(responseCommand)) {
		case GW_NODE_STATE_POSITION_CHANGED_NTF:
			if (thisResponseData.length != 20) {
				logger.error("setResponse(): malformed response packet (length is {} unequal 20).", thisResponseData.length);
				logger.debug("setResponse() data is {}.", responseData.toString());

				if (thisResponseData.length < 20) {
					break;
				}
			}
			 ntfNodeID = responseData.getOneByteValue(0);
			 ntfState = responseData.getOneByteValue(1);
			 ntfCurrentPosition = responseData.getTwoByteValue(2);
			 ntfTarget = responseData.getTwoByteValue(4);
			@SuppressWarnings("unused") int ntfFP1CurrentPosition= responseData.getTwoByteValue(6);
			@SuppressWarnings("unused") int ntfFP2CurrentPosition= responseData.getTwoByteValue(8);
			@SuppressWarnings("unused") int ntfFP3CurrentPosition= responseData.getTwoByteValue(10);
			@SuppressWarnings("unused") int ntfFP4CurrentPosition= responseData.getTwoByteValue(12);
			int ntfRemainingTime = responseData.getTwoByteValue(14);
			int ntfTimeStamp= responseData.getFourByteValue(16);
			// Extracting information items
			logger.debug("setResponse(): ntfNodeID={}.",ntfNodeID);
			logger.debug("setResponse(): ntfState={}.",ntfState);
			logger.debug("setResponse(): ntfCurrentPosition={}.",ntfCurrentPosition);
			logger.debug("setResponse(): ntfTarget={}.",ntfTarget);
			logger.debug("setResponse(): ntfRemainingTime={}.",ntfRemainingTime);
			logger.debug("setResponse(): ntfTimeStamp={}.",ntfTimeStamp);
			
			success = true;
			break;
		
			
		default:
			logger.trace("setResponse(): cannot handle response {} ({}).",
					Command.get(responseCommand).toString(), responseCommand);
		}
		logger.trace("setResponse(): finished={},success={}.", finished, success);
	}


	@Override
	public boolean isCommunicationFinished() {
		return true;
	}
	@Override
	public boolean isCommunicationSuccessful() {
		return true;
	}
	
	/*
	 * ===========================================================
	 * Methods in addition to the interface {@link BridgeCommunicationProtocol}
	 */


	/**
	 * @return the ntfNodeID
	 */
	public int getNtfNodeID() {
		return ntfNodeID;
	}


	/**
	 * @return the ntfState
	 */
	public int getNtfState() {
		return ntfState;
	}


	/**
	 * @return the ntfCurrentPosition
	 */
	public int getNtfCurrentPosition() {
		return ntfCurrentPosition;
	}


	/**
	 * @return the ntfTarget
	 */
	public int getNtfTarget() {
		return ntfTarget;
	}


}
/**
 * end-of-bridge/comm/slip/SCreceiveStatus.java
 */
