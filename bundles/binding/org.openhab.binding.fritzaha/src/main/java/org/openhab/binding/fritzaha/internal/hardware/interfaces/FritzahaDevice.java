/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal.hardware.interfaces;

import org.openhab.core.binding.BindingConfig;

/**
 * Classes implementing this interface are responsible for storing the binding
 * configuration and carrying out actions.
 * 
 * @author Christian Brauers
 * @since 1.3.0
 */
public interface FritzahaDevice extends BindingConfig {
	/**
	 * Getter for host ID
	 * 
	 * @return Host ID
	 */
	public String getHost();

	/**
	 * Getter for device ID
	 * 
	 * @return Device ID
	 */
	public String getId();
}
