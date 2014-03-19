/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.ccu;

import java.util.Set;

import org.openhab.binding.homematic.internal.device.physical.HMPhysicalDevice;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnection;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackReceiver;

/**
 * This interface defines the main interface for developers using this API. The
 * complete functionality of a CCU as usable by this API is defined here.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public interface CCU<T extends HMPhysicalDevice> extends CallbackReceiver {

    public XmlRpcConnection getConnection();

    public T getPhysicalDevice(String address);

    public Set<T> getPhysicalDevices();

    public <S extends T> Set<S> getPhysicalDevices(Class<S> clazz);

    public void addCCUListener(CCUListener l);

    public void removeCCUListener(CCUListener l);

}
