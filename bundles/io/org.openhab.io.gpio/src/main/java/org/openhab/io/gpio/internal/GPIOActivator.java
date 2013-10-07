/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-${year}, openHAB.org <admin@openhab.org>
 * 
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 * 
 * Additional permission under GNU GPL version 3 section 7
 * 
 * If you modify this Program, or any covered work, by linking or 
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
 * @since 1.3.1
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
