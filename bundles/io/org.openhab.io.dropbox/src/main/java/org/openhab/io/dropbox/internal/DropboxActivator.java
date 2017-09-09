/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.dropbox.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator.
 *
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class DropboxActivator implements BundleActivator {

    private Logger logger;
    private BundleContext context;

    /**
     * Called whenever the OSGi framework starts our bundle
     */
    @Override
    public void start(BundleContext bc) throws Exception {
        logger = LoggerFactory.getLogger(DropboxActivator.class);
        context = bc;
        logger.debug("Dropbox I/O Bundle has been started.");
    }

    /**
     * Called whenever the OSGi framework stops our bundle
     */
    @Override
    public void stop(BundleContext bc) throws Exception {
        context = null;
        logger.debug("Dropbox I/O Bundle has been stopped.");
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
