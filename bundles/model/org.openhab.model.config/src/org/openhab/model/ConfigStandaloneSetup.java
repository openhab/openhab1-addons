
package org.openhab.model;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class ConfigStandaloneSetup extends ConfigStandaloneSetupGenerated{

	public static void doSetup() {
		new ConfigStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

