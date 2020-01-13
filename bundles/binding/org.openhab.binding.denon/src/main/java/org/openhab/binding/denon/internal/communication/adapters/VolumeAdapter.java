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
package org.openhab.binding.denon.internal.communication.adapters;

import java.math.BigDecimal;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Maps Denon volume values in db to percentage
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class VolumeAdapter extends XmlAdapter<String, BigDecimal> {

    private static final BigDecimal OFFSET = new BigDecimal("80");

    @Override
    public BigDecimal unmarshal(String v) throws Exception {
        if (v != null && !v.trim().equals("--")) {
            return new BigDecimal(v).add(OFFSET);
        }

        return BigDecimal.ZERO;
    }

    @Override
    public String marshal(BigDecimal v) throws Exception {
        if (v.equals(BigDecimal.ZERO)) {
            return "--";
        }

        return v.subtract(OFFSET).toString();
    }
}
