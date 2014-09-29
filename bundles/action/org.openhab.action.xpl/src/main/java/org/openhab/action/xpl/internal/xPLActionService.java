/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.xpl.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * This class registers an OSGi service for the xPL action.
 * 
 * @author clinique
 * @since 1.5.0
 */
public class xPLActionService implements ActionService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(xPLActionService.class);

	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the
	 * action methods before executing code.
	 */
	/* default */ static boolean isProperlyConfigured = false;
	
	public xPLActionService() {}
	
	public void activate() {}
	
	public void deactivate() {
		xPL.stopManager();
	}

	@Override
	public String getActionClassName() {
		return xPL.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return xPL.class;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("Updating config");
		if (config != null) {
			String instancename = (String) config.get("instance");
			logger.debug("Received new config : " + instancename);
			xPL.setInstance(instancename);
			isProperlyConfigured = true;
			// check mandatory settings
			if (StringUtils.isBlank(xPL.getInstance())) {
				throw new ConfigurationException("xPL", "Parameters xPL:instance is mandatory and must be configured. Please check your openhab.cfg!");
			}
		}
	}
	
}
