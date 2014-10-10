/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.xpl.internal;

import org.cdp1802.xpl.xPL_IdentifierI;
import org.cdp1802.xpl.xPL_MessageI;
import org.cdp1802.xpl.xPL_MutableMessageI;
import org.cdp1802.xpl.xPL_Utils;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.openhab.io.transport.xpl.XplTransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class contains the methods that are made available in scripts and rules for xPL.
 * 
 * @author Clinique
 * @since 1.6.0
 */
public class Xpl {

	private static final Logger logger = LoggerFactory.getLogger(Xpl.class);
	public static XplTransportService xplTransportService;
	
	@ActionDoc(text="Send an xPL Message", returns="<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean sendxPLMessage(
			@ParamDoc(name="target") String target, 
			@ParamDoc(name="msgType") String msgType, 
			@ParamDoc(name="schema") String schema,			
			@ParamDoc(name="bodyElements") String ... bodyElements
					) 
	{		
		xPL_MutableMessageI theMessage  = xPL_Utils.createMessage();
		
	    xPL_IdentifierI targetIdentifier = xplTransportService.parseNamedIdentifier(target); 
		if (targetIdentifier == null) {
			logger.error("Invalid target identifier");
			return false;
		}
	    
	    theMessage.setTarget(targetIdentifier);
	    
	    // Parse type
	    if (msgType.equalsIgnoreCase("TRIGGER"))
	      theMessage.setType(xPL_MessageI.MessageType.TRIGGER);
	    else if (msgType.equalsIgnoreCase("STATUS"))
	      theMessage.setType(xPL_MessageI.MessageType.STATUS);
	    else if (msgType.equalsIgnoreCase("COMMAND"))
	      theMessage.setType(xPL_MessageI.MessageType.COMMAND);
	    else {
			logger.error("Invalid message type");
			return false;
		}

	    // Parse Schema
	    int delimPtr = schema.indexOf(".");
		if (delimPtr == -1) {
			logger.error("Invalid/improperly formatted schema class.type");
			return false;
		}
		
	    String schemaClass = schema.substring(0, delimPtr);
	    String schemaType = schema.substring(delimPtr + 1);
	    if ((schemaClass.length() == 0) || (schemaType.length() == 0)) {
			logger.error("Empty/missing parts of schema class.type");
			return false;
		}
	    
	    theMessage.setSchema(schemaClass, schemaType);
	    
	    // Parse name/value pairs 	   
	    String theName = null;
	    String theValue = null;
	    theMessage.clearMessageBody();
	    
	    for (int pairPtr = 0; pairPtr < bodyElements.length; pairPtr++) {
	      if ((delimPtr = bodyElements[pairPtr].indexOf("=")) == -1) {
				logger.error("Invalid message body name/value pair");
				return false;
	      }
	      if ((theName = bodyElements[pairPtr].substring(0, delimPtr)).length() == 0) {
				logger.error("Empty name in message body name/value pair");
				return false;
	      }
	      
	      theValue = bodyElements[pairPtr].substring(delimPtr + 1);

	      theMessage.addNamedValue(theName, theValue);
	    } 
	    
	    xplTransportService.sendMessage(theMessage);
	    
		return true;
	}

}
