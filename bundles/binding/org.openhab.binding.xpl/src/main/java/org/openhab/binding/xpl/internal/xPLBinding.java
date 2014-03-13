/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xpl.internal;

import java.util.Dictionary;
import org.openhab.binding.xpl.xPLBindingProvider;

import org.cdp1802.xpl.xPL_IdentifierI;
import org.cdp1802.xpl.xPL_Manager;
import org.cdp1802.xpl.xPL_MediaHandlerException;
import org.cdp1802.xpl.xPL_MessageI;
import org.cdp1802.xpl.xPL_MessageListenerI;
import org.cdp1802.xpl.device.xPL_DeviceI;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.*;
import org.openhab.core.library.types.*;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openhab.binding.xpl.xPLBindingConfig;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Clinique
 * @since 1.5.0
 */
public class xPLBinding extends AbstractActiveBinding<xPLBindingProvider> implements ManagedService, xPL_MessageListenerI {
	/*class LogFilter {
		boolean filterValid = false;
		String fromVendor = null, fromDeviceID = null, fromInstance = null;
		String toVendor = null, toDeviceID = null, toInstance = null;
		xPL_MessageI.MessageType messageType = xPL_Message.MessageType.UNKNOWN;
		String schemaClass = null, schemaType = null;

		LogFilter(String theFilter) {
			setFilter(theFilter);
		}

		public void resetFilters() {
			messageType = xPL_MessageI.MessageType.UNKNOWN;
			fromVendor = null;
			fromDeviceID = null;
			fromInstance = null;
			toVendor = null;
			toDeviceID = null;
			toInstance = null;
			schemaClass = null;
			schemaType = null;
			filterValid = false;
		}

		public boolean isValid() {
			return filterValid;
		}

		public void setFilter(String filterSpec) {
			if ((filterSpec == null) || (filterSpec.length() == 0)) {
				resetFilters();
				return;
			}

			StringTokenizer theTokenizer = new StringTokenizer(filterSpec, ".");

			try {
				// Parse Message Type
				String theMessageType = theTokenizer.nextToken();
				if (theMessageType.equals("*"))
					messageType = xPL_MessageI.MessageType.UNKNOWN;
				else if (theMessageType.equalsIgnoreCase("xpl-cmnd"))
					messageType = xPL_MessageI.MessageType.COMMAND;
				else if (theMessageType.equalsIgnoreCase("xpl-stat"))
					messageType = xPL_MessageI.MessageType.STATUS;
				else if (theMessageType.equalsIgnoreCase("xpl-trig"))
					messageType = xPL_MessageI.MessageType.TRIGGER;

				// Parse out source
				fromVendor = theTokenizer.nextToken();
				fromDeviceID = theTokenizer.nextToken();
				fromInstance = theTokenizer.nextToken();
				if (fromVendor.equals("*"))
					fromVendor = null;
				if (fromDeviceID.equals("*"))
					fromDeviceID = null;
				if (fromInstance.equals("*"))
					fromInstance = null;

				// Parse target
				toVendor = theTokenizer.nextToken();
				toDeviceID = theTokenizer.nextToken();
				toInstance = theTokenizer.nextToken();
				if (toVendor.equals("*"))
					toVendor = null;
				if (toDeviceID.equals("*"))
					toDeviceID = null;
				if (toInstance.equals("*"))
					toInstance = null;

				// Parse out Schema
				schemaClass = theTokenizer.nextToken();
				if (schemaClass.equals("*"))
					schemaClass = null;
				schemaType = theTokenizer.nextToken();
				if (schemaType.equals("*"))
					schemaType = null;

				// And we are done
				filterValid = true;
			} catch (NoSuchElementException shortError) {
				System.err.println("LOGGER:: Bad Logging Filter spec ["
						+ filterSpec + "] -- filter not used");
				resetFilters();
			}
		}

		public boolean doesMessageMatchFilter(xPL_MessageI theMessage) {
			// If we have a specific message type, insure this matches
			if ((messageType != xPL_MessageI.MessageType.UNKNOWN)
					&& (messageType != theMessage.getType()))
				return false;

			// Get the source and test
			xPL_IdentifierI sourceIdent = theMessage.getSource();
			if (sourceIdent == null)
				return false;
			if ((fromVendor != null)
					&& !sourceIdent.getVendorID().equalsIgnoreCase(fromVendor))
				return false;
			if ((fromDeviceID != null)
					&& !sourceIdent.getDeviceID()
							.equalsIgnoreCase(fromDeviceID))
				return false;
			if ((fromInstance != null)
					&& !sourceIdent.getInstanceID().equalsIgnoreCase(
							fromInstance))
				return false;

			// Get the target and test
			xPL_IdentifierI targetIdent = theMessage.getTarget();
			if (targetIdent == null)
				return false;
			if ((toVendor != null)
					&& !targetIdent.getVendorID().equalsIgnoreCase(toVendor))
				return false;
			if ((toDeviceID != null)
					&& !targetIdent.getDeviceID().equalsIgnoreCase(toDeviceID))
				return false;
			if ((toInstance != null)
					&& !targetIdent.getInstanceID()
							.equalsIgnoreCase(toInstance))
				return false;

			// Check/Filter on schema
			if ((schemaClass != null)
					&& !theMessage.getSchemaClass().equalsIgnoreCase(
							schemaClass))
				return false;
			if ((schemaType != null)
					&& !theMessage.getSchemaType().equalsIgnoreCase(schemaType))
				return false;

			// Filter matches
			return true;
		}

		@SuppressWarnings("incomplete-switch")
		public String toString() {
			StringBuffer theSpec = new StringBuffer();

			switch (messageType) {
			case STATUS:
				theSpec.append("xpl-stat");
				break;
			case COMMAND:
				theSpec.append("xpl-cmnd");
				break;
			case TRIGGER:
				theSpec.append("xpl-trig");
				break;
			}
			theSpec.append('.');

			theSpec.append(fromVendor == null ? '*' : fromVendor);
			theSpec.append('.');
			theSpec.append(fromDeviceID == null ? '*' : fromDeviceID);
			theSpec.append('.');
			theSpec.append(fromInstance == null ? '*' : fromInstance);
			theSpec.append('.');

			theSpec.append(toVendor == null ? '*' : toVendor);
			theSpec.append('.');
			theSpec.append(toDeviceID == null ? '*' : toDeviceID);
			theSpec.append('.');
			theSpec.append(toInstance == null ? '*' : toInstance);
			theSpec.append('.');

			theSpec.append(schemaClass == null ? '*' : schemaClass);
			theSpec.append('.');
			theSpec.append(schemaType == null ? '*' : schemaType);

			return theSpec.toString();
		}
	}*/

	private static final Logger logger = LoggerFactory.getLogger(xPLBinding.class);
	private static final String vendor = "clinique";
	private static final String device = "openhab";
	private static xPL_Manager theManager = null;
	private xPL_IdentifierI sourceIdentifier = null;
	public xPL_DeviceI loggerDevice = null;
	
	private EventPublisher eventPublisher;
		
	/**
	 * the refresh interval which is used to poll values from the xPL server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public xPLBinding() {
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	protected void setInstance(String instance) {
		sourceIdentifier = xPL_Manager.getManager().getIdentifierManager()
				.parseNamedIdentifier(vendor + "-" + device + "." + instance);
		logger.debug("xPL Binding source set to " + sourceIdentifier.toString());
	}

	protected String getInstance() {
		if (sourceIdentifier == null) {
			setInstance("bulk");
		}
		return sourceIdentifier.getInstanceID();
	}

	public void activate() {
		super.activate();
		try {
			theManager = xPL_Manager.getManager();
			theManager.createAndStartNetworkHandler();

			// Create the loggers service. We create this as a configurable
			// service and
			// give it a file to store things from. The xPL4Java server does
			// this for modules automatically by reading the xPL_Modules.cfg
			// file
			//File loggerConfigFile = new File("logger.xpl");
			loggerDevice = theManager.getDeviceManager().createDevice(vendor, device, getInstance());
			//		.createConfigurableDevice(vendor, device, loggerConfigFile);
			
			if (loggerDevice == null) 
				logger.error("Unable to create xPL Logger device");
			else
				logger.debug("xPL Binding manager started");

			// Create the configurable items for this device. The xPL4Java
			// server automatically reads those from the xPL_Modules.cfg
			// and installs them.
			//loggerDevice.addConfigurable("LogHeartbeats", 1, true, true).setValue(false);
			//loggerDevice.addConfigurable("Exclude", 255, false, true);
			//loggerDevice.addConfigurable("Include", 255, false, true);

			// Enable the device and start logging
			loggerDevice.setEnabled(true);
			theManager.addMessageListener(this);

		} catch (xPL_MediaHandlerException startError) {
			logger.error("xPL Binding : Unable to start xPL Manager" + startError.getMessage());
		}
		setProperlyConfigured(true);
	}

	public void deactivate() {
		theManager.removeMessageListener(this);
		theManager.stopAllMediaHandlers();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "xPL Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		// logger.debug("execute() method is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		for (xPLBindingProvider provider : providers) {
			xPLBindingConfig config = provider.getConfig(itemName);
			if ((config == null) || (config.NamedParameter == null)) continue;

			if (config.Message.getSource() == null) 
				config.Message.setSource(sourceIdentifier);
			
			config.Message.setNamedValue(config.NamedParameter, command.toString().toLowerCase());
			theManager.sendMessage(config.Message);
			//logger.debug("About to send " + config.Message.toString() + " with command " + command.toString());
			//logger.debug(config.NamedParameter);
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			logger.debug("xPL Binding : Updating config");

			setInstance((String) config.get("instance"));

			// if (StringUtils.isBlank(getInstance())) {
			// throw new ConfigurationException("xPL",
			// "Parameters xPL:instance is mandatory and must be configured. Please check your openhab.cfg!");
			// }

			setProperlyConfigured(true);
		}
	}

	// xPL Part of the binding
	
	// Install defaults from the configuration and add in the message listeners.
	/*public void startLogging() {
	   // Prep defaults from config
	   //readConfiguration();
	    
	   // And install our listener
	   //loggerDevice.addDeviceChangeListener(this);
	}
	  
	// Stops the logging process
	public void stopLogging() {
	    // Remove Listeners
	    theManager.removeMessageListener(this);
	    //loggerDevice.removeDeviceChangeListener(this);
	}*/
	
	/*public void readConfiguration() {
		excludeList.clear();
		//includeList.clear();

		//if (!loggerDevice.getBoolConfigValue("LogHeartbeats", false)) {
		excludeList.add(new LogFilter("xpl-stat.*.*.*.*.*.*.hbeat.*"));
		excludeList.add(new LogFilter("xpl-stat.*.*.*.*.*.*.config.*"));
		//}

		LogFilter logFilter = null;
		xPL_DeviceConfigItemI filterList = loggerDevice.getConfigurable("Include");
		if ((filterList != null) && (filterList.getValueCount() != 0)) {
			for (String theValue : filterList.getValues()) {
				logFilter = new LogFilter(theValue);
				if (!logFilter.isValid())
					continue;
				includeList.add(logFilter);
				continue;
			}
		}

		filterList = loggerDevice.getConfigurable("Exclude");
		if ((filterList != null) && (filterList.getValueCount() != 0)) {
			for (String theValue : filterList.getValues()) {
				logFilter = new LogFilter(theValue);
				if (!logFilter.isValid())
					continue;
				excludeList.add(logFilter);
				continue;
			}
		}

		theManager.addMessageListener(this);
	}*/

	/*public void handleXPLDeviceChange(xPL_DeviceChangeEventI deviceEvent) {
		if (deviceEvent.getChangeReason() == xPL_DeviceChangeEventI.ChangeReason.CONFIGURATION_CHANGED) {
			readConfiguration();
		}
	}*/
	
	/*public boolean logMessage(xPL_MessageI theMessage) {
		for (LogFilter theFilter: includeList) {
			if (theFilter.doesMessageMatchFilter(theMessage)) {
				return true;
			}
		}

		for (LogFilter theFilter: excludeList) {
			if (theFilter.doesMessageMatchFilter(theMessage)) {
				return false;
			}
		}

		return true;
	}*/

	public void handleXPLMessage(xPL_MessageI theMessage) {
		// Make sure we want to see such messages logged
		//if (!logMessage(theMessage)) return;
		
		for (xPLBindingProvider provider : providers) {
			String itemName = provider.hasMessage(theMessage);
			if (itemName != null) {
				xPLBindingConfig config = provider.getConfig(itemName);
				if (config == null) continue;
				
				String current = theMessage.getNamedValue(config.NamedParameter);
			
				Item item = provider.getItem(itemName);
				if (item != null) {
					if (item instanceof SwitchItem) {
					   OnOffType status = ( current.equalsIgnoreCase("on") || current.equalsIgnoreCase("true") ||
							   				current.equalsIgnoreCase("1")	|| current.equalsIgnoreCase("open") || 
							   				current.equalsIgnoreCase("high")) ? OnOffType.ON : OnOffType.OFF;
					   synchronized (item) {
						 if (!item.getState().equals(status)) {
							 eventPublisher.postUpdate(itemName, status);
							 ((SwitchItem) item).setState(status);
						 }
					   }						
					} else 
					if (item instanceof NumberItem) {
						DecimalType value = new DecimalType(current);
						synchronized (item) {
							if (!item.getState().equals(value)) {								
								eventPublisher.postUpdate(itemName, value);
								((NumberItem) item).setState(value);
							}
						}
					}
					if (item instanceof StringItem) {
						StringType value = new StringType(current);
						synchronized (item) {
							if (!item.getState().equals(value)) {								
								eventPublisher.postUpdate(itemName, value);
								((StringItem) item).setState(value);
							}
						}						
					}
				}
			}										
		}




	}
}
