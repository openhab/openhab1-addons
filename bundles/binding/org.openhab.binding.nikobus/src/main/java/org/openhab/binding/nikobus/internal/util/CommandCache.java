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
package org.openhab.binding.nikobus.internal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command cache. Simple utility class for persisting guessed checksum values in
 * a properties file.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class CommandCache {

	private Logger log = LoggerFactory.getLogger(CommandCache.class);

	private Properties properties;

	private String path;

	private static CommandCache cache;

	/**
	 * Get the command cache at the specified location. If it doesn't exists, a
	 * new one is created.
	 * 
	 * @param cachePath
	 * @return existing or new cache.
	 */
	public static synchronized CommandCache getCache(String cachePath) {

		if (cache == null) {
			cache = new CommandCache(cachePath);
		} else {
			// reset cache to new file
			cache = new CommandCache(cachePath);
		}

		return cache;
	}

	/**
	 * Create a command cache and initialize with the file at the given
	 * location. If no file exists, a new one will be created.
	 * 
	 * @param cachePath
	 *            absolute path to cache file.
	 */
	private CommandCache(String cachePath) {

		if (cachePath == null) {
			cachePath = System.getProperty("user.home") + "/nikobus.cache";
			log.info("No cache path specified. Defaulting to {}", cachePath);
		}

		path = cachePath;
		properties = new Properties();

		try {
			File f = new File(path);
			if (f.exists()) {
				properties.load(new FileInputStream(f));
			} else {
				f.createNewFile();
			}
		} catch (Exception e) {
			log.error(
					"Error reading/creating Nikobus cache file {}.  Commands will NOT be persisted! : {}",
					cachePath, e.getMessage());
			path = null;
		}
	}

	/**
	 * Add a new command and checksum pair to the cache. The cache will be
	 * flushed to disk immediately.
	 * 
	 * @param command
	 *            nikobus command
	 * @param checksum
	 */
	public void put(String command, String checksum) {
		if (checksum == null) {
			properties.remove(command);
		} else {
			properties.put(command, checksum);
		}
		try {
			properties.store(new FileOutputStream(new File(path)), null);
		} catch (Exception e) {
			log.error("Error persisting command cache: {}", e.getMessage());
		}

	}

	/**
	 * Get a checksum value for a given command.
	 * 
	 * @param command
	 * @return checksum value or null if not found.
	 */
	public String get(String command) {
		return properties.getProperty(command);
	}

	/**
	 * @return current cache location
	 */
	public String getPath() {
		return path;
	}

}
