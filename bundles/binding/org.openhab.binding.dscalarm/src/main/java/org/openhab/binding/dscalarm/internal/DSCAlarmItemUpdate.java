/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.dscalarm.DSCAlarmBindingConfig;
import org.openhab.binding.dscalarm.internal.model.DSCAlarmDeviceType;
import org.openhab.binding.dscalarm.internal.model.Panel;
import org.openhab.binding.dscalarm.internal.model.Partition;
import org.openhab.binding.dscalarm.internal.model.Zone;
import org.openhab.binding.dscalarm.internal.model.Keypad;
import org.openhab.core.items.Item;
import org.openhab.core.events.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to manage OpenHAB item updates
 * 
 * @author Russell Stephens
 * @since 1.6.0
 */
public class DSCAlarmItemUpdate {
	
	private static final Logger logger = LoggerFactory.getLogger(DSCAlarmItemUpdate.class);

	public DSCAlarmItemUpdate() {
		
	}
	
	/**
	 * Keep maps of the DSC Alarm Device types
	 */
	private Map<Integer, Panel> panelMap = new HashMap<Integer, Panel>();
	private Map<Integer, Partition> partitionMap = new HashMap<Integer, Partition>();
	private Map<Integer, Zone> zoneMap = new HashMap<Integer, Zone>();
	private Map<Integer, Keypad> keypadMap = new HashMap<Integer, Keypad>();
	private boolean connected = false;
	String sysMessage = "";
	
	/**
	 * Get connection status
	 * 
	 * @return
	 */
	public boolean getConnected() {
		return connected;
	}

	/**
	 * Set connection status
	 * 
	 * @param connected
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	/**
	 * Set the system message
	 * 
	 * @param sysMessage
	 */
	public void setSysMessage(String sysMessage) {
		this.sysMessage = sysMessage;
	}
	
	/**
	 * Update a DSC Alarm Device Item
	 * 
	 * @param item
	 * @param config
	 * @param eventPublisher
	 * @param event
	 */
	public synchronized void updateDeviceItem(Item item, DSCAlarmBindingConfig config, EventPublisher eventPublisher, DSCAlarmEvent event) {
		logger.debug("updateDeviceItem(): Item Name: {}", item.getName());

		if( config != null) {
			DSCAlarmDeviceType dscAlarmDeviceType = config.getDeviceType(); 
			int panelId = 1;
			int partitionId;
			int zoneId;
			int keypadId = 1;
	
			switch (dscAlarmDeviceType) {
				case PANEL:
					Panel panel = null;
		
					//Right now we can only connect to one Panel so we only create a single Panel object;
					if(panelMap.isEmpty()) {
						panel = new Panel(panelId);
						panelMap.put(panelId, panel);
					}
					else {
						panel = panelMap.get(1);
					}
					
					if(config.getDSCAlarmItemType() == DSCAlarmItemType.PANEL_CONNECTION)
						updateDeviceProperties(item, config, connected ? 1:0, "Panel Connected");
	
					if((config.getDSCAlarmItemType() == DSCAlarmItemType.PANEL_MESSAGE) && (sysMessage != "")) {
						updateDeviceProperties(item, config, 0, sysMessage);
						sysMessage = "";
					}
	
					if(event != null) {
						panel.handleEvent(item, config, eventPublisher, event);
					}
					else {
						panel.refreshItem(item, config, eventPublisher);
					}
					break;
				case PARTITION:
					partitionId = config.getPartitionId();
					Partition partition = partitionMap.get(partitionId);
					if(partition == null) {
						partition = new Partition(partitionId);
						partitionMap.put(partitionId, partition);
					}
					
					if(event != null) {
						partition.handleEvent(item, config, eventPublisher, event);
					}
					else {
						partition.refreshItem(item, config, eventPublisher);
					}
					break;
				case ZONE:
					partitionId = config.getPartitionId();
					zoneId = config.getZoneId();
					Zone zone = zoneMap.get(zoneId);
					if(zone == null) {
						zone = new Zone(partitionId, zoneId);
						zoneMap.put(zoneId, zone);
					}
					
					if(event != null) {
						zone.handleEvent(item, config, eventPublisher, event);
					}
					else {
						zone.refreshItem(item, config, eventPublisher);
					}
					break;
				case KEYPAD:
					Keypad keypad = null;
					
					//There is only one Keypad object per panel;
					if(keypadMap.isEmpty()) {
						keypad = new Keypad(keypadId);
						keypadMap.put(keypadId, keypad);
					}
					else {
						keypad = keypadMap.get(1);
					}
					
					if(event != null) {
						keypad.handleEvent(item, config, eventPublisher, event);
					}
					else {
						keypad.refreshItem(item, config, eventPublisher);
					}
					break;
				default:
					logger.debug("updateDeviceItem(): Item not updated.");
					break;
			}
		}
	}
	
	/**
	 * Update DSC Alarm Device Properties
	 * 
	 * @param item
	 * @param config
	 * @param state
	 * @param description
	 */
	public synchronized void updateDeviceProperties(Item item, DSCAlarmBindingConfig config, int state, String description) {
		logger.debug("updateDeviceProperties(): Item Name: {}", item.getName());
	
		if( config != null) {
			DSCAlarmDeviceType dscAlarmDeviceType = config.getDeviceType(); 
			int panelId = 1;
			int partitionId;
			int zoneId;
			int keypadId = 1;
	
			switch (dscAlarmDeviceType) {
				case PANEL:
					Panel panel = panelMap.get(panelId);
					if(panel != null)
						panel.updateProperties(item, config, state, description);
					break;
				case PARTITION:
					partitionId = config.getPartitionId();
					Partition partition = partitionMap.get(partitionId);
					if(partition != null)
						partition.updateProperties(item, config, state, description);
					break;
				case ZONE:
					partitionId = config.getPartitionId();
					zoneId = config.getZoneId();
					Zone zone = zoneMap.get(zoneId);
					if(zone != null)
						zone.updateProperties(item, config, state, description);
					break;
				case KEYPAD:
					Keypad keypad = keypadMap.get(keypadId);
					if(keypad != null)
						keypad.updateProperties(item, config, state, description);
					break;
				default:
					logger.debug("updateDeviceProperties(): Item properties not updated.");
					break;
			}
		}
	}
}
