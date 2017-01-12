/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
