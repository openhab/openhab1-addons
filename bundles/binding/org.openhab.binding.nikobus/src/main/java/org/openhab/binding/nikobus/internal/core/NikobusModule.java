/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikobus.internal.core;


/**
 * A NikobusModule represents a physical module like a switch module or dimmer
 * module. Some modules may allow their status to be queried. This interface
 * should be implemented by all modules for which the status should be refreshed
 * at regular intervals or when buttons are pressed.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public interface NikobusModule extends NikobusCommandListener {

	/**
	 * Get the nikobus command to send in order to request a status update from
	 * the module.
	 * 
	 * @return command or null if none available.
	 */
	public NikobusCommand getStatusRequestCommand();

	/**
	 * Get the address of the module.
	 * 
	 * @return configured address of module.
	 */
	public String getAddress();
	
}
