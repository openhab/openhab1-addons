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
 *
 */
public class ConfigConstants {

	/** The program argument name for setting the main config directory path */
	final static public String CONFIG_DIR_PROG_ARGUMENT = "openhab.configdir";
	
	/** The program argument name for setting the main config file name */
	final static public String CONFIG_FILE_PROG_ARGUMENT = "openhab.configfile";
	
	/** The main configuration directory name of openHAB */
	final static public String MAIN_CONFIG_FOLDER = "configurations"; 
	
	/** The default filename of the main openHAB configuration file */
	final static public String MAIN_CONFIG_FILENAME = "openhab.cfg";

	/** The default filename of the default openHAB configuration file */
	final static public String DEFAULT_CONFIG_FILENAME = "openhab_default.cfg";
}
