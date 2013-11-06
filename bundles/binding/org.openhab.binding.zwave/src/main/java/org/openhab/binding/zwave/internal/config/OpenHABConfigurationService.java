package org.openhab.binding.zwave.internal.config;

import java.util.List;

/**
 * 
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public interface OpenHABConfigurationService {
	/**
	 * Returns the name of this bundle
	 * 
	 * @return String with the name of the bundle.
	 */
	public String getBundleName();

	/**
	 * Returns the name of this bundle
	 * 
	 * @return String with the name of the bundle.
	 */
	public String getCommonName();

	/**
	 * Returns the name of this bundle
	 * 
	 * @return String with the name of the bundle.
	 */
	public String getDescription();

	/**
	 * Returns the version of this bundle
	 * 
	 * @return String with the name of the bundle.
	 */
	public String getVersion();

	/**
	 * Gets the configuration items for a domain.
	 * 
	 * @param domain
	 * @returns
	 */
	public List<OpenHABConfigurationRecord> getConfiguration(String domain);

	/**
	 * Sets the configuration items for a domain.
	 *  
	 * @param domain
	 * @param records
	 */
	public void setConfiguration(String domain, List<OpenHABConfigurationRecord> records);

	/**
	 * Requests an action be performed on a domain.
	 *  
	 * @param domain
	 * @param action
	 */
	public void doAction(String domain, String action);

	/**
	 * Requests an action be performed on a domain.
	 *  
	 * @param domain
	 * @param name
	 * @param action
	 */
	public void doSet(String domain, String value);
}
