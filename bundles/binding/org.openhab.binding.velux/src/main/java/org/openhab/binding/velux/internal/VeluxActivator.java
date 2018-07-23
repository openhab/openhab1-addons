/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guenther Schreiner
 * @since 1.13.0
 */
public final class VeluxActivator implements BundleActivator {

    private static Logger logger = LoggerFactory.getLogger(VeluxActivator.class);

    private static BundleContext context;

    /**
     * Returns the bundle context of this bundle.
     *
     * @return the bundle context
     */
    public static BundleContext getContext() {
        logger.debug("getContext() called.");
        return context;
    }

    /**
     * Called whenever the OSGi framework starts our bundle
     * {@inheritDoc}
     */
    @Override
    public void start(final BundleContext bc) throws Exception {
        context = bc;
        logger.info("Velux binding has been started.");
    }

    /**
     * Called whenever the OSGi framework stops our bundle
     * {@inheritDoc}
     */
    @Override
    public void stop(final BundleContext bc) throws Exception {
        context = null;
        logger.info("Velux binding has been stopped.");
    }

}
/*
 * end-of-internal/VeluxActivator.java
 */
