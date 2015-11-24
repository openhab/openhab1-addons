package org.openhab.binding.connectsdk.internal;

import org.openhab.core.binding.BindingConfig;

/**
 * This is a helper class holding binding specific configuration details
 * 
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public class ConnectSDKBindingConfig implements BindingConfig {
	public String property;
	public String clazz;
	public String device;
}