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
package org.openhab.binding.caldav_command;

import java.util.List;

import org.openhab.binding.caldav_command.internal.CalDavNextEventConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * Binding provider for the calDAV command
 * 
 * @author Robert Delbrück
 * @since 1.8.0
 */
public interface CalDavBindingProvider extends BindingProvider {
    /**
     * Returns the config for a given itemname.
     * 
     * @param item itemname for which the config should be returned
     * @return the calDAV item configuration
     */
    CalDavNextEventConfig getConfig(String item);

    /**
     * Returns the calDAV config for an itemname to which should be listened.
     * 
     * @param item item to which should be listened
     * @return the calDAV configurations for the requested item
     */
    List<CalDavNextEventConfig> getConfigForListenerItem(String item);
}
