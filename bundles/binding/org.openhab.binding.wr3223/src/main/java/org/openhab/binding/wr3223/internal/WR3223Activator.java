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
