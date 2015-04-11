/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav;

/**
 * Cal DAV loader interface
 * 
 * @author Robert Delbrück
 * @since 1.7.0
 */
public interface CalDavLoader {
	/**
	 * add listener to notify for event changes
	 * @param notifier the event listener
	 */
	public void addListener(EventNotifier notifier);
	
	/**
	 * remove listener to notify for event changes
	 * @param notifier the event listener
	 */
	public void removeListener(EventNotifier notifier);
}
