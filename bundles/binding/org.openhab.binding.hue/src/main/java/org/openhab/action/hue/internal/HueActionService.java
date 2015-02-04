/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.hue.internal;

import java.util.Dictionary;

import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the Homematic action.
 * 
 * @author Gernot Eger
 * @since 1.7.0
 */
public class HueActionService implements ActionService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(HueActionService.class);
	
	public void activate() {
		logger.debug("activate");
	}

	public void deactivate() {
		logger.debug("deactivate");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getActionClassName() {
		return HueActions.class.getCanonicalName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getActionClass() {
		return HueActions.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		logger.debug("updated");
	}

}
