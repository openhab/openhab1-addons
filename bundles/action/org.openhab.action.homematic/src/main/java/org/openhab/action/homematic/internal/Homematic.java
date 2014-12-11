/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.homematic.internal;

import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.client.HomematicClientException;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides static methods that can be used in automation rules for
 * sending messages to Homematic remote control displays.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class Homematic {
	private static HomematicContext context = HomematicContext.getInstance();

	private static final Logger logger = LoggerFactory.getLogger(Homematic.class);

	/**
	 * Sends a text to a Homematic remote control display.
	 */
	@ActionDoc(text = "Sends a text to a Homematic remote control display")
	public static boolean sendHomematicDisplay(
			@ParamDoc(name = "remoteControlAddress", text = "The address of the remote control") String remoteControlAddress,
			@ParamDoc(name = "text", text = "The text to send to the display") String text) {

		return sendDisplay(remoteControlAddress, text, null);
	}

	/**
	 * Sends a text to a Homematic remote control display with options.
	 */
	@ActionDoc(text = "Sends a text to a Homematic remote control display with options")
	public static boolean sendHomematicDisplay(
			@ParamDoc(name = "remoteControlAddress", text = "The address of the remote control") String remoteControlAddress,
			@ParamDoc(name = "text", text = "The text to send to the display") String text,
			@ParamDoc(name = "options", text = "The beep, backlight, unit and symbol options to send to the display") String options) {
		return sendDisplay(remoteControlAddress, text, options);
	}

	private static boolean sendDisplay(String remoteControlAddress, String text, String options) {
		if (!context.getHomematicClient().isStarted()) {
			logger.warn("The Homematic client is not started, ignoring action sendHomematicDisplay!");
			return false;
		} else {
			try {
				context.getHomematicClient().setRemoteControlDisplay(remoteControlAddress, text, options);
				return true;
			} catch (HomematicClientException ex) {
				logger.error(ex.getMessage(), ex);
				return false;
			}
		}
	}

}
