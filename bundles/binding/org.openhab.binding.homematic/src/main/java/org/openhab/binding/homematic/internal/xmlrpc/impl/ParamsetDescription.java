/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.homematic.internal.xmlrpc.AbstractXmlRpcObject;

/**
 * A ParamsetDescription contains one ParameterDescription for each attribute in
 * a Paramset.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class ParamsetDescription extends AbstractXmlRpcObject {

    private Map<String, ParameterDescription> params;

    @SuppressWarnings("unchecked")
    public ParamsetDescription(Map<String, Object> values) {
        super(values);

        params = new HashMap<String, ParameterDescription>();

        for (String key : values.keySet()) {
            Map<String, Object> map = (Map<String, Object>) values.get(key);
            ParameterDescription descr = new ParameterDescription(map);
            params.put(key, descr);
        }

    }

    public ParameterDescription getParameterDescription(String parameter) {
        if (parameter == null) {
            throw new IllegalArgumentException("parameter must no be null");
        }
        return params.get(parameter);
    }

    public Set<String> getParameters() {
        return new HashSet<String>(params.keySet());
    }

    @Override
    public String toString() {
        return "ParamsetDescription [params=" + params + "]";
    }

}
