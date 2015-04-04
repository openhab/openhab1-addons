/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal;

import static org.openhab.binding.ulux.internal.UluxBinding.LOG;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public final class UluxActivator implements BundleActivator {

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
		LOG.debug("u::Lux binding has been started.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop(final BundleContext bc) throws Exception {
		context = null;
		LOG.debug("u::Lux binding has been stopped.");
	}

}
