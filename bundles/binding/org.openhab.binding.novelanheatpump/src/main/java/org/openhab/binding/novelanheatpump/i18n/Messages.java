/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
