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
package org.openhab.binding.expire;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * @author Michael Wyraz
 * @since 1.9.0
 */
public interface ExpireBindingProvider extends BindingProvider {
    public String getDurationString(String itemName);

    public long getDuration(String itemName);

    public State getExpireState(String itemName);

    public Command getExpireCommand(String itemName);
}
