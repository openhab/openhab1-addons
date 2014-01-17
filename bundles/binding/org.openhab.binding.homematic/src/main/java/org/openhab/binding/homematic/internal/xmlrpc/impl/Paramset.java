/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc.impl;

import java.util.Map;

import org.openhab.binding.homematic.internal.xmlrpc.AbstractXmlRpcObject;

/**
 * Paramset represent attribute names and their values as defined by a device.
 * They are used to access information on the device and to interact with the
 * device (e.g. turn switch on and off). The values can the accessed directly
 * through the getValue and setValue methods provided by the XML-RPC interface.
 * 
 * The values defined by a Paramset vary greatly from device to device, so no
 * better getter methods can be provided. Paramset are only supposed to be used
 * by their respective devices and such devices should know the exact names of
 * the attributes they want to access.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class Paramset extends AbstractXmlRpcObject {

    public Paramset(Map<String, Object> values) {
        super(values);
    }

    @Override
    public String toString() {
        return "Paramset [getValues()=" + getValues() + "]";
    }

}
