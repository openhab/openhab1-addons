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
package org.openhab.binding.ebus;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Default OSGi bundle activator
 * 
 * @author Christian Sowada
 * @since 1.8.0
 */
public class Activator implements BundleActivator {

    private static BundleContext context;

    /**
     * Returns the static BundleContext
     * 
     * @return
     */
    public static BundleContext getInstance() {
        return context;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        Activator.context = context;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        Activator.context = null;
    }
}
