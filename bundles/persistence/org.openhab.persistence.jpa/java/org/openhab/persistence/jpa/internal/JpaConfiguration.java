/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.jpa.internal;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The configuration required for Jpa binding.
 * @author mbergmann
 *
 */
public class JpaConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(JpaConfiguration.class);

	private static final String CFG_CONNECTION_URL = "url";
	private static final String CFG_DRIVER_CLASS = "driver";
	private static final String CFG_USERNAME = "user";
	private static final String CFG_PASSWORD = "password";
	private static final String CFG_SYNCMAPPING = "syncmappings";	
	
	public static boolean isInitialized = false;
	
	public static String dbConnectionUrl = "";
	public static String dbDriverClass = "";
	public static String dbUserName = "";
	public static String dbPassword = "";
	public static String dbSyncMapping = "";

	public void activate(final BundleContext bundleContext, final Map<String, Object> properties) {
		logger.debug("Update config...");
		
		if(properties == null) {
			logger.error("Got a null properties object!");
			return;
		}
		
		String param = (String)properties.get(CFG_CONNECTION_URL);
		logger.debug("url: " + param);
		if(param == null) {
			logger.warn("Connection url is required in openhab.cfg!");
		}
		if(StringUtils.isBlank(param)) {
			logger.warn("Empty connection url in openhab.cfg!");
		}
		dbConnectionUrl = (String)param;

		param = (String)properties.get(CFG_DRIVER_CLASS);
		logger.debug("driver: " + param);
		if(param == null) {
			logger.warn("Driver class is required in openhab.cfg!");
		}
		if(StringUtils.isBlank(param)) {
			logger.warn("Empty driver class in openhab.cfg!");
		}
		dbDriverClass = (String)param;
		
		if(properties.get(CFG_USERNAME) == null) {
			logger.info("{} was not specified!", CFG_USERNAME);
		}
		dbUserName = (String)properties.get(CFG_USERNAME);
		
		if(properties.get(CFG_PASSWORD) == null) {
			logger.info("{} was not specified!", CFG_PASSWORD);
		}
		dbPassword = (String)properties.get(CFG_PASSWORD);		

		if(properties.get(CFG_SYNCMAPPING) == null) {
			logger.info("{} was not specified!", CFG_SYNCMAPPING);
		}
		dbSyncMapping = (String)properties.get(CFG_SYNCMAPPING);		

		isInitialized = true;
		logger.debug("Update config...done");
	}	

}
