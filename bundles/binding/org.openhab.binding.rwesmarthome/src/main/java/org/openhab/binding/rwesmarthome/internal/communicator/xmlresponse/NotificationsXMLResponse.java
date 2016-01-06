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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.ConfigurationChangedException;
import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.LogoutNotificationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class handles the response of the "/update" call, which loads the
 * notifications of RWE Smarthome. Those notifications include e.g. device
 * state changes, configuration changes etc.
 * 
 * @author ollie-dev
 *
 */
public class NotificationsXMLResponse extends XMLResponse {

	/**
	 * Constructor with input stream.
	 * 
	 * @param is
	 * @throws LogoutNotificationException
	 * @throws ConfigurationChangedException
	 */
	public NotificationsXMLResponse(InputStream is) throws LogoutNotificationException, ConfigurationChangedException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
        	//Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            Document doc = db.parse(is);
            
            //get the root element
            Element docEle = doc.getDocumentElement();
            
            // Check for LogoutNotification
			NodeList nl = docEle.getElementsByTagName("LogoutNotification");
			if (nl != null && nl.getLength() > 0) {
				Element el = (Element) nl.item(0);
				String reason = getTextValueFromAttribute(el, "Reason");
				throw new LogoutNotificationException("Logged out from Notifications. Reason:" + reason);
			}
			
			// Check for ConfigurationChanges
			nl = docEle.getElementsByTagName("ConfigurationChangedNotification");
			if (nl != null && nl.getLength() > 0) {
				Element el = (Element) nl.item(0);
				String configVersion = getTextValueFromAttribute(el, "ConfigurationVersion");
				throw new ConfigurationChangedException("Configuration changed to version " + configVersion);
			}
            
        } catch (SAXException ex) {
            Logger.getLogger(NotificationsXMLResponse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NotificationsXMLResponse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(NotificationsXMLResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

}
