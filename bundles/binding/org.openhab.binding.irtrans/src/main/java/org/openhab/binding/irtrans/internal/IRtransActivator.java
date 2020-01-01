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
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        logger.debug("IRtrans binding has been started.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        logger.debug("IRtrans binding has been stopped.");
    }

}
