/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.dscalarm.internal;

import java.util.Dictionary;

import org.openhab.binding.dscalarm.DSCAlarmActionProvider;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the DSC Alarm action.
 * 
 * @author Russell Stephens
 * @since 1.8.0
 */
public class DSCAlarmActionService implements ActionService, ManagedService {

	private static Logger logger = LoggerFactory.getLogger(DSCAlarmActionService.class);
	
	private DSCAlarmActionProvider actionProvider = null;
	private static DSCAlarmActionService actionService = null;

	public DSCAlarmActionService() {
	}
	
	public void activate() {
		logger.debug("DSC Alarm Action Service Activated!");
		actionService = this;
	}
	
	public void deactivate() {
		logger.debug("DSC Alarm Action Service Deactivated!");
		actionService = null;
	}

	@Override
	public String getActionClassName() {
		return getActionClass().getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return DSCAlarmAction.class;
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {		
	}
	
	public static DSCAlarmActionService getDSCAlarmActionService() {
		return actionService;
	}

	/**
	 * Sets the DSC Alarm Action Provider for use by OSGi injection.
	 * 
	 * @param actionProvider the DSC Alarm Action Provider (provided by the DSC Alarm Binding).
	 */
	public void setDSCAlarmActionProvider(DSCAlarmActionProvider actionProvider) {
		this.actionProvider = actionProvider;
		logger.trace("setDSCAlarmActionProvider(): DSC Alarm Action Provider Set!");
	}

	/**
	 * Resets the DSC Alarm Action Provider to null.
	 * 
	 */
	public void resetDSCAlarmActionProvider() {
		this.setDSCAlarmActionProvider(null);
		logger.trace("resetDSCAlarmActionProvider(): DSC Alarm Action Provider Reset!");
	}

	/**
	 * Get the DSC Alarm Action Provider instance injected by OSGi.
	 * 
	 * @return the DSC Alarm Action Provider associated with this Action Service.
	 */
	public DSCAlarmActionProvider getDSCAlarmActionProvider() {
		return this.actionProvider;
	}

}
