/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp;

import java.util.Set;

/**
 * Interface for Storage for items changed in configuration, to be reloaded.
 * 
 * @author Paolo Denti
 * @since 1.8.0
 */
public interface SappUpdatePendingRequestsProvider {

	/**
	 * adds itemName to the items list to be refreshed
	 * 
	 * @param itemName
	 *            name of item
	 */
	public void addPendingUpdateRequest(String itemName);

	/**
	 * clears all pending requests and adds itemName to the items list to be refreshed. Must be implemented atomically
	 * 
	 * @param itemName
	 *            name of item
	 */
	public void replaceAllPendingUpdateRequests(String itemName);

	/**
	 * returns all the pending update requests after having cleared the list. Must be implemented atomically
	 * 
	 * @return Set of item names to be refreshed
	 */
	public Set<String> getAndClearPendingUpdateRequests();

	/**
	 * Checks if any pending update request is present
	 * 
	 * @return true is at least one request is present; false otherwise
	 */
	public boolean areUpdatePendingRequestsPresent();
}
