/*
 * SSDPDeviceDescriptionParser
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 6 Jan 2015
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.connectsdk.discovery.provider.ssdp;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SSDPDeviceDescriptionParser extends DefaultHandler {
    public static final String TAG_DEVICE_TYPE = "deviceType";
    public static final String TAG_FRIENDLY_NAME = "friendlyName";
    public static final String TAG_MANUFACTURER = "manufacturer";
    public static final String TAG_MANUFACTURER_URL = "manufacturerURL";
    public static final String TAG_MODEL_DESCRIPTION = "modelDescription";
    public static final String TAG_MODEL_NAME = "modelName";
    public static final String TAG_MODEL_NUMBER = "modelNumber";
    public static final String TAG_MODEL_URL = "modelURL";
    public static final String TAG_SERIAL_NUMBER = "serialNumber";
    public static final String TAG_UDN = "UDN";
    public static final String TAG_UPC = "UPC";
    public static final String TAG_ICON_LIST = "iconList";
    public static final String TAG_SERVICE_LIST = "serviceList";

    public static final String TAG_SEC_CAPABILITY = "sec:Capability";
    public static final String TAG_PORT = "port";
    public static final String TAG_LOCATION = "location";

    String currentValue = null;
    Icon currentIcon;
    Service currentService;

    SSDPDevice device;
    Map<String, String> data;

    public SSDPDeviceDescriptionParser(SSDPDevice device) {
        this.device = device;
        data = new HashMap<String, String>();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentValue == null) {
            currentValue = new String(ch, start, length);
        }
        else {
            // append to existing string (needed for parsing character entities)
            currentValue += new String(ch, start, length);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (Icon.TAG.equals(qName)) {
            currentIcon = new Icon();
        } else if (Service.TAG.equals(qName)) {
            currentService = new Service();
            currentService.baseURL = device.baseURL;
        } else if (TAG_SEC_CAPABILITY.equals(qName)) {      // Samsung MultiScreen Capability 
            String port = null;
            String location = null;

            for (int i = 0; i < attributes.getLength(); i++) {
                if (TAG_PORT.equals(attributes.getLocalName(i))) {
                    port = attributes.getValue(i);
                }
                else if (TAG_LOCATION.equals(attributes.getLocalName(i))) {
                    location = attributes.getValue(i);
                }
            }

            if (port == null) {
                device.serviceURI = String.format("%s%s", device.serviceURI, location);
            }
            else {
                device.serviceURI = String.format("%s:%s%s", device.serviceURI, port, location);
            }
        }
        currentValue = null;
    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        /* Parse device-specific information */
        if (TAG_DEVICE_TYPE.equals(qName)) {
            device.deviceType = currentValue;
        } else if (TAG_FRIENDLY_NAME.equals(qName)) {
            device.friendlyName = currentValue;
        } else if (TAG_MANUFACTURER.equals(qName)) {
            device.manufacturer = currentValue;
//        } else if (TAG_MANUFACTURER_URL.equals(qName)) {
//            device.manufacturerURL = currentValue;
        } else if (TAG_MODEL_DESCRIPTION.equals(qName)) {
            device.modelDescription = currentValue;
        } else if (TAG_MODEL_NAME.equals(qName)) {
            device.modelName = currentValue;
        } else if (TAG_MODEL_NUMBER.equals(qName)) {
            device.modelNumber = currentValue;
//        } else if (TAG_MODEL_URL.equals(qName)) {
//            device.modelURL = currentValue;
//        } else if (TAG_SERIAL_NUMBER.equals(qName)) {
//            device.serialNumber = currentValue;
        } else if (TAG_UDN.equals(qName)) {
            device.UDN = currentValue;
//        } else if (TAG_UPC.equals(qName)) {
//            device.UPC = currentValue;
        }
//        /* Parse icon-list information */
//        else if (Icon.TAG_MIME_TYPE.equals(qName)) {
//            currentIcon.mimetype = currentValue;
//        } else if (Icon.TAG_WIDTH.equals(qName)) {
//            currentIcon.width = currentValue;
//        } else if (Icon.TAG_HEIGHT.equals(qName)) {
//            currentIcon.height = currentValue;
//        } else if (Icon.TAG_DEPTH.equals(qName)) {
//            currentIcon.depth = currentValue;
//        } else if (Icon.TAG_URL.equals(qName)) {
//            currentIcon.url = currentValue;
//        } else if (Icon.TAG.equals(qName)) {
//            device.iconList.add(currentIcon);
//        }
        /* Parse service-list information */
        else if (Service.TAG_SERVICE_TYPE.equals(qName)) {
            currentService.serviceType = currentValue;
        } else if (Service.TAG_SERVICE_ID.equals(qName)) {
            currentService.serviceId = currentValue;
        } else if (Service.TAG_SCPD_URL.equals(qName)) {
            currentService.SCPDURL = currentValue;
        } else if (Service.TAG_CONTROL_URL.equals(qName)) {
            currentService.controlURL = currentValue;
        } else if (Service.TAG_EVENTSUB_URL.equals(qName)) {
            currentService.eventSubURL = currentValue;
        } else if (Service.TAG.equals(qName)) {
            device.serviceList.add(currentService);
        }

        data.put(qName, currentValue);
        currentValue = null;
    }
}
