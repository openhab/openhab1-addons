/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc.callback;

/**
 * CallbackReceiver defines those methods invoked by a Homematic CCU on a
 * registered XML-RPC server. Any object that wants to receive those requests
 * has to implement this interface an must be registered at the respective
 * instance of CallHandler.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public interface CallbackReceiver {

    public Integer event(String interfaceId, String address, String parameterKey, Object value);

    public Object[] listDevices(String interfaceId);

    public Integer newDevices(String interfaceId, Object[] deviceDescriptions);

    public Integer deleteDevices(String interfaceId, Object[] addresses);

    public Integer updateDevice(String interfaceId, String address, Integer hint);

}
