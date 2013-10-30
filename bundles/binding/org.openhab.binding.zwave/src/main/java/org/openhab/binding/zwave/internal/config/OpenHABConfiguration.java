package org.openhab.binding.zwave.internal.config;

import java.util.List;


/**
 * 
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
interface OpenHABConfiguration {
	/**
	 * Gets the configuration items for a domain.
	 * 
	 * @param domain
	 * @returns
	 */
	List<OpenHABConfigurationRecord> getConfiguration(String domain);

	/**
	 * Sets the configuration items for a domain.
	 *  
	 * @param domain
	 * @param records
	 */
	void setConfiguration(String domain, List<OpenHABConfigurationRecord> records);
}
