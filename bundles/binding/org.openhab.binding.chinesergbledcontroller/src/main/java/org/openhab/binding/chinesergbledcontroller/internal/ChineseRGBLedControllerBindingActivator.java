package org.openhab.binding.chinesergbledcontroller.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChineseRGBLedControllerBindingActivator implements BundleActivator {
	
	
	private static Logger logger = LoggerFactory.getLogger(BundleActivator.class);
	private static BundleContext context;
	
	
	@Override
	public void start(BundleContext bc) throws Exception {
		context = bc;
		logger.debug("ChineseRGBLedControllerBinding binding has been started.");

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		context = null;
		logger.debug("ChineseRGBLedControllerBinding binding has been stopped.");
	}
	
	public static BundleContext getContext() {
		return context;
	}

}
