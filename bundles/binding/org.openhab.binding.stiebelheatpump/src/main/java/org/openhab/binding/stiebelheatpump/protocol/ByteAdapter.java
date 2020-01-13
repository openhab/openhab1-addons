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
package org.openhab.binding.stiebelheatpump.protocol;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * ByteAdapter class used for conversion of bytes during xml parsing
 *
 * @author Peter Kreutzer
 */
public class ByteAdapter extends XmlAdapter<String, Byte> {

    @Override
    public Byte unmarshal(String v) throws Exception {
        return DatatypeConverter.parseHexBinary(v)[0];
    }

    @Override
    public String marshal(Byte v) throws Exception {
        return DatatypeConverter.printHexBinary(new byte[] { v });
    }

}
