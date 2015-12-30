/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm.internal;

import java.util.Collections;
import java.util.Dictionary;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.dscalarm.DSCAlarmActionProvider;
import org.openhab.binding.dscalarm.DSCAlarmBindingConfig;
import org.openhab.binding.dscalarm.DSCAlarmBindingProvider;
import org.openhab.binding.dscalarm.internal.connector.DSCAlarmConnectorType;
import org.openhab.binding.dscalarm.internal.model.DSCAlarmDeviceProperties;
import org.openhab.binding.dscalarm.internal.model.DSCAlarmDeviceType;
import org.openhab.binding.dscalarm.internal.protocol.API;
import org.openhab.binding.dscalarm.internal.protocol.APICode;
import org.openhab.binding.dscalarm.internal.protocol.APIMessage;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DSCAlarmActiveBinding Class. Polls the DSC Alarm panel,
 * responds to item commands, and also handles events coming 
 * from the DSC Alarm panel.
 * 
 * @author Russell Stephens
 * @since 1.6.0
 */

public class DSCAlarmActiveBinding extends AbstractActiveBinding<DSCAlarmBindingProvider> implements ManagedService, DSCAlarmEventListener, DSCAlarmActionProvider {

	private static final Logger logger = LoggerFactory.getLogger(DSCAlarmActiveBinding.class);

	private long refreshInterval = 5000;

	private DSCAlarmConnectorType connectorType = null;

	/** Default Poll Period. **/
	public static final long DEFAULT_POLL_PERIOD = 1;
	
	/** The serial port name of the DSC-IT100 Serial Interface
	 * 	Valid values are e.g. COM1 for Windows and /dev/ttyS0 or /dev/ttyUSB0 for Linux
	 * */
	private String serialPort = null;

	/** The IP address of the EyezOn Envisalink 3/2DS DSC Alarm Interface*/
	private String ipAddress = null;
	
	/** Baud rate for a serial connection */
	private int baudRate = 0;

	/** User name for EyezOn Envisalink 3/2DS authentication */
	/**private String username = null;

	/** Password for EyezOn Envisalink 3/2DS authentication */
	private String password = null;

	/** User Code for some DSC Alarm commands */
	private String userCode = null;

	/** Suppress Acknowledge Messages when received */
	private boolean suppressAcknowledgementMsgs = false;
	
	/** API Session for EyezOn Envisalink 3/2DS */
	private API api = null;
	private boolean connected = false;
	
	private long pollTime = 0;
	private long pollTimeStart = 0;
	private long pollPeriod = DEFAULT_POLL_PERIOD;
	
	/**
	 * New items or items needing to be refreshed get added to refreshmao
	 * the worker thread will refresh and remove them
	 */
	private Map<String, DSCAlarmBindingConfig> dscAlarmUpdateMap = Collections.synchronizedMap(new HashMap<String, DSCAlarmBindingConfig>());
	private DSCAlarmItemUpdate dscAlarmItemUpdate = new DSCAlarmItemUpdate();
	private int itemCount = 0;
	private boolean itemHasChanged = false;
	private boolean processUpdates = false;
		
	/**
	 * Activates the binding. Actually does nothing, because on activation
	 * OpenHAB always calls updated to indicate that the config is updated
	 * Activation is done there
	 */
	public void activate() {
		logger.debug("DSC Alarm Binding Activated!");
	}
	
	/**
	 * Deactivates the binding
	 */
	public void deactivate() {
		logger.debug("DSC Alarm Binding Deactivated!");
		closeConnection();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		logger.debug("DSC Alarm Execute");

		if(api != null) {
			connected = api.isConnected();
		}
		
		if(connected) {
			if(pollTimeStart == 0) {
				pollTimeStart = System.currentTimeMillis();
			}
			
			pollTime = ((System.currentTimeMillis() - pollTimeStart) / 1000) / 60;
			
			//Send Poll command to the DSC Alarm if idle for 'pollPeriod' minutes
			if(pollTime >= pollPeriod) {
				api.sendCommand(APICode.Poll);
				pollTimeStart = 0;
				logger.debug("execute(): Poll Command Sent to DSC Alarm.");
			}
		}
		else {
			closeConnection();
			logger.error("execute(): Not Connected to the DSC Alarm!");
			reconnect();
		}

		//Need to allow one cycle to pass before processing item updates after binding changes.
		if(itemHasChanged) {
			if(processUpdates) {
				processUpdateMap();
				itemHasChanged = false;
				processUpdates = false;
				if(connected) {
					//Get a status report from API.
					api.sendCommand(APICode.StatusReport);
				}
			}
			else
				processUpdates = true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		logger.debug("bindingChanged(): Item Name: {}", itemName);
		
		boolean itemRemoved = false;
		int icount = provider.getItemNames().size();

		if(icount < itemCount) {
			itemRemoved = true;
		}
		
		if(itemRemoved) {
			dscAlarmUpdateMap.clear();
		} else {

			DSCAlarmBindingProvider dscAlarmBindingProvider = (DSCAlarmBindingProvider) provider;
			
			if(dscAlarmBindingProvider != null) {
				DSCAlarmBindingConfig dscAlarmBindingConfig = dscAlarmBindingProvider.getDSCAlarmBindingConfig(itemName);
				if(dscAlarmBindingConfig != null) {
					dscAlarmUpdateMap.put(itemName, dscAlarmBindingConfig);
				}
			}
		}

		itemCount = provider.getItemNames().size();
		itemHasChanged = true;
	}
	
	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		DSCAlarmBindingConfig dscAlarmBindingConfig = null;
		Item item = null;
		for (DSCAlarmBindingProvider prov : providers) {
			dscAlarmBindingConfig = prov.getDSCAlarmBindingConfig(itemName);
			item = prov.getItem(itemName);
			if( dscAlarmBindingConfig != null) {
				DSCAlarmDeviceType dscAlarmDeviceType = dscAlarmBindingConfig.getDeviceType(); 
				int partitionId;
				int zoneId;
				int cmd;
				
				logger.debug("internalReceiveCommand():  Item Name: {} Command: {} Item Device Type: {}",itemName,command,dscAlarmDeviceType);

				if(connected) {
					switch(dscAlarmDeviceType) {
						case PANEL:
							switch (dscAlarmBindingConfig.getDSCAlarmItemType()) {
								case PANEL_CONNECTION:
									if(command.toString().equals("0")) {
										closeConnection();
										if(!connected) {
											dscAlarmItemUpdate.setConnected(false);
											dscAlarmItemUpdate.updateDeviceProperties(item, dscAlarmBindingConfig, 0, "Disconnected");
										}
									}
									break;
								case PANEL_COMMAND:
									cmd = Integer.parseInt(command.toString());
									switch (cmd) {
									case 0: api.sendCommand(APICode.Poll);
										break;
									case 1: api.sendCommand(APICode.StatusReport);
										break;
									case 2: api.sendCommand(APICode.LabelsRequest);
										break;
									case 8: api.sendCommand(APICode.DumpZoneTimers);
										break;
									case 10: api.sendCommand(APICode.SetTimeDate);
										break;
									case 200: api.sendCommand(APICode.CodeSend);
										break;
									default:
										break;
									}

									itemName = getItemName(DSCAlarmItemType.PANEL_COMMAND,0,0);
									if(itemName != "") {
										updateDeviceProperties(itemName,-1,"");
										updateItem(itemName);
									}
									break;
								case PANEL_TIME_STAMP:
									if (command instanceof OnOffType) {
										cmd = command == OnOffType.ON ? 1 : 0;
										api.sendCommand(APICode.TimeStampControl, String.valueOf(cmd));
										updateDeviceProperties(itemName, cmd, "");
										updateItem(itemName);
									}
									break;
								case PANEL_TIME_BROADCAST:
									if (command instanceof OnOffType) {
										cmd = command == OnOffType.ON ? 1 : 0;
										api.sendCommand(APICode.TimeDateBroadcastControl, String.valueOf(cmd));
										updateDeviceProperties(itemName, cmd, "");
										updateItem(itemName);
									}
									break;
								default:
									break;
							}
							
							break;
						case PARTITION:
							partitionId = dscAlarmBindingConfig.getPartitionId();
							switch(dscAlarmBindingConfig.getDSCAlarmItemType()) {
								case PARTITION_ARM_MODE:
									if(command.toString().equals("0")) {
										if(api.sendCommand(APICode.PartitionDisarmControl, String.valueOf(partitionId))) {
											dscAlarmItemUpdate.updateDeviceProperties(item, dscAlarmBindingConfig, 0, "Partition Disarmed");
										}
									}else if(command.toString().equals("1")) {
										if(api.sendCommand(APICode.PartitionArmControlAway, String.valueOf(partitionId))) {
											dscAlarmItemUpdate.updateDeviceProperties(item, dscAlarmBindingConfig, 1, "Partition Armed (Away)");
										}
									}else if(command.toString().equals("2")) {
										if(api.sendCommand(APICode.PartitionArmControlStay, String.valueOf(partitionId))) {
											dscAlarmItemUpdate.updateDeviceProperties(item, dscAlarmBindingConfig, 2, "Partition Armed (Stay)");
										}
									}else if(command.toString().equals("3")) {
										if(api.sendCommand(APICode.PartitionArmControlZeroEntryDelay, String.valueOf(partitionId))) {
											dscAlarmItemUpdate.updateDeviceProperties(item, dscAlarmBindingConfig, 3, "Partition Armed (Zero Entry Delay)");
										}
									}else if(command.toString().equals("4")) {
										if(api.sendCommand(APICode.PartitionArmControlWithUserCode, String.valueOf(partitionId))) {
											dscAlarmItemUpdate.updateDeviceProperties(item, dscAlarmBindingConfig, 4, "Partition Armed (With User Code)");
										}
									}
									break;
								default:
									break;
							}
							
							dscAlarmUpdateMap.put(itemName, dscAlarmBindingConfig);
							processUpdateMap();
							break;
						case ZONE:
							partitionId = dscAlarmBindingConfig.getPartitionId();
							zoneId = dscAlarmBindingConfig.getZoneId();
							switch(dscAlarmBindingConfig.getDSCAlarmItemType()) {
								case ZONE_BYPASS_MODE:
									if(command.toString().equals("0")) {
										String data = String.valueOf(partitionId) + "*1" + String.format("%02d", zoneId) + "#";
										if(api.sendCommand(APICode.KeySequence, data)) {
											dscAlarmItemUpdate.updateDeviceProperties(item, dscAlarmBindingConfig, 0, "Zone Armed");
										}
									}else if(command.toString().equals("1")) {
										String data = String.valueOf(partitionId) + "*1" + String.format("%02d", zoneId) + "#";
										if(api.sendCommand(APICode.KeySequence, data)) {
											dscAlarmItemUpdate.updateDeviceProperties(item, dscAlarmBindingConfig, 1, "Zone Bypassed");
										}										
									}
									break;
	
										
								default:
									break;
							}
							
							break;
						default:
							logger.debug("internalReceiveCommand(): No Command Sent.");
							break;
					}
				}
				else {
					if(dscAlarmDeviceType == DSCAlarmDeviceType.PANEL) {
						if(dscAlarmBindingConfig.getDSCAlarmItemType() == DSCAlarmItemType.PANEL_CONNECTION) {
							if(command.toString().equals("1")) {
								if (api != null) {
									openConnection();
									if(connected){
										dscAlarmItemUpdate.setConnected(true);
										dscAlarmItemUpdate.updateDeviceProperties(item, dscAlarmBindingConfig, 1, "Connected");
									}
								}
							}
						}
					}
				}
				/*dscAlarmUpdateMap.put(itemName, dscAlarmBindingConfig);
				itemHasChanged = true;*/
			}
		}
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
		return "DSC Alarm Monitor Service";
	}

	/**
	 * @{inheritDoc
	 */
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("updated(): Configuration updated, config {}", config != null ? true:false);
		
		if (config != null) {
			
			if(StringUtils.isNotBlank((String) config.get("serialPort"))) {
				serialPort = (String) config.get("serialPort");
			}

			if(StringUtils.isNotBlank((String) config.get("ip"))) {
				ipAddress = (String) config.get("ip");
			}

			if(serialPort != null && ipAddress != null) {
				logger.error("updated(): Can only configure one connection type at a time: Serial Port or Ip Address!");
				return;
			}

			if(serialPort != null) {
				connectorType = DSCAlarmConnectorType.SERIAL;
				try {
					if(StringUtils.isNotBlank((String) config.get("baud")))
						baudRate = Integer.parseInt((String) config.get("baud"));
				} catch(NumberFormatException numberFormatException) {
					baudRate=0;
					logger.error("updated(): Baud Rate not configured correctly!");
					return;
				}
			}
			
			if(ipAddress != null) {
				connectorType = DSCAlarmConnectorType.TCP;
			}
			
			logger.debug("updated(): Connector Type: {}",connectorType);
			
			
			if(StringUtils.isNotBlank((String) config.get("password"))) {
				password = (String) config.get("password");
			}

			if(StringUtils.isNotBlank((String) config.get("usercode"))) {
				userCode = (String) config.get("usercode");
			}
			
			if(StringUtils.isNotBlank((String) config.get("pollPeriod"))) {
				try {
					pollPeriod = Integer.parseInt((String) config.get("pollperiod"));
				} catch(NumberFormatException numberFormatException) {
					logger.error("updated(): Poll Period not configured correctly!");
					return;
				}

				if(pollPeriod > 15) {
					pollPeriod = 15;
				}
				else if(pollPeriod < 1) {
					pollPeriod = 1;
				}
			} else {
				pollPeriod = DEFAULT_POLL_PERIOD;
			}
			
			if(StringUtils.isNotBlank((String) config.get("suppressAcknowledgementMsgs"))) {
				try {
					suppressAcknowledgementMsgs = Boolean.parseBoolean((String) config.get("suppressAcknowledgementMsgs"));				
				} catch (NumberFormatException numberFormatException) {
					suppressAcknowledgementMsgs = false;
					logger.error("updated(): Error parsing 'suppressAcknowledgementMsgs'. This must be boolean!");
				}
			}
			
		}
		else {
			logger.debug("updated(): No Configuration!");
			return;
		}
		
		initialize();
	}

	/**
	 * Initializes the binding
	 */
 	private void initialize() {

		//Check to see if openHAB read in our items while the binding was configuring, and add them to dscAlarmUpdateMap. 
		 if(dscAlarmUpdateMap.isEmpty() && itemHasChanged == false) {
			 buildUpdateMap();
			 itemHasChanged = true;
		 }
		
		//Open a connection to the DSC Alarm Panel
		openConnection();
		if(!connected) {
			dscAlarmItemUpdate.setConnected(false);
		}
		else {
			dscAlarmItemUpdate.setConnected(true);
		}
		
		this.setProperlyConfigured(true);
		logger.debug("initialize(): Binding initialized!");
 	}

 	/**
	 * Build the Update Items Map
	 */
	private void buildUpdateMap() {
		DSCAlarmBindingConfig config;

		for (DSCAlarmBindingProvider prov : providers) {
	 		if(!prov.getItemNames().isEmpty()) {
	 			itemCount = prov.getItemNames().size();	 			
				dscAlarmUpdateMap.clear();
				for (String iName : prov.getItemNames()) {
					config = prov.getDSCAlarmBindingConfig(iName); 
					if(config != null) {
						dscAlarmUpdateMap.put(iName, config);
					}
				}
			}
		}
	}
	
	/**
	 * Processes the Update Items Map
	 */
	private void processUpdateMap() {
	
		if (dscAlarmUpdateMap.size() == 0) {
			logger.debug("processUpdateMap(): Nothing to update.");
			return;
		}
		
		Map<String, DSCAlarmBindingConfig> itemsMap = new HashMap<String, DSCAlarmBindingConfig>(dscAlarmUpdateMap);
	
		for (String itemName : itemsMap.keySet()) {
			DSCAlarmBindingConfig dscAlarmBindingConfig = itemsMap.get(itemName);
			dscAlarmUpdateMap.remove(itemName);
			Item item = null;
			for (DSCAlarmBindingProvider provider : providers) {
				item = provider.getItem(itemName);
			}

			dscAlarmItemUpdate.updateDeviceItem(item, dscAlarmBindingConfig, eventPublisher, null);
			logger.debug("processUpdateMap(): Updated item: {}", itemName);
		}
	}

	/**
	 * Open a TCP or Serial connection to the DSC Alarm Panel
	 */
 	private void openConnection() {

    	switch (connectorType) {
	    	case SERIAL:
	     		if (api == null) {
	    			api = new API(serialPort, baudRate, userCode);
	    		}
	     		break;
	    	case TCP:
	     		if (api == null) {
	    			api = new API(ipAddress, password, userCode);
	    		}
   			break;
	        default:
	        	connected = false;
				logger.debug("openConnection(): Unable to make a connection!");
				return;
    	}

    	connected = api.open();

		if(connected){
			api.addEventListener(this);
		}
	}
	
	/**
	 * Attempt to reconnect to TCP or Serial connection
	 */
	private void reconnect() {
		String itemName;
		logger.debug("reconnect(): API Reconnection!");	

		openConnection();
		
		if(connected) {
			dscAlarmItemUpdate.setConnected(true);
			itemName = getItemName(DSCAlarmItemType.PANEL_CONNECTION,0,0);
			if(itemName != "") {
				updateItem(itemName);
			}

			buildUpdateMap();
			itemHasChanged = true;
		}
		else {
			setPanelMessage("PANEL DISCONNECTED!!!");
			logger.error("reconnect(): API reconnection failed!");	
		}
	}

	/**
	 * Close TCP or Serial connection to the DSC Alarm Panel and remove the Event Listener
	 */
	private void closeConnection() {
		String itemName;

		if(api != null) {
			connected = api.close();
			api.removeEventListener(this);
		}

		dscAlarmItemUpdate.setConnected(false);
		itemName = getItemName(DSCAlarmItemType.PANEL_CONNECTION,0,0);
		if(itemName != "") {
			updateItem(itemName);
		}
		
		logger.debug("closeConnection(): {} Connection Closed!",connectorType);	
	}	
	
	/**
	 * Find Item by Item Name
	 * 
	 * @param itemName
	 * @return item
	 */
	private Item getItem(String itemName) {
		Item item = null;
		
		for (DSCAlarmBindingProvider prov : providers) {
			for (String iName : prov.getItemNames()) {
				if(itemName == iName) {
					item = prov.getItem(itemName);
					break;
				}
			}			
		}
		
		return item;
	}

	/**
	 * Searches for an items name and returns it
	 * 
	 * @param dscAlarmItemType
	 * @param partitionId
	 * @param zoneId
	 * @return itemName
	 */
	private String getItemName(DSCAlarmItemType dscAlarmItemType, int partitionId, int zoneId) {
		String itemName = "";
		DSCAlarmBindingConfig config = null;
		
		for (DSCAlarmBindingProvider prov : providers) {
			for (String iName : prov.getItemNames()) {
				config = prov.getDSCAlarmBindingConfig(iName);
				if(config.getDSCAlarmItemType() == dscAlarmItemType) {
					if(partitionId == -1) {
						if(config.getZoneId() == zoneId) { 
							itemName = iName;
							break;
						}
					}else if(zoneId == -1) {
						if(config.getPartitionId() == partitionId) {
							itemName = iName;
							break;
						}
					}else if((config.getPartitionId() == partitionId) && (config.getZoneId() == zoneId)) {
						itemName = iName;
						break;
					}
				}
			}
		}
		
		return itemName;
	}
		
	/**
	 * Find Item Configuration by Item Name
	 * 
	 * @param itemName
	 * @return item
	 */
	private DSCAlarmBindingConfig getItemConfig(String itemName) {
		DSCAlarmBindingConfig config = null;
		
		for (DSCAlarmBindingProvider prov : providers) {
			for (String iName : prov.getItemNames()) {
				if(itemName == iName) {
					config = prov.getDSCAlarmBindingConfig(iName);
					break;
				}
			}			
		}
		
		return config;
	}

	/**
	 * Update an item by item name
	 * 
	 * @param itemName
	 */
	private void updateItem(String itemName) {
		DSCAlarmBindingConfig config = null;
		Item item = null;

		for (DSCAlarmBindingProvider prov : providers) {
			for (String iName : prov.getItemNames()) {
				if(itemName == iName) {
					config = prov.getDSCAlarmBindingConfig(iName);
					if(config != null) {
						item = prov.getItem(itemName);
						dscAlarmItemUpdate.updateDeviceItem(item, config, eventPublisher, null);
						break;
					}
				}
			}
		}
	}

	/**
	 * Update an item by DSC Alarm Item Type
	 * 
	 * @param dscAlarmItemType
	 * @param trigger
	 */
	private void updateItemType(DSCAlarmItemType dscAlarmItemType, int partitionId, int zoneId, int propertyState) {
		String itemName = "";
		itemName = getItemName(dscAlarmItemType,partitionId,zoneId);
		logger.debug("updateTriggerItem(): Item Name: {} Partition: {} Zone: {}", itemName,partitionId,zoneId);

		if(itemName != "") {
			updateDeviceProperties(itemName, propertyState, "");
			updateItem(itemName);
		}
	}

	/**
	 * Update a DSC Alarm device Properties
	 * 
	 * @param itemName
	 * @param state
	 * @param description
	 */	
	private void updateDeviceProperties(String itemName, int state, String description) {
		DSCAlarmBindingConfig config = null;
		Item item = null;

		for (DSCAlarmBindingProvider prov : providers) {
			for (String iName : prov.getItemNames()) {
				if(itemName == iName) {
					config = prov.getDSCAlarmBindingConfig(iName);
					if(config != null) {
						item = prov.getItem(itemName);
						dscAlarmItemUpdate.updateDeviceProperties(item, config, state, description);
						break;
					}
				}
			}
		}
	}

	/**
	 * Method to set the panel message item
	 * 
	 * @param message
	 */
	private void setPanelMessage(String message) {
		String itemName;

		dscAlarmItemUpdate.setSysMessage(message);
		itemName = getItemName(DSCAlarmItemType.PANEL_MESSAGE,0,0);
		if(itemName != "") {
			updateItem(itemName);
		}
	}

	/**
	 * Method to set a partition status item
	 * 
	 * @param partitionID
	 * @param state
	 * @param description
	 */
	private void setPartitionStatus(int partitionID, int state, String description) {
		String itemName;

		itemName = getItemName(DSCAlarmItemType.PARTITION_STATUS,partitionID,0);
		updateDeviceProperties(itemName, state, description);
		if(itemName != "") {
			updateItem(itemName);
		}
	}
	
	/**
	 * Method to send a sequence of key presses one at a time using the '070' command.
	 *  
	 * @param keySequence
	 */
	@SuppressWarnings("unused")
	private boolean sendKeySequence(String keySequence) {
		logger.debug("sendKeySequence(): Sending key sequence '{}'.", keySequence);
		
		boolean sent = false;
		
		for(char key : keySequence.toCharArray()) {
			sent = api.sendCommand(APICode.KeyStroke, String.valueOf(key));
			
			if(!sent)
				return sent;
		}
		
		return sent;
	}

	/**
	 * Method to set the time stamp state
	 * 
	 * @param timeStamp
	 */
	private void setTimeStampState(String timeStamp) {
		logger.debug("setTimeStampState(): Time Stamp: {}", timeStamp);

		int state = 0;
		String itemName = "";

		itemName = getItemName(DSCAlarmItemType.PANEL_TIME_STAMP, 0, 0);
		
		if(itemName != "") {
			DSCAlarmBindingConfig config = getItemConfig(itemName);
			
			if(config != null) {
				Item item = getItem(itemName);
				if(item != null) {
					DSCAlarmDeviceProperties dsclarmDeviceProperties = dscAlarmItemUpdate.getDeviceProperties(item, config);
					
					if(dsclarmDeviceProperties != null) {
						
						boolean isTimeStamp = dsclarmDeviceProperties.getSystemTimeStamp();
						
						if((timeStamp == "" && isTimeStamp == false)  || (timeStamp != "" && isTimeStamp == true)) {
							logger.debug("setTimeStampState(): Already Set!", timeStamp);
							return;
						}else if (timeStamp != "") {
							state = 1;
						}
					}
				}
			}
		}

		updateItemType(DSCAlarmItemType.PANEL_TIME_STAMP, 0, 0, state);

		logger.debug("setTimeStampState(): Changed state to '{}'.", state == 1 ? OnOffType.ON : OnOffType.OFF);
	}

	/**
	 * Handle Keypad LED events for the EyezOn Envisalink 3/2DS DSC Alarm Interface
	 * 
	 * @param event
	 */	
	private void keypadLEDStateEventHandler(EventObject event) {
		DSCAlarmEvent dscAlarmEvent = (DSCAlarmEvent) event;
		APIMessage apiMessage = dscAlarmEvent.getAPIMessage();
		DSCAlarmItemType[] dscAlarmItemTypes =
		{
			DSCAlarmItemType.KEYPAD_READY_LED,DSCAlarmItemType.KEYPAD_ARMED_LED,
			DSCAlarmItemType.KEYPAD_MEMORY_LED,DSCAlarmItemType.KEYPAD_BYPASS_LED,
			DSCAlarmItemType.KEYPAD_TROUBLE_LED,DSCAlarmItemType.KEYPAD_PROGRAM_LED,
			DSCAlarmItemType.KEYPAD_FIRE_LED,DSCAlarmItemType.KEYPAD_BACKLIGHT_LED
		};

		String itemName;
		APICode apiCode = APICode.getAPICodeValue(apiMessage.getAPICode());
		
		int bitField = Integer.decode("0x" + apiMessage.getAPIData());
		int[] masks = {1,2,4,8,16,32,64,128};
		int[] bits = new int[8];

		for(int i=0; i < 8; i++) {
			bits[i] = bitField & masks[i];
			
			itemName = getItemName(dscAlarmItemTypes[i],0,0);

			if(itemName != "") {
				
				switch(apiCode) {
					case KeypadLEDState: /*510*/
						updateDeviceProperties(itemName, bits[i] != 0 ? 1:0, "");
						break;
					case KeypadLEDFlashState: /*511*/
						if(bits[i] != 0) {
							updateDeviceProperties(itemName, 2, "");
						}
						break;
					default:
						break;
				}
				
				updateItem(itemName);
			}
		}		
	}
	
	/**
	 * Handle Verbose Trouble Status events for the EyezOn Envisalink 3/2DS DSC Alarm Interface
	 * 
	 * @param event
	 */	
	private void verboseTroubleStatusHandler(EventObject event) {
		DSCAlarmEvent dscAlarmEvent = (DSCAlarmEvent) event;
		APIMessage apiMessage = dscAlarmEvent.getAPIMessage();
		DSCAlarmItemType[] dscAlarmItemTypes =
		{
			DSCAlarmItemType.PANEL_SERVICE_REQUIRED,DSCAlarmItemType.PANEL_AC_TROUBLE,
			DSCAlarmItemType.PANEL_TELEPHONE_TROUBLE,DSCAlarmItemType.PANEL_FTC_TROUBLE,
			DSCAlarmItemType.PANEL_ZONE_FAULT,DSCAlarmItemType.PANEL_ZONE_TAMPER,
			DSCAlarmItemType.PANEL_ZONE_LOW_BATTERY,DSCAlarmItemType.PANEL_TIME_LOSS
		};

		String itemName;
		
		int bitField = Integer.decode("0x" + apiMessage.getAPIData());
		int[] masks = {1,2,4,8,16,32,64,128};
		int[] bits = new int[8];

		for(int i=0; i < 8; i++) {
			bits[i] = bitField & masks[i];
			
			itemName = getItemName(dscAlarmItemTypes[i],0,0);

			if(itemName != "") {				
				updateDeviceProperties(itemName, bits[i] != 0 ? 1:0, "");
				updateItem(itemName);
			}
		}		
	}

	/**
	 * DSC Alarm incoming message event handler
	 * 
	 * @param event
	 */
	public void dscAlarmEventRecieved(EventObject event) {
		DSCAlarmEvent dscAlarmEvent = (DSCAlarmEvent) event;
		APIMessage apiMessage = dscAlarmEvent.getAPIMessage();
		APIMessage.APIMessageType apiMessageType = apiMessage.getAPIMessageType();

		DSCAlarmItemType dscAlarmItemType = null;
		APICode apiCode = APICode.getAPICodeValue(apiMessage.getAPICode());
		String apiData = apiMessage.getAPIData();
		DSCAlarmBindingConfig config = null;
		Item item = null;
		String itemName = "";

		boolean found = false;
		boolean suppressPanelMsg = false;
		int state = 0;
		int partitionId = apiMessage.getPartition();		
		int zoneId = apiMessage.getZone();
		
		setTimeStampState(apiMessage.getTimeStamp());
		
		switch(apiCode) {
			case CommandAcknowledge: /*500*/
				dscAlarmItemUpdate.setConnected(true);
				if(apiData.equals("000")) {
					dscAlarmItemType = DSCAlarmItemType.PANEL_CONNECTION;
				}

				if(suppressAcknowledgementMsgs) {
					suppressPanelMsg = true;
				}
				
				break;
			case SystemError: /*502*/
				dscAlarmItemType = DSCAlarmItemType.PANEL_SYSTEM_ERROR;
				break;
			case KeypadLEDState: /*510*/
			case KeypadLEDFlashState: /*511*/
				keypadLEDStateEventHandler(event);
				break;
			case TimeDateBroadcast: /*550*/
				dscAlarmItemType = DSCAlarmItemType.PANEL_TIME;
				updateItemType(DSCAlarmItemType.PANEL_TIME_BROADCAST, 0, 0, 1);

				if(suppressAcknowledgementMsgs) {
					suppressPanelMsg = true;
				}
				
				break;
			case PartitionReady: /*650*/
			case PartitionNotReady: /*651*/
			case PartitionReadyForceArming: /*653*/
			case FailureToArm: /*672*/
			case SystemArmingInProgress: /*674*/
				dscAlarmItemType = DSCAlarmItemType.PARTITION_STATUS;
				break;
			case PartitionArmed: /*652*/
				updateItemType(DSCAlarmItemType.PARTITION_ARMED, apiMessage.getPartition(), -1, 1);

				updateItemType(DSCAlarmItemType.PARTITION_ENTRY_DELAY, apiMessage.getPartition(), -1, 0);
				updateItemType(DSCAlarmItemType.PARTITION_EXIT_DELAY, apiMessage.getPartition(), -1, 0);

				dscAlarmItemType = DSCAlarmItemType.PARTITION_ARM_MODE;
				setPartitionStatus(partitionId, 0, apiMessage.getAPIName());
				break;
			case PartitionDisarmed: /*655*/
				updateItemType(DSCAlarmItemType.PARTITION_ARMED, apiMessage.getPartition(), -1, 0);

				updateItemType(DSCAlarmItemType.PARTITION_ENTRY_DELAY, apiMessage.getPartition(), -1, 0);
				updateItemType(DSCAlarmItemType.PARTITION_EXIT_DELAY, apiMessage.getPartition(), -1, 0);
				updateItemType(DSCAlarmItemType.PARTITION_IN_ALARM, apiMessage.getPartition(), -1, 0);

				dscAlarmItemType = DSCAlarmItemType.PARTITION_ARM_MODE;
				setPartitionStatus(partitionId, 0, apiMessage.getAPIName());
				break;
			case PartitionInAlarm: /*654*/
				updateItemType(DSCAlarmItemType.PARTITION_IN_ALARM, apiMessage.getPartition(), -1, 1);
				dscAlarmItemType = DSCAlarmItemType.PARTITION_STATUS;
				break;
			case ZoneAlarm: /*601*/
				state = 1;
			case ZoneAlarmRestore: /*602*/
				updateItemType(DSCAlarmItemType.ZONE_IN_ALARM, apiMessage.getPartition(), apiMessage.getZone(), state);
				dscAlarmItemType = DSCAlarmItemType.ZONE_ALARM_STATUS;
				break;
			case ZoneTamper: /*603*/
				state = 1;
			case ZoneTamperRestore: /*604*/
				updateItemType(DSCAlarmItemType.ZONE_TAMPER, apiMessage.getPartition(), apiMessage.getZone(), state);
				dscAlarmItemType = DSCAlarmItemType.ZONE_TAMPER_STATUS;
				break;
			case ZoneFault: /*605*/
				state = 1;
			case ZoneFaultRestore: /*606*/
				updateItemType(DSCAlarmItemType.ZONE_FAULT, apiMessage.getPartition(), apiMessage.getZone(), state);
				dscAlarmItemType = DSCAlarmItemType.ZONE_FAULT_STATUS;
				break;
			case ZoneOpen: /*609*/
				state = 1;
			case ZoneRestored: /*610*/
				updateItemType(DSCAlarmItemType.ZONE_TRIPPED, apiMessage.getPartition(), apiMessage.getZone(), state);
				dscAlarmItemType = DSCAlarmItemType.ZONE_GENERAL_STATUS;
				break;
			case FireKeyAlarm: /*621*/
				state = 1;
			case FireKeyRestored: /*622*/
				updateItemType(DSCAlarmItemType.PANEL_FIRE_KEY_ALARM, apiMessage.getPartition(), apiMessage.getZone(), state);
				break;
			case PanicKeyAlarm: /*625*/
				state = 1;
			case PanicKeyRestored: /*626*/
				updateItemType(DSCAlarmItemType.PANEL_PANIC_KEY_ALARM, apiMessage.getPartition(), apiMessage.getZone(), state);
				break;
			case AuxiliaryKeyAlarm: /*625*/
				state = 1;
			case AuxiliaryKeyRestored: /*626*/
				updateItemType(DSCAlarmItemType.PANEL_AUX_KEY_ALARM, apiMessage.getPartition(), apiMessage.getZone(), state);
				break;
			case AuxiliaryInputAlarm: /*625*/
				state = 1;
			case AuxiliaryInputAlarmRestored: /*631*/
				updateItemType(DSCAlarmItemType.PANEL_AUX_INPUT_ALARM, apiMessage.getPartition(), apiMessage.getZone(), state);
				break;
			case EntryDelayInProgress: /*656*/
				updateItemType(DSCAlarmItemType.PARTITION_ENTRY_DELAY, apiMessage.getPartition(), -1, 1);
				break;				
			case ExitDelayInProgress: /*656*/
				updateItemType(DSCAlarmItemType.PARTITION_EXIT_DELAY, apiMessage.getPartition(), -1, 1);
				break;
			case UserClosing: /*700*/
			case SpecialClosing: /*701*/
			case PartialClosing: /*702*/
			case UserOpening: /*750*/
			case SpecialOpening: /*751*/
				dscAlarmItemType = DSCAlarmItemType.PARTITION_OPENING_CLOSING_MODE;
				break;
			case TroubleLEDOn: /*840*/
				dscAlarmItemType = DSCAlarmItemType.PANEL_TROUBLE_LED;
				break;
			case TroubleLEDOff: /*841*/
				updateItemType(DSCAlarmItemType.PANEL_SERVICE_REQUIRED, 0, 0, 0);
				updateItemType(DSCAlarmItemType.PANEL_AC_TROUBLE, 0, 0, 0);
				updateItemType(DSCAlarmItemType.PANEL_TELEPHONE_TROUBLE, 0, 0, 0);
				updateItemType(DSCAlarmItemType.PANEL_FTC_TROUBLE, 0, 0, 0);
				updateItemType(DSCAlarmItemType.PANEL_ZONE_FAULT, 0, 0, 0);
				updateItemType(DSCAlarmItemType.PANEL_ZONE_TAMPER, 0, 0, 0);
				updateItemType(DSCAlarmItemType.PANEL_ZONE_LOW_BATTERY, 0, 0, 0);
				updateItemType(DSCAlarmItemType.PANEL_TIME_LOSS, 0, 0, 0);
				dscAlarmItemType = DSCAlarmItemType.PANEL_TROUBLE_LED;
				break;
			case PanelBatteryTrouble: /*800*/
			case PanelACTrouble: /*802*/
			case SystemBellTrouble: /*806*/
			case TLMLine1Trouble: /*810*/
			case TLMLine2Trouble: /*812*/
			case FTCTrouble: /*814*/
			case GeneralDeviceLowBattery: /*821*/
			case WirelessKeyLowBatteryTrouble: /*825*/
			case HandheldKeypadLowBatteryTrouble: /*827*/
			case GeneralSystemTamper: /*829*/
			case HomeAutomationTrouble: /*831*/
			case KeybusFault: /*896*/
				dscAlarmItemType = DSCAlarmItemType.PANEL_TROUBLE_MESSAGE;
				break;
			case PanelBatteryTroubleRestore: /*801*/
			case PanelACRestore: /*803*/
			case SystemBellTroubleRestore: /*807*/
			case TLMLine1TroubleRestore: /*811*/
			case TLMLine2TroubleRestore: /*813*/
			case GeneralDeviceLowBatteryRestore: /*822*/
			case WirelessKeyLowBatteryTroubleRestore: /*826*/
			case HandheldKeypadLowBatteryTroubleRestore: /*828*/
			case GeneralSystemTamperRestore: /*830*/
			case HomeAutomationTroubleRestore: /*832*/
			case KeybusFaultRestore: /*897*/
				updateItemType(DSCAlarmItemType.PANEL_TROUBLE_MESSAGE, 0, 0, 0);
				break;
			case VerboseTroubleStatus: /*849*/
				verboseTroubleStatusHandler(event);
				break;			
			case CodeRequired: /*900*/
				api.sendCommand(APICode.CodeSend);
				break;
			case LEDStatus: /*903*/
				int aData = Integer.parseInt(apiData.substring(0,1));
				switch(aData) {
					case 1:
						dscAlarmItemType = DSCAlarmItemType.KEYPAD_READY_LED;
						break;
					case 2:
						dscAlarmItemType = DSCAlarmItemType.KEYPAD_ARMED_LED;
						break;
					case 3:
						dscAlarmItemType = DSCAlarmItemType.KEYPAD_MEMORY_LED;
						break;
					case 4:
						dscAlarmItemType = DSCAlarmItemType.KEYPAD_BYPASS_LED;
						break;
					case 5:
						dscAlarmItemType = DSCAlarmItemType.KEYPAD_TROUBLE_LED;
						break;
					case 6:
						dscAlarmItemType = DSCAlarmItemType.KEYPAD_PROGRAM_LED;
						break;
					case 7:
						dscAlarmItemType = DSCAlarmItemType.KEYPAD_FIRE_LED;
						break;
					case 8:
						dscAlarmItemType = DSCAlarmItemType.KEYPAD_BACKLIGHT_LED;
						break;
					case 9:
						dscAlarmItemType = DSCAlarmItemType.KEYPAD_AC_LED;
						break;
				}
			default:
				break;
		
		}

		if(!suppressPanelMsg) {
			setPanelMessage(apiMessage.getAPIDescription());
		}

		logger.debug("dscAlarmEventRecieved(): Event received! Looking for item: {}", dscAlarmItemType);

		if(dscAlarmItemType != null) {
			for (DSCAlarmBindingProvider prov : providers) {
				for (String iName : prov.getItemNames()) {
					config = prov.getDSCAlarmBindingConfig(iName);
					if(config != null) {
						switch(apiMessageType) {
							case PANEL_EVENT:
								if(dscAlarmItemType == config.getDSCAlarmItemType()) {
									itemName = iName;
									found = true;
								}
								break;
							case PARTITION_EVENT:
								if(partitionId == config.getPartitionId() && dscAlarmItemType == config.getDSCAlarmItemType()) {
									itemName = iName;
									found = true;
								}
								break;
							case ZONE_EVENT:
								if(zoneId == config.getZoneId() && dscAlarmItemType == config.getDSCAlarmItemType()) {
									itemName = iName;
									found = true;
								}
								break;
							case KEYPAD_EVENT:
								if(dscAlarmItemType == config.getDSCAlarmItemType()) {
									itemName = iName;
									found = true;
								}
								break;
							default:
								found = false;
								break;
						}
						
					}
					
					if(found) {
						item = prov.getItem(itemName);
						dscAlarmItemUpdate.updateDeviceItem(item, config, eventPublisher, dscAlarmEvent);
						pollTimeStart = 0;
						break;
					}
				}
				
				if(found)
					break;
			}
		}
	}

	@Override
	public boolean sendDSCAlarmCommand(String command, String data) {
		logger.debug("sendDSCAlarmCommand(): Attempting to send DSC Alarm Command: command - {} - data: {}", command, data);
		
		try {
			APICode apiCode = APICode.getAPICodeValue(command);
			
			if(connectorType.equals(DSCAlarmConnectorType.SERIAL) && apiCode.equals(APICode.KeySequence)) {
				return sendKeySequence(data);
			}
			else {
				return api.sendCommand(apiCode, data);
			}
		} catch (Exception e) {
			logger.error("sendDSCAlarmCommand(): Failed to send DSC Alarm Command! - {}", e);
			return false;
		}
	}
}

