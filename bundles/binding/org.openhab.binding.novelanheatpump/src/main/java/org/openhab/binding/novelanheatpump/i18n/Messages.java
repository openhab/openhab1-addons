/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.novelanheatpump.i18n;

import org.eclipse.osgi.util.NLS;


public class Messages extends NLS {
	
	private static final String BUNDLE_NAME = "org.openhab.binding.novelanheatpump.i18n.messages"; //$NON-NLS-1$
	
	public static String HeatPumpBinding_APPEAR;
	public static String HeatPumpBinding_DEFROSTING;
	public static String HeatPumpBinding_ERROR;
	public static String HeatPumpBinding_RUNNING;
	public static String HeatPumpBinding_STOPPED;
	public static String HeatPumpBinding_UNKNOWN;
	// ExtendeState Values
	public static String HeatPumpBinding_HEATING;
	public static String HeatPumpBinding_STANDBY;
	public static String HeatPumpBinding_SWITCH_ON_DELAY;
	public static String HeatPumpBinding_SWITCHING_CYCLE_BLOCKING;	
	public static String HeatPumpBinding_PROVIDER_LOCK_TIME;
	public static String HeatPumpBinding_SERVICE_WATER;
	public static String HeatPumpBinding_SCREED_HEAT_UP;
	public static String HeatPumpBinding_PUMP_FLOW;
	public static String HeatPumpBinding_DISINFECTION;
	public static String HeatPumpBinding_COOLING;
	public static String HeatPumpBinding_POOL_WATER;
	public static String HeatPumpBinding_HEATING_EXT;
	public static String HeatPumpBinding_SERVICE_WATER_EXT;
	public static String HeatPumpBinding_FLOW_MONITORING;
	public static String HeatPumpBinding_ZWE_OPERATION;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
