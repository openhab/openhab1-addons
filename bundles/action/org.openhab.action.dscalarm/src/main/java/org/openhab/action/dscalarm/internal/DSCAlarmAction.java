/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.dscalarm.internal;

import org.openhab.binding.dscalarm.DSCAlarmActionProvider;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * This class provides static methods, for the DSC Alarm, that can be used in automation rules.
 * 
 * @author Russell Stephens
 * @since 1.8.0
 */
public class DSCAlarmAction {
	private static Logger logger = LoggerFactory.getLogger(DSCAlarmAction.class);
	
	/**
	 * Sends a string that is equivalent to a key sequence on an existing keypad.
	 */
	@ActionDoc(text = "Sends a DSC Alarm command that required no extra data.")
	public static boolean sendDSCAlarmCommand(
			@ParamDoc(name = "command", text = "The command to be sent to the DSC Alarm.") String command ) {

		return sendDSCAlarmCommandInternal(command, "");
	}

	@ActionDoc(text = "Sends a DSC Alarm command along with required data.")
	public static boolean sendDSCAlarmCommand(
			@ParamDoc(name = "command", text = "The command to be sent to the DSC Alarm.") String command,
			@ParamDoc(name = "data", text = "The data string for the command.") String data) {
		
		return sendDSCAlarmCommandInternal(command, data);
	}

	private static DSCAlarmActionProvider getDSCAlarmActionProvider() throws Exception {
		DSCAlarmActionService actionService = DSCAlarmActionService.getDSCAlarmActionService();
		
		if (actionService == null) {
			throw new Exception(String.format("DSC Alarm Action Service is not configured!"));
		}

		DSCAlarmActionProvider actionProvider = actionService.getDSCAlarmActionProvider();
		
		if (actionProvider == null) {
			throw new Exception(String.format("DSC Alarm Action Service is not configured!"));
		}

		return actionProvider;
	}

	private static boolean sendDSCAlarmCommandInternal(String command, String data) {
		
		try {
			logger.debug("sendDSCAlarmCommandInternal(): Sending DSC Alarm command!");
			
			DSCAlarmActionProvider actionProvider = getDSCAlarmActionProvider();
			
			return actionProvider.sendDSCAlarmCommand(command, data);
			
		} catch (Exception e) {
			logger.error("sendDSCAlarmCommandInternal(): Error Sending DSC Alarm Command: {}", e);
		}
		
		return false;
	}
}
	
