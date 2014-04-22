/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.insteonplm.internal.device;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Reads the device categories and sub categories from an xml file.
 * 
 * @author Daniel Pfrommer
 * @since 1.5.0
 */

public class DeviceCategoryLoader {
	
	/**
	 * Reads the device categories from file and stores them in memory for
	 * later access.
	 * @param is  the input stream from which to read the file 
	 */
	public static void loadCategoryDefinitionsXML(InputStream is) throws ParserConfigurationException, 
																	SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		doc.getDocumentElement().normalize();
		Node root = doc.getDocumentElement();
		NodeList nodes = root.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node  = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) processElement((Element) node);
		}
	}

	private static void processElement(Element e) {
		if (e.getNodeName().equals("category")) processCategory(e);
	}
	private static void processProductKey(Element e, DeviceSubCategory subCat) {
		String productKey = e.getAttribute("name");
		subCat.addProductKey(productKey);
		// now loop through all the features
		NodeList nodes = e.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node  = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element subElement = (Element) node;
				if (subElement.getNodeName().equals("feature")) {
					processFeature(productKey, subElement, subCat);
				}
			}
		}
	}
	private static void processFeature(String productKey, Element e, DeviceSubCategory subCat) {
		String name = e.getAttribute("name");
		if (!name.equals("")) {
			subCat.addFeature(productKey, name, e.getTextContent());
		}
	}
	
		
	private static void processSubcategory(Element e, DeviceCategory cat) {
		DeviceSubCategory subCategory = new DeviceSubCategory(0xff);
		NodeList nodes = e.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node  = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element subElement = (Element) node;

				if (subElement.getNodeName().equals("name")) {
					subCategory.setName(subElement.getTextContent());
				} else if (subElement.getNodeName().equals("description") ||
						subElement.getNodeName().equals("desc")) {
					subCategory.setDesc(subElement.getTextContent());
				} else if (subElement.getNodeName().equals("productKey")) {
					processProductKey(subElement, subCategory);
				} else if (subElement.getNodeName().equals("subCat")) {
					subCategory.setSubCat(parseHex(subElement.getTextContent()));
				}
			}
		}
		subCategory.setCategory(cat);
		DeviceSubCategory.addSubCategory(subCategory);
	}
		
	private static void processCategory(Element e) {
		DeviceCategory category = new DeviceCategory();
		
		NodeList nodes = e.getChildNodes();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node  = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element subElement = (Element) node;
				
				if (subElement.getNodeName().equals("name")) {
					category.setName(subElement.getTextContent());
				} else if (subElement.getNodeName().equals("description") ||
							subElement.getNodeName().equals("desc")) {
					category.setDesc(subElement.getTextContent());
				} else if (subElement.getNodeName().equals("devCat")) {
					category.setDevCat(parseHex(subElement.getTextContent()));
				} else if (subElement.getNodeName().equals("subcategory")) {
					processSubcategory(subElement, category);
				}
			}
		}
		DeviceCategory.addCategory(category);
	}
	
	private static int parseHex(String hex) {
		hex = hex.trim();
		if (hex != null && hex.length() > 0) {
			if (hex.startsWith("0x")) {
				hex = hex.substring(2);
			}
			int value = Integer.parseInt(hex, 16);
			return value;
		}
		return 0xFF;
	}
	/**
	 * Test function for debugging
	 */
	public static void main(String[] arg) throws Exception {
		File file = new File(System.getProperty("user.home") + 
				"/workspace/openhab/bundles/binding/org.openhab.binding.insteonplm/target/classes/categories.xml");
		InputStream in = new FileInputStream(file);
		DeviceCategoryLoader.loadCategoryDefinitionsXML(in);
	}
}
