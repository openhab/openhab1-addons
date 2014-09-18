/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config;

/**
 * Class with the available binding actions.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public enum BindingAction {
	RELOAD_VARIABLES, RELOAD_DATAPOINTS, RELOAD_RSSI;

	/**
	 * Parses a string and returns the BindingAction.
	 */
	public static BindingAction parse(String action) {
		if (action == null) {
			return null;
		} else if (RELOAD_VARIABLES.toString().equalsIgnoreCase(action)) {
			return RELOAD_VARIABLES;
		} else if (RELOAD_DATAPOINTS.toString().equalsIgnoreCase(action)) {
			return RELOAD_DATAPOINTS;
		} else if (RELOAD_RSSI.toString().equalsIgnoreCase(action)) {
			return RELOAD_RSSI;
		} else {
			return null;
		}

	}

}
