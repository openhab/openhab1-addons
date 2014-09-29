/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.rule;

import com.google.inject.Injector;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class RulesStandaloneSetup extends RulesStandaloneSetupGenerated{

	private static Injector injector;
	
	public static void doSetup() {
		if(injector==null) {
			injector = new RulesStandaloneSetup().createInjectorAndDoEMFRegistration();
		}
	}
	
	static public Injector getInjector() {
		if(injector==null) {
			doSetup();
		}
		return injector;
	}
}

