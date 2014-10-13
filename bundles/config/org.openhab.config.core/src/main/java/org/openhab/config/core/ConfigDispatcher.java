/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.config.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.config.core.internal.ConfigActivator;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a mean to read any kind of configuration data from a
 * config folder file and dispatch it to the different bundles using the
 * {@link ConfigurationAdmin} service.
 * 
 * <p>
 * The name of the configuration folder can be provided as a program argument
 * "openhab.configdir" (default is "configurations"). Configurations for OSGi services
 * are kept in a subfolder that can be provided as a program argument "openhab.servicedir"
 * (default is "services"). Any file in this folder with the extension .cfg will be
 * processed.
 * </p>
 * 
 * <p>
 * The format of the configuration file is similar to a standard property file,
 * with the exception that the property name can be prefixed by the service pid
 * of the {@link ManagedService}:
 * </p>
 * <p>
 * &lt;service-pid&gt;:&lt;property&gt;=&lt;value&gt;
 * </p>
 * <p>
 * In case the pid does not contain any ".", the default service pid namespace
 * is prefixed, which can be defined by the program argument
 * "openhab.servicepid" (default is "org.openhab").
 * </p>
 * <p>
 * If no pid is defined in the property line, the default pid namespace will be
 * used together with the filename. E.g. if you have a file "security.cfg", the
 * pid that will be used is "org.openhab.security".
 * </p>
 * <p>
 * Last but not least, a pid can be defined in the first line of a cfg file by
 * prefixing it with "pid:", e.g. "pid: org.acme.openhab.security".
 * 
 * @author Kai Kreuzer - Initial contribution and API
 * @author Thomas.Eichstaedt-Engelen - port from ESH to openHAB 1.x
 */
public class ConfigDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(ConfigDispatcher.class);

	// by default, we use the "configurations" folder in the home directory, but
	// this location might be changed in certain situations (especially when setting a config
	// folder in the openHAB Designer).
	private static String configFolder = ConfigConstants.MAIN_CONFIG_FOLDER;
	
	private static final String PID_MARKER = "pid:";

	private static WatchService watchService;
	
	
	public void activate() {
		initializeWatchService();
		readDefaultConfig();
		readMainConfig();
		readConfigs();
	}

	public void deactivate() {
		stopWatchService();
	}

	/**
	 * Returns the configuration folder path name. The main config folder
	 * <code>&lt;smarthome&gt;/configurations</code> could be overwritten by
	 * setting the System property <code>smarthome.configdir</code>.
	 * 
	 * @return the configuration folder path name
	 */
	public static String getConfigFolder() {
		String progArg = System.getProperty(ConfigConstants.CONFIG_DIR_PROG_ARGUMENT);
		if (progArg != null) {
			return progArg;
		} else {
			return configFolder;
		}
	}

	/**
	 * Sets the configuration folder to use. Calling this method will
	 * automatically trigger the loading and dispatching of the contained
	 * configuration files.
	 * 
	 * @param configFolder
	 *            the path name to the new configuration folder
	 */
	public static void setConfigFolder(String configFolder) {
		ConfigDispatcher.configFolder = configFolder;
		initializeWatchService();
	}

	private static String getDefaultServiceConfigFile() {
		String progArg = System.getProperty(ConfigConstants.SERVICECFG_PROG_ARGUMENT);
		if (progArg != null) {
			return progArg;
		} else {
			return getConfigFolder() + "/" + ConfigConstants.DEFAULT_SERVICE_CFG_FILE;
		}
	}

	private void readDefaultConfig() {
		File defaultCfg = new File(getDefaultServiceConfigFile());
		try {
			processConfigFile(defaultCfg);
		} catch (IOException e) {
			logger.warn("Could not process default config file '{}': {}", getDefaultServiceConfigFile(), e);
		}
	}

	private void readMainConfig() {
		File mainCfg = new File(getConfigFolder() + "/" + ConfigConstants.MAIN_SERVICE_CFG_FILE);
		try {
			processConfigFile(mainCfg);
		} catch (IOException e) {
			logger.info("Could not process main config file '{}'", getDefaultServiceConfigFile());
		}
	}
	
	private void readConfigs() {
		File dir = new File(getServiceConfigFolder());
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				try {
					processConfigFile(file);
				} catch (IOException e) {
					logger.warn("Could not process config file '{}': {}", file.getName(), e);
				}
			}
		} else {
			logger.debug("Configuration folder '{}' does not exist.", dir.toString());
		}
	}

	private static void initializeWatchService() {
		if (watchService != null) {
			try {
				watchService.close();
			} catch (IOException e) {
				logger.warn("Cannot deactivate folder watcher", e);
			}
		}
		Path toWatch = Paths.get(getServiceConfigFolder());
		if (toWatch.toFile().exists()) {
			try {
				watchService = toWatch.getFileSystem().newWatchService();
				WatchQueueReader reader = new WatchQueueReader(watchService, toWatch);
				Thread qr = new Thread(reader, "Dir Watcher");
				qr.start();
				toWatch.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
			} catch (IOException e) {
				logger.error("Cannot activate folder watcher for folder '{}': ", toWatch, e);
			}
		}
	}

	private void stopWatchService() {
		if (watchService != null) {
			try {
				watchService.close();
			} catch (IOException e) {
				logger.warn("Cannot deactivate folder watcher", e);
			}
			watchService = null;
		}
	}

	private static String getServiceConfigFolder() {
		String progArg = System.getProperty(ConfigConstants.SERVICEDIR_PROG_ARGUMENT);
		if (progArg != null) {
			return getConfigFolder() + "/" + progArg;
		} else {
			return getConfigFolder() + "/" + ConfigConstants.SERVICES_FOLDER;
		}
	}

	private static String getServicePidNamespace() {
		String progArg = System.getProperty(ConfigConstants.SERVICEPID_PROG_ARGUMENT);
		if (progArg != null) {
			return progArg;
		} else {
			return ConfigConstants.SERVICE_PID_NAMESPACE;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void processConfigFile(File configFile) throws IOException, FileNotFoundException {
		if (configFile.isDirectory() || !configFile.getName().endsWith(".cfg")) {
			logger.debug("Ignoring file '{}'", configFile.getName());
			return;
		}
		logger.debug("Processing config file '{}'", configFile.getName());
		ConfigurationAdmin configurationAdmin = 
			(ConfigurationAdmin) ConfigActivator.configurationAdminTracker.getService();
		if (configurationAdmin != null) {
			// we need to remember which configuration needs to be updated
			// because values have changed.
			Map<Configuration, Dictionary> configsToUpdate = new HashMap<Configuration, Dictionary>();

			// also cache the already retrieved configurations for each pid
			Map<Configuration, Dictionary> configMap = new HashMap<Configuration, Dictionary>();

			String pid;
			String filenameWithoutExt = StringUtils.substringBeforeLast(configFile.getName(), ".");
			if (filenameWithoutExt.contains(".")) {
				// it is a fully qualified namespace
				pid = filenameWithoutExt;
			} else {
				pid = getServicePidNamespace() + "." + filenameWithoutExt;
			}

			// read configured PID from the first line of the configuration file 
			List<String> lines = IOUtils.readLines(new FileInputStream(configFile));
			if (lines.size() > 0 && lines.get(0).startsWith(PID_MARKER)) {
				pid = lines.get(0).substring(PID_MARKER.length()).trim();
			}
			
			for (String line : lines) {
				String[] contents = parseLine(configFile.getPath(), line);
				// no valid configuration line, so continue
				if (contents == null) {
					continue;
				}
				
				if (contents[0] != null) {
					pid = contents[0];
					// PID is not fully qualified, so prefix with namespace 
					if (!pid.contains(".")) {
						pid = getServicePidNamespace() + "." + pid;
					}
				}
				
				String property = contents[1];
				String value = contents[2];
				Configuration configuration = configurationAdmin.getConfiguration(pid, null);
				if (configuration != null) {
					Dictionary configProperties = configMap.get(configuration);
					if (configProperties == null) {
						configProperties = new Properties();
						configMap.put(configuration, configProperties);
					}
					if (!value.equals(configProperties.get(property))) {
						configProperties.put(property, value);
						configsToUpdate.put(configuration, configProperties);
					}
				}
			}

			for (Entry<Configuration, Dictionary> entry : configsToUpdate.entrySet()) {
				entry.getKey().update(entry.getValue());
			}
		}
	}

	private static String[] parseLine(final String filePath, final String line) {
		String trimmedLine = line.trim();
		if (trimmedLine.startsWith("#") || trimmedLine.isEmpty()) {
			return null;
		}

		String pid = null; // no override of the pid
		String key = StringUtils.substringBefore(trimmedLine, "=");
		if (key.contains(":")) {
			pid = StringUtils.substringBefore(key, ":");
			trimmedLine = trimmedLine.substring(pid.length() + 1);
			pid = pid.trim();
		}
		if (!trimmedLine.isEmpty() && trimmedLine.substring(1).contains("=")) {
			String property = StringUtils.substringBefore(trimmedLine, "=");
			String value = trimmedLine.substring(property.length() + 1);
			return new String[] { pid, property.trim(), value.trim() };
		} else {
			logger.warn("Could not parse line '{}'", line);
			return null;
		}
	}

	private static class WatchQueueReader implements Runnable {

		private WatchService watchService;

		public WatchQueueReader(WatchService watchService, Path dir) {
			this.watchService = watchService;
		}

		@Override
		public void run() {
			try {
				for (;;) {
					WatchKey key = null;
					try {
						key = watchService.take();
					} catch (InterruptedException e) {
						return;
					}

					for (WatchEvent<?> event : key.pollEvents()) {
						WatchEvent.Kind<?> kind = event.kind();

						if (kind == OVERFLOW) {
							continue;
						}

						// Context for directory entry event is the file name of
						// entry
						WatchEvent<Path> ev = cast(event);
						Path name = ev.context();

						// print out event
						if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY) {
							try {
								processConfigFile(new File(getServiceConfigFolder() + File.separator + name.toString()));
							} catch (IOException e) {
								logger.warn("Could not process config file '{}': {}", name, e);
							}
						}
					}
					key.reset();
				}
			} catch (ClosedWatchServiceException ecx) {
				logger.debug("Terminated thread {}", Thread.currentThread().getName());
				return;
			}
		}

		@SuppressWarnings("unchecked")
		static <T> WatchEvent<T> cast(WatchEvent<?> event) {
			return (WatchEvent<T>) event;
		}

	}
	
}
