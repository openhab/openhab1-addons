package org.openhab.binding.hue.internal;

import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * a base class for all hue binding configs
 * 
 * @author Gernot Eger
 * @since 1.7.0
 *
 */
public abstract class AbstractHueBindingConfig {

	/**
	 * The number under which the bulb is filed in the Hue bridge.
	 */
	protected final int deviceNumber;

	public AbstractHueBindingConfig(String deviceNumber) throws BindingConfigParseException {
		super();
		this.deviceNumber = parseDeviceNumberConfigString(deviceNumber);
	}

	/**
	 * Parses a device number string that has been found in the configuration.
	 * 
	 * @param configString
	 *            The device number as a string.
	 * @return The device number as an integer value.
	 * @throws BindingConfigParseException
	 */
	protected int parseDeviceNumberConfigString(String configString)
			throws BindingConfigParseException {
				try {
					return Integer.parseInt(configString);
				} catch (Exception e) {
					throw new BindingConfigParseException(
							"Error parsing device number.");
				}
			}

	/**
	 * @return The device number that has been declared in the binding
	 *         configuration.
	 */
	public int getDeviceNumber() {
		return deviceNumber;
	}

}