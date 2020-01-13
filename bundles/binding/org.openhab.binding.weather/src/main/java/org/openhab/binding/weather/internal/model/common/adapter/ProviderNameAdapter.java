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

import org.openhab.binding.weather.internal.model.ProviderName;

/**
 * JAXB Adapter to convert between a string and a ProviderName object.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ProviderNameAdapter extends XmlAdapter<String, ProviderName> {
    /**
     * {@inheritDoc}
     */
    @Override
    public String marshal(ProviderName providerName) throws Exception {
        return providerName == null ? null : providerName.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderName unmarshal(String providerName) throws Exception {
        return ProviderName.parse(providerName);
    }
}
