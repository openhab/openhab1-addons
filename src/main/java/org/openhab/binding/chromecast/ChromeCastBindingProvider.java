package org.openhab.binding.chromecast;

import org.openhab.binding.chromecast.internal.ChromeCastGenericBindingProvider.ChromecastBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * @author ibaton
 * @since 1.7
 */
public interface ChromeCastBindingProvider extends BindingProvider {
	ChromecastBindingConfig getChromecastInstance(String itemname);
	boolean containsChromecastInstance(String itemname);
}
