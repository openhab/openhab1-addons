/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
