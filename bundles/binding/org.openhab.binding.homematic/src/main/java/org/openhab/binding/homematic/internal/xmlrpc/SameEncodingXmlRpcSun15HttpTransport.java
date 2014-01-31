/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc;

import java.io.IOException;
import java.io.InputStream;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransport;
import org.apache.xmlrpc.common.XmlRpcStreamRequestConfig;
import org.apache.xmlrpc.parser.XmlRpcResponseParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * The SameEncodingXmlRpcSun15HttpTransport uses the same encoding in the
 * request as the response.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 */
public class SameEncodingXmlRpcSun15HttpTransport extends XmlRpcSun15HttpTransport {

    public SameEncodingXmlRpcSun15HttpTransport(XmlRpcClient pClient) {
        super(pClient);
    }

    @Override
    protected Object readResponse(XmlRpcStreamRequestConfig pConfig, InputStream pStream) throws XmlRpcException {
        InputSource isource = new InputSource(pStream);
        isource.setEncoding(pConfig.getEncoding());
        XMLReader xr = newXMLReader();
        XmlRpcResponseParser xp;
        try {
            xp = new XmlRpcResponseParser(pConfig, getClient().getTypeFactory());
            xr.setContentHandler(xp);
            xr.parse(isource);
        } catch (SAXException e) {
            throw new XmlRpcClientException("Failed to parse server's response: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new XmlRpcClientException("Failed to read server's response: " + e.getMessage(), e);
        }
        if (xp.isSuccess()) {
            return xp.getResult();
        }
        Throwable t = xp.getErrorCause();
        if (t == null) {
            throw new XmlRpcException(xp.getErrorCode(), xp.getErrorMessage());
        }
        if (t instanceof XmlRpcException) {
            throw (XmlRpcException) t;
        }
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }
        throw new XmlRpcException(xp.getErrorCode(), xp.getErrorMessage(), t);
    }

}
