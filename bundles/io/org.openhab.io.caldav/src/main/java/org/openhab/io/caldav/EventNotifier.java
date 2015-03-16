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
 * Public event notifier interface. Other bundles can register via this listener.
 * 
 * @author Robert Delbrück
 * @since 1.7.0
 */
public interface EventNotifier {
	void eventLoaded(CalDavEvent event);
	void eventRemoved(CalDavEvent event);
	void eventChanged(CalDavEvent event);
	void eventBegins(CalDavEvent event);
	void eventEnds(CalDavEvent event);
}
