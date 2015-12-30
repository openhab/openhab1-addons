/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.messages;

/**
 * The reset preferences function sets all of the user configurable settings back to the factory default values. This
 * function call will not only reset the top level thermostat settings such as hvacMode, lastServiceDate and vent, but
 * also all of the user configurable fields of the thermostat.settings and thermostat.program objects.
 * 
 * Note that this does not reset all values. For example, the installer settings and wifi details remain untouched.
 * 
 * @see <a
 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/ResetPreferences.shtml">ResetPreferences</a>
 * @author John Cocula
 * @since 1.8.0
 */
public final class ResetPreferencesFunction extends AbstractFunction {

	public ResetPreferencesFunction() {
		super("resetPreferences");
	}
}
