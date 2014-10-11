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
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Reads the device types from an xml file.
 * 
 * @author Daniel Pfrommer
 * @author Bernd Pfrommer
 * @since 1.5.0
 */

public class DeviceTypeLoader {
	private static final Logger logger = LoggerFactory.getLogger(DeviceTypeLoader.class);
	private HashMap<String, DeviceType> m_deviceTypes = new HashMap<String, DeviceType>();
	private static DeviceTypeLoader s_deviceTypeLoader = null;
	
	private DeviceTypeLoader() {} // private so nobody can call it
	/**
	 * Finds the device type for a given product key
	 * @param aProdKey  product key to search for
	 * @return the device type, or null if not found
	 */
	public DeviceType getDeviceType(String aProdKey) {
		return (m_deviceTypes.get(aProdKey));
	}
	
	/**
	 * Must call loadDeviceTypesXML() before calling this function!
	 * @return currently known device types
	 */
	public HashMap<String, DeviceType> getDeviceTypes() { return (m_deviceTypes); }
	
	/**
	 * Reads the device types from input stream and stores them in memory for
	 * later access.
	 * @param is  the input stream from which to read 
	 */
	public void loadDeviceTypesXML(InputStream in) throws ParserConfigurationException, 
	SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(in);
		doc.getDocumentElement().normalize();
		Node root = doc.getDocumentElement();
		NodeList nodes = root.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node  = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE
					&& node.getNodeName().equals("device")) {
				processDevice((Element) node);
			}
		}
	}
	/**
	 * Reads the device types from file and stores them in memory for later access.
	 * @param aFileName The name of the file to read from
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void loadDeviceTypesXML(String aFileName) throws ParserConfigurationException, 
		SAXException, IOException {
		File file = new File(aFileName);
		InputStream in = new FileInputStream(file);
		loadDeviceTypesXML(in);
	}
	/**
	 * Process device node
	 * @param e name of the element to process
	 * @throws SAXException
	 */
	private void processDevice(Element e) throws SAXException {
		String productKey = e.getAttribute("productKey");
		if (productKey.equals("")) {
			throw new SAXException("device in device_types file has no product key!");
		}
		if (m_deviceTypes.containsKey(productKey)) {
			logger.warn("overwriting previous definition of device {}", productKey);
			m_deviceTypes.remove(productKey);
		}
		DeviceType devType = new DeviceType(productKey);
		
		NodeList nodes = e.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node  = nodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element subElement = (Element) node;
			if (subElement.getNodeName().equals("model")) {
				devType.setModel(subElement.getTextContent());
			} else if (subElement.getNodeName().equals("description")) {
				devType.setDescription(subElement.getTextContent());
			} else if (subElement.getNodeName().equals("feature")) {
				String name = subElement.getAttribute("name");
				if (name.equals("")) {
					throw new SAXException("device type " + productKey
								+ " has feature without name!");
				}
				devType.addFeature(name, subElement.getTextContent());
			}
			m_deviceTypes.put(productKey, devType);
		}
	}
	/**
	 * Helper function for debugging
	 */
	private void logDeviceTypes() {
		for (Entry<String, DeviceType> dt : getDeviceTypes().entrySet()) {
			logger.debug(String.format("%-10s->", dt.getKey()) + dt.getValue());
		}
	}

	/**
	 * Singleton instance function, creates DeviceTypeLoader
	 * @return DeviceTypeLoader singleton reference
	 */
	public static DeviceTypeLoader s_instance() {
		if (s_deviceTypeLoader == null) {
			s_deviceTypeLoader = new DeviceTypeLoader();
			InputStream input = DeviceTypeLoader.class.getResourceAsStream("/device_types.xml");
			try {
				s_deviceTypeLoader.loadDeviceTypesXML(input);
			} catch (ParserConfigurationException e) {
				logger.error("parser config error when reading device types xml file: ", e);
			} catch (SAXException e) {
				logger.error("SAX exception when reading device types xml file: ", e);
			} catch (IOException e) {
				logger.error("I/O exception when reading device types xml file: ", e);
			}
			logger.debug("loaded {} devices: ", s_deviceTypeLoader.getDeviceTypes().size());
			s_deviceTypeLoader.logDeviceTypes();
		}
		return s_deviceTypeLoader;
	}
	/**
	 * Test function for debugging
	 */
	public static void main(String[] arg) throws Exception {
		String fileName = System.getProperty("user.home") + 
				"/workspace/openhab/bundles/binding/org.openhab.binding.insteonplm/target/classes/device_types.xml";
		try  {
			DeviceTypeLoader dtl = s_instance();
			dtl.loadDeviceTypesXML(fileName);
			for (Entry<String, DeviceType> dt : dtl.getDeviceTypes().entrySet()) {
				System.out.println(String.format("%-10s ->", dt.getKey()) + dt.getValue());
			}
		} catch (SAXException e) {
			System.out.println("got exception: " + e);
		}
	}
}
