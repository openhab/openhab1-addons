/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import java.util.Random;

import org.openhab.binding.velux.bridge.comm.SendCommand;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of product configurations.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.json.JsonBridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class SCsendCommand extends SendCommand implements SlipBridgeCommunicationProtocol {
	private final Logger logger = LoggerFactory.getLogger(SCsendCommand.class);

	private final static String description = "send Command to Actuator via SLIP";
	private final static Command command = Command.GW_COMMAND_SEND_REQ;

	/* ===========================================================
	 * Message Content Parameters
	 */

	int reqSessionID = 0;
	int reqCommandOriginator = 8;		// SAAC
	int reqPriorityLevel = 5;			// Comfort Level 2
	int reqParameterActive = 0;			// Main Parameter
	int reqFPI1 = 0;					// Functional Parameter Indicator 1 set of bits
	int reqFPI2 = 0;					// Functional Parameter Indicator 2 set of bits
	int reqMainParameter = 0;			// for FunctionalParameterValueArray
	int reqIndexArrayCount = 1;			// One node will be addressed
	int reqIndexArray01 = 1;			// This is the node
	int reqPriorityLevelLock = 0;		// Do not set a new lock on priority level 
	int reqPL_0_3 = 0;					// unused
	int reqPL_4_7 = 0;					// unused
	int reqLockTime = 0;				// 30 seconds

	/* ===========================================================
	 * Message Objects
	 */

	private byte[] requestData;

	/* ===========================================================
	 * Result Objects
	 */

	private boolean success = false;
	private boolean finished = false;

	/* ===========================================================
	 * Constructor Method
	 */

	public SCsendCommand() {
		logger.debug("SCgetProduct(Constructor) called.");
		Random rand = new Random();
		reqSessionID = rand.nextInt(0x0fff);
		logger.debug("SCgetProduct(): starting sessions with the random number {}.",reqSessionID);
	}

	/* ===========================================================
	 * Methods required for interface {@link BridgeCommunicationProtocol}.
	 */

	@Override
	public String name() {
		return description;
	}

	@Override
	public CommandNumber getRequestCommand() {
		success = false;
		finished = false;
		Packet request = new Packet(new byte[66]);	
		reqSessionID = (reqSessionID + 1) & 0xffff;
		request.setTwoByteValue(0, reqSessionID);
		request.setOneByteValue(2, reqCommandOriginator);
		request.setOneByteValue(3, reqPriorityLevel);
		request.setOneByteValue(4, reqParameterActive);
		request.setOneByteValue(5, reqFPI1);
		request.setOneByteValue(6, reqFPI2);
		request.setTwoByteValue(7, reqMainParameter);
		request.setOneByteValue(41, reqIndexArrayCount);
		request.setOneByteValue(42, reqIndexArray01);
		request.setOneByteValue(62, reqPriorityLevelLock);
		request.setOneByteValue(63, reqPL_0_3);
		request.setOneByteValue(64, reqPL_4_7);
		request.setOneByteValue(65, reqLockTime);
		requestData = request.toByteArray();
		logger.debug("getRequestCommand() returns {}.", command.getCommand());
		return command.getCommand();
	}

	@Override
	public byte[] getRequestDataAsArrayOfBytes() {
		logger.debug("getRequestDataAsArrayOfBytes() data is {}.", new Packet(requestData).toString());
		return requestData;
	}

	public void setResponse(short responseCommand, byte[] thisResponseData){
		logger.debug("setResponse({} with {} bytes of data) called.", Command.get(responseCommand).toString(), thisResponseData.length);
		logger.debug("setResponse(): handling response {} ({}).",
				Command.get(responseCommand).toString(), new CommandNumber(responseCommand).toString());
		success = false;
		finished = false;
		Packet responseData = new Packet(thisResponseData);
		switch (Command.get(responseCommand)) {
		case GW_COMMAND_SEND_CFM:
			if (thisResponseData.length != 3) {
				logger.error("setResponse(): malformed response packet (length is {} unequal three).", thisResponseData.length);
				finished = true;
				break;
			}
			int cfmSessionID = responseData.getTwoByteValue(0);
			int cfmStatus = responseData.getOneByteValue(2);
			switch (cfmStatus) {
			case 0:
				logger.info("setResponse(): returned status: Error – Command rejected.");
				finished = true;
				break;
			case 1:
				logger.debug("setResponse(): returned status: OK - Command is accepted.");
				if (cfmSessionID != reqSessionID) {
					logger.error("setResponse(): returned SessionID {} is NOT the requested SessionID {}.",cfmSessionID,reqSessionID);
					finished = true;
				}
				break;
			default:
				logger.error("setResponse(): returned status={} (not defined).",cfmStatus);
				finished = true;
				break;
			}
			break;
		case GW_COMMAND_RUN_STATUS_NTF:
			if (responseData.length() != 13) {
				logger.error("setResponse(): malformed response packet (length is {} unequal 13).", responseData.length());
				finished = true;
				break;
			}
			int ntfSessionID = responseData.getTwoByteValue(0);
			int ntfStatusiD = responseData.getOneByteValue(2);
			int ntfIndex = responseData.getOneByteValue(3);
			int ntfNodeParameter = responseData.getOneByteValue(4);
			int ntfParameterValue= responseData.getTwoByteValue(5);
			int ntfRunStatus = responseData.getOneByteValue(7);
			int ntfStatusReply= responseData.getOneByteValue(8);
			int ntfInformationCode = responseData.getFourByteValue(9);
			// Extracting information items
			logger.debug("setResponse(): ntfSessionID={} (requested {}).",ntfSessionID,reqSessionID);
			logger.debug("setResponse(): ntfStatusiD={}.",ntfStatusiD);
			logger.debug("setResponse(): ntfIndex={}.",ntfIndex);
			logger.debug("setResponse(): ntfNodeParameter={}.",ntfNodeParameter);
			logger.debug("setResponse(): ntfParameterValue={}.",ntfParameterValue);
			logger.debug("setResponse(): ntfRunStatus={}.",ntfRunStatus);
			logger.debug("setResponse(): ntfStatusReply={}.",ntfStatusReply);
			logger.debug("setResponse(): ntfInformationCode={}.",ntfInformationCode);

			if (ntfSessionID != reqSessionID) {
				logger.error("setResponse(): returned SessionID {} is NOT the requested SessionID {}.",ntfSessionID,reqSessionID);
				finished = true;
			}
			switch (ntfRunStatus) {
			case 0:
				logger.debug("setResponse(): returned ntfRunStatus: EXECUTION_COMPLETED.");
				success = true;
				break;
			case 1:
				logger.info("setResponse(): returned ntfRunStatus: EXECUTION_FAILED.");
				finished = true;
				break;
			case 2:
				logger.debug("setResponse(): returned ntfRunStatus: EXECUTION_ACTIVE.");
				break;
			default:
				logger.error("setResponse(): returned ntfRunStatus={} (not defined).",ntfRunStatus);
				finished = true;
				break;
			}
			break;
			
		case GW_COMMAND_REMAINING_TIME_NTF:
			if (responseData.length() != 6) {
				logger.error("setResponse(): malformed response packet (length is {} unequal 6).", responseData.length());
				finished = true;
				break;
			}
			int timeNtfSessionID = responseData.getTwoByteValue(0);
			int timeNtfIndex = responseData.getOneByteValue(2);
			int timeNtfNodeParameter = responseData.getOneByteValue(3);
			int timeNtfSeconds= responseData.getTwoByteValue(4);
			
			// Extracting information items
			logger.debug("setResponse(): timeNtfSessionID={} (requested {}).",timeNtfSessionID,reqSessionID);
			logger.debug("setResponse(): timeNtfIndex={}.",timeNtfIndex);
			logger.debug("setResponse(): timeNtfNodeParameter={}.",timeNtfNodeParameter);
			logger.debug("setResponse(): timeNtfSeconds={}.",timeNtfSeconds);			
			break;

		case GW_SESSION_FINISHED_NTF:
			if (responseData.length() != 2) {
				logger.error("setResponse(): malformed response packet (length is {} unequal 2).", responseData.length());
				finished = true;
				break;
			}
			int finishedNtfSessionID = responseData.getTwoByteValue(0);
	
			// Extracting information items
			logger.debug("setResponse(): finishedNtfSessionID={} (requested {}).",finishedNtfSessionID,reqSessionID);
			finished = true;
			success = true;
			break;

		default:
			logger.error("setResponse(): cannot handle response {} ({}).",
					Command.get(responseCommand).toString(), responseCommand);
			finished = true;
		}
		logger.debug("setResponse(): finished={},success={}.", finished, success);
	}



	public boolean isCommunicationFinished() {
		return finished;
	}

	@Override
	public boolean isCommunicationSuccessful() {
		return success;
	}

	/*
	 * ===========================================================
	 * Methods in addition to the interface {@link BridgeCommunicationProtocol}
	 * and the abstract class {@link GetProducts}
	 */

	public SCsendCommand setNodeId(int nodeId) {
		logger.debug("setProductId({}) called.",nodeId);
		this.reqIndexArray01 = nodeId;
		return this;
	}

	public SCsendCommand setMainParameter(int value) {
		logger.debug("setMainParameter({}) called.",value);
		this.reqMainParameter = value;
		return this;
	}

}
/**
 * end-of-bridge/comm/SCsendCommand.java
 */
