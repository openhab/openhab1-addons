/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.gpio.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.openhab.io.gpio.GPIO;
import org.openhab.io.gpio.linux.GPIOLinux;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of OSGi bundle activator.
 * 
 * @author Dancho Penev
 * @since 1.5.0
 */
public class GPIOActivator implements BundleActivator {

	private static final Logger logger = LoggerFactory.getLogger(GPIOActivator.class);

	/**
	 * Called when this bundle is started by OSGi Framework.
	 * Determines underlying platform and loads the relevant service
	 * which implements <code>GPIO</code> interface.
	 */
	public void start(BundleContext context) throws Exception {

		/* Linux user space GPIO implementation, "sysfs" based */
		if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {

			GPIOLinux gpioLinux = new GPIOLinux();

			Dictionary<String, String> properties = new Hashtable<String, String>();
			properties.put("service.pid", "org.openhab.gpio");

			context.registerService(GPIO.class, gpioLinux, null);
			context.registerService(ManagedService.class, gpioLinux, properties);
		} else {
			/* Throwing exception is not implemented because it's causing Equinox to constantly trying to start the bundle */
			logger.error("No supported operating system was found, GPIO service won't be available");
		}
	}

	/**
	 * Called when this bundle is stopped by OSGi Framework.
	 */
	public void stop(BundleContext context) throws Exception {}
}
