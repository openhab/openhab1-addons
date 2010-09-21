package org.openhab.binding.bluetooth.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class BluetoothActivator implements BundleActivator {

	private static BundleContext context;	
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		BluetoothActivator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		BluetoothActivator.context = null;
	}

}
