/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.sitewhere.internal;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sitewhere.agent.BaseCommandProcessor;
import com.sitewhere.agent.ISiteWhereEventDispatcher;
import com.sitewhere.agent.SiteWhereAgentException;
import com.sitewhere.device.communication.protobuf.proto.Sitewhere.Device.Header;
import com.sitewhere.device.communication.protobuf.proto.Sitewhere.Device.RegistrationAck;
import com.sitewhere.spi.device.event.IDeviceEventOriginator;

/**
 * Handles sending openHAB data to SiteWhere and receiving commands from
 * SiteWhere.
 * 
 * @author Derek
 */
public class OpenHabCommandProcessor extends BaseCommandProcessor {

	/** Static logger instance */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OpenHabCommandProcessor.class);

	/** Event publisher for openHAB */
	private EventPublisher publisher;

	public OpenHabCommandProcessor(EventPublisher publisher) {
		this.publisher = publisher;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sitewhere.agent.BaseCommandProcessor#executeStartupLogic(java.lang
	 * .String, java.lang.String, com.sitewhere.agent.ISiteWhereEventDispatcher)
	 */
	@Override
	public void executeStartupLogic(String hardwareId,
			String specificationToken, ISiteWhereEventDispatcher dispatcher)
			throws SiteWhereAgentException {
		sendRegistration(hardwareId, specificationToken);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sitewhere.agent.BaseCommandProcessor#handleRegistrationAck(com.sitewhere
	 * .device.communication.protobuf.proto.Sitewhere.Device.Header,
	 * com.sitewhere
	 * .device.communication.protobuf.proto.Sitewhere.Device.RegistrationAck)
	 */
	@Override
	public void handleRegistrationAck(Header header, RegistrationAck ack) {
		switch (ack.getState()) {
		case ALREADY_REGISTERED: {
			LOGGER.info("SiteWhere found existing registration for openHAB device.");
			break;
		}
		case NEW_REGISTRATION: {
			LOGGER.info("SiteWhere created a new registration for openHAB device.");
			break;
		}
		case REGISTRATION_ERROR: {
			LOGGER.error("SiteWhere was unable to register openHAB device. "
					+ ack.getErrorMessage());
			break;
		}
		}
	}

	/**
	 * Called in response to SiteWhere event of matching signature.
	 * 
	 * @param itemName
	 * @param command
	 */
	public void sendOnOffCommand(String itemName, String command,
			IDeviceEventOriginator originator) {
		LOGGER.debug("Received On/Off command for: " + itemName + " ("
				+ command + ")");
		getPublisher().sendCommand(itemName, OnOffType.valueOf(command));
	}

	/**
	 * Called in response to SiteWhere event of matching signature.
	 * 
	 * @param itemName
	 * @param command
	 */
	public void sendOpenCloseCommand(String itemName, String command,
			IDeviceEventOriginator originator) {
		LOGGER.debug("Received Open/Close command for: " + itemName + " ("
				+ command + ")");
		getPublisher().sendCommand(itemName, OpenClosedType.valueOf(command));
	}

	public EventPublisher getPublisher() {
		return publisher;
	}

	public void setPublisher(EventPublisher publisher) {
		this.publisher = publisher;
	}
}