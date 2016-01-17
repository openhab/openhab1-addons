/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmowelcome.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ing. Peter Weiss
 * @since 1.8.0
 */
public final class NetatmoWelcomeActivator implements BundleActivator {

	private static Logger logger = LoggerFactory
			.getLogger(NetatmoWelcomeActivator.class);

	private static BundleContext context;

	/**
	 * Returns the bundle context of this bundle
	 * 
	 * @return the bundle context
	 */
	public static BundleContext getContext() {
		return context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(final BundleContext bc) throws Exception {
		context = bc;
		logger.debug("NetatmoWelcome binding has been started.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop(final BundleContext bc) throws Exception {
		context = null;
		logger.debug("NetatmoWelcome binding has been stopped.");
	}

}
