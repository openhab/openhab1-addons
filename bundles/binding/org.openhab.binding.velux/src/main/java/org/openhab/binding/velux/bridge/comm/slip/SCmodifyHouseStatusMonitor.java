/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import org.openhab.binding.velux.bridge.comm.ModifyHouseStatusMonitor;
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
 * as described by the {@link org.openhab.binding.velux.bridge.comm.slip.SlipBridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class SCmodifyHouseStatusMonitor extends ModifyHouseStatusMonitor implements SlipBridgeCommunicationProtocol {
	private final Logger logger = LoggerFactory.getLogger(SCmodifyHouseStatusMonitor.class);

	private final static String description = "send Command to Actuator via SLIP";

	/* ===========================================================
	 * Message Content Parameters
	 */

	boolean activateService = false;

	/* ===========================================================
	 * Message Objects
	 */

	private byte[] requestData = new byte[0];

	/* ===========================================================
	 * Result Objects
	 */

	private boolean success = false;
	private boolean finished = false;

	/* ===========================================================
	 * Constructor Method
	 */

	public SCmodifyHouseStatusMonitor() {
		logger.debug("SCmodifyHouseStatusMonitor(Constructor) called.");
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
		Command command =  activateService ? Command.GW_HOUSE_STATUS_MONITOR_ENABLE_REQ : Command.GW_HOUSE_STATUS_MONITOR_DISABLE_REQ;
		success = false;
		finished = false;
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
		success = false;
		finished = true;
		switch (Command.get(responseCommand)) {
		case GW_HOUSE_STATUS_MONITOR_ENABLE_CFM:
			logger.error("setResponse(): service enable confirmed by bridge.");
			success = true;
			break;
		case GW_HOUSE_STATUS_MONITOR_DISABLE_CFM:
			logger.error("setResponse(): service disable confirmed by bridge.");
			success = true;
			break;

		default:
			logger.error("setResponse(): cannot handle response {} ({}).",
					Command.get(responseCommand).toString(), responseCommand);
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
	 * and the abstract class {@link ModifyHouseStatusMonitor}
	 */
	
	@Override
	public ModifyHouseStatusMonitor serviceActivation(boolean enableService) {
		this.activateService = enableService;
		return this;
	}

	
}
/**
 * end-of-bridge/comm/slip/SCmodifyHouseStatusMonitor.java
 */
