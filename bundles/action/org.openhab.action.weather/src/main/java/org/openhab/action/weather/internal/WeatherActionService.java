/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.weather.internal;

import java.util.Dictionary;

import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
	

/**
 * This class registers an OSGi service for the Weather action.
 * 
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public class WeatherActionService implements ActionService, ManagedService {

	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the
	 * action methods before executing code.
	 */
	static boolean isProperlyConfigured = false;
	
	@Override
	public String getActionClassName() {
		return Weather.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Weather.class;
	}
	
	public void activate() {}
	
	public void deactivate() {}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			isProperlyConfigured = true;
		}
	}
	
}
