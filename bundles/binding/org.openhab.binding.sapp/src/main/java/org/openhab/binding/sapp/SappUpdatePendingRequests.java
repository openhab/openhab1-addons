/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp;

import java.util.HashSet;
import java.util.Set;

/**
 * Storage for items changed in cofiguration, to be reloaded. Synchronized access 
 * 
 * @author Paolo Denti
 * @since 1.8.0
 */
public class SappUpdatePendingRequests {
	
	private Boolean mutex;
	private Set<String> pendingUpdateRequests;
	
	public SappUpdatePendingRequests() {
		mutex = new Boolean(true);
		pendingUpdateRequests = new HashSet<String>();
	}
	
	public void addPendingUpdateRequest(String itemName) {
		synchronized (mutex) {
			pendingUpdateRequests.add(itemName);
		}
	}
	
	public void replaceAllPendingUpdateRequests(String itemName) {
		synchronized (mutex) {
			pendingUpdateRequests.clear();
			pendingUpdateRequests.add(itemName);
		}
	}
	
	public Set<String> getAndClearPendingUpdateRequests() {
		Set<String> toBeReturned = new HashSet<String>();
		synchronized (mutex) {
			toBeReturned.addAll(pendingUpdateRequests);
			pendingUpdateRequests.clear();
		}
		
		return toBeReturned;
	}
	
	public boolean areUpdatePendingRequestsPresent() {
		synchronized (mutex) {
			return pendingUpdateRequests.size() > 0;
		}
	}
}
