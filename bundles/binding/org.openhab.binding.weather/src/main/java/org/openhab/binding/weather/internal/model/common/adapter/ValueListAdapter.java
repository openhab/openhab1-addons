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
package org.openhab.binding.weather.internal.model.common.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.StringUtils;

/**
 * JAXB Adapter to convert a list of strings.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ValueListAdapter extends XmlAdapter<String, String[]> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] unmarshal(String value) throws Exception {
        String[] result = StringUtils.splitByWholeSeparatorPreserveAllTokens(value, ",");
        return (result.length == 0 ? null : result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String marshal(String[] values) throws Exception {
        return StringUtils.join(values, ", ");
    }

}
