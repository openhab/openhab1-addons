/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.serialmessage;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a serial message from the zwave controller
 * @author Chris Jackson
 * @since 1.5.0
 */
public class GetControllerCapabilitiesMessageClass extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(GetControllerCapabilitiesMessageClass.class);

	private final byte CONTROLLER_IS_SECONDARY          = 0x01;
	private final byte CONTROLLER_ON_OTHER_NETWORK      = 0x02;
	private final byte CONTROLLER_NODEID_SERVER_PRESENT = 0x04;
	private final byte CONTROLLER_IS_REAL_PRIMARY       = 0x08;
	private final byte CONTROLLER_IS_SUC                = 0x10;

	private boolean isSecondary = false;
	private boolean isOnOtherNetwork = false;
	private boolean isServerPresent = false;
	private boolean isRealPrimary = false;
	private boolean isSUC = false;

	public SerialMessage doRequest() {
		return new SerialMessage(SerialMessageClass.GetControllerCapabilities, SerialMessageType.Request, SerialMessageClass.GetControllerCapabilities, SerialMessagePriority.High);
	}
	
	@Override
	public boolean handleResponse(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		logger.trace("Handle Message Get Controller Capabilities - Length {}", incomingMessage.getMessagePayload().length);

		isSecondary = ((incomingMessage.getMessagePayloadByte(0) & CONTROLLER_IS_SECONDARY) != 0) ? true : false;
		isOnOtherNetwork = ((incomingMessage.getMessagePayloadByte(0) & CONTROLLER_ON_OTHER_NETWORK) != 0) ? true : false;
		isServerPresent = ((incomingMessage.getMessagePayloadByte(0) & CONTROLLER_NODEID_SERVER_PRESENT) == 1) ? true : false;
		isRealPrimary = ((incomingMessage.getMessagePayloadByte(0) & CONTROLLER_IS_REAL_PRIMARY) != 0) ? true : false;
		isSUC = ((incomingMessage.getMessagePayloadByte(0) & CONTROLLER_IS_SUC) != 0) ? true : false;

		logger.debug("Controller is secondary = {}", isSecondary);
		logger.debug("Controller is on other network = {}", isOnOtherNetwork);
		logger.debug("Controller is server present = {}", isServerPresent);
		logger.debug("Controller is real primary = {}", isRealPrimary);
		logger.debug("Controller is SUC = {}", isSUC);

		checkTransactionComplete(lastSentMessage, incomingMessage);
		
		return true;
	}
	
	public boolean getIsSecondary() {
		return isSecondary;
	}
	public boolean getIsOnOtherNetwork() {
		return isOnOtherNetwork;
	}
	public boolean getIsServerPresent() {
		return isServerPresent;
	}
	public boolean getIsRealPrimary() {
		return isRealPrimary;
	}
	public boolean getIsSUC() {
		return isSUC;
	}
}
