/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal;

import org.openhab.binding.plex.internal.communication.MediaContainer;

/**
* Callback interface to signal listeners that an update was received from Plex 
* 
* @author Jeroen Idserda
* @since 1.7.0
*/
public interface PlexUpdateReceivedCallback {

	/**
	 * Update received
	 * 
	 * @param The update, wrapped in session object
	 */
	public void updateReceived(PlexSession session);
	
	/**
	 * Server list was updated
	 * 
	 * @param MediaContainer object, containing the server list 
	 */
	public void serverListUpdated(MediaContainer container);
}
