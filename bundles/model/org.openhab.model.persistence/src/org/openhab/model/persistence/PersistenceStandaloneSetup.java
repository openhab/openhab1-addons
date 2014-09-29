/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
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

