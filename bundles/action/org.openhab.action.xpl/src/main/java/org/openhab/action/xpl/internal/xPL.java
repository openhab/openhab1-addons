/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.xpl.internal;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.cdp1802.xpl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class contains the methods that are made available in scripts and rules for xPL.
 * 
 * @author Clinique
 * @since 1.5.0
 */
public class xPL {

	private static final Logger logger = LoggerFactory.getLogger(xPL.class);
	private static final String vendor = "clinique";
	private static final String device = "openhab";
	private static xPL_Manager theManager = null;
	private static xPL_IdentifierI sourceIdentifier = null;
	private static xPL_MutableMessageI theMessage = null;

	public static String getInstance() {
		return sourceIdentifier.getInstanceID();
	}

	public static void setInstance(String instance) {
	    
		if (theManager == null) {
			try {
			    theManager = xPL_Manager.getManager();
			    theManager.createAndStartNetworkHandler();
			    logger.debug("manager started");				
				theMessage = xPL_Utils.createMessage();			    
			} catch (xPL_MediaHandlerException startError) {
			    logger.error("Unable to start xPL Manager" + startError.getMessage());
			}
	    }
		sourceIdentifier = theManager.getIdentifierManager().parseNamedIdentifier(vendor + "-" + device + "." + instance);
		logger.info("sender set to  : " + sourceIdentifier.toString());
		theMessage.setSource(sourceIdentifier);	    
	}
	
	public static void stopManager(){
		if (theManager != null) {
			theManager.stopAllMediaHandlers();
			logger.debug("manager stopped");
		}
	}
	
	@ActionDoc(text="Send an xPL Message", returns="<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean sendxPLMessage(
			@ParamDoc(name="target") String target, 
			@ParamDoc(name="msgType") String msgType, 
			@ParamDoc(name="schema") String schema,			
			@ParamDoc(name="bodyElements") String ... bodyElements
					) 
	{		
		if (!xPLActionService.isProperlyConfigured) {
			logger.error("xPL action is not yet configured - execution aborted!");
			return false;
		}	    
		
	    xPL_IdentifierI targetIdentifier = theManager.getIdentifierManager().parseNamedIdentifier(target);
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
	    
	    // Send the message
	    logger.debug(theMessage.toString());
	    theManager.sendMessage(theMessage);
	    
		return true;
	}

}
