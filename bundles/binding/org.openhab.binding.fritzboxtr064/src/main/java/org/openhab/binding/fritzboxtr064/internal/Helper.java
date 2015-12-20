/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;


/***
 * Static Helper methods
 * 
 * @author gitbock
 * @since 1.8.0
 *
 */
public class Helper {
	
	
	/***
	 * Helper method which converts XML Document into pretty formatted string
	 * @param doc to convert
	 * @return converted XML as String
	 */
	public static String documentToString(Document doc){
		String strMsg ="";
		OutputFormat format = new OutputFormat(doc);
        format.setIndenting(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLSerializer serializer = new XMLSerializer(baos, format);
        try {
			serializer.serialize(doc);
			strMsg = baos.toString("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strMsg;
	}
	
	
	/***
	 * Converts  a xml Node into String
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
		    System.out.println("nodeToString Transformer Exception");
		  }
		  return sw.toString();
		}
	

}
