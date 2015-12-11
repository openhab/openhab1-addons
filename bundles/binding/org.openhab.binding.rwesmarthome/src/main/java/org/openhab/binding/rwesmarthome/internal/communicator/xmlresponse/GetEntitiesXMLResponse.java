/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal.communicator.xmlresponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.openhab.binding.rwesmarthome.internal.model.Location;
import org.openhab.binding.rwesmarthome.internal.model.LogicalDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class handles the response of the "GetEntities" call, which loads the
 * configuration of all RWE Smarthome entities (devices, locations, profiles...).
 * 
 * @author ollie-dev
 *
 */
public class GetEntitiesXMLResponse extends XMLResponse {

	private static final Logger logger = LoggerFactory.getLogger(GetEntitiesXMLResponse.class);
	
	private ConcurrentHashMap<String, Location> locations = null;
	private ConcurrentHashMap<String, LogicalDevice> logicalDevices = null;
	private String configurationVersion = null;
	
	/**
	 * Constructor with inputstream.
	 * 
	 * @param inputStream
	 */
	public GetEntitiesXMLResponse(InputStream is) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			Document dom = db.parse(is);
			// get the root element
			Element docEle = dom.getDocumentElement();
			
			// configurationVersion
			configurationVersion = getTextValueFromAttribute(docEle, "ConfigurationVersion");
			
			// Locations
			NodeList nlLocations = docEle.getElementsByTagName("LC");
			locations = new ConcurrentHashMap<String, Location>(5);
			if (nlLocations != null && nlLocations.getLength() > 0) {
				for (int i = 0; i < nlLocations.getLength(); i++) {
					Element locEl = (Element) nlLocations.item(i);
					Location loc = new Location(getTextValueFromElements(locEl, "Id"), getTextValueFromElements(locEl, "Name"));
					locations.put(loc.getId(), loc);
				}
			}
			
			// LogicalDevices
			NodeList nlLogicalDevices = docEle.getElementsByTagName("LD");
			logicalDevices = new ConcurrentHashMap<String, LogicalDevice>(5);
			if (nlLogicalDevices != null && nlLogicalDevices.getLength() > 0) {
				for (int i = 0; i < nlLogicalDevices.getLength(); i++) {
					Element logDevEl = (Element) nlLogicalDevices.item(i);

					LogicalDevice logDev = new LogicalDevice(
							getTextValueFromElements(logDevEl, "Id"),
							locations.get(getTextValueFromAttribute(logDevEl, "LCID")),
							getTextValueFromAttribute(logDevEl, "xsi:type"), 
							getTextValueFromAttribute(logDevEl, "Name")
						);
					
					if(LogicalDevice.Type_GenericActuator.equals(logDev.getType())) {
						if("Value".equals(getTextValueFromElements(logDevEl, "SDPpN")))
							logDev.setType(LogicalDevice.Type_GenericActuator_Value);
						else if("EmailNumberAvailable".equals(getTextValueFromElements(logDevEl, "SDPpN")))
							logDev.setType(LogicalDevice.Type_GenericActuator_Email);
						else if("SMSNumberAvailable".equals(getTextValueFromElements(logDevEl, "SDPpN")))
							logDev.setType(LogicalDevice.Type_GenericActuator_SMS);
						else if("NextTimeEvent".equals(getTextValueFromElements(logDevEl, "SDPpN")))
							logDev.setType(LogicalDevice.Type_GenericActuator_SunriseSunset);
					}
					logicalDevices.put(logDev.getId(), logDev);
				}
			}

		} catch (SAXException ex) {
			logger.debug(ex.getMessage(), ex);
		} catch (IOException ex) {
			logger.debug(ex.getMessage(), ex);
		} catch (ParserConfigurationException ex) {
			logger.debug(ex.getMessage(), ex);
		}
	}

	/**
	 * Returns a HashMap of locations (id, Location)
	 * 
	 * @return the location HashMap
	 */
	public ConcurrentHashMap<String, Location> getLocations() {
		return locations;
	}

	/**
	 * Returns a HashMap of LogicalDevices (id, LogicalDevice)
	 * @return the logicalDevices
	 */
	public ConcurrentHashMap<String, LogicalDevice> getLogicalDevices() {
		return logicalDevices;
	}

	/**
	 * Returns the version of the configuration.
	 * 
	 * @return
	 */
	public String getConfigurationVersion() {
		return configurationVersion;
	}
}
