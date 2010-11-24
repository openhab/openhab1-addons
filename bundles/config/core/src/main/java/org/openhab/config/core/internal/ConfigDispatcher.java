/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.config.core.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.config.core.ConfigConstants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a mean to read any kind of configuration data from a shared config
 * file and dispatch it to the different bundles using the {@link ConfigurationAdmin} service.
 * 
 * <p>The name of the configuration file can be provided as a program argument "openhab.configfile".
 * If this argument is not set, the default "configurations/openhab.cfg" will be used.
 * In case the configuration file does not exist, a warning will be logged and no action
 * will be performed.</p>
 * 
 * <p>The format of the configuration file is similar to a standard property file, with the
 * exception that the property name must be prefixed by the service pid of the {@link ManagedService}:</p>
 * <p>&lt;service-pid&gt;:&lt;property&gt;=&lt;value&gt;</p>
 * <p>The prefix "org.openhab" can be omitted on the service pid, it is automatically added if
 * the pid does not contain any "."</p> 
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public class ConfigDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(ConfigDispatcher.class);

	static public void initializeBundleConfigurations() {
		String defaultConfigFilePath = getDefaultConfigurationFilePath();
		File defaultConfigFile = new File(defaultConfigFilePath);
		try {
			processConfigFile(defaultConfigFile);
		} catch (FileNotFoundException e) {
			// we do not care if we do not have a default file
		} catch (IOException e) {
			logger.error("Default openHAB configuration file '{}' cannot be read.", defaultConfigFilePath, e);
		}			

		String mainConfigFilePath = getMainConfigurationFilePath();
		File mainConfigFile = new File(mainConfigFilePath);
		try {
			processConfigFile(mainConfigFile);
		} catch (FileNotFoundException e) {
			logger.warn("Main openHAB configuration file '{}' does not exist.", mainConfigFilePath);
		} catch (IOException e) {
			logger.error("Main openHAB configuration file '{}' cannot be read.", mainConfigFilePath, e);
		}			
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void processConfigFile(File configFile) throws IOException, FileNotFoundException {
		ConfigurationAdmin configurationAdmin = (ConfigurationAdmin) ConfigActivator.configurationAdminTracker.getService();
		if(configurationAdmin!=null) {
			// we need to remember which configuration needs to be updated because values have changed.
			Map<Configuration, Dictionary> configsToUpdate = new HashMap<Configuration, Dictionary>();
			
			// also cache the already retrieved configurations for each pid
			Map<Configuration, Dictionary> configMap = new HashMap<Configuration, Dictionary>();
			
			List<String> lines = IOUtils.readLines(new FileInputStream(configFile));
			for(String line : lines) {					
				String[] contents = parseLine(configFile.getPath(), line);
				// no valid configuration line, so continue
				if(contents==null) continue;
				String pid = contents[0];
				String property = contents[1];
				String value = contents[2];
				Configuration configuration = configurationAdmin.getConfiguration(pid, null);
				if(configuration!=null) {
					Dictionary configProperties = configMap.get(configuration);
					if(configProperties==null) {
						configProperties = new Properties();
						configMap.put(configuration, configProperties);
					}
					if(!value.equals(configProperties.get(property))) {
						configProperties.put(property, value);
						configsToUpdate.put(configuration, configProperties);
					}
				}
			}
			for(Entry<Configuration, Dictionary> entry : configsToUpdate.entrySet()) {
				entry.getKey().update(entry.getValue());
			}
		}
	}

	private static String[] parseLine(final String filePath, final String line) {
		String trimmedLine = line.trim();
		if(trimmedLine.startsWith("#") || trimmedLine.isEmpty()) {
			return null;
		}
		if(trimmedLine.substring(1).contains(":")) { 
			String pid = StringUtils.substringBefore(line, ":");
			String rest = line.substring(pid.length() + 1);
			if(!pid.contains(".")) {
				pid = "org.openhab." + pid;
			}
			if(!rest.isEmpty() && rest.substring(1).contains("=")) {
				String property = StringUtils.substringBefore(rest, "=");
				String value = rest.substring(property.length() + 1);
				return new String[] { pid.trim(), property.trim(), value.trim() };
			}
		}
		logger.warn("Cannot parse line '{}' of main configuration file '{}'.", line, filePath);
		return null;
	}

	private static String getMainConfigurationFilePath() {
		String progArg = System.getProperty(ConfigConstants.CONFIG_FILE_PROG_ARGUMENT);
		if(progArg!=null) {
			return progArg;
		} else {
			return ConfigConstants.MAIN_CONFIG_FOLDER + "/" + ConfigConstants.MAIN_CONFIG_FILENAME;
		}
	}

	private static String getDefaultConfigurationFilePath() {
		return ConfigConstants.MAIN_CONFIG_FOLDER + "/" + ConfigConstants.DEFAULT_CONFIG_FILENAME;
	}
}
