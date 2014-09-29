/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.db4o.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Configuration class which implements {@link ManagedService} to act as a central
 * handler for configuration issues. It holds the current configuration values
 * and gives access through static member fields.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class Db4oConfiguration implements ManagedService {
	
	private static final Logger logger = 
		LoggerFactory.getLogger(Db4oConfiguration.class);

	/** the backup interval as Cron-Expression (optional, defaults to '0 0 1 * * ?' which means every morning at 1 o'clock) */
	public static String backupInterval = "0 0 1 * * ?";
	
	/** the commit interval in seconds (optional, default to '5') */
	public static int commitInterval = 5;

	/** the amount of backup files allowed in DB_FOLDER_NAME (optional, defaults to '7') */
	public static int maxBackups = 7;
	
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			String backupIntervalString = (String) config.get("backupinterval");
			if (StringUtils.isNotBlank(backupIntervalString)) {
				backupInterval = backupIntervalString;
			}
			
			String commitIntervalString = (String) config.get("commitinterval");
			if (StringUtils.isNotBlank(commitIntervalString)) {
				try {
					commitInterval = Integer.valueOf(commitIntervalString);
				}
				catch (IllegalArgumentException iae) {
					logger.warn("couldn't parse '{}' to an integer");
				}
			}

			String maxBackupsString = (String) config.get("maxbackups");
			if (StringUtils.isNotBlank(maxBackupsString)) {
				try {
					maxBackups = Integer.valueOf(maxBackupsString);
				}
				catch (IllegalArgumentException iae) {
					logger.warn("couldn't parse '{}' to an integer");
				}
			}
		}
	}
	
	
}
