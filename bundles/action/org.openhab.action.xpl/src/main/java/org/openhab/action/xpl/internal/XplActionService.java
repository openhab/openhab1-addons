/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.xpl.internal;

import org.openhab.core.scriptengine.action.ActionService;
import org.openhab.io.transport.xpl.XplTransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * This class registers an OSGi service for the Xpl action.
 * 
 * @author clinique
 * @since 1.6.0
 */
public class XplActionService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(XplActionService.class);

	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the
	 * action methods before executing code.
	 */
	/* default */ static boolean isProperlyConfigured = false;
	
	public XplActionService() {}
	
	public void activate() {
		logger.debug("xPL action service activated");
	}
	
	public void deactivate() {
		logger.debug("xPL action service deactivated");
	}

	@Override
	public String getActionClassName() {
		return Xpl.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Xpl.class;
	}

	/**
	 * Setter for Declarative Services. Adds the xPLManager instance.
	 * 
	 * @param xPLManager
	 *            Service.
	 */
	public void setXplTransportService(XplTransportService xPLTransportService) {
		Xpl.xplTransportService = xPLTransportService;
	}

	/**
	 * Unsetter for Declarative Services.
	 * 
	 * @param xPLManager
	 *            Service to remove.
	 */
	public void unsetXplTransportService(XplTransportService xPLTransportService) {
		Xpl.xplTransportService = null;
	}	
	
}
