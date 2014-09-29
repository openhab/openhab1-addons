/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openhab.binding.insteonplm.internal.utils.Pair;
import org.openhab.binding.insteonplm.internal.utils.Utils.DataTypeParser;
import org.openhab.binding.insteonplm.internal.utils.Utils.ParsingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
* Reads the Msg definitions from an XML file
* 
* @author Daniel Pfrommer
* @since 1.5.0
*/

public class XMLMessageReader {
	/**
	 * Reads the message definitions from an xml file
	 * @param input input stream from which to read
	 * @return what was read from file: the map between clear text string and Msg objects
	 * @throws IOException couldn't read file etc
	 * @throws ParsingException something wrong with the file format
	 * @throws FieldException something wrong with the field definition
	 */
	public static HashMap<String, Msg> s_readMessageDefinitions(InputStream input)
				throws IOException, ParsingException, FieldException {
		HashMap<String, Msg> messageMap = new HashMap<String, Msg>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//Parse it!
			Document doc = dBuilder.parse(input);
			doc.getDocumentElement().normalize();

			Node root = doc.getDocumentElement();

			NodeList nodes = root.getChildNodes();

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node  = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (node.getNodeName().equals("msg")) {
						Pair<String, Msg> msgDef = s_readMessageDefinition((Element) node);
						messageMap.put(msgDef.getKey(), msgDef.getValue());
					}
				}
			}
		} catch (SAXException e) {
			throw new ParsingException("Failed to parse XML!", e);
		} catch (ParserConfigurationException e) {
			throw new ParsingException("Got parser config exception! ", e);
		}
		return messageMap;
	}
	
	private static Pair<String, Msg> s_readMessageDefinition(Element msg)
				throws FieldException, ParsingException {
		int length	= 0;
		int hlength	= 0;
		LinkedHashMap<Field, Object> fieldMap = new LinkedHashMap<Field, Object>();
		String dir	= msg.getAttribute("direction");
		String name = msg.getAttribute("name");
		Msg.Direction direction = Msg.Direction.s_getDirectionFromString(dir);
		
		if (msg.hasAttribute("length")) length = Integer.parseInt(msg.getAttribute("length"));
		
		NodeList nodes = msg.getChildNodes();
		
		int offset = 0;
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node  = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals("header")) {
					int o = s_readHeaderElement((Element) node, fieldMap);
					hlength = o;
					//Increment the offset by the header length
					offset += o;
				} else {
					Pair<Field, Object> field = s_readField((Element) node, offset);
					fieldMap.put(field.getKey(), field.getValue());
					//Increment the offset
					offset += field.getKey().getType().getSize();
				}
			}
		}
		if (offset != length) {
			throw new ParsingException(
				"Actual msg length " + offset + " differs from given msg length " + length + "!");
		}
		if (length == 0) length = offset;
		
		return new Pair<String, Msg>(name, s_createMsg(fieldMap, length, hlength, direction));
	}
	
	private static int s_readHeaderElement(Element header, LinkedHashMap<Field, Object> fields)
				throws ParsingException {		
		int offset = 0;
		int headerLen = Integer.parseInt(header.getAttribute("length"));
		
		NodeList nodes = header.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node  = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Pair<Field, Object> definition = s_readField((Element) node, offset);
				if (definition != null) {
					offset += definition.getKey().getType().getSize();
					fields.put(definition.getKey(),  definition.getValue());
				}
			}
		}
		if (headerLen != offset) {
			throw new ParsingException("Actual header length " + offset + " differs from given length " + headerLen + "!");
		}
		return headerLen;
	}

	private static Pair<Field, Object> s_readField(Element field, int offset) {
		DataType dType = DataType.s_getDataType(field.getTagName());
		//Will return blank if no name attribute
		String name = field.getAttribute("name");
		Field f = new Field(name, dType, offset);
		//Now we have field, only need value
		String sVal = field.getTextContent();
		Object val = DataTypeParser.s_parseDataType(dType, sVal);
		Pair<Field, Object> pair = new Pair<Field, Object>(f, val);
		return pair;
	}
	
	private static Msg s_createMsg(HashMap<Field, Object> values, int length, int headerLength, Msg.Direction dir) throws FieldException {
		Msg msg = new Msg(headerLength, new byte[length], length, dir);
		for (Entry<Field,Object> e : values.entrySet()) {
			Field f = e.getKey();
			f.set(msg.getData(), e.getValue());
			if (f.getName() != null && !f.getName().equals("")) msg.addField(f);
		}
		return msg;
	}
	

	public static void main(String[] args) throws Exception {
		// for local testing
		File f = new File(System.getProperty("user.home") + 
		"/workspace/openhab/bundles/binding/org.openhab.binding.insteonplm/src/main/resources/msg_definitions.xml");
		InputStream s = new FileInputStream(f);
		HashMap<String, Msg> msgs = XMLMessageReader.s_readMessageDefinitions(s);
		for (Msg msg : msgs.values()) {
			System.out.println(msg);
		}
	}
}
