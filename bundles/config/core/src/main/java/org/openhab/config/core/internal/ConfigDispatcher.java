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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	static public void initializeBundleConfigurations() {
		ConfigurationAdmin configurationAdmin = (ConfigurationAdmin) ConfigActivator.configurationAdminTracker.getService();
		if(configurationAdmin!=null) {
			String configFilePath = getConfigurationFilePath();
			File configFile = new File(configFilePath);
			try {
				// we need to remember which configuration needs to be updated because values have changed.
				Map<Configuration, Dictionary> configsToUpdate = new HashMap<Configuration, Dictionary>();
				
				// also cache the already retrieved configurations for each pid
				Map<String, Configuration> configMap = new HashMap<String, Configuration>();
				
				List<String> lines = IOUtils.readLines(new FileInputStream(configFile));
				for(String line : lines) {					
					String[] contents = parseLine(configFilePath, line);
					// no valid configuration line, so continue
					if(contents==null) continue;
					String pid = contents[0];
					String property = contents[1];
					String value = contents[2];
					Configuration configuration = configMap.get(pid);
					if(configuration==null) {
						configuration = configurationAdmin.getConfiguration(pid, null);
						configMap.put(pid, configuration);
					}
					if(configuration!=null) {
						Dictionary configProperties = configuration.getProperties();
						if(configProperties==null) {
							configProperties = new Properties();
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
			} catch (FileNotFoundException e) {
				logger.warn("Main openHAB configuration file '{}' does not exist.", configFilePath);
			} catch (IOException e) {
				logger.error("Main openHAB configuration file '{}' cannot be read.", configFilePath, e);
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
		logger.warn("Cannot parse line Ô{}Ô of main configuration file Ô{}Ô.", line, filePath);
		return null;
	}

	private static String getConfigurationFilePath() {
		String progArg = System.getProperty(ConfigConstants.CONFIG_FILE_PROG_ARGUMENT);
		if(progArg!=null) {
			return progArg;
		} else {
			return ConfigConstants.DEFAULT_CONFIG_FOLDER + "/" + ConfigConstants.DEFAULT_CONFIG_FILENAME;
		}
	}
}
