package org.openhab.binding.megadevice.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MegaDeviceActivator implements BundleActivator {

	private static Logger logger = LoggerFactory
			.getLogger(MegaDeviceActivator.class);
	
	private static BundleContext context;

	
	public void start(BundleContext context) throws Exception {
		logger.info("MegaDevice binding has been started.");
	}

	
	public void stop(BundleContext context) throws Exception {
		logger.debug("MegaDevice binding has been stopped.");
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
