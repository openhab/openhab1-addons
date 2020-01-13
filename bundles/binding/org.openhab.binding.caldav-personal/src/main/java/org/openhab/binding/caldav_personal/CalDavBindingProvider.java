/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
     * 
     * @param item item for which the configuration is requested
     * @return configuration
     */
    CalDavConfig getConfig(String item);

}
