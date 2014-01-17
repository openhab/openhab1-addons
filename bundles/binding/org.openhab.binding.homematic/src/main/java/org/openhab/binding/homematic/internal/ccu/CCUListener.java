/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.ccu;

import org.openhab.binding.homematic.internal.device.HMDevice;

/**
 * To listen to CCU events.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public interface CCUListener {

    public void deviceAdded(HMDevice device);

    public void deviceRemoved(HMDevice device);

}
