/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.telegram.internal;

import java.util.Dictionary;

import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the Telegram action.
 * 
 * @author Paolo Denti
 * @since 1.8.0
 */
public class TelegramActionService implements ActionService, ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(TelegramActionService.class);

	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the action
	 * methods before executing code.
	 */
	/* default */static boolean isProperlyConfigured = false;

	public TelegramActionService() {
	}

	public void activate() {
	}

	public void deactivate() {
		// deallocate Resources here that are no longer needed and
		// should be reset when activating this binding again
	}

	@Override
	public String getActionClassName() {
		return Telegram.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Telegram.class;
	}

	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null && config.get("bots") != null) {

			String[] bots = ((String) config.get("bots")).split(",");
			for (String bot : bots) {
				String chatIdKey = String.format("%s.chatId", bot);
				String tokenKey = String.format("%s.token", bot);
				if (config.get(chatIdKey) != null
						&& config.get(tokenKey) != null) {
					Telegram.addToken(bot, (String) config.get(chatIdKey),
							(String) config.get(tokenKey));
					logger.info("Bot {} loaded from config file", bot);
				} else {
					logger.warn(
							"Bot {} is misconfigured. Please check the configuration",
							bot);
				}
			}
			isProperlyConfigured = true;
		}
	}
}
