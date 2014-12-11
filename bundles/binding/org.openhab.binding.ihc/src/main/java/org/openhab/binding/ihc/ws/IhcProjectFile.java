/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.ws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class to store controller's project file information.
 * 
 * @author Pauli Anttila
 * @since 1.5.0
 */
public class IhcProjectFile {

	private static final Logger logger = LoggerFactory.getLogger(IhcProjectFile.class);

	static HashMap<Integer, ArrayList<IhcEnumValue>> parseProject(String filePath, String dumpResourcesToFile) throws IhcExecption {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(filePath));
			return parseProject(doc, dumpResourcesToFile);

		} catch (ParserConfigurationException e) {
			throw new IhcExecption(e);
		} catch (SAXException e) {
			throw new IhcExecption(e);
		} catch (IOException e) {
			throw new IhcExecption(e);
		}
	}

	/**
	 * Parse IHC / ELKO LS project file.
	 * 
	 */
	static HashMap<Integer, ArrayList<IhcEnumValue>> parseProject(Document doc, String dumpResourcesToFile) {

		logger.debug("Parsing project file...");

		HashMap<Integer, ArrayList<IhcEnumValue>> enumDictionary = new HashMap<Integer, ArrayList<IhcEnumValue>>();

		NodeList nodes = doc.getElementsByTagName("enum_definition");

		// iterate enum definitions from project

		for (int i = 0; i < nodes.getLength(); i++) {

			Element element = (Element) nodes.item(i);

			// String enumName = element.getAttribute("name");
			int typedefId = Integer.parseInt(element.getAttribute("id")
					.replace("_0x", ""), 16);

			ArrayList<IhcEnumValue> enumValues = new ArrayList<IhcEnumValue>();

			NodeList name = element.getElementsByTagName("enum_value");

			for (int j = 0; j < name.getLength(); j++) {
				Element val = (Element) name.item(j);
				IhcEnumValue enumVal = new IhcEnumValue();
				enumVal.id = Integer.parseInt(val.getAttribute("id")
						.replace("_0x", ""), 16);
				enumVal.name = val.getAttribute("name");

				enumValues.add(enumVal);
			}

			enumDictionary.put(typedefId, enumValues);
		}
		
		if (StringUtils.isNotBlank(dumpResourcesToFile)) {
			IhcProjectFile.parseResources(doc, dumpResourcesToFile);
		}
		
		return enumDictionary;

	}

	/**
	 * Parse resources from IHC / ELKO LS project file.
	 * 
	 */
	static void parseResources(Document doc, String fileName) {

		logger.debug("Parsing resources from project file...");

		String val = "";
		
		val += nodeListToString(doc.getElementsByTagName("dataline_input"), "dataline_input");
		val += nodeListToString(doc.getElementsByTagName("dataline_output"), "dataline_output");
		
		val += nodeListToString(doc.getElementsByTagName("airlink_input"), "airlink_input");
		val += nodeListToString(doc.getElementsByTagName("airlink_output"), "airlink_output");
		val += nodeListToString(doc.getElementsByTagName("airlink_dimming"), "airlink_dimming");
		
		val += nodeListToString(doc.getElementsByTagName("resource_temperature"), "resource_temperature");
		val += nodeListToString(doc.getElementsByTagName("resource_flag"), "resource_flag");
		val += nodeListToString(doc.getElementsByTagName("resource_timer"), "resource_timer");
		val += nodeListToString(doc.getElementsByTagName("resource_counter"), "resource_counter");
		val += nodeListToString(doc.getElementsByTagName("resource_weekday"), "resource_weekday");
		val += nodeListToString(doc.getElementsByTagName("resource_light_level"), "resource_light_level");
		val += nodeListToString(doc.getElementsByTagName("resource_integer"), "resource_integer");
		val += nodeListToString(doc.getElementsByTagName("resource_time"), "resource_time");
		val += nodeListToString(doc.getElementsByTagName("resource_date"), "resource_date");
		val += nodeListToString(doc.getElementsByTagName("resource_scene"), "resource_scene");
		val += nodeListToString(doc.getElementsByTagName("resource_enum"), "resource_enum");
		
		try {
			File file = new File(fileName);
			logger.info("Saving IHC resource info to file '{}'", file.getAbsolutePath());
			
			PrintWriter out = new PrintWriter(file);
			out.println(val);
			out.close();
			
		} catch (FileNotFoundException e) {
			logger.warn("Unable to write IHC resources to file", e);
		}
	}

	static private String nodeListToString( NodeList nodes, String header) {
		
		String val = "";
		
		for (int i = 0; i < nodes.getLength(); i++) {

			Element element = (Element) nodes.item(i);
			Element parent = (Element) nodes.item(i).getParentNode();
			Element parentParent = (Element) nodes.item(i).getParentNode().getParentNode();
			
			String parentName = parent.getAttribute("name");
			String parentPosition = parent.getAttribute("position");
			String parentParentName = parentParent.getAttribute("name");
			
			String resourceName = element.getAttribute("name");
			String resourceId = element.getAttribute("id").replace("_", "");
			
			val += resourceId;
			val += " = " + header;
			val += " -> " + parentParentName;
			val += " -> " + parentPosition;
			val += " -> " + parentName;
			val += " -> " + resourceName;
			val += "\n";
		}
		
		return val;
	}	

}
