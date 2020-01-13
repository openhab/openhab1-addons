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
package org.openhab.binding.ihc.ws.datatypes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.openhab.binding.ihc.ws.IhcExecption;
import org.xml.sax.InputSource;

/**
 * Base data class for all controllers data values.
 *
 * @author Pauli Anttila
 * @since 1.5.0
 */
public abstract class WSBaseDataType {

    static public String parseValue(String xml, String xpathExpression) throws IhcExecption {
        InputStream is;
        try {
            is = new ByteArrayInputStream(xml.getBytes("UTF8"));
        } catch (UnsupportedEncodingException e) {
            throw new IhcExecption(e);
        }

        XPath xpath = XPathFactory.newInstance().newXPath();
        InputSource inputSource = new InputSource(is);

        xpath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if (prefix == null) {
                    throw new NullPointerException("Null prefix");
                } else if ("SOAP-ENV".equals(prefix)) {
                    return "http://schemas.xmlsoap.org/soap/envelope/";
                } else if ("ns1".equals(prefix)) {
                    return "utcs";
                } else if ("ns2".equals(prefix)) {
                    return "utcs.values";
                }
                return null;
            }

            @Override
            public String getPrefix(String uri) {
                return null;
            }

            @Override
            @SuppressWarnings("rawtypes")
            public Iterator getPrefixes(String uri) {
                throw new UnsupportedOperationException();
            }
        });

        try {
            return (String) xpath.evaluate(xpathExpression, inputSource, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            throw new IhcExecption(e);
        }
    }

    public boolean parseValueToBoolean(String xml, String xpathExpression) throws IhcExecption {
        return Boolean.parseBoolean(parseValue(xml, xpathExpression));
    }

    public abstract void encodeData(String data) throws IhcExecption;

}
