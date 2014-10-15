/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
import org.openhab.binding.dscalarm.DSCAlarmBindingConfig;
import org.openhab.binding.dscalarm.DSCAlarmBindingProvider;
import org.openhab.binding.dscalarm.internal.connector.DSCAlarmConnectorType;
import org.openhab.binding.dscalarm.internal.model.DSCAlarmDeviceType;
import org.openhab.binding.dscalarm.internal.protocol.API;
import org.openhab.binding.dscalarm.internal.protocol.APICode;
import org.openhab.binding.dscalarm.internal.protocol.APIMessage;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DSCAlarmActiveBinding Class. Polls the DSC Alarm panel,
 * responds to item commands, and also handles events coming 
 * from the DSC Alarm panel.
 * @author Russell Stephens
 * @since 1.6.0
 */

public class DSCAlarmActiveBinding extends AbstractActiveBinding<DSCAlarmBindingProvider> implements ManagedService, DSCAlarmEventListener {

	private static final Logger logger = LoggerFactory.getLogger(DSCAlarmActiveBinding.class);

	private long refreshInterval = 5000;

	private DSCAlarmConnectorType connectorType = null;
	
	/** The serial port name of the DSC-IT100 Serial Interface. 
	 * 	Valid values are e.g. COM1 for Windows and /dev/ttyS0 or /dev/ttyUSB0 for Linux.
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

	/** API Session for EyezOn Envisalink 3/2DS */
	private API api = null;
	private boolean connected = false;
	
	private long pollTime = 0;
	private long pollPeriod = 0;
	
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
	 * OpenHAB always calls updated to indicate that the config is updated.
	 * Activation is done there.
	 */
	public void activate() {
		logger.debug("Activate DSC Alarm");
	}
	
	/**
	 * Deactivates the binding.
	 */
 	public void deactivate() {
		logger.debug("Deactivate DSC Alarm");
		closeConnection();
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
		
				logger.debug("internalReceiveCommand():  Item Name: {} Command: {} Item Device Type: {}",itemName,command,dscAlarmDeviceType);

				if(connected) {
					switch(dscAlarmDeviceType) {
						case PANEL:
							if(dscAlarmBindingConfig.getDSCAlarmItemType() == DSCAlarmItemType.PANEL_CONNECTION) {
								if(command.toString() == "0") {
									closeConnection();
									if(!connected) {
										dscAlarmItemUpdate.setConnected(false);
										dscAlarmItemUpdate.updateDeviceProperties(item, dscAlarmBindingConfig, 0, "Disconnected");
									}
								}
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
				dscAlarmUpdateMap.put(itemName, dscAlarmBindingConfig);
				itemHasChanged = true;
			}
		}
	}
	
	@Override
	protected void execute() {
		logger.debug("DSC Alarm Execute");

		if(api != null) {
			connected = api.isConnected();
		}
		
		if(connected) {
			if(pollPeriod == 0) {
				pollPeriod = System.currentTimeMillis();
			}
			
			pollTime = ((System.currentTimeMillis() - pollPeriod) / 1000) / 60;
			
			//Send Poll command to the DSC Alarm if idle for 15 minutes
			if(pollTime >= 1) {
				api.sendCommand(APICode.Poll);
				pollPeriod = 0;
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

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "DSC Alarm Monitor Service";
	}

	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("updated(): Configuration updated, config {}", config != null ? true:false);
		
		if (config != null) {
			
			serialPort = (String) config.get("serialPort");
			ipAddress = (String) config.get("ip");

			if (serialPort != null && ipAddress != null) {
				logger.error("updated(): Can only configure one connection type at a time: Serial Port or Ip Address!");
				return;
			}

			if(serialPort != null) {
				connectorType = DSCAlarmConnectorType.SERIAL;
				String baud = (String) config.get("baud");
				try {
					if(baud != null && StringUtils.isNotBlank(baud))
						baudRate = Integer.parseInt((String) config.get("baud"));
				} catch(NumberFormatException numberFormatException) {
					logger.error("updated(): Baud Rate not configured correctly!");
					return;
				}
			}
			
			if(ipAddress != null) {
				connectorType = DSCAlarmConnectorType.TCP;
			}
			
			logger.debug("updated(): Connector Type: {}",connectorType);
			
			
			password = (String) config.get("password");
			userCode = (String) config.get("usercode");
			
		}
		else {
			logger.debug("updated(): No Configuration!");
			return;
		}
		
		initialize();
	}

	/**
	 * Initializes the binding. 
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
	 * Open a TCP connection to the DSC Alarm Panel
	 * @param ip
	 * @param pw
	 * @param uc
	 */
 	private void openConnection() {

    	switch (connectorType) {
	    	case SERIAL:
	     		if (api == null) {
	    			api = new API(serialPort, baudRate);
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
			dscAlarmItemUpdate.setSysMessage("PANEL DISCONNECTED!!!");
			itemName = getItemName(DSCAlarmItemType.PANEL_MESSAGE,0,0);
			if(itemName != "") {
				updateItem(itemName);
			}
			logger.error("reconnect(): API reconnection failed!");	
		}
	}

	/**
	 * Close TCP connection to the DSC Alarm Panel and remove the Event Listener
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
	
	private String getItemName(DSCAlarmItemType dscAlarmItemType, int partitionId, int zoneId) {
		String itemName = "";
		DSCAlarmBindingConfig config = null;
		
		for (DSCAlarmBindingProvider prov : providers) {
			for (String iName : prov.getItemNames()) {
				config = prov.getDSCAlarmBindingConfig(iName);
				if(config.getDSCAlarmItemType() == dscAlarmItemType) {
					if((config.getPartitionId() == partitionId) && (config.getZoneId() == zoneId)) {
						itemName = iName;
						break;
					}
				}
			}
		}
		
		return itemName;
	}
	
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

	private void keypadLEDStateEventHandler(EventObject event) {
		DSCAlarmEvent dscAlarmEvent = (DSCAlarmEvent) event;
		APIMessage apiMessage = dscAlarmEvent.getAPIMessage();
		DSCAlarmItemType[] dscAlarmItemTypes = 
			{DSCAlarmItemType.KEYPAD_READY_LED,DSCAlarmItemType.KEYPAD_ARMED_LED,
			DSCAlarmItemType.KEYPAD_MEMORY_LED,DSCAlarmItemType.KEYPAD_BYPASS_LED,DSCAlarmItemType.KEYPAD_TROUBLE_LED,
			DSCAlarmItemType.KEYPAD_PROGRAM_LED,DSCAlarmItemType.KEYPAD_FIRE_LED,DSCAlarmItemType.KEYPAD_BACKLIGHT_LED};

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
	
	@Override
	public String toString() {
		return "DSC Alarm: IP Address=" + ipAddress;
	}

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
		int forLimit = 1;

		boolean found = false;
		
		switch(apiCode) {
			case CommandAcknowledge: /*500*/
				dscAlarmItemUpdate.setConnected(true);
				dscAlarmItemType = DSCAlarmItemType.PANEL_CONNECTION;
				break;
			case SystemError: /*502*/
				dscAlarmItemType = DSCAlarmItemType.PANEL_SYSTEM_ERROR;
				break;
			case KeypadLEDState: /*510*/
			case KeypadLEDFlashState: /*511*/
				keypadLEDStateEventHandler(event);
				break;
			case TimeDateBroadcast: /*550*/
				dscAlarmItemType = DSCAlarmItemType.PANEL_TIME_DATE;
				break;
			case PartitionReady: /*650*/
			case PartitionNotReady: /*651*/
			case PartitionReadyForceArming: /*653*/
			case PartitionInAlarm: /*654*/
			case FailureToArm: /*672*/
			case SystemArmingInProgress:
				dscAlarmItemType = DSCAlarmItemType.PARTITION_STATUS;
				break;
			case PartitionArmed: /*652*/
				forLimit = 2;
			case PartitionDisarmed: /*655*/
				dscAlarmItemType = DSCAlarmItemType.PARTITION_ARM_MODE;
				break;
			case ZoneAlarm: /*601*/
			case ZoneAlarmRestore: /*602*/
				dscAlarmItemType = DSCAlarmItemType.ZONE_ALARM_STATUS;
				break;
			case ZoneTamper: /*603*/
			case ZoneTamperRestore: /*604*/
				dscAlarmItemType = DSCAlarmItemType.ZONE_TAMPER_STATUS;
				break;
			case ZoneFault: /*605*/
			case ZoneFaultRestore: /*606*/
				dscAlarmItemType = DSCAlarmItemType.ZONE_FAULT_STATUS;
				break;
			case ZoneOpen: /*609*/
			case ZoneRestored: /*610*/
				dscAlarmItemType = DSCAlarmItemType.ZONE_GENERAL_STATUS;
				break;
			case CodeRequired: /*900*/
				api.sendCommand(APICode.CodeSend, api.getUserCode());
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

		int partitionId = apiMessage.getPartition();
		
		int zoneId = apiMessage.getZone();
		
		logger.debug("dscAlarmEventRecieved(): Event received! Looking for item: {}", dscAlarmItemType);

		for (int i=0; i <= forLimit; i++) {
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
							pollPeriod = 0;
							break;
						}
					}
					
					if(found)
						break;
				}
			}
			
			if(dscAlarmItemType ==  DSCAlarmItemType.PARTITION_ARM_MODE && apiCode == APICode.PartitionArmed) {
				dscAlarmItemType = DSCAlarmItemType.PARTITION_STATUS;
				apiMessageType = APIMessage.APIMessageType.PARTITION_EVENT;
				found = false;
			}
			else {
				//Want to post the entire event message as a Panel Message so the parameters are reset.
				dscAlarmItemType = DSCAlarmItemType.PANEL_MESSAGE;
				apiMessageType = APIMessage.APIMessageType.PANEL_EVENT;
				partitionId = 0;
				zoneId = 0;
				found = false;
			}
		}
	}
}

