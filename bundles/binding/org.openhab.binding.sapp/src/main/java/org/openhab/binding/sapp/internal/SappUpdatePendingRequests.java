/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.openhab.binding.sapp.SappUpdatePendingRequestsProvider;

/**
 * Storage for items changed in configuration, to be reloaded. Synchronized access
 * 
 * @author Paolo Denti
 * @since 1.8.0
 */
public class SappUpdatePendingRequests implements SappUpdatePendingRequestsProvider {

	private Set<String> pendingUpdateRequests = Collections.synchronizedSet(new HashSet<String>());

	@Override
	public void addPendingUpdateRequest(String itemName) {
		pendingUpdateRequests.add(itemName);
	}

	@Override
	public void replaceAllPendingUpdateRequests(String itemName) {
		synchronized (pendingUpdateRequests) {
			pendingUpdateRequests.clear();
			pendingUpdateRequests.add(itemName);
		}
	}

	@Override
	public Set<String> getAndClearPendingUpdateRequests() {
		Set<String> toBeReturned = new HashSet<String>();
		synchronized (pendingUpdateRequests) {
			toBeReturned.addAll(pendingUpdateRequests);
			pendingUpdateRequests.clear();
		}

		return toBeReturned;
	}

	@Override
	public boolean areUpdatePendingRequestsPresent() {
		return pendingUpdateRequests.size() > 0;
	}
}
