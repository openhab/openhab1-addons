package org.openhab.config.core.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigService {

	private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);

	@SuppressWarnings("unchecked")
	public void initializeBundleConfigurations() {
		ConfigurationAdmin configurationAdmin = (ConfigurationAdmin) ConfigActivator.configurationAdminTracker.getService();
		if(configurationAdmin!=null) {
			File configFile = new File("config/openhab.cfg");
			try {
				List<String> lines = IOUtils.readLines(new FileInputStream(configFile));
				for(String line : lines) {
					String pid = StringUtils.substringBefore(line, ":");
					line = line.substring(pid.length() + 1);
					String property = StringUtils.substringBefore(line, "=");
					String value = line.substring(property.length() + 1);
					
					Configuration configuration = configurationAdmin.getConfiguration(pid);
					if(configuration!=null) {
						@SuppressWarnings("rawtypes")
						Dictionary configProperties = configuration.getProperties();
						if(configProperties==null) {
							configProperties = new Properties();
						}
						configProperties.put(property, value);
						configuration.update(configProperties);
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}		
	}
}
