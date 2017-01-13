/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Activator extension
 *
 * @author Vita Tucek
 * @since 1.9.0
 */
public class SimpleBinaryActivator implements BundleActivator {

    private static Logger logger = LoggerFactory.getLogger(SimpleBinaryActivator.class);

    @Override
    public void start(BundleContext context) throws Exception {
        logger.info("SimpleBinary binding has been started.");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        logger.info("SimpleBinary binding has been stopped.");

    }
}
