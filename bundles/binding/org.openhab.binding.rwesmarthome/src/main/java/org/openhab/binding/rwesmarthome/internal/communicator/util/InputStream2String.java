/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
