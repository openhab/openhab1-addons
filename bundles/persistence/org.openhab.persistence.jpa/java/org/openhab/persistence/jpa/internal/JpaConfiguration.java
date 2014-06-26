package org.openhab.persistence.jpa.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JpaConfiguration implements ManagedService {
	private static final Logger logger = LoggerFactory.getLogger(JpaConfiguration.class);

	private static final String CFG_CONNECTION_URL = "url";
	private static final String CFG_DRIVER_CLASS = "driver";
	private static final String CFG_USERNAME = "user";
	private static final String CFG_PASSWORD = "password";
	
	public static boolean isInitialized = false;
	
	public static String dbConnectionUrl = "";
	public static String dbDriverClass = "";
	public static String dbUserName = "";
	public static String dbPassword = "";

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		logger.debug("Update config...");
		
		if(properties == null) {
			logger.error("Got a null properties object!");
			return;
		}
		
		String param = (String)properties.get(CFG_CONNECTION_URL);
		logger.debug("url: " + param);
		if(param == null) {
			logger.error("Connection url is required in openhab.cfg!");
			throw new ConfigurationException(CFG_CONNECTION_URL, "Connection url is required in openhab.cfg!");
		}
		if(StringUtils.isBlank(param)) {
			logger.error("Empty connection url in openhab.cfg!");
			throw new ConfigurationException(CFG_CONNECTION_URL, "Empty connection url in openhab.cfg!");
		}
		dbConnectionUrl = (String)param;

		param = (String)properties.get(CFG_DRIVER_CLASS);
		logger.debug("driver: " + param);
		if(param == null) {
			throw new ConfigurationException(CFG_DRIVER_CLASS, "Driver class is required in openhab.cfg!");
		}
		if(StringUtils.isBlank(param)) {
			throw new ConfigurationException(CFG_DRIVER_CLASS, "Empty driver class in openhab.cfg!");
		}
		dbDriverClass = (String)param;
		
		if(properties.get(CFG_USERNAME) == null) {
			logger.info(CFG_USERNAME + " was not specified!");
		}
		dbUserName = (String)properties.get(CFG_USERNAME);
		
		if(properties.get(CFG_PASSWORD) == null) {
			logger.info(CFG_PASSWORD + " was not specified!");
		}
		dbPassword = (String)properties.get(CFG_PASSWORD);		

		isInitialized = true;
		logger.debug("Update config...done");
	}	

}
