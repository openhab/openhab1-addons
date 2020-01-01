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
package org.openhab.binding.onewire.internal.deviceproperties;

import org.openhab.core.types.Command;

/**
 * Interface to implement a special behavior for Items (like a Switch Tab)
 *
 * @author Dennis Riegelbauer
 * @since 1.9.0
 *
 */
public interface OneWireDevicePropertyExecutableBindingConfig {

    public void execute(Command pvCommand);

}
