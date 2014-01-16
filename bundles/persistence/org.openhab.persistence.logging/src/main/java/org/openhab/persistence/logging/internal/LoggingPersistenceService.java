/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	private String pattern = null;
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
	public void store(Item item) {
		// use the item name as the log file name
		store(item, item.getName());
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item, String alias) {
		if (initialized) {
			FileAppender<ILoggingEvent> appender = appenders.get(alias);
			if (appender==null) {
				synchronized(appenders) {
					// do a second check in case one exists by now
					if (!appenders.containsKey(alias)) {
						appender = createNewAppender(alias);
						appenders.put(alias, appender);
					}
				}
			}
			
			ItemLoggingEvent event = new ItemLoggingEvent(item);
			appender.doAppend(event);
			logger.debug("Logged item '{}' to file '{}.log'", new String[] { item.getName(), alias });
		}
	}

	protected FileAppender<ILoggingEvent> createNewAppender(String alias) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(context);
		encoder.setPattern(pattern);
		encoder.start();

		FileAppender<ILoggingEvent> appender = new FileAppender<ILoggingEvent>();
		appender.setAppend(true);
		appender.setFile(LOG_FOLDER + File.separator + alias + LOG_FILEEXT);
		appender.setEncoder(encoder);
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
			pattern = (String) config.get("pattern");
			if (StringUtils.isBlank(pattern)) {
				pattern = DEFAULT_PATTERN;
			}
			initialized = true;
		}
	}
	
}
