/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal;

/**
 * Callback interface to signal any listeners that the current pilight configuration was received. 
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public interface IPilightConfigReceivedCallback {
	
	/**
	 * Configuration received. 
	 * 
	 * @param connection The connection to pilight that received the configuration 
	 */
	public void configReceived(PilightConnection connection);
	
}
