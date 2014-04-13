/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.protocol;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ByteAdapter extends XmlAdapter<String, Byte> {

    @Override
    public Byte unmarshal(String v) throws Exception {
        return DatatypeConverter.parseHexBinary(v)[0];
    }

    @Override
    public String marshal(Byte v) throws Exception {
        return DatatypeConverter.printHexBinary(new byte[] {v});
    }

}
