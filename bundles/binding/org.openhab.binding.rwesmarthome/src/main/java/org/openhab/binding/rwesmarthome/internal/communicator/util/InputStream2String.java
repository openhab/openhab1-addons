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
package org.openhab.binding.rwesmarthome.internal.communicator.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

/**
 * Helperclass to convert an inputstream to a string.
 *
 * @author ollie-dev
 *
 */
public class InputStream2String {

    /**
     * Copies an inputstream to the returned string.
     *
     * @param in
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String copyFromInputStream(InputStream in, String encoding) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(in, writer, encoding);
        String outString = writer.toString();
        return outString;
    }
}
