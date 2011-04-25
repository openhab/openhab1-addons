/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.io.net.actions;

import java.util.Dictionary;

import net.sourceforge.prowl.api.DefaultProwlEvent;
import net.sourceforge.prowl.api.ProwlClient;
import net.sourceforge.prowl.api.ProwlEvent;
import net.sourceforge.prowl.exception.ProwlException;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * This class provides static methods that can be used in automation rules
 * for pushin Prowl notifications. 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public class Prowl implements ManagedService {
	
	static private final Logger logger = LoggerFactory.getLogger(Prowl.class);
	
	private static boolean initialized = false;
	
	private static String url;
	private static String apiKey;
	private static int priority;
	
	
	/**
	 * Pushes a Prowl Notification
	 * 
	 * @param message the message of the notification
	 */
	static public void pushNotification(String subject, String message) {
		
		if (initialized) {
			
			ProwlClient client = new ProwlClient();
			if (Prowl.url != null && !Prowl.url.isEmpty()) {
				client.setProwlUrl(Prowl.url);
			}
			
			ProwlEvent event = new DefaultProwlEvent(
					Prowl.apiKey, "openhab", subject,
					message, Prowl.priority);
			
			try {
				String returnMessage = client.pushEvent(event);
				logger.info(returnMessage);
			}
			catch (ProwlException pe) {
				logger.error("pushing prowl event throws exception", pe);
			}
			
		} else {
			logger.error("Cannot push Prowl notification because of missing configuration settings. The current settings are: " +
					"apiKey: '{}', priority: {}, url: '{}'",
					new String[] { apiKey, String.valueOf(priority), url} );
		}
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		
		if (config!=null) {

			Prowl.apiKey = (String) config.get("apikey");
			Prowl.url = (String) config.get("url");
			
			String priorityString = (String) config.get("defaultpriority");
			if(priorityString!=null) {
				Prowl.priority = Integer.valueOf(priorityString);
			}
			
			// check mandatory settings
			if(apiKey == null || apiKey.isEmpty()) return;
			
			initialized = true;
		}
	}
	
	
	
}
