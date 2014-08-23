/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.prowl.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
	

/**
 * This class registers an OSGi service for the Prowl action.
 * 
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class ProwlActionService implements ActionService, ManagedService {

	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the
	 * action methods before executing code.
	 */
	/* default */ static boolean isProperlyConfigured = false;
	
	public ProwlActionService() {
	}
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate Resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	@Override
	public String getActionClassName() {
		return Prowl.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Prowl.class;
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config!=null) {
			Prowl.url = (String) config.get("url");
			
			Prowl.apiKey = (String) config.get("apikey");
			if (StringUtils.isBlank(Prowl.apiKey)) {
				throw new ConfigurationException("prowl.apikey", "The parameter 'apikey' must be configured. Please refer to your 'openhab.cfg'");
			}
			
			String priorityString = (String) config.get("defaultpriority");
			if (StringUtils.isNotBlank(priorityString)) {
				Prowl.priority = Integer.valueOf(priorityString);
			}
			
			isProperlyConfigured = true;
		}
	}
	
}
