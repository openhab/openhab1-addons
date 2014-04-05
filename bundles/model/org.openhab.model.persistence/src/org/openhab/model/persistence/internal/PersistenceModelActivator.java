/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.persistence.internal;

import org.openhab.model.persistence.PersistenceStandaloneSetup;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistenceModelActivator implements BundleActivator {

	private final static Logger logger = LoggerFactory.getLogger(PersistenceModelActivator.class);

	public void start(BundleContext bc) throws Exception {
		PersistenceStandaloneSetup.doSetup();
		logger.debug("Registered 'persistence' configuration parser");	
	}

	public void stop(BundleContext context) throws Exception {
	}
}
