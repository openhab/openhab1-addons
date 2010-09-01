package org.openhab.binding.bluetooth.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class BluetoothActivator implements BundleActivator {

	private static BundleContext context;
	private static BTDeviceDiscoveryService btService;
	
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		BluetoothActivator.context = bundleContext;
		btService = new BTDeviceDiscoveryService();
		btService.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		BluetoothActivator.context = null;
		btService.setInterrupted(true);
		btService = null;
	}

}
