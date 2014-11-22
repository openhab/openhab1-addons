/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator.
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public final class MiosActivator implements BundleActivator {

	private static Logger logger = LoggerFactory.getLogger(MiosActivator.class);

	private static BundleContext context;

	/**
	 * Called whenever the OSGi framework starts our bundle.
	 * 
	 * @param bc
	 *            the OSGi BundleContext associated with our openHAB Binding.
	 */
	public void start(BundleContext bc) throws Exception {
		context = bc;
		logger.debug("MiOS binding has been started");
	}

	/**
	 * Called whenever the OSGi framework stops our bundle.
	 * 
	 * @param bc
	 *            the OSGi BundleContext associated with our openHAB Binding.
	 */
	public void stop(BundleContext bc) throws Exception {
		context = null;
		logger.debug("MiOS binding has been stopped");
	}

	/**
	 * Returns the OSGi BundleContext of this bundle.
	 * 
	 * The OSGi BundleContext is needed to talk with other services running
	 * under OSGi.
	 * <p>
	 * eg. openHAB's {@code TransformationService}.
	 * 
	 * @return the bundle context
	 */
	public static BundleContext getContext() {
		return context;
	}

}
