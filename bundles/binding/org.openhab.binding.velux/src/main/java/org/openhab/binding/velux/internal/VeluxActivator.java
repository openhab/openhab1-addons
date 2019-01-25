/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.internal;

import org.openhab.binding.velux.VeluxBindingConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public final class VeluxActivator implements BundleActivator {

    private static Logger logger = LoggerFactory.getLogger(VeluxActivator.class);

    private static BundleContext context;

    /**
     * Returns the bundle context of this bundle.
     *
     * @return <b>context</b> of type {@link BundleContext}.
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
        logger.info("{} binding has been started.", VeluxBindingConstants.BINDING_ID);
    }

    /**
     * Called whenever the OSGi framework stops our bundle
     * {@inheritDoc}
     */
    @Override
    public void stop(final BundleContext bc) throws Exception {
        context = null;
        logger.info("{} binding has been stopped.", VeluxBindingConstants.BINDING_ID);
    }

}
