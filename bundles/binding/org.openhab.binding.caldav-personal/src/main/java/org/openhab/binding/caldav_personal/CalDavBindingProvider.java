/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.caldav_personal;

import org.openhab.binding.caldav_personal.internal.CalDavConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * Binding provider for the calDAV personal
 * 
 * @author Robert Delbrück
 * @since 1.8.0
 */
public interface CalDavBindingProvider extends BindingProvider {
	
	/**
	 * returns the configuration for a given item
	 * @param item item for which the configuration is requested
	 * @return configuration
	 */
	CalDavConfig getConfig(String item);
	
}
