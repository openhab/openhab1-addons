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
package org.openhab.binding.jointspace;

import org.openhab.core.binding.BindingProvider;

/**
 * @author David Lenz
 * @since 1.5.0
 */
public interface JointSpaceBindingProvider extends BindingProvider {

    /**
     * Returns the TV Command string that is stored for given @code itemName in
     * response to a given @code command
     * 
     * @param itemName
     * @param command
     * @return Stored string, or null if unknown combination of itemName and
     *         command
     */
    String getTVCommand(String itemName, String command);

}
