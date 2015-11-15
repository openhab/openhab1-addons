/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.caldav.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Contains configuration parameters for the caldav persistence
 * 
 * @author Robert Delbrück
 * @since 1.8.0
 */
public class CaldavConfiguration implements ManagedService {
	
	private static final Logger logger = 
		LoggerFactory.getLogger(CaldavConfiguration.class);

	public static String calendarId = null;
	
	public static int duration = 5;
	
	public static boolean singleEvents = true;
	
	/**
	 * {@inheritDoc}
	 */
	public void updated(Dictionary<String, ? > config) throws ConfigurationException {
		if (config != null) {
			String calendarIdString = (String) config.get("calendarId");
			if (StringUtils.isNotBlank(calendarIdString)) {
				calendarId = calendarIdString;
			}
			
			String durationString = (String) config.get("duration");
			if (StringUtils.isNotBlank(durationString)) {
				try {
					duration = Integer.parseInt(durationString);
				} catch (NumberFormatException e) {
					logger.error("cannot convert to int: {}", durationString);
				}
			}
			
			String singleEventsString = (String) config.get("singleEvents");
			if (StringUtils.isNotBlank(singleEventsString)) {
				singleEvents = Boolean.parseBoolean(singleEventsString);
			}
		}
	}
	
	
}
