/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.opensprinkler;

import org.openhab.core.binding.BindingProvider;

/**
 * A custom binding provide for OpenSprinkler, to allow for configuration to
 * specify a station number for a given switch.
 * 
 * @author Jonathan Giles (http://www.jonathangiles.net)
 * @since 1.3.0
 */
public interface OpenSprinklerBindingProvider extends BindingProvider {

	/**
	 * Returns the station number specified for item {@code itemName}.
	 */
	public int getStationNumber(String itemName);
	
	/**
	 * Returns the command value for item {@code itemName}.
	 */
	public String getCommand(String itemName);

}
