/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
 * 
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
