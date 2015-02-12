/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.persistence;

/**
 * Restores the persistent state of all items.
 * 
 * @author Gerhard Riegler
 * @since 1.6.1
 */
public interface PersistentStateRestorer {

	/**
	 * Initializes all persistent items with their saved state.
	 */
	public void initializeItems(String modelName);

}
