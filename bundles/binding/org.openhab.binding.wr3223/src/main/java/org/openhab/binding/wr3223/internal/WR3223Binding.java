/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wr3223.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.wr3223.WR3223BindingProvider;
import org.openhab.binding.wr3223.WR3223CommandType;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * This class is an active binding to control the WR3223. To control the WR3223 over RS232/USB it must receive 
 * at least every 20 second a message.
 * 
 * @author Michael Fraefel
 * @since 1.7.0
 */
public class WR3223Binding extends AbstractActiveBinding<WR3223BindingProvider> {

	private static final WR3223CommandType[] READ_COMMANDS = {
		WR3223CommandType.TEMPERATURE_EVAPORATOR,
		WR3223CommandType.TEMPERATURE_CONDENSER,
		WR3223CommandType.TEMPERATURE_OUTSIDE,
		WR3223CommandType.TEMPERATURE_AFTER_HEAT_EXCHANGER,
		WR3223CommandType.TEMPERATURE_SUPPLY_AIR,
		WR3223CommandType.TEMPERATURE_AFTER_BRINE_PREHEATING,
		WR3223CommandType.TEMPERATURE_AFTER_PREHEATING_RADIATOR,
		WR3223CommandType.VENTILATION_LEVEL,
		WR3223CommandType.ROTATION_SPEED_SUPPLY_AIR_MOTOR,
		WR3223CommandType.ROTATION_SPEED_EXHAUST_AIR_MOTOR,
		WR3223CommandType.OPERATION_MODE
	};
	
	
	private static final Logger logger = 
		LoggerFactory.getLogger(WR3223Binding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
	 */
	private BundleContext bundleContext;

	
	/** 
	 * the refresh interval which is used to poll values from the WR3223
	 * server (optional, defaults to 15000ms)
	 */
	private long refreshInterval = 15000;
	
	/**
	 * Host if connection over IP is used.
	 */
	private String host;
	
	/**
	 * Port if connection over IP is used.
	 */	
	private int port;
	
	/**
	 * Serial port if connection over serial interface is used.
	 */
	private String serialPort;
	
	/**
	 * Controller address.
	 */
	private byte controllerAddr=1;
	
	/**
	 * WR3223 Connector
	 */
	private AbstractWR3223Connector connector;
	
	private boolean wpOn = false;
	private boolean additionalHieaterOn = false;
	private boolean cooling = false;
	private int ventilationLevel = 2;
	private int operationMode = 2;
	private int targetTemperatureSupplyAir = 20;
	
	private boolean hasUpdate = false;
	
	public WR3223Binding() {
	}
		
	
//	private ItemRegistry itemRegistry;
//	public void setItemRegistry(ItemRegistry itemRegistry) {
//		this.itemRegistry = itemRegistry;
//	}
//
//	public void unsetItemRegistry(ItemRegistry itemRegistry) {
//		this.itemRegistry = null;
//	}	
	
	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;		
		configure(configuration);
	}
	
	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * @param configuration Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		configure(configuration);
	}
	
	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or
	 * mandatory references are no longer satisfied or the component has simply been stopped.
	 * @param reason Reason code for the deactivation:<br>
	 * <ul>
	 * <li> 0 – Unspecified
     * <li> 1 – The component was disabled
     * <li> 2 – A reference became unsatisfied
     * <li> 3 – A configuration was changed
     * <li> 4 – A configuration was deleted
     * <li> 5 – The component was disposed
     * <li> 6 – The bundle was stopped
     * </ul>
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;
		if(connector != null){
			try{
				connector.close();
			}catch(IOException ex){
				logger.error("Error by closing connector.", ex);
			}
		}
	}
	
	/**
	 * Configure binding.
	 * @param configuration
	 */
	private void configure(final Map<String, Object> configuration) {		
		//Configure refresh
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
			if(refreshInterval>20000){
				logger.warn("Refresh interval should not be over 20 seconds.");
			}
		}
		
		//Controller
		String controllerAddrString = (String) configuration.get("controllerAddr");
		if (StringUtils.isNotBlank(controllerAddrString)) {
			controllerAddr = Byte.parseByte(controllerAddrString);
		}
		
		//Configure connection
		String hostString = (String) configuration.get("host");
		String portString = (String) configuration.get("port");
		String serialPortString = (String) configuration.get("serialPort");
		if (StringUtils.isNotBlank(hostString) && StringUtils.isNotBlank(portString)) {
			host = hostString;
			port = Integer.parseInt(portString);
			serialPort = null;
			logger.info("Connect over IP to host {}:{}", host, port);
			setProperlyConfigured(true);
		}else if(StringUtils.isNotBlank(serialPortString)){
			serialPort = serialPortString;
			host = null;
			logger.info("Connect over serial port {}", serialPort);
			setProperlyConfigured(true);			
		}else{
			setProperlyConfigured(false);
			logger.error("No connection configured");
		}

		//Close the connector if already one is open.
		if(connector != null){
			try{
				connector.close();
			}catch(IOException ex){
				logger.error("Error by closing connector.", ex);
			}
		}
		

	}	

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "WR3223 Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {

		//setup connector
		try{
			if(connector == null){
				if(host != null){
					TcpWR3223Connector tcpConnector = new TcpWR3223Connector();
					tcpConnector.connect(host, port);
					connector = tcpConnector;
				}else{
					
				}
			}
		}catch(IOException ex){			
			logger.error("Couldn't establish connection to WR3223.", ex);
			connector = null;
			return;
		}
	
		
		try {		
			int data = 0;
			if(wpOn){
				data += 1;
			}
			if(ventilationLevel == 2 || ventilationLevel == 1){
				data += 2;
			}
			if(ventilationLevel == 3|| ventilationLevel == 1){
				data += 4;
			}
			if(additionalHieaterOn){
				data += 8;
			}
			if(ventilationLevel == 0){
				data += 16;
			}
			if(cooling){
				data += 32;
			}		
			connector.write(controllerAddr, WR3223Commands.SW, String.valueOf(data));
			
			if(hasUpdate){
				connector.write(controllerAddr, WR3223Commands.MD, String.valueOf(operationMode));
				connector.write(controllerAddr, WR3223Commands.SP, String.valueOf(targetTemperatureSupplyAir));
			}
									
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Read values from WR3223
		for(WR3223CommandType readCommand : READ_COMMANDS){
			readAndPublishValue(readCommand);
		}
	

	}
	
	/**
	 * Read value of given command and publish it to the bus.
	 * @param wr3223CommandType
	 */
	private void readAndPublishValue(WR3223CommandType wr3223CommandType) {
		List<String> itemNames = getBoundItemsForType(wr3223CommandType);
		if(itemNames.size()>0){
			try {
				String value = connector.read(controllerAddr, wr3223CommandType.getWr3223Command());
				if(value != null){
					State state = null;
					if(wr3223CommandType.getItemClass() == NumberItem.class){
						try{
							state = DecimalType.valueOf(value.trim());
						}catch(NumberFormatException nfe){
							logger.error("Can't set value {} to item type {} because it's not a decimal number.", value, wr3223CommandType.getCommand());
						}
					}
					if(state != null){
						for(String itemName : itemNames){
							eventPublisher.postUpdate(itemName, state);
						}
					}
				}else{
					logger.error("Can't set NULL value to item type {}.", wr3223CommandType.getCommand());
					
				}
			} catch (IOException ex) {
				logger.error("Error by reading values from WR3223:", ex);
				if(connector != null){
					try{
						connector.close();
					}catch(IOException ex2){
						logger.error("Error by closing connector.", ex2);
					}finally{
						connector = null;
					}
				}				
			}
		}
	}	
	
	private List<String> getBoundItemsForType( WR3223CommandType wr3223CommandType) {
		List<String> itemNames = new ArrayList<String>(); 
		for (WR3223BindingProvider provider : providers) {
			for (String itemName : provider.getItemNamesForType(wr3223CommandType)) {
				itemNames.add(itemName);
			}
		}
		return itemNames;
	}
	
	private WR3223CommandType getWR3223BindingConfig(String itemName) {
		WR3223CommandType type = null;
		Iterator<WR3223BindingProvider> providerIt = providers.iterator();
		while(providerIt.hasNext() && type == null){
			type = providerIt.next().getWR3223CommandTypeForItemName(itemName);
		}
		return type;
	}	
	
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
		WR3223CommandType type = getWR3223BindingConfig(itemName);
		if(type == null){
			logger.error("Item {} is not bound to WR3223 binding.", itemName);
		}else{
			switch(type){
			case TEMPERATURE_SUPPLY_AIR_TARGET:
				if(command instanceof DecimalType){
					targetTemperatureSupplyAir = ((DecimalType)command).intValue();
					hasUpdate = true;
				}else{
					logger.warn("WR3223 item {} must be from type:{}." , itemName, DecimalType.class.getSimpleName());						
				}
				break;
			case VENTILATION_LEVEL:
				if(command instanceof DecimalType){
					int value = ((DecimalType)command).intValue();
					if(value >= 0 && value <= 3){
						ventilationLevel = value;
					}else{
						//FIXME Error
					}
				}else{
					logger.warn("WR3223 item {} must be from type:{}." , itemName, DecimalType.class.getSimpleName());						
				}
				break;
			case OPERATION_MODE:
				if(command instanceof DecimalType){
					int value = ((DecimalType)command).intValue();
					if(value >= 0 && value <= 3){
						operationMode = value;
						hasUpdate = true;
					}else{
						
					}
				}else{
					logger.warn("WR3223 item {} must be from type:{}." , itemName, DecimalType.class.getSimpleName());						
				}
				break;
			default:
				logger.warn("Can't receive commands of type {}.", type.getCommand());
			}
		}
	}
	


}
