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
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openhab.binding.rwesmarthome.RWESmarthomeBindingProvider;
import org.openhab.binding.rwesmarthome.internal.RWESmarthomeContext;
import org.openhab.binding.rwesmarthome.internal.model.LogicalDevice;
import org.openhab.binding.rwesmarthome.internal.model.RoomTemperatureActuator;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class handles the response of the "GetAllLogicalDeviceStates" call, which
 * loads the current state of all logical devices.
 * 
 * @author ollie-dev
 *
 */
public class GetAllLogicalDeviceStatesXMLResponse extends XMLResponse {

	private static final Logger logger = LoggerFactory.getLogger(GetAllLogicalDeviceStatesXMLResponse.class);
	private RWESmarthomeContext context = RWESmarthomeContext.getInstance();
	
	/**
	 * Constructor with an input stream.
	 * 
	 * @param inputStream
	 */
	public GetAllLogicalDeviceStatesXMLResponse(InputStream is) {

		RWESmarthomeBindingProvider provider = context.getProviders().iterator().next();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			// parse using builder to get DOM representation of the XML file
			Document dom = db.parse(is);
			// get the root element
			Element docEle = dom.getDocumentElement();
			
			// LogicalDeviceStates
			NodeList nodelist = docEle.getElementsByTagName("LogicalDeviceState");
			if (nodelist != null && nodelist.getLength() > 0) {
				for (int i = 0; i < nodelist.getLength(); i++) {
					// LogicalDeviceState element
					Element element = (Element) nodelist.item(i);
					String deviceId = getTextValueFromAttribute(element, "LID");
					String deviceType = getTextValueFromAttribute(element, "xsi:type");
					
					// ROOMTEMPERATURESENSOR
					if(LogicalDevice.Type_RoomTemperatureSensorState.equals(deviceType)) {
						String itemName = provider.getItemNameByIdAndParam(deviceId, "temperature");
						if(itemName != null) {
							context.getEventPublisher().postUpdate(itemName, new DecimalType(getDoubleValueFromAttribute(element, "Temperature")));
							logger.debug("Updated item '{}' to '{}'", itemName, getDoubleValueFromAttribute(element, "Temperature"));
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "temperature");
						}
						
					// ROOMTEMPERATUREACTUATOR
					} else if(LogicalDevice.Type_RoomTemperatureActuatorState.equals(deviceType)) {
						// SETTEMPERATURE
						String itemName = provider.getItemNameByIdAndParam(deviceId, "settemperature");
						if(itemName != null) {
							State newState = new DecimalType(getDoubleValueFromAttribute(element, "PtTmp"));
							if(!isEcho(itemName, newState.format("%.1f").replace(",", "."))) {
								context.getEventPublisher().postUpdate(itemName, newState);
								logger.debug("Updated item '{}' to '{}'", itemName, newState);
							}
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "settemperature");
						}
						
						// OPERATIONMODEAUTO
						itemName = provider.getItemNameByIdAndParam(deviceId, "operationmodeauto");
						if(itemName != null) {
							State newState = getTextValueFromAttribute(element, "OpnMd").equals(RoomTemperatureActuator.OPERATION_MODE_AUTO) ? OnOffType.ON : OnOffType.OFF;
							if(!isEcho(itemName, newState.toString())) {
								context.getEventPublisher().postUpdate(itemName, newState);
								logger.debug("Updated item '{}' to '{}'", itemName, newState);
							}
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "operationmodeauto");
						}
					
					// SWITCHACTUATOR
					} else if(LogicalDevice.Type_SwitchActuatorState.equals(deviceType)) {
						String itemName = provider.getItemNameByIdAndParam(deviceId, "switch");
						if(itemName != null) {
							State newState = getBooleanValueFromAttribute(element, "IsOn") ? OnOffType.ON : OnOffType.OFF;
							if(!isEcho(itemName, newState.toString())) {
								context.getEventPublisher().postUpdate(itemName, newState);
								logger.debug("Updated item '{}' to '{}'", itemName, newState);
							}
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "switch");
						}
						
					// WINDOWDOORSENSOR
					} else if(LogicalDevice.Type_WindowDoorSensorState.equals(deviceType)) {
						String itemName = provider.getItemNameByIdAndParam(deviceId, "contact");
						if(itemName != null) {
							context.getEventPublisher().postUpdate(itemName, getBooleanValueFromElements(element, "IsOpen") ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
							logger.debug("Updated item '{}' to '{}'", itemName, getBooleanValueFromElements(element, "IsOpen"));
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "contact");
						}
						
					// LUMINANCESENSOR
					} else if(LogicalDevice.Type_LuminanceSensorState.equals(deviceType)) {
						String itemName = provider.getItemNameByIdAndParam(deviceId, "luminance");
						if(itemName != null) {
							context.getEventPublisher().postUpdate(itemName, new DecimalType(getIntValueFromElements(element, "Luminance")));
							logger.debug("Updated item '{}' to '{}'", itemName, getIntValueFromElements(element, "Luminance"));
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "luminance");
						}
						
					// HUMIDITYSENSOR
					} else if(LogicalDevice.Type_RoomHumiditySensorState.equals(deviceType)) {
						String itemName = provider.getItemNameByIdAndParam(deviceId, "humidity");
						if(itemName != null) {
							context.getEventPublisher().postUpdate(itemName, new DecimalType(getDoubleValueFromAttribute(element, "Humidity")));
							logger.debug("Updated item '{}' to '{}'", itemName, getDoubleValueFromAttribute(element, "Humidity"));
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "humidity");
						}
						
					// ROLLERSHUTTERACTUATOR
					} else if(LogicalDevice.Type_RollerShutterActuatorState.equals(deviceType)) {
						// ROLLERSHUTTER
						String itemName = provider.getItemNameByIdAndParam(deviceId, "rollershutter");
						if(itemName != null) {
							State newState = new PercentType(getIntValueFromElements(element, "ShutterLevel"));
							if(!isEcho(itemName, newState.toString())) {
								context.getEventPublisher().postUpdate(itemName, newState);
								logger.debug("Updated item '{}' to '{}'", itemName, newState);
							}
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "rollershutter");
						}
						
						// ROLLERSHUTTER INVERTED
						itemName = provider.getItemNameByIdAndParam(deviceId, "rollershutterinverted");
						if(itemName != null) {
							State newState = new PercentType(100-getIntValueFromElements(element, "ShutterLevel"));
							if(!isEcho(itemName, newState.toString())) {
								context.getEventPublisher().postUpdate(itemName, newState);
								logger.debug("Updated item '{}' to '{}'", itemName, newState);
							}
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "rollershutterinverted");
						}

						
					// DIMMERACTUATOR
					} else if(LogicalDevice.Type_DimmerActuatorState.equals(deviceType)) {
						// DIMMER
						String itemName = provider.getItemNameByIdAndParam(deviceId, "dimmer");
						if(itemName != null) {
							State newState = new PercentType(getIntValueFromAttribute(element, "DmLvl"));
							if(!isEcho(itemName, newState.toString())) {
								context.getEventPublisher().postUpdate(itemName, newState);
								logger.debug("Updated item '{}' to '{}'", itemName, newState);
							}
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "dimmer");
						}
						
						// DIMMER INVERTED
						itemName = provider.getItemNameByIdAndParam(deviceId, "dimmerinverted");
						if(itemName != null) {
							State newState = new PercentType(100-getIntValueFromAttribute(element, "DmLvl"));
							if(!isEcho(itemName, newState.toString())) {
								context.getEventPublisher().postUpdate(itemName, newState);
								logger.debug("Updated item '{}' to '{}'", itemName, newState);
							}
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "dimmerinverted");
						}
						
					// SMOKEDETECTORSENSOR
					} else if(LogicalDevice.Type_SmokeDetectionSensorState.equals(deviceType)) {
						String itemName = provider.getItemNameByIdAndParam(deviceId, "smokedetector");
						if(itemName != null) {
							context.getEventPublisher().postUpdate(itemName, getBooleanValueFromElements(element, "IsSmokeAlarm") ? new StringType("ON") : new StringType("OFF"));
							logger.debug("Updated item '{}' to '{}'", itemName, getBooleanValueFromElements(element, "IsSmokeAlarm"));
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "smokedetector");
						}						
						
					// ALARMACTUATOR
					} else if(LogicalDevice.Type_AlarmActuatorState.equals(deviceType)) {
						String itemName = provider.getItemNameByIdAndParam(deviceId, "alarm");
						if(itemName != null) {
							State newState = getBooleanValueFromElements(element, "IsOn") ? OnOffType.ON : OnOffType.OFF;
							if(!isEcho(itemName, newState.toString())) {
								context.getEventPublisher().postUpdate(itemName, newState);
								logger.debug("Updated item '{}' to '{}'", itemName, newState);
							}
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "alarm");
						}	
						
					// GENERICACTUATOR
					} else if(LogicalDevice.Type_GenericDeviceState.equals(deviceType)) {
						// load the properties
						NodeList pptnodes = element.getElementsByTagName("Ppt");
						HashMap<String,String> cache = new HashMap<String,String>();
						for (int j=0; j<pptnodes.getLength();j++) {
							String name = getTextValueFromAttribute((Element)pptnodes.item(j), "Name");
							String value = getTextValueFromAttribute((Element)pptnodes.item(j), "Value");
							cache.put(name,value);
						}
						
						// VARIABLE
						if(cache.get("Value") != null) {
							String itemName = provider.getItemNameByIdAndParam(deviceId, "variable");
							if(itemName != null) {
								State newState = "True".equals(cache.get("Value")) ? OnOffType.ON : OnOffType.OFF;
								if(!isEcho(itemName, newState.toString())) {
									context.getEventPublisher().postUpdate(itemName, newState);
									logger.debug("Updated item '{}' to '{}'", itemName, newState);
								}
							} else {
								logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "variable");
							}
						}						
					} else {
						logger.debug("Updated itemtype {} [{}] not supported - ignored.", deviceType, deviceId);
					}
					
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
	 * Checks if the update is just an echo of a command, we already sent to RWE.
	 * 
	 * @param itemName
	 * @param newState
	 * @return
	 */
	private boolean isEcho(String itemName, String newState) {
		String ignoreEventListKey = itemName + newState;

		// Check eventlist and remove all items older than 15 seconds
		for (String key : context.getIgnoreEventList().keySet()) {
			if ((System.currentTimeMillis() - context.getIgnoreEventList().get(key)) > 15000) {
				context.getIgnoreEventList().remove(key);
				logger.debug("Ignorelist: removed ghost entry '{}'", key);
			}
		}
		
		if (context.getIgnoreEventList().remove(ignoreEventListKey) != null) {
			logger.debug("We sent this event (item='{}', command='{}') to RWE, so we don't handle it again -> ignore!", itemName, newState);
			return true;
		} else {
			logger.debug("Event (item='{}', command='{}') is not an echo!", itemName, newState);
			return false;
		}
	}

}
