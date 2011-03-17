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

package org.openhab.core.internal.logging;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * This is an adapter for forwarding OSGi Log Service entries to our logging backend (slf4j).
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class OSGILogListener {
	
	/** a map for storing the different listeners (one for each log service) */
	static private Map<LogReaderService, NLogListener> logReaderMap = new HashMap<LogReaderService, NLogListener>();
	
	public void activate() {}
	
	public void deactivate() {
		logReaderMap.clear();
	}
	
	/**
	 * This method is called by the OSGi DS whenever there is a new log reader service available.
	 * Usually there is only one, but in general there could be multiple services available.
	 * 
	 * @param aLogReaderService a new log reader service
	 */
	public void addLogReaderService(LogReaderService aLogReaderService) {
		NLogListener listener = new NLogListener();
		logReaderMap.put(aLogReaderService, listener);
		aLogReaderService.addLogListener(listener);
	}

	/**
	 * This method is called by the OSGi DS whenever a service becomes unavailable.
	 * 
	 * @param aLogReaderService the service that is no longer available
	 */
	public void removeLogReaderService(LogReaderService aLogReaderService) {
		logReaderMap.remove(aLogReaderService);
	}

	/**
	 * We use this private class as a listener for OSGi log entries. Whenever it is
	 * notified of a log entry, it forwards this entry to slf4j.
	 * 
	 * @author Kai Kreuzer
	 * @since 0.1.0
	 *
	 */
	private static class NLogListener implements LogListener {
	 	public void logged(LogEntry entry) {
	 		Logger logger = LoggerFactory.getLogger("OSGi");
	 		Marker marker = MarkerFactory.getMarker(entry.getBundle().getSymbolicName());
	 		switch(entry.getLevel()) {
	 		case LogService.LOG_DEBUG:   logger.debug(marker, entry.getMessage(), entry.getException()); break;
	 		case LogService.LOG_INFO:    logger.info(marker, entry.getMessage(), entry.getException()); break;
	 		case LogService.LOG_WARNING: logger.warn(marker, entry.getMessage(), entry.getException()); break;
	 		case LogService.LOG_ERROR:   logger.error(marker, entry.getMessage(), entry.getException()); break;
	 		}
	   	}
	}
	
}
