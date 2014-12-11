/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.openwebif.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.action.openwebif.internal.impl.OpenWebIfCommunicator;
import org.openhab.action.openwebif.internal.impl.config.OpenWebIfConfig;
import org.openhab.action.openwebif.internal.impl.model.MessageType;
import org.openhab.action.openwebif.internal.impl.model.SimpleResult;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides static methods that can be used in automation rules for
 * sending messages to enigma2 based sat receivers with installed OpenWebIf
 * plugin.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class OpenWebIf {
	private static final Logger logger = LoggerFactory.getLogger(OpenWebIf.class);
	private static OpenWebIfCommunicator communicator = new OpenWebIfCommunicator();
	private static Map<String, OpenWebIfConfig> configs = new HashMap<String, OpenWebIfConfig>();

	/**
	 * Returns all OpenWebIfConfig entries.
	 */
	public static Map<String, OpenWebIfConfig> getConfigs() {
		return configs;
	}

	/**
	 * Sends a text to a enigma2 based sat receiver with installed OpenWebIf
	 * plugin.
	 */
	@ActionDoc(text = "Sends a text to a enigma2 based sat receiver with installed OpenWebIf plugin")
	public static boolean sendOpenWebIfNotification(
			@ParamDoc(name = "name", text = "The configured name of the receiver") String name,
			@ParamDoc(name = "message", text = "The message to send to the receiver") String message,
			@ParamDoc(name = "messageType", text = "The message type (INFO, WARNING, ERROR)") String messageType,
			@ParamDoc(name = "timeout", text = "How long the text will stay on the screen in seconds") int timeout) {

		if (configs.isEmpty()) {
			throw new RuntimeException("No OpenWebIf receiver configs available, please check your openhab.cfg!");
		}
		OpenWebIfConfig config = configs.get(name);
		if (config == null) {
			throw new RuntimeException("OpenWebIf receiver config with name '" + name + "' not found!");
		}
		try {
			MessageType type = MessageType.valueOf(StringUtils.upperCase(messageType));
			if (!communicator.isOff(config)) {
				if (!communicator.isStandby(config)) {
					SimpleResult result = communicator.sendMessage(config, message, type, timeout);
					if (!result.isValid()) {
						logger.warn("Can't send message to OpenWebIf receiver with name '{}': {}", config.getName(),
								result.getStateText());
					} else {
						return true;
					}
				} else {
					logger.debug("OpenWebIf receiver with name '{}' is in standby", config.getName());
				}
			} else {
				logger.debug("OpenWebIf receiver with name '{}' is off", config.getName());
			}
			return false;
		} catch (IOException ex) {
			throw new RuntimeException("OpenWebIf error for receiver with name '" + config.getName() + "': " + ex.getMessage(), ex);
		}
	}
}
