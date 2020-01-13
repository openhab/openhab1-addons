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
package org.openhab.binding.homematic.internal.model.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.StringUtils;

/**
 * JAXB Adapter to trim a string value to null.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */

public class TrimToNullStringAdapter extends XmlAdapter<String, Object> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object unmarshal(String value) throws Exception {
        return StringUtils.trimToNull(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String marshal(Object value) throws Exception {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

}
