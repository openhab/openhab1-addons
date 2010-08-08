package org.openhab.config.core.internal;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class ConfigActivator implements BundleActivator {
	
	/** Tracker for the ConfigurationAdmin service */
	public static ServiceTracker configurationAdminTracker;
	
	/**
	 * Called whenever the OSGi framework starts our bundle
	 */
	public void start(BundleContext bc) throws Exception {
		configurationAdminTracker = new ServiceTracker(bc, ConfigurationAdmin.class.getName(), null);
		configurationAdminTracker.open();
		
		// upon startup of this bundle, we directly launch the configuration process
		ConfigDispatcher.initializeBundleConfigurations();
	}

	/**
	 * Called whenever the OSGi framework stops our bundle
	 */
	public void stop(BundleContext bc) throws Exception {
		configurationAdminTracker.close();
	}
}
