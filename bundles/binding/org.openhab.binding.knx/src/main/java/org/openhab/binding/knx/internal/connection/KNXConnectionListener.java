/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.internal.connection;

/**
 * This interface should be implemented by classes that need to be informed when
 * a KNX connection has been established.
 * 
 * @author Andrea Giacosi
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public interface KNXConnectionListener {
	
	/**
	 * The callback method that is used to notify listeners about a successfull KNX connection
	 */
	void connectionEstablished();
}
