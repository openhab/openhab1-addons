package org.openhab.binding.zwave.internal.config;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;


/**
 * 
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public abstract class OpenHABConfigurationService {
	/**
	 * Returns the name of this bundle
	 * 
	 * @return String with the name of the bundle.
	 */
	String getBundleName() {
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass())
                .getBundleContext();
		return bundleContext.getBundle().getSymbolicName();
	}

	/**
	 * Returns the name of this bundle
	 * 
	 * @return String with the name of the bundle.
	 */
	abstract String getCommonName();

	/**
	 * Returns the name of this bundle
	 * 
	 * @return String with the name of the bundle.
	 */
	abstract String getDescription();
	/**
	 * Returns the version of this bundle
	 * 
	 * @return String with the name of the bundle.
	 */
	String getVersion() {
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass())
                .getBundleContext();
		return bundleContext.getBundle().getVersion().toString();
	}

	/**
	 * Gets the configuration items for a domain.
	 * 
	 * @param domain
	 * @returns
	 */
	abstract List<OpenHABConfigurationRecord> getConfiguration(String domain);

	/**
	 * Sets the configuration items for a domain.
	 *  
	 * @param domain
	 * @param records
	 */
	abstract void setConfiguration(String domain, List<OpenHABConfigurationRecord> records);
}
