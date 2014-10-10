/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.xpl;

import java.util.Dictionary;

import org.cdp1802.xpl.xPL_IdentifierI;
import org.cdp1802.xpl.xPL_Manager;
import org.cdp1802.xpl.xPL_MediaHandlerException;
import org.cdp1802.xpl.xPL_MessageListenerI;
import org.cdp1802.xpl.xPL_MutableMessageI;
import org.cdp1802.xpl.device.xPL_DeviceI;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bundle activator for xPL transport bundle. 
 * 
 * @author clinique
 * @since 1.6.0
 */
public class XplTransportService implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(XplTransportService.class);
	private static final String vendor = "clinique";
	private static final String device = "openhab";
	private static xPL_IdentifierI sourceIdentifier = null;
	private static xPL_Manager theManager = null;
	public xPL_DeviceI loggerDevice = null;

	/**
	 * Start service.
	 */
	public void activate() {
		try {
			theManager = xPL_Manager.getManager();
			theManager.createAndStartNetworkHandler();
			loggerDevice = theManager.getDeviceManager().createDevice(vendor, device, getInstance());
						
			// Enable the device and start logging
			loggerDevice.setEnabled(true);
			
			logger.info("xPL transport has been started");

		} catch (xPL_MediaHandlerException startError) {
			logger.error("Unable to start xPL transport" + startError.getMessage());
		}
	}

	/**
	 * Stop service.
	 */
	public void deactivate() {		
		theManager.stopAllMediaHandlers();
		logger.debug("xPL transport has been stopped");
	}

	@Override
	public void updated(Dictionary<String, ?> config) {
		logger.info("xPL transport configuration");
		if (config != null) {
			String instancename = (String) config.get("instance");
			logger.info("Received new config : " + instancename);
			setInstance(instancename);
		}	
	}
	
	protected void setInstance(String instance) {
		sourceIdentifier = xPL_Manager.getManager().getIdentifierManager()
				.parseNamedIdentifier(vendor + "-" + device + "." + instance);
		logger.info("xPL Manager source address set to " + sourceIdentifier.toString());
	}
	
	protected String getInstance() {
		if (sourceIdentifier == null) {
			setInstance("openhab");
		}
		return sourceIdentifier.getInstanceID();
	}
	
	public void addMessageListener(xPL_MessageListenerI theListener) {
		theManager.addMessageListener(theListener);
	}

		  
	public void removeMessageListener(xPL_MessageListenerI theListener) {
		theManager.removeMessageListener(theListener);
	}

	public xPL_IdentifierI getSourceIdentifier() {
		return sourceIdentifier;
	}

	public void sendMessage(xPL_MutableMessageI message) {
		if (message.getSource() == null) {
			message.setSource(sourceIdentifier);
		}
	    logger.debug(message.toString());
		theManager.sendMessage(message);		
	}

	public xPL_IdentifierI parseNamedIdentifier(String target) {
		return theManager.getIdentifierManager().parseNamedIdentifier(target);
	}

}
