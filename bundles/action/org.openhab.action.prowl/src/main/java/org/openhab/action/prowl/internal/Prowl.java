/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.action.prowl.internal;

import net.sourceforge.prowl.api.DefaultProwlEvent;
import net.sourceforge.prowl.api.ProwlClient;
import net.sourceforge.prowl.api.ProwlEvent;
import net.sourceforge.prowl.exception.ProwlException;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** 
 * This class provides static methods that can be used in automation rules
 * for pushing Prowl notifications. 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public class Prowl {

	private static final Logger logger = LoggerFactory.getLogger(Prowl.class);

	// provide public static methods here
	
	// Example
	@ActionDoc(text="A cool method that does some Prowl", 
			returns="<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean doProwl(@ParamDoc(name="something", text="the something to do") String something) {
		if (!ProwlActionService.isProperlyConfigured) {
			logger.debug("Prowl action is not yet configured - execution aborted!");
			return false;
		}
		// now do something cool
		return true;
	}
	
	static String url = null;
	static String apiKey = null;
	static int priority = 0;
	
	
	/**
	 * Pushes a Prowl notification with the configured api
	 * key and takes the default priority into account
	 * 
	 * @param subject the subject of the notification
	 * @param message the message of the notification
	 * 
	 * @return <code>true</code>, if pushing the notification has been successful
	 * and <code>false</code> in all other cases.
	 */
	@ActionDoc(text="Pushes a Prowl notification and takes the default priority into account", 
			returns="<code>true</code>, if successful and <code>false</code> otherwise.")
	static public boolean pushNotification(
			@ParamDoc(name="subject", text = "subject the subject of the notification.") String subject, 
			@ParamDoc(name="message", text = "message the message of the notification.") String message) {
		return pushNotification(Prowl.apiKey, subject, message, Prowl.priority);
	}
	
	/**
	 * Pushes a Prowl notification and takes the default priority into account
	 * 
	 * @param apiKey apiKey to use for the notification
	 * @param subject the subject of the notification
	 * @param message the message of the notification
	 * 
	 * @return <code>true</code>, if pushing the notification has been successful
	 * and <code>false</code> in all other cases.
	 */
	@ActionDoc(text="Pushes a Prowl notification and takes the default priority into account", 
			returns="<code>true</code>, if successful and <code>false</code> otherwise.")
	static public boolean pushNotification(
			@ParamDoc(name="apiKey", text = "apiKey to use for the notification.") String apiKey,
			@ParamDoc(name="subject", text = "subject the subject of the notification.") String subject, 
			@ParamDoc(name="message", text = "message the message of the notification.") String message) {
		return pushNotification(apiKey, subject, message, Prowl.priority);
	}
	
	/**
	 * Pushes a Prowl notification
	 * 
	 * @param apiKey apiKey to use for the notification
	 * @param subject the subject of the notification 
	 * @param message the message of the notification
	 * @param priority the priority of the notification (a value between
	 * '-2' and '2')
	 * 
	 * @return <code>true</code>, if pushing the notification has been successful
	 * and <code>false</code> in all other cases.
	 */
	@ActionDoc(text="Pushes a Prowl notification", 
			returns="<code>true</code>, if successful and <code>false</code> otherwise.")
	static public boolean pushNotification(
			@ParamDoc(name="apiKey", text = "apiKey to use for the notification.") String apiKey,
			@ParamDoc(name="subject", text = "the subject of the notification.") String subject, 
			@ParamDoc(name="message", text = "the message of the notification.") String message, 
			@ParamDoc(name="priority", text = "the priority of the notification (a value between '-2' and '2'.") int priority) {
		boolean success = false;
		
		int normalizedPriority = priority;
		if (priority < -2) {
			normalizedPriority = -2;
			logger.info("Prowl-Notification priority '{}' is invalid - normalized value to '{}'", priority, normalizedPriority);
		} else if (priority > 2) {
			normalizedPriority = 2;
			logger.info("Prowl-Notification priority '{}' is invalid - normalized value to '{}'", priority, normalizedPriority);
		}
		
		if (ProwlActionService.isProperlyConfigured) {
			ProwlClient client = new ProwlClient();
			if (StringUtils.isNotBlank(Prowl.url)) {
				client.setProwlUrl(Prowl.url);
			}
			
			ProwlEvent event = new DefaultProwlEvent(
					apiKey, "openhab", subject,
					message, normalizedPriority);
			
			try {
				String returnMessage = client.pushEvent(event);
				logger.info(returnMessage);
				success = true;
			}
			catch (ProwlException pe) {
				logger.error("pushing prowl event throws exception", pe);
			}
			
		} else {
			logger.error("Cannot push Prowl notification because of missing configuration settings. The current settings are: " +
					"apiKey: '{}', priority: {}, url: '{}'",
					new Object[] { apiKey, String.valueOf(normalizedPriority), url} );
		}
		
		return success;
	}
	
	
}
