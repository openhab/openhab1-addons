/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.lcn.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator for the LCN binding.
 * Used to activate the LCN binding.
 *
 * @author Tobias J�ttner
 */
public class LcnBindingActivator implements BundleActivator {

    /** Logger for this class. */
    private static final Logger logger = LoggerFactory.getLogger(LcnBindingActivator.class);

    /** The bundle context of this activator. */
    private static BundleContext context;

    /**
     * Called whenever the OSGi framework starts the bundle.
     * 
     * @param bc the bundle context
     */
    @Override
    public void start(BundleContext bc) throws Exception {
        context = bc;
        logger.debug("LCN binding has been started.");
    }

    /**
     * Called whenever the OSGi framework stops the bundle.
     * 
     * @param bc the bundle context
     */
    @Override
    public void stop(BundleContext bc) throws Exception {
        context = null;
        logger.debug("LCN binding has been stopped.");
    }

    /**
     * Gets the context of this bundle.
     * 
     * @return the bundle context
     */
    public static BundleContext getContext() {
        return context;
    }

}
