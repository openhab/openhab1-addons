package org.openhab.io.transport.cul;

import org.openhab.io.transport.cul.internal.CULSerialHandlerImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CULActivator implements BundleActivator {

	private static final Logger logger = LoggerFactory.getLogger(CULActivator.class);

	private static BundleContext context;

	@Override
	public void start(BundleContext bc) throws Exception {
		context = bc;
		logger.debug("CUL transport has been started.");
		CULManager.registerHandlerClass("serial", CULSerialHandlerImpl.class);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		context = null;
		logger.debug("CUL transport has been stopped.");

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
