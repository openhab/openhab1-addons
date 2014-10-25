/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.binding.openhab.samsungac.communicator;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.samsungac.internal.CommandEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class ResponseParser {
	
	private static final Logger logger = LoggerFactory
			.getLogger(ResponseParser.class);
	
	public static boolean isResponseWithToken(String response) {
        return response.matches("Token=\"(.*)\"");
    }

	public static String parseTokenFromResponse(String response) {
    	Pattern pattern = Pattern.compile("Token=\"(.*)\"");
        Matcher matcher = pattern.matcher(response);
        return matcher.group();
    }

    public static boolean isFailedAuthenticationResponse(String line) {
        return line.contains("<Response Status=\"Fail\" Type=\"Authenticate\" ErrorCode=\"301\" />");
    }

    public static boolean isFailedResponse(String line) {
        return line.contains("<Response Status=\"Fail\" Type=\"Authenticate\" ErrorCode=\"103\" />");
    }

    public static boolean isCorrectCommandResponse(String line, String commandId) {
        return line.contains("CommandID=\"" + commandId + "\"");
    }

    public static boolean isSuccessfulLoginResponse(String line) {
        return line.contains("Response Type=\"AuthToken\" Status=\"Okay\"");
    }

    public static boolean isFirstLine(String line) {
        return line.contains("DRC-1.00");
    }

    public static boolean isNotLoggedInResponse(String line) {
        return line.contains("Type=\"InvalidateAccount\"");
    }

    public static boolean isReadyForTokenResponse(String line) {
        return line.contains("<Response Type=\"GetToken\" Status=\"Ready\"/>");
    }

    public static boolean isDeviceControl(String line) {
        return line.contains("Response Type=\"DeviceControl\"");
    }

    public static boolean isDeviceState(String line) {
        return line.contains("Response Type=\"DeviceState\" Status=\"Okay\"") && !line.contains("CommandID=\"cmd");
    }
    
    public static boolean isUpdateStatus(String line){
    	return line.contains("Update Type=\"Status\"");
    }

    public static String getStatusValue(String line) {
    	Pattern pattern = Pattern.compile("(\\w*)");
    	Matcher matcher = pattern.matcher(line);
        return matcher.group();
    }

    public static Map<CommandEnum, String> parseStatusResponse(String response) throws SAXException {
    	logger.info("Response is: " + response);
    	Map<CommandEnum, String> status = new HashMap<CommandEnum, String>();
    	try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			StatusHandler statusHandler = new StatusHandler();
			reader.setContentHandler(statusHandler);
			reader.parse(new InputSource(new StringReader(response)));
			status = statusHandler.getStatusMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return status;
    }
    
    static private class StatusHandler extends DefaultHandler {
    	
    	private Map<CommandEnum, String> values = new HashMap<CommandEnum, String>();
    	
    	@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if ("Attr".equals(qName)) {
				try {
					CommandEnum cmd = CommandEnum.valueOf(attributes.getValue("ID"));
					if (cmd != null)
							values.put(cmd, attributes.getValue("Value"));
				} catch (IllegalArgumentException e) {
					logger.debug("Does not support attribute: '" + attributes.getValue("ID"));
				}
			}
		}
		
		public Map<CommandEnum, String> getStatusMap() {
			return values;
		}
    }
}
