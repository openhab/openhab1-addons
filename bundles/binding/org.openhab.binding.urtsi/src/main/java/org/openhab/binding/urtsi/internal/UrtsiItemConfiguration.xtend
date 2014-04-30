package org.openhab.binding.urtsi.internal

import org.openhab.core.binding.BindingConfig

/**
 * This class contains all the configuration parameters you can define for the binding.
 * @author Oliver Libutzki
 * @since 1.3.0
 *
 */
@Data
class UrtsiItemConfiguration implements BindingConfig {
	
	/**
	 * Serial port of the urtsi device
	 */
	String deviceId
	
	/**
	 * Channel of the urtsi device
	 */
	int channel

	/**
	 * Address of the urtsi device
	 */
	int address
}