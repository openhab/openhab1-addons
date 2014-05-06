/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.serialmessage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a serial message from the zwave controller
 * This class is the base class for the serial message class. It handles
 * the request from the application, and the processing of the responses
 * from the controller.
 * When a message is sent to the controller, the controller responds with a RESPONSE.
 * When the controller has further data, it responds with a REQUEST.
 * These calls map to the handleResponse and handleRequest methods
 * which must be overridden by the individual classes.
 * @author Chris Jackson
 * @since 1.5.0
 */
public abstract class ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ZWaveCommandProcessor.class);

	private static HashMap<SerialMessage.SerialMessageClass, Class<? extends ZWaveCommandProcessor>> messageMap = null;
	protected boolean transactionComplete = false;

	public ZWaveCommandProcessor() {
	}
	
	/**
	 * Checks if the processor marked the transaction as complete
	 * @return true is the transaction was completed.
	 */
	public boolean isTransactionComplete() {
		return transactionComplete;
	}

	/**
	 * Perform a check to see if this is the expected reply
	 * and we can complete the transaction
	 * @param lastSentMessage The original message we sent to the controller
	 * @param incomingMessage The response from the controller
	 */
	protected void checkTransactionComplete(SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		if (incomingMessage.getMessageClass() == lastSentMessage.getExpectedReply() && !incomingMessage.isTransActionCanceled()) {
			transactionComplete = true;
		}
	}

	/**
	 * Method for handling the response from the controller
	 * @param zController the ZWave controller
	 * @param lastSentMessage The original message we sent to the controller
	 * @param incomingMessage The response from the controller
	 * @return
	 */
	public boolean handleResponse(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		logger.warn("TODO: {} unsupported RESPONSE.", incomingMessage.getMessageClass().getLabel());
		return false;
	}

	/**
	 * Method for handling the request from the controller
	 * @param zController the ZWave controller
	 * @param lastSentMessage The original message we sent to the controller
	 * @param incomingMessage The response from the controller
	 * @return
	 */
	public boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		logger.warn("TODO: {} unsupported REQUEST.", incomingMessage.getMessageClass().getLabel());
		return false;
	}

	/**
	 * Returns the message processor for the specified message class
	 * @param serialMessage The message class required to be processed
	 * @return The message processor
	 */
	public static ZWaveCommandProcessor getMessageDispatcher(SerialMessage.SerialMessageClass serialMessage) {
		if(messageMap == null) {
			messageMap = new HashMap<SerialMessage.SerialMessageClass, Class<? extends ZWaveCommandProcessor>>();
			messageMap.put(SerialMessage.SerialMessageClass.AddNodeToNetwork, AddNodeMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.ApplicationCommandHandler, ApplicationCommandMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.ApplicationUpdate, ApplicationUpdateMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.AssignReturnRoute, AssignReturnRouteMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.AssignSucReturnRoute, AssignSucReturnRouteMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.DeleteReturnRoute, DeleteReturnRouteMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.GetRoutingInfo, GetRoutingInfoMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.GetVersion, GetVersionMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.GetControllerCapabilities, GetControllerCapabilitiesMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.IdentifyNode, IdentifyNodeMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.MemoryGetId, MemoryGetIdMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.RemoveFailedNodeID, RemoveFailedNodeMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.RequestNodeInfo, RequestNodeInfoMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.RequestNodeNeighborUpdate, RequestNodeNeighborUpdateMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.SendData, SendDataMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.SerialApiGetCapabilities, SerialApiGetCapabilitiesMessageClass.class);
			messageMap.put(SerialMessage.SerialMessageClass.SerialApiGetInitData, SerialApiGetInitDataMessageClass.class);
		}

		Constructor<? extends ZWaveCommandProcessor> constructor;
		try {
			constructor = messageMap.get(serialMessage).getConstructor();
			return constructor.newInstance();
		} catch (NoSuchMethodException e) {
			logger.error("Command processor error: {}", e);
		} catch (InvocationTargetException e) {
			logger.error("Command processor error: {}", e);
		} catch (InstantiationException e) {
			logger.error("Command processor error: {}", e);
		} catch (IllegalAccessException e) {
			logger.error("Command processor error: {}", e);
		} catch (SecurityException e) {
			logger.error("Command processor error: {}", e);
		} catch (IllegalArgumentException e) {
			logger.error("Command processor error: {}", e);
		}
		
		return null;
	}
}
