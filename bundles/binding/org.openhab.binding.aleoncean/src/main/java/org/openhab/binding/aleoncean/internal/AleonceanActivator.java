/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package org.openhab.binding.aleoncean.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 * @since 1.6.0
 */
public final class AleonceanActivator implements BundleActivator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AleonceanActivator.class);

    private BundleContext context;

    /**
     * Called whenever the OSGi framework starts our bundle
     *
     * @param bc
     * @throws java.lang.Exception
     */
    @Override
    public void start(final BundleContext bc) throws Exception {
        context = bc;
        LOGGER.debug("aleoncean binding has been started.");
    }

    /**
     * Called whenever the OSGi framework stops our bundle
     *
     * @param bc
     * @throws java.lang.Exception
     */
    @Override
    public void stop(final BundleContext bc) throws Exception {
        context = null;
        LOGGER.debug("aleoncean binding has been stopped.");
    }

    /**
     * Returns the bundle context of this bundle
     *
     * @return the bundle context
     */
    public BundleContext getContext() {
        return context;
    }

}
