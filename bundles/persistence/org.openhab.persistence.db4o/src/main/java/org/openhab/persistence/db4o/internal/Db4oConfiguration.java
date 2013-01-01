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
