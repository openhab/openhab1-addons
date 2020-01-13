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

import org.openhab.binding.homematic.internal.model.HmInterface;

/**
 * JAXB Adapter to convert between a string and a HmInterface object.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class HmInterfaceAdapter extends XmlAdapter<String, HmInterface> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String marshal(HmInterface interfaceType) throws Exception {
        return interfaceType == null ? null : interfaceType.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HmInterface unmarshal(String interfaceType) throws Exception {
        return HmInterface.parse(interfaceType);
    }

}
