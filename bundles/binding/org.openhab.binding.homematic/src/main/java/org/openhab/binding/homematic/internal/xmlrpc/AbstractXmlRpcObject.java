/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc;

import java.util.Map;

/**
 * This is an abstract implementation of XmlRpcObject. It implements both
 * methods required by the interfaces and stores the Map itself.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class AbstractXmlRpcObject implements XmlRpcObject {

    private Map<String, Object> values;

    public AbstractXmlRpcObject(Map<String, Object> values) {
        if (values == null) {
            throw new IllegalArgumentException("values must no be null");
        }
        this.values = values;
    }

    /**
     * Returns the values for the given attribute name.
     */
    @Override
    public Object getValue(String attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("attribute must no be null");
        }
        return values.get(attribute);
    }

    /**
     * Returns a copy of the actual values map so that the values cannot be
     * changed by the caller.
     */
    @Override
    public Map<String, Object> getValues() {
        return values;
    }

}
