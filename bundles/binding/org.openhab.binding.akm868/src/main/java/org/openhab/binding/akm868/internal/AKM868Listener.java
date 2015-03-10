/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.akm868.internal;

public interface AKM868Listener {

	/**
	 * Inform all the interested items in this method
	 * @param ID of key and if the ID is present
	 */
	public void publishUpdate(String id, boolean isPresent);
}
	

