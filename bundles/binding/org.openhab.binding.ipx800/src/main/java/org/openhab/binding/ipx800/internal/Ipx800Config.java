package org.openhab.binding.ipx800.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.service.cm.ConfigurationException;
/**
 * Global configuration of ipx800.
 * 
 * Configuration example :
 * ipx800:myipx.host=<hostname or ip>
 * ipx800:myipx.port=<port>
 * ipx800:myipx.x400.[1-3]=<extension alias>
 * ipx800:myipx.x880.[1-3]=<extension alias>
 * 
 * @author Seebag
 * @since 1.8.0
 *
 */
public class Ipx800Config {
	/** Pattern for all valid device configuration keys. */
	private final static Pattern CONFIG_PATTERN = Pattern.compile(
			"^(.+?)\\.(host|port|(x400|x880)\\.([1-3]))()$", Pattern.CASE_INSENSITIVE);
	/** Single instance of configuration */
	public final static Ipx800Config INSTANCE = new Ipx800Config();
	/** Map of all device configuration */
	private Map<String, Ipx800DeviceConfig> devices = new HashMap<String, Ipx800Config.Ipx800DeviceConfig>();
	
	public class Ipx800DeviceConfig {
		String name;
		String host;
		String port = "9870";
		/** Names of x400 extensions */
		String[] x400extensions = new String[3];
		/** Names of x880 extensions */
		String[] x880extensions = new String[3];
		
		public Ipx800DeviceConfig(String name) {
			this.name = name;
		}
		/**
		 * @return the number of x400 extensions
		 */
		public int getX400length() {
			return getExtensionSize(x400extensions);
		}
		/**
		 * @return the number of x880 extensions
		 */
		public int getX880length() {
			return getExtensionSize(x880extensions);
		}
		
		private int getExtensionSize(String[] extension) {
			int size = 0;
			for(String s : extension) {
				if (s != null && !s.isEmpty()) {
					size++;
				}
			}
			return size;
		}
	}
	
	/**
	 * @return deviceConfig map with deviceName as key
	 */
	public Map<String, Ipx800DeviceConfig> getDevices() {
		return devices;
	}
	
	/**
	 * Return the device identified by name, create one if it doesn't exist
	 * @param name
	 * @return
	 */
	public Ipx800DeviceConfig retrieveDevice(String name) {
		if (!devices.containsKey(name)) {
			devices.put(name, new Ipx800DeviceConfig(name));
		}
		return devices.get(name);
	}
	
	/**
	 * Read and parse the config creating the Ipx800Config object
	 * @param config
	 * @return the configuration
	 * @throws ConfigurationException when configuration is invalid
	 */
	static Ipx800Config readConfig(Dictionary<String, ?> config) throws ConfigurationException {
		Ipx800Config ipx800config = Ipx800Config.INSTANCE;
		if (config == null || config.isEmpty())
			return null;

		for (final Enumeration<String> e = config.keys(); e.hasMoreElements();) {
			final String key = e.nextElement();
			final String value = (String) config.get(key);

			// skip empty values
			if (value == null || value.trim().isEmpty())
				continue;

			// skip keys that we don't want to process here ...
			if ("service.pid".equals(key))
				continue;

			try {

				// check for config keys
				final Matcher matcher = CONFIG_PATTERN.matcher(key);
				if (matcher.matches()) {
					final String device = matcher.group(1);
					final String property = matcher.group(2);

					// device config
					Ipx800DeviceConfig deviceConfig = ipx800config.retrieveDevice(device);
					
					// dispatch individual properties
					if ("host".equalsIgnoreCase(property)) {
						deviceConfig.host = value.trim();
					} else if ("port".equalsIgnoreCase(property)) {
						deviceConfig.port = value.trim();
					} else {
						final String ext = matcher.group(3);
						final String number = matcher.group(4);
						if ("x880".equalsIgnoreCase(ext)) {
							deviceConfig.x880extensions[Integer.parseInt(number) - 1] = value.trim();
						} else {
							deviceConfig.x400extensions[Integer.parseInt(number) - 1] = value.trim();
						}
					}
				} else {
					throw new ConfigurationException(key, "Invalid config key");
				}
			} catch (NumberFormatException ex) {
				throw new ConfigurationException(key, "Invalid value: '" + value + "'");
			}
		}
		return ipx800config;
	}
}
