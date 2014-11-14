/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.config.core;

/**
 * This class provides constants relevant for the configuration of openHAB
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 */
public class ConfigConstants {

	/** The program argument name for setting the main config directory path */
	final static public String CONFIG_DIR_PROG_ARGUMENT = "openhab.configdir";
	
	/** The program argument name for setting the service config directory path */
	final static public String SERVICEDIR_PROG_ARGUMENT = "openhab.servicedir";
	
	/** The program argument name for setting the default services config file name */
	final static public String SERVICECFG_PROG_ARGUMENT = "openhab.configfile";
	
	/** The main configuration directory name of openHAB */
	final static public String MAIN_CONFIG_FOLDER = "configurations"; 
	
	/** The default folder name of the configuration folder of services */
	final static public String SERVICES_FOLDER = "services";

	/** The default services configuration filename */
	final static public String DEFAULT_SERVICE_CFG_FILE = "openhab_default.cfg";

	/** The main services configuration filename */
	final static public String MAIN_SERVICE_CFG_FILE = "openhab.cfg";

	/** The program argument name for setting the service pid namespace */
	final static public String SERVICEPID_PROG_ARGUMENT = "openhab.servicepid";

	/** The default namespace for service pids */
	final static public String SERVICE_PID_NAMESPACE = "org.openhab";
	
}
