/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal;

import java.io.InputStream;

import org.openhab.binding.souliss.internal.network.typicals.Constants;
import org.openhab.binding.souliss.internal.network.typicals.SoulissNetworkParameter;
import org.openhab.binding.souliss.internal.network.typicals.StateTraslator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public final class SoulissActivator implements BundleActivator {

	private static Logger logger = LoggerFactory.getLogger(SoulissActivator.class); 
	
	String sConfigurationFileName=Constants.ConfigurationFileName_typicals_value_bytes;
	String sConfigurationFileName_commands_OHtoSOULISS=Constants.ConfigurationFileName_commands_OHtoSOULISS;
	String sConfigurationFileName_states_SOULISStoOH=Constants.ConfigurationFileName_states_SOULISStoOH;
	String sConfigurationFileName_ItemsType_OHtoSOULISS=Constants.ConfigurationFileName_ItemsType_SOULISS;
	String sConfigurationFileName_commands_to_states=Constants.ConfigurationFileName_commands_to_states;
	
	
	/**
	 * Called whenever the OSGi framework starts our bundle
	 * 	 
	 * Load informations from properties files
	 * @author Antonino Fazio
	 * @since 1.7.0
	 */
	public void start(BundleContext bc) throws Exception {
		logger.info("souliss binding has been started.");
		
        InputStream is = getClass().getResourceAsStream("/"+sConfigurationFileName);
		logger.info("Load parameter from file: " + sConfigurationFileName);  
		SoulissNetworkParameter.load(is);
		
		logger.info("Load parameter from file: " + sConfigurationFileName_commands_OHtoSOULISS);  
		is = getClass().getResourceAsStream("/"+sConfigurationFileName_commands_OHtoSOULISS);
		StateTraslator.loadCommands(is);
		
		logger.info("Load parameter from file: " + sConfigurationFileName_states_SOULISStoOH);  
		is = getClass().getResourceAsStream("/"+sConfigurationFileName_states_SOULISStoOH);
		StateTraslator.loadStates(is);
		
		logger.info("Load parameter from file: " + sConfigurationFileName_ItemsType_OHtoSOULISS);  
		is = getClass().getResourceAsStream("/"+sConfigurationFileName_ItemsType_OHtoSOULISS);
		StateTraslator.loadItemsType(is);
		
		logger.info("Load parameter from file: " + sConfigurationFileName_commands_to_states);  
		is = getClass().getResourceAsStream("/"+sConfigurationFileName_commands_to_states);
		StateTraslator.loadCommands_to_states(is);
	}


	/**
	 * Called whenever the OSGi framework stops our bundle
	 * 
	 * Only log info
	 * @author Antonino Fazio
	 * @since 1.7.0
	 */
	public void stop(BundleContext bc) throws Exception {
		logger.info("souliss binding has been stopped.");
	}

}
