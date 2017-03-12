/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm1.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator
 *
 * @author Russell Stephens
 * @since 1.6.0
 */
public final class DSCAlarmActivator implements BundleActivator {

    private static Logger logger = LoggerFactory.getLogger(DSCAlarmActivator.class);

    private static BundleContext context;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(BundleContext bc) throws Exception {
        context = bc;
        logger.debug("start(): DSC Alarm binding has been started.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(BundleContext bc) throws Exception {
        context = null;
        logger.debug("stop(): DSC Alarm binding has been stopped.");
    }

    /**
     * Returns the bundle context of this bundle
     * 
     * @return the bundle context
     */
    public static BundleContext getContext() {
        return context;
    }
}
