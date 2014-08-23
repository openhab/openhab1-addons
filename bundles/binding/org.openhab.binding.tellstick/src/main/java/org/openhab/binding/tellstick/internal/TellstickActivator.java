/**
 * Copyright (c) 2013-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal;

import org.openhab.binding.tellstick.internal.device.TellstickDevice;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Platform;

/**
 * Tellstick activator, starts the JNA connection to Telldus Center.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public final class TellstickActivator implements BundleActivator {

	private static Logger logger = LoggerFactory.getLogger(TellstickActivator.class);

	private static BundleContext context;

	/**
	 * Called whenever the OSGi framework starts our bundle
	 */
	public void start(BundleContext bc) throws Exception {
		context = bc;
		logger.debug("Tellstick binding has been started." + Thread.currentThread());
		try {
			if (Platform.isWindows()) {
				System.setProperty("jna.library.path", "C:/Program Files/Telldus/;C:/Program Files (x86)/Telldus/");
			}

			TellstickDevice.setSupportedMethods(JNA.CLibrary.TELLSTICK_BELL | JNA.CLibrary.TELLSTICK_TURNOFF
					| JNA.CLibrary.TELLSTICK_TURNON | JNA.CLibrary.TELLSTICK_DIM | JNA.CLibrary.TELLSTICK_LEARN
					| JNA.CLibrary.TELLSTICK_EXECUTE | JNA.CLibrary.TELLSTICK_STOP);
			JNA.CLibrary.INSTANCE.tdInit();
		} catch (Exception e) {
			logger.error("Failed to init ", e);
			throw e;
		} catch (Throwable e) {
			logger.error("Failed to init ", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Called whenever the OSGi framework stops our bundle
	 */
	public void stop(BundleContext bc) throws Exception {
		context = null;
		logger.debug("Tellstick binding has been stopped.");
		JNA.CLibrary.INSTANCE.tdClose();

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
