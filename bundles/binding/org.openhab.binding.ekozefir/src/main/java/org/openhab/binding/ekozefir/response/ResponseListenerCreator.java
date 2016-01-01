/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.response;

import org.openhab.core.events.EventPublisher;

/**
 * Interface for creator of responses listener.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public interface ResponseListenerCreator {
	/**
	 * Create response listener.
	 * 
	 * @param item item to update
	 * @param publisher publisher to update
	 * @return listener
	 */
	ResponseListener create(String item, EventPublisher publisher);

	/**
	 * Get id of response.
	 * 
	 * @return id
	 */
	String getId();
}
