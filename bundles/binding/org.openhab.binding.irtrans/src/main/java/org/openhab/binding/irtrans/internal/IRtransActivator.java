/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.irtrans.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * OSGi activator class for the IRtrans binding
 * 
 * @author Karel Goderis
 * @since 1.4.0
 *
 */
public class IRtransActivator implements BundleActivator {

	private static Logger logger = LoggerFactory.getLogger(IRtransActivator.class); 

	/**
	 * {@inheritDoc}
	 */
	public void start(BundleContext bundleContext) throws Exception {
		logger.debug("IRtrans binding has been started.");
	}

	/**
	 * {@inheritDoc}
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		logger.debug("IRtrans binding has been stopped.");
	}

}
