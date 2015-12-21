/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.ecobee.internal;

import java.util.Dictionary;

import org.openhab.binding.ecobee.EcobeeActionProvider;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the Ecobee Action.
 * 
 * @author John Cocula
 * @since 1.8.0
 */
public class EcobeeActionService implements ActionService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(EcobeeActionService.class);

	private EcobeeActionProvider actionProvider;
	private static EcobeeActionService service;

	public static EcobeeActionService getEcobeeActionService() {
		return service;
	}

	public void activate() {
		logger.debug("Ecobee action service activated");
		service = this;
	}

	public void deactivate() {
		logger.debug("Ecobee action service deactivated");
		service = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getActionClassName() {
		return getActionClass().getCanonicalName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getActionClass() {
		return EcobeeAction.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
	}

	/**
	 * Setter for use by OSGi injection.
	 * 
	 * @param actionProvider the Ecobee Action Provider (provided by the Ecobee Binding).
	 */
	public void setEcobeeActionProvider(EcobeeActionProvider actionProvider) {
		this.actionProvider = actionProvider;
		logger.trace("setEcobeeActionProvider called");
	}

	/**
	 * Unsetter for use by OSGi injection.
	 * 
	 * @param actionProvider Ecobee Action Provider to remove.
	 */
	public void unsetEcobeeActionProvider(EcobeeActionProvider actionProvider) {
		this.actionProvider = null;
		logger.trace("unsetEcobeeActionProvider called");
	}

	/**
	 * Get the EcobeeActionProvider instance injected by OSGi.
	 * 
	 * @return the Ecobee Action Provider associated with this Action Service.
	 */
	public EcobeeActionProvider getEcobeeActionProvider() {
		return this.actionProvider;
	}
}
