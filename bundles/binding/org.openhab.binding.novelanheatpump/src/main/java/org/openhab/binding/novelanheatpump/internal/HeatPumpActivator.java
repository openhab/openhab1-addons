/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.novelanheatpump.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Extension of the default OSGi bundle activator
 * 
 * @author Jan-Philipp Bolle
 * @since 1.0.0
 */
public class HeatPumpActivator implements BundleActivator {

	private static Logger logger = LoggerFactory.getLogger(HeatPumpActivator.class); 
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/**
	 * Called whenever the OSGi framework starts our bundle
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		HeatPumpActivator.context = bundleContext;
		logger.debug("Novelan HeatPump binding has been started.");
	}

	/**
	 * Called whenever the OSGi framework stops our bundle
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		HeatPumpActivator.context = null;
		logger.debug("Novelan HeatPump binding has been stopped.");
	}

}
