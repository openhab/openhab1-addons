package org.openhab.binding.bluetooth.internal;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BluetoothConfig implements ManagedService {
	
	private static final String SERVICE_PID = "bluetooth";
	private static final String REFRESH_RATE = "refresh";

	private static final Logger logger = LoggerFactory.getLogger(BluetoothConfig.class);

	/** the frequency of how often a new Bluetooth device scan is triggered in seconds */
	static public int refreshRate = 30;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
		if(config!=null) {
			String value = (String) config.get(REFRESH_RATE);
			try {
				int newRefreshRate = Integer.parseInt(value);
				refreshRate = newRefreshRate;
			} catch(NumberFormatException e) {
				logger.warn("Invalid configuration value '{}' for parameter '" + SERVICE_PID + ":" + REFRESH_RATE+ "'", value);
			}
		}
	}

}
