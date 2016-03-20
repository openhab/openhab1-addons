/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/***
 * Static Helper methods
 *
 * @author gitbock
 * @since 1.8.0
 */
public class Helper {
    private static final Logger logger = LoggerFactory.getLogger(FritzboxTr064Binding.class);

    /***
     * Helper method which converts XML Document into pretty formatted string
     *
     * @param doc to convert
     * @return converted XML as String
     */
    public static String documentToString(Document doc) {

        String strMsg = "";
        try {
            DOMImplementation domImpl = doc.getImplementation();
            DOMImplementationLS domImplLS = (DOMImplementationLS) domImpl.getFeature("LS", "3.0");
            LSSerializer lsSerializer = domImplLS.createLSSerializer();
            lsSerializer.getDomConfig().setParameter("format-pretty-print", true);

            Writer stringWriter = new StringWriter();
            LSOutput lsOutput = domImplLS.createLSOutput();
            lsOutput.setEncoding("UTF-8");
            lsOutput.setCharacterStream(stringWriter);
            lsSerializer.write(doc, lsOutput);
            strMsg = stringWriter.toString();
        } catch (Exception e) {
            logger.warn("Error occured when converting document to string", e);
        }
        return strMsg;
    }

    /***
     * Converts a xml Node into String
     *
     * @param node to convert
     * @return converted string
     */
    public static String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            logger.warn("nodeToString Transformer Exception", te);
        }
        return sw.toString();
    }

}
