/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
/**
 * Parser to read the response from the air conditioner. 
 * Will check responses and see what type they are, and if 
 * they contain any important information.
 * 
 * @author Stein Tore Tøsse
 * @since 1.6.0
 */
public class ResponseParser {
	
	private static final Logger logger = LoggerFactory
			.getLogger(ResponseParser.class);
	
	/**
	 * See if the response from the air conditioner contains a token-
	 * 
	 * @param response The response from the air conditioner
	 * @return true if the response contains a token, otherwise false
	 */
	public static boolean isResponseWithToken(String response) {
        return response.contains("Update Type=\"GetToken\" Status=\"Completed\" Token=\"");
    }

	/**
	 * Parses the token in the response.
	 * @param response The response from the AC
	 * @return the token
	 */
	public static String parseTokenFromResponse(String response) {
    	Pattern pattern = Pattern.compile("Token=\"(.+)\"");
        Matcher matcher = pattern.matcher(response);
        matcher.find();
        return matcher.group().replaceFirst("Token=\"", "").replaceAll("\"", "");
    }

	/**
	 * @param response The response from the AC
	 * @return true if authentication failed, otherwise false
	 */
    public static boolean isFailedAuthenticationResponse(String response) {
        return response.contains("<Response Status=\"Fail\" Type=\"Authenticate\" ErrorCode=\"301\" />");
    }

    /**
     * 
     * @param response The response from the AC
     * @return true if response if marked as failed, otherwise false
     */
    public static boolean isFailedResponse(String response) {
        return response.contains("<Response Status=\"Fail\" Type=\"Authenticate\" ErrorCode=\"103\" />");
    }

    /**
     * 
     * @param response The response from the AC
     * @param commandId The id of the command we sent
     * @return true if the response is the correct response, otherwise false
     */
    public static boolean isCorrectCommandResponse(String response, String commandId) {
        return response.contains("CommandID=\"" + commandId + "\"");
    }

    /**
     * 
     * @param response The response from the AC
     * @return true if we logged in to the AC successfully, otherwise false
     */
    public static boolean isSuccessfulLoginResponse(String response) {
        return response.contains("Response Type=\"AuthToken\" Status=\"Okay\"");
    }

    /**
     * 
     * @param response The response from the AC
     * @return true if it is the first line, otherwise false
     */
    public static boolean isFirstLine(String response) {
        return response.contains("DRC-1.00");
    }

    /**
     * 
     * @param response The response from the AC
     * @return true if response lets us know we are not logged in, otherwise false
     */
    public static boolean isNotLoggedInResponse(String response) {
        return response.contains("Type=\"InvalidateAccount\"");
    }

    /**
     * 
     * @param response The response from the AC
     * @return true if air conditioner is ready for sending a token, otherwise false
     */
    public static boolean isReadyForTokenResponse(String response) {
        return response.contains("<Response Type=\"GetToken\" Status=\"Ready\"/>");
    }

    /**
     * 
     * @param response The response from the AC
     * @return true if response is someone controlling the device(e.g. from a remote), otherwise false
     */
    public static boolean isDeviceControl(String response) {
        return response.contains("Response Type=\"DeviceControl\"");
    }

    /**
     * 
     * @param response The response from the AC
     * @return true if we get a state from the ac, otherwise false
     */
    public static boolean isDeviceState(String response) {
        return response.contains("Response Type=\"DeviceState\" Status=\"Okay\"") && !response.contains("CommandID=\"cmd");
    }
    
    /**
     * 
     * @param response The response from the AC
     * @return true if we get a status update, otherwise false
     */
    public static boolean isUpdateStatus(String response){
    	return response.contains("Update Type=\"Status\"");
    }

    /**
     * 
     * @param response The response from the AC
     * @return A Map with all the commands and the status of each item
     * @throws SAXException If we cannot read the response from the AC
     */
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
    
    /**
     * Class to hold the status and to parse the XML from the air conditioner
     * 
     * @author Stein Tore Tøsse
     * @since 1.6.0
     *
     */
    static private class StatusHandler extends DefaultHandler {
    	
    	private Map<CommandEnum, String> values = new HashMap<CommandEnum, String>();
    	
    	@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if ("Attr".equals(qName)) {
				try {
					CommandEnum cmd = CommandEnum.valueOf(attributes.getValue("ID"));
					if (cmd != null) {
							values.put(cmd, attributes.getValue("Value"));
					}
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
