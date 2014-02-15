/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
