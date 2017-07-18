/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wr3223.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WR3223Activator implements BundleActivator {

    private static Logger logger = LoggerFactory.getLogger(WR3223Activator.class);

    @Override
    public void start(BundleContext context) throws Exception {
        logger.debug("WR3223 Binding has been started.");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        context = null;
        logger.debug("WR3223 Binding has been stopped.");

    }

}
