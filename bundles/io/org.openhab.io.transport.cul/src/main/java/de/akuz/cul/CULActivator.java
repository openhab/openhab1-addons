package de.akuz.cul;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akuz.cul.internal.CULSerialHandlerImpl;

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
