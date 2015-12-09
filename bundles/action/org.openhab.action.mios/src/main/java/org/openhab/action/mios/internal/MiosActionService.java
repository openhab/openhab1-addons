/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.mios.internal;

import java.util.Dictionary;

import org.openhab.binding.mios.MiosActionProvider;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the MiOS Action.
 * 
 * @author Mark Clark
 * @since 1.7.0
 */
public class MiosActionService implements ActionService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(MiosActionService.class);

	private MiosActionProvider actionProvider;
	private static MiosActionService service;

	public static MiosActionService getMiosActionService() {
		return service;
	}

	public void activate() {
		logger.debug("MiOS action service activated");
		service = this;
	}

	public void deactivate() {
		logger.debug("MiOS action service activated");
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
		return MiosAction.class;
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
	 * @param actionProvider the MiOS Action Provider (provided by the MiOS Binding).
	 */
	public void setMiosActionProvider(MiosActionProvider actionProvider) {
		this.actionProvider = actionProvider;
		logger.debug("MiOS setMiosActionProvider called");
	}

	/**
	 * Unsetter for use by OSGi injection.
	 * 
	 * @param actionProvider MiOS Action Provider to remove.
	 */
	public void unsetMiosActionProvider(MiosActionProvider actionProvider) {
		this.actionProvider = null;
		logger.debug("MiOS unsetMiosActionProvider called");
	}

	/**
	 * Get the MiosActionProvider instance injected by OSGi.
	 * 
	 * @return the MiOS Action Provider associated with this Action Service.
	 */
	public MiosActionProvider getMiosActionProvider() {
		return this.actionProvider;
	}
}
