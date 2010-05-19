
package org.openhab.model;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class SitemapStandaloneSetup extends SitemapStandaloneSetupGenerated{

	public static void doSetup() {
		new SitemapStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

