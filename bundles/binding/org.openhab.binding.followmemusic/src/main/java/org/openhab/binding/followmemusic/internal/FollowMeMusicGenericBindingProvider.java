/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.followmemusic.internal;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.followmemusic.model.Door;
import org.followmemusic.model.Room;
import org.followmemusic.model.Sensor;
import org.openhab.binding.followmemusic.FollowMeMusicBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author FollowMe
 * @since 0.1.0.dev
 */
public class FollowMeMusicGenericBindingProvider extends AbstractGenericBindingProvider implements FollowMeMusicBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(FollowMeMusicBindingProvider.class);
	static final String BINDING_TYPE = "followmemusic";
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return BINDING_TYPE;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
				
		if(!(item instanceof GroupItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only GroupItem- and NumberItem are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
				
		if (bindingConfig != null) {
			FollowMeMusicBindingConfig config = parseBindingConfig(item, bindingConfig);
			addBindingConfig(item, config);
		}
		else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.binding.modbus.tcp.master.ModbusBindingProvider#getConfig(java.lang.String)
	 */
	@Override
	public FollowMeMusicBindingConfig getConfig(String name) {
		return (FollowMeMusicBindingConfig) bindingConfigs.get(name);
	}
	
	/**
	 * Checks if the bindingConfig contains a valid binding type and returns an appropriate instance.
	 * 
	 * @param item
	 * @param bindingConfig
	 * 
	 * @throws BindingConfigParseException if bindingConfig is no valid binding type
	 */
	protected FollowMeMusicBindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		
		// Data
		FollowMeMusicItemType itemType = FollowMeMusicItemType.ROOM;
		Object itemModel = null;
		
		// Item type
		Boolean isGroup = item instanceof GroupItem;
				
		// Is valid structure ?
		Pattern pattern = Pattern.compile("^(ROOM|DOOR|SENSOR):(\\d+)$");
		Matcher matcher = pattern.matcher(bindingConfig);
		
		// Valid
		if(matcher.matches())
		{
			Boolean error = true;
			
			// Identify item
			String _itemType = matcher.group(1);
			if(_itemType.equals("ROOM")) {
				if(isGroup) {
					
					// Extract data
					int id = Integer.parseInt(matcher.group(2));
					
					// Generate model
					Room room = new Room(id, item.getName());
					itemModel = room;
					itemType = FollowMeMusicItemType.ROOM;
					
					// No error
					error = false;
				}
			}
			else if(_itemType.equals("DOOR")) {
				if(isGroup) {
					
					// Extract data
					int id = Integer.parseInt(matcher.group(2));

					// Generate model
					Door door = new Door(id, item.getName());
					itemModel = door;
					itemType = FollowMeMusicItemType.DOOR;
					
					// No error
					error = false;
				}
			}
			else if(_itemType.equals("SENSOR")) {
				if(!isGroup) {
					
					// Extract data
					int id = Integer.parseInt(matcher.group(2));
					List<String> groups = item.getGroupNames();
					
					// Generate model
					Sensor sensor = new Sensor(id, item.getName());
					itemModel = sensor;
					itemType = FollowMeMusicItemType.SENSOR;
					
					// Search
					for(String g : groups) {
						if (this.providesBindingFor(g)) {
							FollowMeMusicBindingConfig config = this.getConfig(g);
									
							
							switch(config.itemType)
							{
								case ROOM: // Link to room
									Room room = (Room)config.itemModel;
									sensor.setRoom(room);
									room.getSensors().add(sensor);
									break;
								
								case DOOR: // Link to door
									Door door = (Door)config.itemModel;
									sensor.setDoor(door);
									if(door.getSensors()[0] == null) {
										door.getSensors()[0] = sensor;
									}
									else if(door.getSensors()[1] == null) {
										door.getSensors()[1] = sensor;
									}
									else {
										throw new BindingConfigParseException("item '" + item.getName()
												+ "' is of type '" + item.getClass().getSimpleName()
												+ "' with bindingConfig : '" + bindingConfig
												+ "', Trying to connect a sensor to a door that already have 2 sensors - please check your *.items configuration");
									}
									break;
								
								default:
									throw new BindingConfigParseException("item '" + item.getName()
											+ "' is of type '" + item.getClass().getSimpleName()
											+ "' with bindingConfig : '" + bindingConfig
											+ "', Trying to connect a sensor to an invalid item - please check your *.items configuration");
							}
							
							
						}
					}
					
					// No errors
					error = false;
				}
			}
			// Invalid config type
			else {
				// Wrong binding configuration
				throw new BindingConfigParseException("item '" + item.getName()
						+ "' is of type '" + item.getClass().getSimpleName()
						+ "' with bindingConfig : '" + bindingConfig
						+ "', only bindingConfig format 'ROOM:{id}' or 'DOOR:{id}' or 'SENSOR:{id}' is accepted - please check your *.items configuration");
			}
			
			if(error) {
				// Wrong item type
				throw new BindingConfigParseException("item '" + item.getName()
						+ "' is of type '" + item.getClass().getSimpleName()
						+ "' with bindingConfig : '" + bindingConfig
						+ "', ROOM and DOOR must be GroupItem, SENSOR must be NumberItem - please check your *.items configuration");
			}
		}
		else {
			// Wrong binding configuration
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "' with bindingConfig : '" + bindingConfig
					+ "', only bindingConfig format 'ROOM:{id}' or 'DOOR:{id}' or 'SENSOR:{id}' is accepted - please check your *.items configuration");
		}
		
		return new FollowMeMusicBindingConfig(item, itemType, itemModel);
	}
	
	
	public enum FollowMeMusicItemType
	{
		ROOM,
		DOOR,
		SENSOR
	}
	
	public class FollowMeMusicBindingConfig implements BindingConfig {
	
		private Item item;
		private FollowMeMusicItemType itemType;
		private Object itemModel;
		
		public Item getItem() {
			return this.item;
		}
		
		public Object getItemModel() {
			return this.itemModel;
		}
		
		public FollowMeMusicItemType getItemType() {
			return this.itemType;
		}
		
		/**
		 * Constructor for config object
		 * @param item
		 * @param config
		 * @throws BindingConfigParseException if 
		 */
		FollowMeMusicBindingConfig(Item item, FollowMeMusicItemType type, Object model) throws BindingConfigParseException {
			this.item = item;
			this.itemModel = model;
			this.itemType = type;
		}
		
	}
	
	
}
