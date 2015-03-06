/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.conditionalaction.internal;

import java.util.Dictionary;

import org.openhab.core.items.ItemRegistry;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * This class registers an OSGi service for the ConditionalAction action.
 * 
 * @author Juergen Richtsfeld
 * @since 1.7.0
 */
public class ConditionalActionActionService implements ActionService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(ConditionalActionActionService.class);
	
	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the
	 * action methods before executing code.
	 */
	/* default */ static boolean isProperlyConfigured = true;
	
	public ConditionalActionActionService() {
	}
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate Resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	@Override
	public String getActionClassName() {
		return ConditionalAction.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return ConditionalAction.class;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			
			// read config parameters here ...

			isProperlyConfigured = true;
		}
	}
	
}
