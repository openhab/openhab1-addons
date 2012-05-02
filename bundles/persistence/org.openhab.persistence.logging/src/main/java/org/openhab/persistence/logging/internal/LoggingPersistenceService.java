/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.persistence.logging.internal;

import java.io.File;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;


/**
 * This is a {@link PersistenceService} implementation, which logs item states through
 * a logback file appender.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class LoggingPersistenceService implements PersistenceService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(LoggingPersistenceService.class);
	
	private static final String LOG_FOLDER = "logs";
	private static final String LOG_FILEEXT = ".log";
	
	private static final String DEFAULT_PATTERN ="%date{ISO8601} - %-25logger: %msg%n";
	
	private PatternLayoutEncoder encoder;

	private boolean initialized = false;
	
	private Map<String,FileAppender<ILoggingEvent>> appenders = new HashMap<String,FileAppender<ILoggingEvent>>();
	
	public void activate() {
	}

	public void deactivate() {
		for(FileAppender<ILoggingEvent> appender : appenders.values()) {
			appender.stop();
		}
		appenders.clear();
	}

	/**
	 * @{inheritDoc}
	 */
	public String getName() {
		return "logging";
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item, String alias) {
		if (initialized) {
			FileAppender<ILoggingEvent> appender = appenders.get(alias);
			if(appender==null) {
				synchronized(appenders) {
					// do a second check in case one exists by now
					if(!appenders.containsKey(alias)) {
						appender = createNewAppender(alias);
					}
				}
			}
			ItemLoggingEvent event = new ItemLoggingEvent(item);
			appender.doAppend(event);
			logger.debug("Logged item '{}' to file '{}.log'", new String[] { item.getName(), alias });
		}
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item) {
		// use the item name as the log file name
		store(item, item.getName());
	}

	protected FileAppender<ILoggingEvent> createNewAppender(String alias) {
		FileAppender<ILoggingEvent> appender;
		appender = new FileAppender<ILoggingEvent>();
		appender.setAppend(true);
		appender.setFile(LOG_FOLDER + File.separator + alias + LOG_FILEEXT);
		appender.setEncoder(encoder);
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		appender.setContext(context);
		appender.start();
		return appender;
	}

	/**
	 * @{inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config!=null) {
			String pattern = (String) config.get("pattern");
			if (StringUtils.isBlank(pattern)) {
				pattern = DEFAULT_PATTERN;
			}
			encoder = new PatternLayoutEncoder();
			LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
			encoder.setContext(context);
			encoder.setPattern(pattern);
			encoder.start();
			initialized = true;
		}
	}
	
}
