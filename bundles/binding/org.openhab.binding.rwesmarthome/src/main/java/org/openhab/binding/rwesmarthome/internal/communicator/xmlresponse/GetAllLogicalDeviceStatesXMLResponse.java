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
import org.openhab.core.items.Item;
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
						Item item = provider.getItemByIdAndParam(deviceId, "temperature");
						if(item != null) {
							context.getEventPublisher().postUpdate(item.getName(), new DecimalType(getDoubleValueFromAttribute(element, "Temperature")));
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "temperature");
						}
						
					// ROOMTEMPERATUREACTUATOR
					} else if(LogicalDevice.Type_RoomTemperatureActuatorState.equals(deviceType)) {
						// SETTEMPERATURE
						Item item = provider.getItemByIdAndParam(deviceId, "settemperature");
						if(item != null) {
							State newState = new DecimalType(getDoubleValueFromAttribute(element, "PtTmp"));
							if(!isEcho(item.getName(), newState.format("%.1f").replace(",", "."))) 
								context.getEventPublisher().postUpdate(item.getName(), newState);
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "settemperature");
						}
						
						// OPERATIONMODEAUTO
						item = provider.getItemByIdAndParam(deviceId, "operationmodeauto");
						if(item != null) {
							State newState = getTextValueFromAttribute(element, "OpnMd").equals(RoomTemperatureActuator.OPERATION_MODE_AUTO) ? OnOffType.ON : OnOffType.OFF;
							if(!isEcho(item.getName(), newState.toString())) {
								context.getEventPublisher().postUpdate(item.getName(), newState);
							}
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "operationmodeauto");
						}
					
					// SWITCHACTUATOR
					} else if(LogicalDevice.Type_SwitchActuatorState.equals(deviceType)) {
						Item item = provider.getItemByIdAndParam(deviceId, "switch");
						if(item != null) {
							State newState = getBooleanValueFromAttribute(element, "IsOn") ? OnOffType.ON : OnOffType.OFF;
							if(!isEcho(item.getName(), newState.toString()))
								context.getEventPublisher().postUpdate(item.getName(), newState);
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "switch");
						}
						
					// WINDOWDOORSENSOR
					} else if(LogicalDevice.Type_WindowDoorSensorState.equals(deviceType)) {
						Item item = provider.getItemByIdAndParam(deviceId, "contact");
						if(item != null) {
							context.getEventPublisher().postUpdate(item.getName(), getBooleanValueFromElements(element, "IsOpen") ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "contact");
						}
						
					// LUMINANCESENSOR
					} else if(LogicalDevice.Type_LuminanceSensorState.equals(deviceType)) {
						Item item = provider.getItemByIdAndParam(deviceId, "luminance");
						if(item != null) {
							context.getEventPublisher().postUpdate(item.getName(), new DecimalType(getIntValueFromElements(element, "Luminance")));
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "luminance");
						}
						
					// HUMIDITYSENSOR
					} else if(LogicalDevice.Type_RoomHumiditySensorState.equals(deviceType)) {
						Item item = provider.getItemByIdAndParam(deviceId, "humidity");
						if(item != null) {
							context.getEventPublisher().postUpdate(item.getName(), new DecimalType(getDoubleValueFromAttribute(element, "Humidity")));
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "humidity");
						}
						
					// ROLLERSHUTTERACTUATOR
					} else if(LogicalDevice.Type_RollerShutterActuatorState.equals(deviceType)) {
						// ROLLERSHUTTER
						Item item = provider.getItemByIdAndParam(deviceId, "rollershutter");
						if(item != null) {
							State newState = new PercentType(getIntValueFromElements(element, "ShutterLevel"));
							if(!isEcho(item.getName(), newState.toString()))
								context.getEventPublisher().postUpdate(item.getName(), newState);
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "rollershutter");
						}
						
						// ROLLERSHUTTER INVERTED
						item = provider.getItemByIdAndParam(deviceId, "rollershutterinverted");
						if(item != null) {
							State newState = new PercentType(100-getIntValueFromElements(element, "ShutterLevel"));
							if(!isEcho(item.getName(), newState.toString()))
								context.getEventPublisher().postUpdate(item.getName(), newState);
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "rollershutterinverted");
						}

						
					// DIMMERACTUATOR
					} else if(LogicalDevice.Type_DimmerActuatorState.equals(deviceType)) {
						// DIMMER
						Item item = provider.getItemByIdAndParam(deviceId, "dimmer");
						if(item != null) {
							State newState = new PercentType(getIntValueFromAttribute(element, "DmLvl"));
							if(!isEcho(item.getName(), newState.toString()))
								context.getEventPublisher().postUpdate(item.getName(), newState);
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "dimmer");
						}
						
						// DIMMER INVERTED
						item = provider.getItemByIdAndParam(deviceId, "dimmerinverted");
						if(item != null) {
							State newState = new PercentType(100-getIntValueFromAttribute(element, "DmLvl"));
							if(!isEcho(item.getName(), newState.toString()))
								context.getEventPublisher().postUpdate(item.getName(), newState);
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "dimmerinverted");
						}
						
					// SMOKEDETECTORSENSOR
					} else if(LogicalDevice.Type_SmokeDetectionSensorState.equals(deviceType)) {
						Item item = provider.getItemByIdAndParam(deviceId, "smokedetector");
						if(item != null) {
							context.getEventPublisher().postUpdate(item.getName(), getBooleanValueFromElements(element, "IsSmokeAlarm") ? new StringType("ON") : new StringType("OFF"));
						} else {
							logger.debug("Updated item {} [{},{}] ignored.", deviceType, deviceId, "smokedetector");
						}						
						
					// ALARMACTUATOR
					} else if(LogicalDevice.Type_AlarmActuatorState.equals(deviceType)) {
						Item item = provider.getItemByIdAndParam(deviceId, "alarm");
						if(item != null) {
							State newState = getBooleanValueFromElements(element, "IsOn") ? OnOffType.ON : OnOffType.OFF;
							if(!isEcho(item.getName(), newState.toString()))
								context.getEventPublisher().postUpdate(item.getName(), newState);
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
							Item item = provider.getItemByIdAndParam(deviceId, "variable");
							if(item != null) {
								State newState = "True".equals(cache.get("Value")) ? OnOffType.ON : OnOffType.OFF;
								if(!isEcho(item.getName(), newState.toString()))
									context.getEventPublisher().postUpdate(item.getName(), newState);
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
		if (context.getIgnoreEventList().remove(ignoreEventListKey)) {
			logger.debug("We sent this event (item='{}', command='{}') to RWE, so we don't handle it again -> ignore!", itemName, newState);
//			context.getIgnoreEventList().
			return true;
		}
		else {
			logger.debug("Event (item='{}', command='{}') is not an echo!", itemName, newState);
			return false;
		}
	}

}
