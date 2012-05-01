
package org.openhab.model.persistence;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class PersistenceStandaloneSetup extends PersistenceStandaloneSetupGenerated{

	public static void doSetup() {
		new PersistenceStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

