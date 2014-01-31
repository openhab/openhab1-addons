/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.squeezebox.internal;

import org.openhab.core.scriptengine.action.ActionService;
import org.openhab.io.squeezeserver.SqueezeServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the Squeezebox action.
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class SqueezeboxActionService implements ActionService {

	private static final Logger logger = 
		LoggerFactory.getLogger(SqueezeboxActionService.class);

	public void activate() {
		logger.debug("Squeezebox action service activated");
	}

	public void deactivate() {
		logger.debug("Squeezebox action service deactivated");
	}
	
	public String getActionClassName() {
		return Squeezebox.class.getCanonicalName();
	}

	public Class<?> getActionClass() {
		return Squeezebox.class;
	}
	
	/**
	 * Setter for Declarative Services. Adds the SqueezeServer instance.
	 * 
	 * @param squeezeServer
	 *            Service.
	 */
	public void setSqueezeServer(SqueezeServer squeezeServer) {
		Squeezebox.squeezeServer = squeezeServer;
	}

	/**
	 * Unsetter for Declarative Services.
	 * 
	 * @param squeezeServer
	 *            Service to remove.
	 */
	public void unsetSqueezeServer(SqueezeServer squeezeServer) {
		Squeezebox.squeezeServer = null;
	}	
}
