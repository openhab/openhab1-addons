/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.digitalstrom.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openhab.binding.digitalstrom.DigitalSTROMBindingProvider;
import org.openhab.binding.digitalstrom.internal.client.DigitalSTROMAPI;
import org.openhab.binding.digitalstrom.internal.client.connection.JSONResponseHandler;
import org.openhab.binding.digitalstrom.internal.client.connection.transport.HttpTransport;
import org.openhab.binding.digitalstrom.internal.client.constants.ApartmentSceneEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.DeviceConstants;
import org.openhab.binding.digitalstrom.internal.client.constants.DeviceParameterClassEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONApiResponseKeysEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONRequestConstants;
import org.openhab.binding.digitalstrom.internal.client.constants.MeteringTypeEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.MeteringUnitsEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.OutputModeEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.SceneToStateMapper;
import org.openhab.binding.digitalstrom.internal.client.constants.SensorIndexEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.ZoneSceneEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.JobPriority;
import org.openhab.binding.digitalstrom.internal.client.entity.Apartment;
import org.openhab.binding.digitalstrom.internal.client.entity.CachedMeteringValue;
import org.openhab.binding.digitalstrom.internal.client.entity.DSID;
import org.openhab.binding.digitalstrom.internal.client.entity.DetailedGroupInfo;
import org.openhab.binding.digitalstrom.internal.client.entity.Device;
import org.openhab.binding.digitalstrom.internal.client.entity.DeviceConfig;
import org.openhab.binding.digitalstrom.internal.client.entity.DeviceSceneSpec;
import org.openhab.binding.digitalstrom.internal.client.entity.Event;
import org.openhab.binding.digitalstrom.internal.client.entity.EventItem;
import org.openhab.binding.digitalstrom.internal.client.entity.Zone;
import org.openhab.binding.digitalstrom.internal.client.entity.impl.JSONEventImpl;
import org.openhab.binding.digitalstrom.internal.client.events.DeviceListener;
import org.openhab.binding.digitalstrom.internal.client.events.EventPropertyEnum;
import org.openhab.binding.digitalstrom.internal.client.impl.DigitalSTROMJSONImpl;
import org.openhab.binding.digitalstrom.internal.config.ConnectionConfig;
import org.openhab.binding.digitalstrom.internal.config.ConsumptionConfig;
import org.openhab.binding.digitalstrom.internal.config.ContextConfig;
import org.openhab.binding.digitalstrom.internal.config.DigitalSTROMBindingConfigItem;
import org.openhab.binding.digitalstrom.internal.listener.ItemListener;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 * 
 */
public class DigitalSTROMGenericBindingProvider extends AbstractGenericBindingProvider implements DigitalSTROMBindingProvider, DeviceListener, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(DigitalSTROMGenericBindingProvider.class);
	
	/** the binding type to register for as a binding config reader */
	private static final String DIGITALSTROM_BINDING_TYPE = "digitalstrom";
	
	private DigitalSTROMAPI digitalSTROM = null;
	
	/** Mapping digitalSTROM-Scene to digitalSTROM-State */
	private SceneToStateMapper stateMapper = new SceneToStateMapper();
	
	
	// ######### MAPS ###########
	
	// openHABItemName - DigitalSTROMDevice
	private Map<String, Device> deviceMap = Collections.synchronizedMap(new HashMap<String, Device>());
		
	// dsid-String - DigitalSTROMDevice
	private Map<String, Device> dsidToDeviceMap = Collections.synchronizedMap(new HashMap<String, Device>());
			
	// openHABItemName - openHABItem
	private Map<String, Item>	nameToItemMap = Collections.synchronizedMap(new HashMap<String, Item>());
		
	// DSIDString - openHABItemName
	private Map<String, List<String>> dsidToNameMap	= Collections.synchronizedMap(new HashMap<String, List<String>>());
		
	// zoneID - Map < groupID, List<dsid-String>>
	private Map<Integer, Map<Short, List<String>>> digitalSTROMZoneGroupMap	= Collections.synchronizedMap(new HashMap<Integer, Map<Short,List<String>>>());
	
	
	// ####### LISTs #########
	
	private List<String> echoBox = Collections.synchronizedList(new LinkedList<String>());
			
	private List<Thread> highPriorityJobBox = Collections.synchronizedList(new LinkedList<Thread>());
		
	private List<Thread> lowPriorityJobBox = Collections.synchronizedList(new LinkedList<Thread>());
		
	private List<Thread> deviceConsumptionJobBox = Collections.synchronizedList(new LinkedList<Thread>());
		
	private List<Thread> meterConsumptionJobBox = Collections.synchronizedList(new LinkedList<Thread>());
		
	private List<TimedMeterConsumptionThread> meterConsumptionThreadList = Collections.synchronizedList(new LinkedList<TimedMeterConsumptionThread>());
		
	private List<TimedDeviceConsumptionThread> deviceConsumptionThreadList = Collections.synchronizedList(new LinkedList<TimedDeviceConsumptionThread>());
	
	private List<ItemListener> itemListenerList = Collections.synchronizedList(new LinkedList<ItemListener>());
		
	
	// ####### THREADS ########
	
	private DigitalSTROMApartmentStructureObserver digitalSTROMApartmentStructureObserver	= null;
		
	private DigitalSTROMEventListener digitalSTROMEventListener	= null;
		
	private RunnerThread runnerThread = null;
		
	private MeterConsumptionWorker meterMeteringRunner = null;
		
	private ApartmentConsumptionThread apartmentConsumptionThread = null;
	
	/** this thread starts after a short countdown (the countdown is finished after the last item configuration is parsed)
	 * and starts the mapping with digitalSTROM hardware and starts the event listener and the metering threads
	 * (when the dSS is available) */
	private Countdown countdown = null;
		
	
	// ####### BOOLEAN ########
	
	private boolean parsingItems = false;
		
	private boolean firstPriority = false;
		
	private boolean sensorReading = false;
		
	private boolean readingFile = false;
		
	private boolean shutdown = false;
	

	// #########
	
	private int connectTimeout = ConnectionConfig.DEFAULT_CONNECT_TIMEOUT;

	private int readTimeout = ConnectionConfig.DEFAULT_READ_TIMEOUT;
	
	private String user = null;

	private String password = null;

	private String loginToken = null;

	private String uri = null;
	
	private String token = null;
	
	private boolean serverIsFound = false;
	
	private Heartbeat heartbeat = null;
	
	private void init() {
		this.digitalSTROM = new DigitalSTROMJSONImpl(uri, connectTimeout, readTimeout);
		
		initHeartBeat();
		startRunnerThreads();
	}
		
	private void initHeartBeat() {
		if (heartbeat == null) {
			heartbeat = new Heartbeat();
			heartbeat.start();
		}
	}
	
	private void login() {
		if (loginToken != null) {
			String token = digitalSTROM.loginApplication(loginToken);
			
			if (token == null && user != null && password != null) {
				token = digitalSTROM.login(user, password);
			}
			setToken(token);
		}
		else if (this.user != null && this.password != null) {
			setToken(digitalSTROM.login(user, password));
		}
	}
	
	private void setToken(String newToken) {
		token = newToken;
		if (newToken != null) {
			setServerIsFound(true);
			logger.info("SUCCESSFULLY got session-token");
		}
		else {
			setServerIsFound(false);
			logger.error("could NOT get session-token");
		}
	}
	
	@Override
	public String getBindingType() {
		return DIGITALSTROM_BINDING_TYPE;
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		
	}
	
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		addBindingConfig(item, parseBindingConfigString(item, bindingConfig));
	}
	
	protected DigitalSTROMBindingConfigItem parseBindingConfigString(Item item, String bindingConfig) throws BindingConfigParseException {
		
		// we first get all items-configurations before we map them to hardware ... 
		// and until we don't know how much items we will get, we reset the countdown ...
		// when the countdown finished without being resetet by the incoming of a new configuration,
		// the items will be mapped with the hardware
		if (countdown != null) {
			countdown.reset();
		}
		
		DigitalSTROMBindingConfigItem configItem = new DigitalSTROMBindingConfigItem();
		configItem.init(item, bindingConfig);
		
		if (configItem.isValid()) {
			addConfiguration(item, configItem);
		}
		else {
			throw new BindingConfigParseException("itemType mismatch ... wrong item:"+item.getName()+" for digitalstrom hardware");
		}
		
		return configItem;
	}	
	
	@Override
	public void removeConfigurations(String context) {
		setReadingFile(true);
	
		boolean initalStart = false;
	
		if (countdown != null) {
			if (countdown.getState() != Thread.State.TERMINATED) {
				countdown.reset();
			}
			else {
				countdown = new Countdown();
				countdown.start();
			}
		}
		else {
			countdown = new Countdown();
			countdown.start();
			initalStart = true;
		}
	
		if (!initalStart) {
	
			stopDigitalSTROMEventListener();
		
			Map<String, Set<Item>> clonedContextMap = getContextMap();
			Set<Item> items = clonedContextMap.get(context);
	
			if (items != null) {
		
				Map<String, Device> clonedNameToDeviceMap = getDeviceMap();
				Map<String, BindingConfig> clonedConfigMap = getConfigMap();
				
				for (Item item: items) {
					Device device = clonedNameToDeviceMap.get(item.getName());
					
					if (device != null) {
				
						List<String> itemNames = getDsidToNameMap().get(device.getDSID().getValue());
						if (itemNames != null) {
							if (itemNames.size() == 1) {
						
								stopConsumption(item, device);
								stopSensorRequests(device);
						
								device.removeDeviceListener(this);
						
								this.deviceMap.remove(item.getName());
								this.nameToItemMap.remove(item.getName());
								this.dsidToNameMap.get(device.getDSID().getValue()).clear();
								this.dsidToNameMap.remove(device.getDSID().getValue());
							}
							else if (itemNames.size()>1) {
							
								this.deviceMap.remove(item.getName());
								this.nameToItemMap.remove(item.getName());
								this.dsidToNameMap.get(device.getDSID().getValue()).remove(item.getName());
								itemNames.remove(item.getName());
							}
						}
					}
					else {
						// the item is not mapped with a device (hardware) or is mapped with a meter (dSM) or is a item to make scene calls or the item was not yet mapped with a device (this happens when the dSS was not/never available)
						stopSpecific(item);
						this.nameToItemMap.remove(item.getName());
					
						for (BindingConfig conf: clonedConfigMap.values()) {
							
							if (conf instanceof DigitalSTROMBindingConfigItem) {
								DigitalSTROMBindingConfigItem confItem = (DigitalSTROMBindingConfigItem) conf;
								
								if (confItem.itemName.equals(item.getName())) {
									if (confItem.dsmid != null) {
										List<String> names = dsidToNameMap.get(confItem.dsmid.getValue());
									
										if (names != null) {
											names.remove(item.getName());
										}
										dsidToNameMap.remove(confItem.dsmid.getValue());
									}
									else if (confItem.dsid != null && (clonedNameToDeviceMap.size() == 0) ) {
										List<String> names = dsidToNameMap.get(confItem.dsid.getValue());
										if (names != null) {
											names.remove(confItem.itemName);
											if (names.size() == 0) {
												dsidToNameMap.remove(confItem.dsid.getValue());
											}
										}
									}
								}
							}
						}							
					}
				}
				echoBox.clear();
			}
		}
	
		super.removeConfigurations(context);
	
		setReadingFile(false);
	}
	
	private void addConfiguration(Item item, DigitalSTROMBindingConfigItem configItem) {
		if (configItem.dsid != null || configItem.dsmid != null) {
				
			String dsid = configItem.dsid != null ? configItem.dsid.getValue() : configItem.dsmid.getValue();
				
			nameToItemMap.put(item.getName(), item);
				
			if (!dsid.equals("ALL")) {
				synchronized(dsidToNameMap) {
					if (!dsidToNameMap.containsKey(dsid)) {
						List<String> itemNameList = new LinkedList<String>();
						itemNameList.add(item.getName());
						dsidToNameMap.put(dsid, itemNameList);
					}
					else {
						List<String> list = dsidToNameMap.get(dsid);
						if (list != null) {
							if (!list.contains(item.getName())) {
								list.add(item.getName());
							}
						}
					}
				}
			}
		}
		else if (configItem.isValidSceneItem()){
			nameToItemMap.put(item.getName(), item);
		}
		else {
			logger.error("could not register item: "+item.getName()+" : missing a dsid or dsmid in configuration");
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {

			String uriStr = (String) config.get("uri");

			if (uriStr == null) {
				logger.error("missing uri in configuration file");
				throw new org.osgi.service.cm.ConfigurationException(uri,
						"need server uri");
			}

			this.uri = uriStr;

			String connectTimeoutStr = (String) config.get("connectTimeout");
			if (connectTimeoutStr != null
					&& !connectTimeoutStr.trim().equals("")) {
				int timeout = -1;
				try {
					timeout = Integer.parseInt(connectTimeoutStr);
				} catch (java.lang.NumberFormatException e) {
					logger.error("NumberFormatException by getting connectTimeout: "
							+ connectTimeoutStr);
				}

				if (timeout != -1) {
					this.connectTimeout = timeout;
				}
				else {
					connectTimeout = ConnectionConfig.DEFAULT_CONNECT_TIMEOUT;
				}
			} else {
				connectTimeout = ConnectionConfig.DEFAULT_CONNECT_TIMEOUT;
			}

			String readTimeoutStr = (String) config.get("readTimeout");
			if (readTimeoutStr != null && !readTimeoutStr.trim().equals("")) {
				int timeout = -1;
				try {
					timeout = Integer.parseInt(readTimeoutStr);
				} catch (java.lang.NumberFormatException e) {
					logger.error("NumberFormatException by getting readTimeout: "
							+ readTimeoutStr);
				}

				if (timeout != -1) {
					readTimeout = timeout;
				}
				else {
					readTimeout = ConnectionConfig.DEFAULT_READ_TIMEOUT;
				}
			} else {
				readTimeout = ConnectionConfig.DEFAULT_READ_TIMEOUT;
			}

			loginToken = (String) config.get("loginToken");

			user = (String) config.get("user");
			password = (String) config.get("password");

			
			setServerIsFound(false);
			init();
		}
	}
	
	// Mapping of openHAB Items with digitalSTROM hardware
	private void setupItems() {
		
		if (getToken() != null) {
			
			Map<String, BindingConfig> clonedConfigMap = getConfigMap();
			
			Map<String, Item> clonedItemMap = getNameToItemMap();
			
			Map<String, Device> clonedCachedDeviceMap = getAllDigitalSTROMDevicesMap();
			
			List<String> meterList = digitalSTROM.getMeterList(getToken());
			
			Map<String, Device> clonedDsidToDeviceMap = getDsidToDeviceMap();
			
			for (BindingConfig conf: clonedConfigMap.values()) {
				DigitalSTROMBindingConfigItem configItem = (DigitalSTROMBindingConfigItem) conf;
					
				if (configItem.dsid != null) {
						
					Device device = null;
					
					if (clonedDsidToDeviceMap.containsKey(configItem.dsid.getValue())) {
						device = clonedDsidToDeviceMap.get(configItem.dsid.getValue());
					}
					else {
						device = clonedCachedDeviceMap.get(configItem.dsid.getValue());
					}
						
					if (device != null) {
					
						addDevice(configItem.itemName, device);
							
						if (configItem.timeinterval > 0) {
							if (configItem.consumption == null) {
								configItem.consumption = ConsumptionConfig.ACTIVE_POWER;
							}
							addDeviceConsumptionThread(device, configItem.consumption, configItem.timeinterval);
						}
					}
						
				}
				else if (configItem.dsmid != null) {
					Item item = clonedItemMap.get(configItem.itemName);
					
					if (item != null) {
						if (configItem.dsmid.getValue().equals("ALL")) {
							if (apartmentConsumptionThread != null) {
								apartmentConsumptionThread.setShutdown(true);
							}
							apartmentConsumptionThread = new ApartmentConsumptionThread(item, configItem.timeinterval);
						}
						else {
							if (meterList.contains(configItem.dsmid.getValue())) {
								if (configItem.timeinterval > 0) {
									if (configItem.consumption == null) {
										configItem.consumption = ConsumptionConfig.ACTIVE_POWER;
									}
									else if (configItem.consumption.equals(ConsumptionConfig.OUTPUT_CURRENT)) {
										configItem.consumption = ConsumptionConfig.ACTIVE_POWER;
										logger.warn(ConsumptionConfig.OUTPUT_CURRENT.name() + " is not supportet by meters, by default setting ACTIVE_POWER");
									}
									addMeterConsumptionThread(item, configItem.dsmid.getValue(), configItem.timeinterval);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void shutDownThreads() {
		if (digitalSTROMApartmentStructureObserver != null) {
			digitalSTROMApartmentStructureObserver.setShutdown(true);
		}
		
		if (apartmentConsumptionThread != null) {
			apartmentConsumptionThread.setShutdown(true);
		}
		
		if (runnerThread != null) {
			runnerThread.setShutdown(true);
		}
		
		if (meterMeteringRunner != null) {
			meterMeteringRunner.setShutdown(true);
		}
		
		stopDSMConsumptionThreads();
		stopDeviceConsumptionThreads();
		
		if (heartbeat != null) {
			heartbeat.setShutdown(true);
		}
		
	}
	
	private void startRunnerThreads() {
		startRunnerThread();
		startMeterRunnerThread();
	}
	
	private void startDigitalstromStructureObserver() {
		if (digitalSTROMApartmentStructureObserver != null) {
			digitalSTROMApartmentStructureObserver.setShutdown(true);
			try {
				digitalSTROMApartmentStructureObserver.join();
			} catch (InterruptedException e) {
				logger.error("InterruptedException at digitalSTROMStructureObserver ... "+e.getLocalizedMessage());
			}
		}
		
		if (!shutdown) {
			digitalSTROMApartmentStructureObserver = new DigitalSTROMApartmentStructureObserver();
			digitalSTROMApartmentStructureObserver.start();
		}
	}
	
	private void startRunnerThread() {
		if (runnerThread != null) {
			runnerThread.setShutdown(true);
			try {
				runnerThread.join();
			} catch (InterruptedException e) {
				logger.error("InterruptedException at runner thread ... "+e.getLocalizedMessage());
			}
		}
		
		if (!shutdown) {
			runnerThread = new RunnerThread();
			runnerThread.start();
		}
	}
	
	private void startMeterRunnerThread() {
		if (meterMeteringRunner != null) {
			meterMeteringRunner.setShutdown(true);
			try {
				meterMeteringRunner.join();
			} catch (InterruptedException e) {
				logger.error("InterruptedException at meter runner thread ... "+e.getLocalizedMessage());
			}
		}
		
		if (!shutdown) {
			meterMeteringRunner = new MeterConsumptionWorker();
			meterMeteringRunner.start();
		}
	}
	
	private void registerEventListener() {
		stopDigitalSTROMEventListener();
		
		if (!shutdown) {
			digitalSTROMEventListener = new DigitalSTROMEventListener();
			digitalSTROMEventListener.start();
		}
	}
	
	private void resetVariables() {
		
		this.deviceMap.clear();
		this.dsidToDeviceMap.clear();
		this.nameToItemMap.clear();
		this.dsidToNameMap.clear();
		this.digitalSTROMZoneGroupMap.clear();
		
		this.echoBox.clear();
		this.highPriorityJobBox.clear();
		this.lowPriorityJobBox.clear();
		this.deviceConsumptionJobBox.clear();
		this.meterConsumptionJobBox.clear();
		
		this.meterConsumptionThreadList.clear();
		this.deviceConsumptionThreadList.clear();
		
		this.digitalSTROMApartmentStructureObserver = null;
		this.apartmentConsumptionThread = null;
		this.runnerThread = null;
		this.meterMeteringRunner = null;
		this.countdown = null;
		
		this.stateMapper = null;
		this.digitalSTROM = null;
	}
	
	private synchronized Map<String, Device> getDeviceMap() {
		return new HashMap<String, Device>(deviceMap);
	}
	
	private synchronized Map<String, Device> getDsidToDeviceMap() {
		return new HashMap<String, Device>(dsidToDeviceMap);
	}
	
	private synchronized Map<String, Item> getNameToItemMap() {
		return new HashMap<String, Item>(nameToItemMap);
	}
	
	private synchronized Map<String, List<String>> getDsidToNameMap() {
		return new HashMap<String, List<String>>(dsidToNameMap);
	}
	
	private synchronized Map<Integer, Map<Short, List<String>>> getDigitalSTROMZoneGroupMap() {
		return new HashMap<Integer, Map<Short,List<String>>>(digitalSTROMZoneGroupMap);
	}
	
	private synchronized Map<String, BindingConfig> getConfigMap() {
		return new HashMap<String, BindingConfig>(super.bindingConfigs);
	}
	
	private synchronized Map<String, Set<Item>> getContextMap() {
		return new HashMap<String, Set<Item>>(super.contextMap);
	}
	
	private synchronized void stopDigitalSTROMEventListener() {
		
		if (digitalSTROMEventListener != null) {
			if (digitalSTROMEventListener.unsubscribe()) {
				logger.info("SUCCESSFULLY unsubscribed eventListener");
			}
			else {
				logger.info("could NOT unsubscribe eventListener");
			}
				
			digitalSTROMEventListener.setShutdown(true);
			try {
				digitalSTROMEventListener.join();
			} catch (InterruptedException e) {
				logger.error("InterruptedException by waiting for the end of eventListener ... "+e.getLocalizedMessage());
			}
			digitalSTROMEventListener = null;
		}
	}
	
	private Map<String, Device> getAllDigitalSTROMDevicesMap() {
		Map<String, Device> deviceMap = new HashMap<String, Device>();
		
		for(Device device: digitalSTROM.getApartmentDevices(getToken(), false)) {
			deviceMap.put(device.getDSID().getValue(), device);
		}
		
		return deviceMap;
	}

	@Override
	public void deviceUpdated(String dsid) {
		Map<String, List<String>> clonedMap = getDsidToNameMap();
		List<String> itemNames = clonedMap.get(dsid);
		
		if (itemNames != null) {
			Map<String, Item> clonedItems = getNameToItemMap();
			
			for (String itemName: itemNames) {
				updateItemState(clonedItems.get(itemName));
			}
		}
	}
	
	private void updateItemState(Item item) {
		if (item != null) {
			Device device = getDeviceMap().get(item.getName());
			
			if (device != null) {
				
				State state = null;
				if (item instanceof DimmerItem) {
					
					//((DimmerItem) item).setState(new PercentType(getPercent(device.getMaxOutPutValue(), device.getOutputValue())));
					state = new PercentType(getPercent(device.getMaxOutPutValue(), device.getOutputValue()));
					
				}
				else if (item instanceof SwitchItem && !(item instanceof DimmerItem)) {
					
					//((SwitchItem) item).setState(device.getOutputValue() > 0 ? OnOffType.ON : OnOffType.OFF);
					state = device.getOutputValue() > 0 ? OnOffType.ON : OnOffType.OFF;
				}
				else if (item instanceof NumberItem ) {
					
					Map<String, BindingConfig> clonedConfigMap = getConfigMap();
					BindingConfig conf = clonedConfigMap.get(item.getName());
					
					if (conf instanceof DigitalSTROMBindingConfigItem) {
						DigitalSTROMBindingConfigItem confItem = (DigitalSTROMBindingConfigItem) conf;
						
						if (confItem.consumption != null) {
							//State state = UnDefType.NULL;
							int value = -1;
							switch (confItem.consumption) {
							
							case ACTIVE_POWER:
								value = device.getPowerConsumption();
								
								if (value != -1) {
									state = new DecimalType(value);
								}
								break;
							case OUTPUT_CURRENT:
								value = device.getEnergyMeterValue();
								
								if (value != -1) {
									state = new DecimalType(value);
								}
								break;
							default:
								break;
								
							}
							//((NumberItem) item).setState(state);
						}
					}
				}
				else if (item instanceof RollershutterItem) {
					
					Map<String, BindingConfig> clonedConfigMap = getConfigMap();
					BindingConfig conf = clonedConfigMap.get(item.getName());
					
					if (conf instanceof DigitalSTROMBindingConfigItem) {
						DigitalSTROMBindingConfigItem confItem = (DigitalSTROMBindingConfigItem) conf;
						
						if (confItem.context != null && confItem.context.equals(ContextConfig.slat)) {
							
							int output = getPercent(device.getMaxSlatPosition(), device.getSlatPosition());
							
							//((RollershutterItem) item).setState(new PercentType(100-output));
							state =new PercentType(100-output);
						}
						else if (confItem.context != null && confItem.context.equals(ContextConfig.awning)) {
							
							int output = getPercent(device.getMaxOutPutValue(), device.getOutputValue());
							
							//((RollershutterItem) item).setState(new PercentType(output));
							state = new PercentType(output);
						}
						else {
							int output = getPercent(device.getMaxOutPutValue(), device.getOutputValue());
							
							//((RollershutterItem) item).setState(new PercentType(100-output));
							state = new PercentType(100-output);
						}
					}
				}
				else if (item instanceof StringItem) {
					Map<String, BindingConfig> clonedConfigMap = getConfigMap();
					BindingConfig conf = clonedConfigMap.get(item.getName());
					
					if (conf instanceof DigitalSTROMBindingConfigItem) {
						DigitalSTROMBindingConfigItem confItem = (DigitalSTROMBindingConfigItem) conf;
						
						if (confItem.consumption != null) {
							//State state = UnDefType.NULL;
							int value = -1;
							
							switch (confItem.consumption) {
							
							case ACTIVE_POWER:
								value = device.getPowerConsumption();
								
								if (value != -1) {
									state = new DecimalType(value);
								}
								break;
							case OUTPUT_CURRENT:
								value = device.getEnergyMeterValue();
								
								if (value != -1) {
									state = new DecimalType(value);
								}
								break;
							default:
								break;
								
							}
							//((StringItem) item).setState(state);
						}
					}
				}
				
				notifyItemListener(item.getName(), state);
			}
			else {
				logger.error("couldn't update item state, because device is null: "+item.getName());
			}
		}
		else {
			logger.error("couldn't update item state, because item is null");
		}

	}	

	@Override
	public void shutdownBinding() {
		setShutdown(true);
		setReadingFile(true);
		
		stopDigitalSTROMEventListener();
		shutDownThreads();
		removeAllDeviceListener();
		resetVariables();
		
		logger.info("shutdownBinding completed");
	}

	@Override
	public void sendCommandToDSS(String itemName, Command cm) {

		if (!isParsingItems()) {
			
			// is set in order to avoid starting of consumption and other things in this time
			setFirstPriority(true);
			
			if (getToken() != null) {
				deviceCall(itemName, cm);
			}
			
			setFirstPriority(false);
			
		}
		else {
			logger.info("Will not send command, because is parsing items ...");
		}
		
	}
	
	// request a sensor job
	private void initOutputValue(Device device, short index) {
		addJob(new OutputValueOnDemand(device, index));
	}
	
	// some jobs (sensor reading) have to be done as soon as possible, but some jobs can be done when there is time
	private void getTableOutputValue(Device device, short sceneId, JobPriority priority) {
		switch (priority) {
			
		case URGENT:
			addJob(new GetSceneOutputValue(device, sceneId));
			break;
		case NO_HURRY:
			addNextTimeJob(new GetSceneOutputValue(device, sceneId));
			break;
		default:
			addJob(new GetSceneOutputValue(device, sceneId));
		}
	}
	
	private void handleDimmScene(Device device, short sceneID,
			short groupID, boolean force) {
		
		if ((groupID == -1 && !force)) {
			return;
		}
		
		if (device.isDimmable()) {
			
			switch (sceneID) {
			
			case 11:
			case 42:
			case 44:
			case 46:
			case 48: 
				decrease(device);
				break;
			
			case 12:
			case 43: 
			case 45: 
			case 47:
			case 49: 
				increase(device);
				break;
			default:
				break;
			
			}
			
		}
		else if (device.isRollershutter()) {
			switch (sceneID) {
			
			case 15:
				
				initOutputValue(device, DeviceConstants.DEVICE_SENSOR_OUTPUT);
				if (device.getOutputMode().equals(OutputModeEnum.SLAT)) {
					initOutputValue(device, DeviceConstants.DEVICE_SENSOR_SLAT_OUTPUT);
				}
				break;
			case 52:
			case 53:
			case 54:
			case 55:
				
				initOutputValue(device, DeviceConstants.DEVICE_SENSOR_OUTPUT);
				if (device.getOutputMode().equals(OutputModeEnum.SLAT)) {
					initOutputValue(device, DeviceConstants.DEVICE_SENSOR_SLAT_OUTPUT);
				}
				break;
			default:
				
			}
		}
		
	}
	
	private void decrease(Device device) {
		initOutputValue(device, DeviceConstants.DEVICE_SENSOR_OUTPUT);
	}
	
	private void increase(Device device) {
		initOutputValue(device, DeviceConstants.DEVICE_SENSOR_OUTPUT);
	}

	private void addDevice(String itemName, Device dev) {
		synchronized (deviceMap) {
			if (!deviceMap.containsKey(itemName)) {
				deviceMap.put(itemName, dev);
				dev.addDeviceListener(this);
			} else {
				logger.warn("device already exists in deviceMap: " + itemName);
			}
		}
		synchronized (dsidToDeviceMap) {
			if (!dsidToDeviceMap.containsKey(dev.getDSID().getValue())) {
				dsidToDeviceMap.put(dev.getDSID().getValue(), dev);
			}
		}
	}

	/**
	 * only works on openhabEvent!
	 * please copy "openhab/openhab.js" to your dSS server (/usr/share/dss/add-ons/)
	 * and 
	 * "openhab.xml" to /usr/share/dss/data/subscriptions.d/
	 * than you need to restart your dSS
	 *  
	 * If you don't, you will not get detailed infos about, what exactly happened
	 * (for example: which device was turned on by a browser or handy app )
	 * 
	 * @param eventItem
	 */
	private void handleOpenhabEvent(EventItem eventItem) {
		if (eventItem != null && !isParsingItems()) {
					
			int		zoneId	= -1;
			short	groupId	= -1;
			short	sceneId	= -1;
					
			boolean isDeviceCall = false;
			String	dsidStr	= null;
					
			String zoneIDStr = eventItem.getProperties().get(EventPropertyEnum.ZONEID);
			if (zoneIDStr != null) {
				try {
					zoneId = Integer.parseInt(zoneIDStr);
				}
				catch (java.lang.NumberFormatException e) {
					logger.error("NumberFormatException by handling event at parsing zoneId");
					}
			}
					
			String sceneStr = eventItem.getProperties().get(EventPropertyEnum.SCENEID);
			if (sceneStr != null) {
				try {
					sceneId = Short.parseShort(sceneStr);
						} catch (java.lang.NumberFormatException e) {
							logger.error("NumberFormatException by handling event at parsing sceneId: "+sceneStr);
						}
					}
					
					String groupStr = eventItem.getProperties().get(EventPropertyEnum.GROUPID);
					if (groupStr != null) {
						try {
							groupId = Short.parseShort(groupStr);
						}
						catch (java.lang.NumberFormatException e) {
							logger.error("NumberFormatException by handling event at parsing groupId");
						}
					}
					
					dsidStr = eventItem.getProperties().get(EventPropertyEnum.DSID);
					
					String deviceCallStr = eventItem.getProperties().get(EventPropertyEnum.IS_DEVICE_CALL);
					if (deviceCallStr != null) {
						isDeviceCall = deviceCallStr.equals("true");
					}
					
					if (sceneId != -1) {
						
						if (!isEcho(dsidStr, sceneId)) {
						
							if (isDeviceCall) {
								
								if (dsidStr != null) {
									Device device = getDsidToDeviceMap().get(dsidStr);
									
									if (device != null) {
										if (isDimmScene(sceneId)) {
											
											if (!device.containsSceneConfig(sceneId)) {
												getSceneSpec(device, sceneId);
											}	
												
											if (!device.doIgnoreScene(sceneId)) {
												handleDimmScene(device, sceneId, (short)-1, true);
											}
											
										}
										else if (stateMapper.isMappable(sceneId)) {
											boolean shouldBeOn = stateMapper.getMapping(sceneId);
											
											if (!device.containsSceneConfig(sceneId)) {
												getSceneSpec(device, sceneId);
											}
											
											if (!device.doIgnoreScene(sceneId)) {
												if (shouldBeOn) {
													device.setOutputValue(device.getMaxOutPutValue());
												}
												else {
													device.setOutputValue(0);
												}
											}
											
										}
										else {
											
											if (!device.containsSceneConfig(sceneId)) {
												getSceneSpec(device, sceneId);
											}
											
											if (!device.doIgnoreScene(sceneId)) {
												short value = device.getSceneOutputValue(sceneId);
												if (value != -1) {
													device.setOutputValue(value);
												}
												else {
													initOutputValue(device, DeviceConstants.DEVICE_SENSOR_OUTPUT);
													getTableOutputValue(device, sceneId, JobPriority.NO_HURRY);
												}
											}
										}
									}
								}
							}
							else {
							
								if (isApartmentScene(sceneId)) {
									handleApartmentScene(sceneId, groupId);
								}
								else {
									
									if (zoneId == 0) {	
										if (isDimmScene(sceneId)) {
											
											Map<String, Device> deviceMap = getDsidToDeviceMap();
											
											if (groupId == 0) {
												
												Set<String> dsidSet = deviceMap.keySet();
												
												if (dsidSet != null) {
													for (String dsid: dsidSet) {
														Device device = deviceMap.get(dsid);
														
														if (device != null) {
															
															if (!device.containsSceneConfig(sceneId)) {
																getSceneSpec(device, sceneId);
															}
															
															if (!device.doIgnoreScene(sceneId)) {
																handleDimmScene(deviceMap.get(dsid), sceneId, groupId, false);
															}
															
														}
													}
												}
											}
											else if (groupId != -1) {
												
												Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap().get(zoneId);
												
												if (map != null) {
													List<String> dsidList = map.get(groupId);
													if (dsidList != null) {
														for (String dsid:dsidList) {
															Device device = deviceMap.get(dsid);
															
															if (device != null) {
																
																if (!device.containsSceneConfig(sceneId)) {
																	getSceneSpec(device, sceneId);
																}
																
																if (!device.doIgnoreScene(sceneId)) {
																	handleDimmScene(deviceMap.get(dsid), sceneId, groupId, false);
																}
															}
														}
													}
												}
											}
										}
										else if (stateMapper.isMappable(sceneId)) {
											
											boolean shouldBeOn = stateMapper.getMapping(sceneId);
											
											if (groupId == 0) {
												
												Map<String, Device> deviceMap = getDsidToDeviceMap();
												Set<String> dsidSet = deviceMap.keySet();
												if (dsidSet != null) {
													for (String dsid:dsidSet) {
														Device device = deviceMap.get(dsid);
														if (device != null) {
															
															if (!device.containsSceneConfig(sceneId)) {
																getSceneSpec(device, sceneId);
															}
															
															if (!device.doIgnoreScene(sceneId)) {
																
																if (shouldBeOn) {
																	device.setOutputValue(device.getMaxOutPutValue());
																}
																else {
																	device.setOutputValue(0);
																}
																
															}
														}
													}
												}
											}
											else if (groupId != -1) {
												
												Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap().get(zoneId);
												Map<String, Device> deviceMap = getDsidToDeviceMap();
												if (map != null) {
													List<String> dsidList = map.get(groupId);
													if (dsidList != null) {
														for (String dsid:dsidList) {
															Device device = deviceMap.get(dsid);
															if (device != null) {
																
																if (!device.containsSceneConfig(sceneId)) {
																	getSceneSpec(device, sceneId);
																}
																
																if (!device.doIgnoreScene(sceneId)) {
																	
																	if (shouldBeOn) {
																		device.setOutputValue(device.getMaxOutPutValue());
																	}
																	else {
																		device.setOutputValue(0);
																	}
																	
																}
															}
														}
													}
												}
											}
										}
										else {
											
											Map<String, Device> deviceMap = getDsidToDeviceMap();
											
											if (groupId != -1) {
												Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap().get(zoneId);
												if (map != null) {
													List<String> dsidList = map.get(groupId);
													if (dsidList != null) {
														for (String dsid:dsidList) {
															Device device = deviceMap.get(dsid);
															
															if (device != null) {
																
																if (!device.containsSceneConfig(sceneId)) {
																	getSceneSpec(device, sceneId);
																}
																
																if (!device.doIgnoreScene(sceneId)) {
																	short sceneValue = device.getSceneOutputValue(sceneId);
																	if (sceneValue == -1) {
																		initOutputValue(device, DeviceConstants.DEVICE_SENSOR_OUTPUT);
																		getTableOutputValue(device, sceneId, JobPriority.NO_HURRY);
																	}
																	else {
																		device.setOutputValue(sceneValue);
																	}
																}
															}
														}
													}
												}
											}
										}
									}
									
									else  {
										
										if (isDimmScene(sceneId)) {
											
											if (groupId != -1) {
												Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap().get(zoneId);
												if (map != null) {
													List<String> devicesInGroup = map.get(groupId);
													if (devicesInGroup != null) {
														Map<String, Device> deviceMap = getDsidToDeviceMap();
														
														for (String dsid:devicesInGroup) {
															Device device = deviceMap.get(dsid);
															
															if (device != null) {
																
																if (!device.containsSceneConfig(sceneId)) {
																	getSceneSpec(device, sceneId);
																}
																
																if (!device.doIgnoreScene(sceneId)) {
																	handleDimmScene(deviceMap.get(dsid), sceneId, groupId, false);
																}
																
															}
														}
													}
												}
											}
										}
										else if (stateMapper.isMappable(sceneId)) {
											
											boolean shouldBeOn = stateMapper.getMapping(sceneId);
											Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap().get(zoneId);
											Map<String, Device> deviceMap = getDsidToDeviceMap();
											
											if (map != null) {
												
												if (groupId != -1) {
													List<String> devicesInGroup = map.get(groupId);
													if (devicesInGroup != null) {
														for (String dsid: devicesInGroup) {
															Device device = deviceMap.get(dsid);
															
															if (device != null) {
																
																if (!device.containsSceneConfig(sceneId)) {
																	getSceneSpec(device, sceneId);
																}
																
																if (!device.doIgnoreScene(sceneId)) {
																	if (shouldBeOn) {
																		device.setOutputValue(device.getMaxOutPutValue());
																	}
																	else {
																		device.setOutputValue(0);
																	}
																}
																
															}
														}
													}
												}
											}
										}
										else {
											
											Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap().get(zoneId);
											Map<String, Device> deviceMap = getDsidToDeviceMap();
											
											if (map != null) {
												
												if (groupId != -1) {
													List<String> devicesInGroup = map.get(groupId);
													if (devicesInGroup != null) {
														for (String dsid: devicesInGroup) {
															Device device = deviceMap.get(dsid);
															if (device != null) {
																
																if (!device.containsSceneConfig(sceneId)) {
																	getSceneSpec(device, sceneId);
																}
																
																if (!device.doIgnoreScene(sceneId)) {
																	short outputValue = device.getSceneOutputValue(sceneId);
																	if (outputValue == -1) {
																		initOutputValue(device, DeviceConstants.DEVICE_SENSOR_OUTPUT);
																		getTableOutputValue(device, sceneId, JobPriority.NO_HURRY);
																	}
																	else {
																		device.setOutputValue(outputValue);
																	}
																}
																
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					else {
						logger.error("an event without a sceneID; groupID:"+groupId+", zoneID:"+zoneId+", isDeviceCall:"+deviceCallStr+", dsid:"+dsidStr);
					}
				}
		}
		
	private void handleApartmentScene(short sceneId, short groupId) {
			
		if (groupId == 0) {
			Map<String, Device> clonedDeviceMap = getDsidToDeviceMap();
			Set<String> dsidSet = clonedDeviceMap.keySet();
		
			for (String dsid: dsidSet) {
				Device device = clonedDeviceMap.get(dsid);
				
				if (device != null) {
					
					if (!device.containsSceneConfig(sceneId)) {
						getSceneSpec(device, sceneId);
					}
					
					if (!device.doIgnoreScene(sceneId)) {
						short output = device.getSceneOutputValue(sceneId);
						if (output != -1) {
							device.setOutputValue(output);
						}
						else {
							initOutputValue(device, DeviceConstants.DEVICE_SENSOR_OUTPUT);
							getTableOutputValue(device, sceneId, JobPriority.NO_HURRY);
						}
					}
				}
			}
		}
		else if (groupId != -1) {
				
			Map<String, Device> clonedDeviceMap = getDsidToDeviceMap();
			Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap().get(0);
			List<String> dsidList = map.get(groupId);
				
			if (dsidList != null) {
				for (String dsid:dsidList) {
					Device device = clonedDeviceMap.get(dsid);
						
					if (device != null) {
							
						if (!device.containsSceneConfig(sceneId)) {
							getSceneSpec(device, sceneId);
						}
							
						if (!device.doIgnoreScene(sceneId)) {
							short output = device.getSceneOutputValue(sceneId);
							if (output != -1) {
								device.setOutputValue(output);
							}
							else {
								initOutputValue(device, DeviceConstants.DEVICE_SENSOR_OUTPUT);
								getTableOutputValue(device, sceneId, JobPriority.NO_HURRY);
							}
						}
					}
				}
			}
		}
	}
		
	private boolean isApartmentScene(short sceneId) {
		return (sceneId > 63);
	}
		
	private boolean isDimmScene(short sceneId) {
		if (sceneId > 9 && sceneId < 13) {
			return true;
		}
		if (sceneId == 15) {				// command to dim or for a roller shutter
			return true;
		}
		if (sceneId > 41 && sceneId < 50) {
			return true;
		}
		if (sceneId > 51 && sceneId < 56) {	// command to dim or for a roller shutter
			return true;
		}
		return false;
	}
	
	// Here we read the configured and stored (in the chip) output value for a specific scene
	// and we store this value in order to know next time what to do.
	// The first time a scene command is called, it takes some time for the sensor reading,
	// but the next time we react very fast because we learned what to do on this command.
	private void getSceneSpec(Device device, short sceneId) {
		
		setSensorReading(true);	// no metering in this time
		DeviceSceneSpec spec = digitalSTROM.getDeviceSceneMode(getToken(), device.getDSID(), null, sceneId);
		setSensorReading(false);
			
		if (spec != null) {
			device.addSceneConfig(sceneId, spec);
			logger.info("UPDATED ignoreList for dsid: "+device.getDSID()+" sceneID: "+sceneId);
		}
			
	}
	
	private void deviceCall(String itemName, Command cm) {
		Device device = getDeviceMap().get(itemName);
			
		if (device != null) {
			if (cm instanceof org.openhab.core.library.types.OnOffType) {
							
					if (((org.openhab.core.library.types.OnOffType) cm).equals(OnOffType.ON)) {
						
						boolean transmitted = digitalSTROM.turnDeviceOn(getToken(), device.getDSID(), null);
						if (transmitted) {
							device.setOutputValue(device.getMaxOutPutValue());
							addEcho(device.getDSID().getValue(), (short)ZoneSceneEnum.MAXIMUM.getSceneNumber());
						}
						
					}
					else if (((org.openhab.core.library.types.OnOffType) cm).equals(OnOffType.OFF)) {
						
						boolean transmitted = digitalSTROM.turnDeviceOff(getToken(), device.getDSID(), null);
						if (transmitted) {
							device.setOutputValue(0);
							addEcho(device.getDSID().getValue(), (short)ZoneSceneEnum.MINIMUM.getSceneNumber());
						}
						
					}
					
				}
				else if (cm instanceof org.openhab.core.library.types.IncreaseDecreaseType) {
					
					if (!device.isDimmable()) {
						logger.warn("device is not in dimm mode: "+ itemName+" outputMode: "+device.getOutputMode().getMode());
						return;
					}
					
					if (((org.openhab.core.library.types.IncreaseDecreaseType) cm).equals(IncreaseDecreaseType.INCREASE)) {
						
						boolean transmitted = digitalSTROM.callDeviceScene(getToken(), device.getDSID(), null, ZoneSceneEnum.INCREMENT, false);
						if (transmitted) {
							addEcho(device.getDSID().getValue(), (short)ZoneSceneEnum.INCREMENT.getSceneNumber());
							
							if (device.getOutputValue() == 0) {
								initOutputValue(device, DeviceConstants.DEVICE_SENSOR_OUTPUT);
							}
							else {
								device.increase();
							}
						}
						else {
							logger.error("transmitting increase command FAILED "+itemName);
						}
						
					}
					else if (((org.openhab.core.library.types.IncreaseDecreaseType) cm).equals(IncreaseDecreaseType.DECREASE)) {
						
						boolean transmitted = digitalSTROM.callDeviceScene(getToken(), device.getDSID(), null, ZoneSceneEnum.DECREMENT, false);
						if (transmitted) {
							addEcho(device.getDSID().getValue(), (short)ZoneSceneEnum.DECREMENT.getSceneNumber());
							device.decrease();
						}
						else {
							logger.error("transmitting decrease command FAILED "+itemName);
						}
					}
					
				}
				else if (cm instanceof org.openhab.core.library.types.PercentType) {
					int percent = -1;
					try {
						percent = (int)Float.parseFloat(cm.toString());
					}catch (java.lang.NumberFormatException e) {
						logger.error("NumberFormatException on a PercentType with command: "+cm.toString());
					}
					if (percent != -1) {
						if (percent > -1 && percent < 101) {
							
							if (device.getOutputMode().equals(OutputModeEnum.SLAT)) {
								
								Map<String, BindingConfig> clonedConfigMap = getConfigMap();
								BindingConfig conf = clonedConfigMap.get(itemName);
								
								if (conf instanceof DigitalSTROMBindingConfigItem) {
									DigitalSTROMBindingConfigItem confItem = (DigitalSTROMBindingConfigItem)conf;
								
									if (confItem.context != null && confItem.context.equals(ContextConfig.slat)) {
										
										int old = device.getSlatPosition();
										device.setSlatPosition(fromPercentToValue(percent, device.getMaxSlatPosition()));
										
										boolean transmitted = digitalSTROM.setDeviceOutputValue(getToken(), device.getDSID(), null, DeviceConstants.DEVICE_SENSOR_SLAT_OUTPUT, fromPercentToValue(percent, device.getMaxSlatPosition()));
										if (!transmitted) {
											device.setSlatPosition(old);
											logger.error("could NOT successfully set new value for slats ..."+cm.toString());
										}
									}
									else {
										
										int old = device.getOutputValue();
										
										device.setOutputValue(fromPercentToValue(percent, device.getMaxOutPutValue()));
										
										boolean transmitted = digitalSTROM.setDeviceValue(getToken(), device.getDSID(), null, fromPercentToValue(percent, device.getMaxOutPutValue()));
										if (!transmitted) {
											device.setOutputValue(old);
											logger.error("could NOT successfully set new value ..."+cm.toString());
										}
										
									}
								}
								
							}
							else {
								
								int old = device.getOutputValue();
								
								device.setOutputValue(fromPercentToValue(percent, device.getMaxOutPutValue()));
								
								boolean transmitted = digitalSTROM.setDeviceValue(getToken(), device.getDSID(), null, fromPercentToValue(percent, device.getMaxOutPutValue()));
								if (!transmitted) {
									device.setOutputValue(old);
									logger.error("could NOT successfully set new value ..."+cm.toString());
								}
								
							}
						}
					}
				}
				else if (cm instanceof org.openhab.core.library.types.StopMoveType) {
					
					if (device.getOutputMode().equals(OutputModeEnum.SLAT)) {
						
						Map<String, BindingConfig> clonedConfigMap = getConfigMap();
						BindingConfig conf = clonedConfigMap.get(itemName);
						
						if (conf instanceof DigitalSTROMBindingConfigItem) {
							DigitalSTROMBindingConfigItem confItem = (DigitalSTROMBindingConfigItem) conf;
							
							if (confItem.context != null && confItem.context.equals(ContextConfig.slat)) {
								logger.warn("stop and move command NOT possible for slats, use PercentType command or up and down please");
							}
							else {
								handleStopMoveForRollershutter(device, cm);
							}
						}
					}
					else if (device.getOutputMode().equals(OutputModeEnum.UP_DOWN)) {
						handleStopMoveForRollershutter(device, cm);
					}
				}
				else if (cm instanceof org.openhab.core.library.types.UpDownType) {
					
					if (device.getOutputMode().equals(OutputModeEnum.SLAT)) {
						
						// 255 is max open, 0 is closed
						Map<String, BindingConfig> clonedConfigMap = getConfigMap();
						BindingConfig conf = clonedConfigMap.get(itemName);
						
						if (conf instanceof DigitalSTROMBindingConfigItem) {
							DigitalSTROMBindingConfigItem confItem = (DigitalSTROMBindingConfigItem) conf;
							
							if (confItem.context != null && confItem.context.equals(ContextConfig.slat)) {
								
								if (((org.openhab.core.library.types.UpDownType) cm).equals(UpDownType.UP)) {
									
									int slatPosition = device.getSlatPosition();
									int newPosition = slatPosition+DeviceConstants.MOVE_STEP_SLAT;
									if (newPosition > device.getMaxSlatPosition()) {
										newPosition = device.getMaxSlatPosition();
									}
									
									boolean transmitted = digitalSTROM.setDeviceOutputValue(getToken(), device.getDSID(), null, DeviceConstants.DEVICE_SENSOR_SLAT_OUTPUT, newPosition);
									if (transmitted) {
										device.setSlatPosition(newPosition);
									}
									
								}
								else if (((org.openhab.core.library.types.UpDownType) cm).equals(UpDownType.DOWN)) {
									
									int slatPosition = device.getSlatPosition();
									int newPosition = slatPosition-DeviceConstants.MOVE_STEP_SLAT;
									if (newPosition < device.getMinSlatPosition()) {
										newPosition = device.getMinSlatPosition();
									}
									boolean transmitted = digitalSTROM.setDeviceOutputValue(getToken(), device.getDSID(), null, DeviceConstants.DEVICE_SENSOR_SLAT_OUTPUT, newPosition);
									
									if (transmitted) {
										device.setSlatPosition(newPosition);
									}
								}
							}
							else {
								handleUpDownForRollershutter(device, cm);
							}
						}
						
					}
					else if (device.getOutputMode().equals(OutputModeEnum.UP_DOWN)) {
						handleUpDownForRollershutter(device, cm);
					}
					else {
						logger.warn("Wrong item configuration ... this hardware is not a rollershutter: "+itemName);
					}
				}
				
			}
			else {	
				
				// this item is not mapped to real hardware, it is to make scene calls (as they are known in digitalSTROM) 
				
				if (cm instanceof DecimalType) {
					Map<String, BindingConfig> clonedConfigMap = getConfigMap();
					DigitalSTROMBindingConfigItem confItem = (DigitalSTROMBindingConfigItem)clonedConfigMap.get(itemName);
					
					if (confItem != null && confItem.context != null) {
						if (confItem.context.equals(ContextConfig.apartment)) {
							digitalSTROM.callApartmentScene(getToken(), confItem.groupID, null, ApartmentSceneEnum.getApartmentScene(((DecimalType)cm).intValue()), false);
						}
						else if (confItem.context.equals(ContextConfig.zone)) {
							digitalSTROM.callZoneScene(getToken(), confItem.zoneID, null, confItem.groupID, null, ZoneSceneEnum.getZoneScene(((DecimalType)cm).intValue()), false);
						}
					}
				}
				else if (cm instanceof StringType) {
					Map<String, BindingConfig> clonedConfigMap = getConfigMap();
					DigitalSTROMBindingConfigItem confItem = (DigitalSTROMBindingConfigItem)clonedConfigMap.get(itemName);
					
					if (confItem != null && confItem.context != null) {
						int scene = -1;
						try {
							scene = Integer.parseInt(cm.toString());
						} catch (java.lang.NumberFormatException e) {
							logger.error("NumberFormatException by parsing "+cm.toString()+" for "+confItem.itemName);
						}
						if (scene != -1) {
							if (confItem.context.equals(ContextConfig.apartment)) {
								digitalSTROM.callApartmentScene(getToken(), confItem.groupID, null, ApartmentSceneEnum.getApartmentScene(scene), false);
							}
							else if (confItem.context.equals(ContextConfig.zone)) {
								digitalSTROM.callZoneScene(getToken(), confItem.zoneID, null, confItem.groupID, null, ZoneSceneEnum.getZoneScene(scene), false);
							}
						}
					}
				}
				else {
					logger.info("couldn't find digitalstrom device for "+itemName);
				}
			}
		}
		
	private void handleUpDownForRollershutter(Device device, Command cm) {
		if (((org.openhab.core.library.types.UpDownType) cm).equals(UpDownType.UP)) {
				
			boolean transmitted = digitalSTROM.callDeviceScene(getToken(), device.getDSID(), null, ZoneSceneEnum.MAXIMUM, false);
			if (transmitted) {
				addEcho(device.getDSID().getValue(), (short)ZoneSceneEnum.MAXIMUM.getSceneNumber());
			}
			else {
				logger.error("could not transmit command UP for dsid: "+device.getDSID().getValue());
			}
				
		}
		else if (((org.openhab.core.library.types.UpDownType) cm).equals(UpDownType.DOWN)) {
				
			boolean transmitted = digitalSTROM.callDeviceScene(getToken(), device.getDSID(), null, ZoneSceneEnum.MINIMUM, false);
				
			if (transmitted) {
				addEcho(device.getDSID().getValue(), (short)ZoneSceneEnum.MINIMUM.getSceneNumber());
			}
			else {
				logger.error("could not transmit command DOWN for dsid: "+device.getDSID().getValue());
			}
		}
	}
		
	private void handleStopMoveForRollershutter(Device device, Command cm) {
		if (((org.openhab.core.library.types.StopMoveType) cm).equals(StopMoveType.MOVE)) {
				
			boolean transmitted = digitalSTROM.callDeviceScene(getToken(), device.getDSID(), null, ZoneSceneEnum.AREA_STEPPING_CONTINUE, false);
				
			if (transmitted) {
				addEcho(device.getDSID().getValue(), (short)ZoneSceneEnum.AREA_STEPPING_CONTINUE.getSceneNumber());
			}
			else {
				logger.error("could NOT transmit command MOVE for dsid: "+device.getDSID().getValue());
			}
				
		}
		else if (((org.openhab.core.library.types.StopMoveType) cm).equals(StopMoveType.STOP)) {
			
			boolean transmitted = digitalSTROM.callDeviceScene(getToken(), device.getDSID(), null, ZoneSceneEnum.STOP, false);
				
			if (transmitted) {
				addEcho(device.getDSID().getValue(), (short)ZoneSceneEnum.STOP.getSceneNumber());
				
				initOutputValue(device,DeviceConstants.DEVICE_SENSOR_OUTPUT);
			}
			else {
				logger.warn("could NOT transmit command STOP for dsid: "+device.getDSID().getValue());
			}
		}
	}
			
	private int getPercent(int max, int val) {
		if (withParameterMax(max) && withParameterValue(val)) {
			return (100 * val / max);
		}
		return -1;
	}

	private int fromPercentToValue(int percent, int max) {
		if (percent < 0 || percent == 0) {
			return 0;
		}
		if (max < 0 || max == 0) {
			return 0;
		}
		return (int)(max * (float)((float)percent / 100));
	}

	private boolean withParameterMax(int max) {
		return (max > 0);

	}
	
	private boolean withParameterValue(int value) {
		return (value > -1);
	}
	
	private void removeAllDeviceListener() {
		Map<String, Device> clonedMap = getDsidToDeviceMap();
		Collection<Device> collection = clonedMap.values();
		
		for (Device device: collection) {
			device.removeDeviceListener(this);
		}
	}
	
	// This method is called after editing the *.items file (removing configurations)
	// ... in order to remove item and device (hardware) specific jobs
	private void stopConsumption(Item item, Device device) {
		stopApartmentConsumption(item);
		
		removeMeterConsumptionThreads(item.getName());
		removeDeviceConsumptionThreads(device.getDSID().getValue());
		removeMeterConsumptionJobs(item.getName());
		removeDeviceConsumptionJobs(item.getName());
	}
	
	// This method is called after editing the *.items file (removing configurations)
	// ... in order to remove item and device (hardware) specific jobs
	private void stopSensorRequests(Device device) {
		removeJob(device.getDSID().getValue());
		removeNextTimeJob(device.getDSID().getValue());
	}
	
	private void stopApartmentConsumption(Item item) {
		if (apartmentConsumptionThread != null) {
			if (apartmentConsumptionThread.getItemName().equals(item.getName())) {
				apartmentConsumptionThread.setShutdown(true);
				try {
					apartmentConsumptionThread.join();
				} catch (InterruptedException e) {
					logger.error("InterruptedException by waiting for the end of ApartmendConsumptionThread");
				}
				apartmentConsumptionThread = null;
			}
		}
	}
	
	// This method is called after editing the *.items file (removing configurations),
	// ... only item specific jobs are affected and not device (hardware) specific
	private void stopSpecific(Item item) {
		stopApartmentConsumption(item);
		
		removeMeterConsumptionThreads(item.getName());
		removeMeterConsumptionJobs(item.getName());
	}
	
	public String getToken() {
		return token;
	}
	
	// ... we want to ignore own 'command-echos'
	private void addEcho(String dsid, short sceneId) {
		synchronized(echoBox) {
			echoBox.add(dsid+"-"+sceneId);
		}
	}
	
	private void addDeviceConsumptionThread(Device device, ConsumptionConfig index, int timeinterval) {
		TimedDeviceConsumptionThread thread = new TimedDeviceConsumptionThread(device, index, timeinterval);
		
		synchronized(deviceConsumptionThreadList) {
			if (!deviceConsumptionThreadList.contains(thread)) {
				deviceConsumptionThreadList.add(thread);
			}
		}
	}
	
	private void addMeterConsumptionThread(Item item, String dsid, int timeinterval) {
		TimedMeterConsumptionThread thread = new TimedMeterConsumptionThread(item, dsid, timeinterval);
		
		synchronized(meterConsumptionThreadList) {
			if (!meterConsumptionThreadList.contains(thread)) {
				meterConsumptionThreadList.add(thread);
			}
		}
	}
	
	private void startApartmentConsumption() {
		if (!shutdown) {
			if (apartmentConsumptionThread != null) {
				apartmentConsumptionThread.start();
			}
		}
	}
	
	private void startMeterConsumption() {
		if (!shutdown) {
			synchronized(meterConsumptionThreadList) {
				for (TimedMeterConsumptionThread mC: meterConsumptionThreadList) {
					mC.start();
				}
			}
		}
	}
	
	private void startDeviceConsumption() {
		if (!shutdown) {
			synchronized(deviceConsumptionThreadList) {
				for (TimedDeviceConsumptionThread tD: deviceConsumptionThreadList) {
					tD.start();
				}
			}
		}
	}
	
	private void stopDSMConsumptionThreads() {
		synchronized(meterConsumptionThreadList) {
			for (TimedMeterConsumptionThread thread: meterConsumptionThreadList) {
				thread.setShutdown(true);
			}
		}
	}
	
	private void stopDeviceConsumptionThreads() {
		synchronized(deviceConsumptionThreadList) {
			for (TimedDeviceConsumptionThread thread: deviceConsumptionThreadList) {
				thread.setShutdown(true);
			}
		}
	}
	
	private void addJob(Thread thread) {
		synchronized(highPriorityJobBox) {
			if (!highPriorityJobBox.contains(thread)) {
				highPriorityJobBox.add(highPriorityJobBox.size(), thread);
			}
		}
	}
	
	private void removeJob(String dsid) {
		synchronized(highPriorityJobBox){
			for (Iterator<Thread> iter = highPriorityJobBox.iterator(); iter.hasNext();) {
				Thread thread = (Thread) iter.next();
				
				if (thread instanceof GetSceneOutputValue) {
					GetSceneOutputValue gS = (GetSceneOutputValue) thread;
					if (gS.getDsid().equals(dsid)) {
						iter.remove();
					}
				}
				else if (thread instanceof OutputValueOnDemand) {
					OutputValueOnDemand oV = (OutputValueOnDemand) thread;
					if (oV.getDsid().equals(dsid)) {
						iter.remove();
					}
				}
			}
		}
	}
	
	private void addNextTimeJob(Thread thread) {
		synchronized (lowPriorityJobBox) {
			if (!lowPriorityJobBox.contains(thread)) {
				lowPriorityJobBox.add(lowPriorityJobBox.size(), thread);
			}
		}
	}
	
	private void removeNextTimeJob(String dsid) {
		synchronized(lowPriorityJobBox) {
			for (Iterator<Thread> iter = lowPriorityJobBox.iterator(); iter.hasNext();) {
				Thread thread = (Thread) iter.next();
				
				if (thread instanceof GetSceneOutputValue) {
					GetSceneOutputValue sO = (GetSceneOutputValue) thread;
					if (sO.getDsid().equals(dsid)) {
						iter.remove();
					}
				}
				else if (thread instanceof OutputValueOnDemand) {
					OutputValueOnDemand oV = (OutputValueOnDemand) thread;
					if (oV.getDsid().equals(dsid)) {
						iter.remove();
					}
				}
			}
		}
	}
	
	private void addDeviceConsumptionJob(Thread thread) {
		synchronized (deviceConsumptionJobBox) {
			if (!deviceConsumptionJobBox.contains(thread)) {
				deviceConsumptionJobBox.add(deviceConsumptionJobBox.size(), thread);
			}
		}
	}
	
	private void addMeterConsumptionJob(Thread thread) {
		synchronized (meterConsumptionJobBox) {
			if (!meterConsumptionJobBox.contains(thread)) {
				meterConsumptionJobBox.add(meterConsumptionJobBox.size(), thread);
			}
		}
	}
	
	private Thread getJob() {
		Thread job = null;
		synchronized(highPriorityJobBox) {
			if (highPriorityJobBox.size()>0) {
				job = highPriorityJobBox.get(0);
				highPriorityJobBox.remove(job);
			}
		}
		return job;
	}
	
	private Thread getLowPriorityJob() {
		Thread job = null;
		synchronized (lowPriorityJobBox) {
			if (lowPriorityJobBox.size()>0) {
				job = lowPriorityJobBox.get(0);
				lowPriorityJobBox.remove(job);
			}
		}
		return job;
	}
	
	private Thread getDeviceConsumptionJob() {
		Thread job = null;
		synchronized (deviceConsumptionJobBox) {
			if (deviceConsumptionJobBox.size() > 0) {
				job = deviceConsumptionJobBox.get(0);
				deviceConsumptionJobBox.remove(job);
			}
		}
		return job;
	}
	
	private Thread getMeterConsumptionJob() {
		Thread job = null;
		synchronized (meterConsumptionJobBox) {
			if (meterConsumptionJobBox.size() > 0) {
				job = meterConsumptionJobBox.get(0);
				meterConsumptionJobBox.remove(job);
			}
		}
		return job;
	}
	
	private void removeMeterConsumptionJobs(String itemName) {
		synchronized (meterConsumptionJobBox) {
			for (Iterator<Thread> iter = meterConsumptionJobBox.iterator(); iter.hasNext();) {
				Thread thread = (Thread) iter.next();
				
				if (thread instanceof MeterConsumptionOnDemand) {
					MeterConsumptionOnDemand metCon = (MeterConsumptionOnDemand) thread;
					if (metCon.getItemName().equals(itemName)) {
						iter.remove();
					}
				}
				else if (thread instanceof ApartmentConsumptionJob) {
					ApartmentConsumptionJob apCon = (ApartmentConsumptionJob) thread;
					if (apCon.getItemName().equals(itemName)) {
						iter.remove();
					}
				}
			}
		}
	}
	
	private void removeDeviceConsumptionJobs(String dsid) {
		synchronized (deviceConsumptionJobBox) {
			for (Iterator<Thread> iter = deviceConsumptionJobBox.iterator(); iter.hasNext();) {
				Thread thread = (Thread) iter.next();
				
				if (thread instanceof DeviceConsumption) {
					DeviceConsumption devCon = (DeviceConsumption) thread;
					if (devCon.getDsid().equals(dsid)) {
						iter.remove();
					}
				}
			}
		}
	}
	
	private void removeMeterConsumptionThreads(String itemName) {
		synchronized(meterConsumptionThreadList) {
			for (Iterator<TimedMeterConsumptionThread> iter = meterConsumptionThreadList.iterator(); iter.hasNext();) {
				TimedMeterConsumptionThread thread = (TimedMeterConsumptionThread) iter.next();
				
				if (thread instanceof TimedMeterConsumptionThread) {
					TimedMeterConsumptionThread timedMetCon = (TimedMeterConsumptionThread) thread;
					if (timedMetCon.getItemName().equals(itemName)) {
						timedMetCon.setShutdown(true);
						iter.remove();
					}
				}
			}
		}
	}
	
	private void removeDeviceConsumptionThreads(String dsid) {
		synchronized(deviceConsumptionThreadList) {
			for (Iterator<TimedDeviceConsumptionThread> iter = deviceConsumptionThreadList.iterator(); iter.hasNext();) {
				TimedDeviceConsumptionThread thread = (TimedDeviceConsumptionThread) iter.next();
				
				if (thread instanceof TimedDeviceConsumptionThread) {
					TimedDeviceConsumptionThread timedDevCon = (TimedDeviceConsumptionThread) thread;
					if (timedDevCon.getDeviceDsid().equals(dsid)) {
						timedDevCon.setShutdown(true);
						iter.remove();
					}
				}
			}
		}
	}
	
	private boolean isEcho(String dsid, short sceneId) {
		String echo = dsid+"-"+sceneId;
		synchronized(echoBox) {
			if (echoBox.contains(echo)) {
				echoBox.remove(echo);
				return true;
			}
		}
		return false;
	}
	
	public boolean isParsingItems() {
		return parsingItems;
	}

	/**
	 * Should be set to true when starting parsing *.items file,
	 * and set to false when parsing is finished.
	 *  
	 * @param parsingItems the parsingItems to set
	 */
	public void setParsingItems(boolean parsingItems) {
		this.parsingItems = parsingItems;
	}
	
	private void setShutdown(boolean flag) {
		this.shutdown = flag;
	}
	
	private boolean isFirstPriority() {
		return firstPriority;
	}

	/**
	 * In order to transmit all commands very fast to digitalSTROM, the
	 * metering jobs, sensor requests and all cycled jobs(event listener and
	 * apartment structure refresher) will not be started in this time
	 * 
	 * @param flag
	 */
	private void setFirstPriority(boolean flag) {
		this.firstPriority = flag;
	}
	
	private boolean isSensorReading() {
		return sensorReading;
	}
	
	/**
	 * To avoid timeouts the metering of dSMs, refreshing apartment structure
	 * and the event listener will not start in this time
	 * 
	 * @param flag	true to stop dSM metering and other cycled lightweight jobs
	 */
	private synchronized void setSensorReading(boolean flag) {
		this.sensorReading = flag;
	}
	
	private boolean systemIsBusy() {
		return isParsingItems() || isFirstPriority() || isSensorReading() || isReadingFile();
	}
	
	private boolean isReadingFile() {
		return readingFile;
	}

	private void setReadingFile(boolean readingFile) {
		this.readingFile = readingFile;
	}

	//TODO Threads
	/**
	 * This thread is getting the digitalSTROM apartment structure
	 * in order to know any changes in the apartment (zones and groups)
	 *   
	 * @author Alexander Betker
	 * @since	1.3.0
	 *
	 */
	private class DigitalSTROMApartmentStructureObserver extends Thread {
		
		private boolean shutdown = false;
		
		public synchronized void setShutdown(boolean shutdown) {
			this.shutdown = shutdown;
		}
		
		@Override
		public void run() {
			while (!this.shutdown) {
					
				if (!systemIsBusy()) {
					Apartment apartment = null;
					
					try {
						apartment = digitalSTROM.getApartmentStructure(getToken());
					} catch (Exception e) {
						logger.error("ERROR in structure observer thread, couldn't get apartment structure; "+e.getLocalizedMessage());
					}
				
					this.handleStructure(apartment);
					
				}
				else {
					logger.info("skipping get apartment structure, because system is busy ... ");
				}
				
				try {
					sleep(60000L);
				} catch (InterruptedException e) {
					this.shutdown = true;
					logger.error("CRASH (InterruptedException) in apartment structure observer thread; "+e.getLocalizedMessage());
				}
			}
			
		}
		
		// Here we build up a new hashmap in order to replace it with the old one.
		// This hashmap is used to find the affected items after an event from digitalSTROM.
		private void handleStructure(Apartment apartment) {
			if (apartment != null) {
				
				Map<Integer, Map<Short, List<String>>> newZoneGroupMap	= Collections.synchronizedMap(new HashMap<Integer, Map<Short,List<String>>>());
				Map<String, Device> clonedDsidMap = getDsidToDeviceMap();
				
				for (Zone zone: apartment.getZoneMap().values()) {
					
					Map<Short, List<String>> groupMap = new HashMap<Short, List<String>>();
					
					for (DetailedGroupInfo g: zone.getGroups()) {
						
						List<String> devicesInGroup = new LinkedList<String>();
						for (String dsid: g.getDeviceList()) {
							if (clonedDsidMap.containsKey(dsid)) {
								devicesInGroup.add(dsid);
							}
						}
						
						groupMap.put(g.getGroupID(), devicesInGroup);
					
					}
					
					newZoneGroupMap.put(zone.getZoneId(), groupMap);
				}
				
				synchronized(digitalSTROMZoneGroupMap) {
					digitalSTROMZoneGroupMap = newZoneGroupMap;
				}
			}
		}
		
	}
	
	/**
	 * If someone turns a device or a zone etc. on, we will get a notification
	 * to update the state of the item
	 *  
	 * @author	Alexander Betker
	 * @since	1.3.0
	 *
	 */
	private class DigitalSTROMEventListener extends Thread {
		
		private boolean 		shutdown 	= false;
		private final String	EVENT_NAME	= "openhabEvent";
		private final int		ID			= 11;
		private int				timeout		= 1000;
		
		private final String	INVALID_SESSION	= "Invalid session!";//Invalid session!
		
		private HttpTransport 		transport	= null;
		private JSONResponseHandler	handler 	= null;
		
		public synchronized void setShutdown(boolean shutdown) {
			this.shutdown = shutdown;
		}
		
		public DigitalSTROMEventListener() {
			this.handler = new JSONResponseHandler();
			this.transport = new HttpTransport(uri, connectTimeout, readTimeout);
			this.subscribe();
		}
		
		private void subscribe() {
			if (getToken() != null) {
				
				boolean transmitted = digitalSTROM.subscribeEvent(getToken(), EVENT_NAME, this.ID, connectTimeout, readTimeout);
				
				if (transmitted) {
					this.setShutdown(false);
				}
				else {
					logger.error("Couldn't subscribe eventListener ... maybe timeout because system is to busy ...");
				}
			}
			else {
				logger.error("Couldn't subscribe eventListener because there is no token (no connection)");
			}
		}
		
		@Override
		public void run() {
			while (!this.shutdown) {
				
				if (!systemIsBusy()) {
					
					String request = this.getEventAsRequest(this.ID, 500);
					
					if (request != null) {
						
						String response = this.transport.execute(request, 2*this.timeout, this.timeout);
						
						JSONObject responseObj = this.handler.toJSONObject(response);
						
						if (this.handler.checkResponse(responseObj)) {
							JSONObject obj = this.handler.getResultJSONObject(responseObj);
							
							if (obj != null && obj.get(JSONApiResponseKeysEnum.EVENT_GET_EVENT.getKey()) instanceof JSONArray) {
								JSONArray array = (JSONArray) obj.get(JSONApiResponseKeysEnum.EVENT_GET_EVENT.getKey());
								try {
									handleEvent(array);
								}
								catch (Exception e) {
									logger.warn("EXCEPTION in eventListener thread : "+e.getLocalizedMessage());
								}
							}
						}
						else {
							String errorStr = null;
							if (responseObj != null && responseObj.get(JSONApiResponseKeysEnum.EVENT_GET_EVENT_ERROR.getKey()) != null) {
								errorStr = responseObj.get(JSONApiResponseKeysEnum.EVENT_GET_EVENT_ERROR.getKey()).toString();
							}
							
							if (errorStr != null && errorStr.equals(this.INVALID_SESSION)) {
								this.subscribe();
							}
							else if (errorStr != null) {
								logger.error("Unknown error message in event response: "+errorStr);
							}
						}
					}
				}
				else {
					logger.debug("Waiting to get events because is parsing items ...");
			
					try {
					sleep(this.timeout);
					} catch (InterruptedException e) {
						logger.error("InterruptedException in eventListener ... "+e.getStackTrace());
					}
				}
			}
			
			this.cleanUp();
		}
		
		private String getEventAsRequest(int subscriptionID, int timeout) {
			if (getToken() != null) {
				return JSONRequestConstants.JSON_EVENT_GET+JSONRequestConstants.PARAMETER_TOKEN+getToken()+
						JSONRequestConstants.INFIX_PARAMETER_SUBSCRIPTION_ID+subscriptionID+
						JSONRequestConstants.INFIX_PARAMETER_TIMEOUT+timeout; 
			}
			return null;
		}
		
		private boolean unsubscribeEvent(String name, int subscriptionID) {
			if (getToken() != null) {
				return digitalSTROM.unsubscribeEvent(getToken(), EVENT_NAME, this.ID, connectTimeout, readTimeout);
			}
			return false;
		}
		
		public synchronized boolean unsubscribe() {
			return this.unsubscribeEvent(this.EVENT_NAME, this.ID);
		}
		
		private void handleEvent(JSONArray array) {
			if (array.size() > 0) {
				Event event = new JSONEventImpl(array);
		
				for (EventItem item: event.getEventItems()) {
					if (item.getName() != null && item.getName().equals(this.EVENT_NAME)) {
						handleOpenhabEvent(item);
					}
				}
			}
		}
		
		private void cleanUp() {
			this.transport = null;
			this.handler = null;
		}
	}

	/**
	 * In order to avoid many sensor readings in a time,
	 * this thread starts the jobs, after the old one is finished
	 * 
	 * @author	Alexander Betker
	 * @since	1.3.0
	 *
	 */
	private class RunnerThread extends Thread {
	
		private boolean shutdown = false;
	
		private final int sleepTime = readTimeout;
	
		@Override
		public void run() {
		
			while (!this.shutdown) {
			
				if (!isFirstPriority() && !isReadingFile()) {
			
					Thread job = getJob();
			
					if (job != null) {
						job.start();
						try {
							job.join();
						} catch (InterruptedException e) {
							logger.error("InterruptedException by waiting for the end of a FirstPriorityJob ... "+e.getLocalizedMessage());
						}
					}
					else {
						job = getLowPriorityJob();
				
						if (job != null) {
							job.start();
							try {
								job.join();
							} catch (InterruptedException e) {
								logger.error("InterruptedException by waiting for the end of ScondPriorityJob ... "+e.getLocalizedMessage());
							}
						}
						else {
							job = getDeviceConsumptionJob();
					
							if (job != null) {
								job.start();
								try {
									job.join();
								} catch (InterruptedException e) {
									logger.error("InterruptedException by waiting for the end of DeviceConsumptionJob ... "+e.getLocalizedMessage());
								}
							}
						}
					}
				}
							
				try {
					sleep(this.sleepTime);
				} catch (InterruptedException e) {
					this.setShutdown(true);
					logger.error("InterruptedException in RunnerThread ... "+e.getStackTrace());
				}
			}
		}
	
		public synchronized void setShutdown(boolean flag) {
			this.shutdown = flag;
		}
	
	}

	/**
	 * Starts dSM metering jobs if system is not busy
	 * 
	 * @author	Alexander Betker
	 * @since	1.3.0
	 *
	 */
	private class MeterConsumptionWorker extends Thread {
	
	private int sleepTime = 500;
	private boolean shutdown = false;
	
	@Override
	public void run() {
		
		while (!this.shutdown) {
			
			if (!systemIsBusy()) {
				Thread job = getMeterConsumptionJob();
			
				if (job != null) {
					job.start();
					try {
						job.join();
					} catch (InterruptedException e) {
						logger.error("InterruptedException by waiting for the end of ApartmentConsumptionJob ... "+e.getLocalizedMessage());
					}
				}
			}
			
			try {
				sleep(this.sleepTime);
			} catch (InterruptedException e) {
				this.setShutdown(true);
				logger.error("InterruptedException in MeterConsumptionWorker ... "+e.getStackTrace());
			}
		}
	}
	
	public synchronized void setShutdown(boolean flag) {
		this.shutdown = flag;
	}
}

	/**
	 * Reads sensor values in order to know for sure, if a device is on or is dimmed
	 * or we want to get the consumption of a device. You can only get the consumption
	 * by reading sensor values and not by the API 'getDeviceConsumption' (you always get 0)
	 * 
	 * @author	Alexander Betker
	 * @since	1.20
	 *
	 */
	private class OutputValueOnDemand extends Thread {
	
		private Device device = null;
		private short index = 0;
	
		public OutputValueOnDemand(Device device, short index) {
			this.device = device;
			this.index = index;
		}
	
		@Override
		public void run() {
		
			if (!isReadingFile()) {
			
				setSensorReading(true);		// to ensure no starting of dSM-metering in this time
				int value = digitalSTROM.getDeviceOutputValue(getToken(), this.device.getDSID(), null, this.index);
				setSensorReading(false);
			
				logger.info("OutputValueThread on Demand : "+value+", DSID: "+this.device.getDSID().getValue());
			
				if (value != 1) {
					switch (this.index) {
					case 0:
						this.device.setOutputValue(value);
						break;
					case 4:
						this.device.setSlatPosition(value);
						break;
				
					default: 
						break;
					}
				}
			}
			
		this.cleanUp();
	}
	
	private void cleanUp() {
		this.device = null;
	}
	
	@Override
	public String toString() {
		return device.getDSID().getValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OutputValueOnDemand) {
			OutputValueOnDemand other = (OutputValueOnDemand) obj;
			String key = this.device.getDSID().getValue()+this.index;
			return key.equals((other.device.getDSID().getValue()+other.index));
		}
		return false;
	}
	
	public String getDsid() {
		return this.device.getDSID().getValue();
	}
	
}

	/**
	 * Creates device consumption jobs for a specific device
	 * 
	 * @author	Alexander Betker
	 * @since	1.3.0
	 *
	 */
	private class TimedDeviceConsumptionThread extends Thread {
	
		private Device device = null;
		private SensorIndexEnum sensorIndex = null;
	
		private int timeinterval = 0;
	
		private boolean shutdown = false;
	
		public TimedDeviceConsumptionThread(Device device, ConsumptionConfig index, int timeinterval) {
			this.device = device;
			
			try {
				this.sensorIndex = SensorIndexEnum.valueOf(index.name());
			} catch (Exception e) {
				this.sensorIndex = SensorIndexEnum.ACTIVE_POWER;
				logger.error("Parse ERROR on TimedDeviceConsumptionThread by DigitalSTROMSensorIndexEnum.valueOf "+index.name());
			}
		
			this.timeinterval = timeinterval;
		
		}
	
		@Override
		public void run() {
			while (!this.shutdown) {
			
				if (!isParsingItems() && !isReadingFile()) {
					addDeviceConsumptionJob(new DeviceConsumption(this.device, this.sensorIndex));
				}
			
				try {
					sleep(this.timeinterval);
				} catch (InterruptedException e) {
					this.setShutdown(true);
					logger.error("InterruptedException in TimedDeviceConsumptionThread for dsid: "+this.device.getDSID().getValue()+", "+e.getLocalizedMessage());
				}
			}
		
			this.cleanUp();
		}
	
		public synchronized void setShutdown(boolean flag) {
			this.shutdown = flag;
		}
	
		private void cleanUp() {
			this.device = null;
			this.sensorIndex = null;
		}
	
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TimedDeviceConsumptionThread) {
				TimedDeviceConsumptionThread other = (TimedDeviceConsumptionThread) obj;
				String str = other.device.getDSID().getValue()+"-"+other.sensorIndex.getIndex();
				return (this.device.getDSID().getValue()+"-"+sensorIndex.getIndex()).equals(str);
			}
			return false;
		}
	
		public String getDeviceDsid() {
			return this.device.getDSID().getValue();
		}
	
	}

	/**
	 * Device consumption of a specific device
	 * 
	 * @author	Alexander Betker
	 * @since	1.3.0
	 *
	 */
	private class DeviceConsumption extends Thread {
	
		private Device device = null;
		private SensorIndexEnum sensorIndex = null;
	
		public DeviceConsumption(Device device, SensorIndexEnum index) {
			this.device = device;
			this.sensorIndex = index;
		}
	
		@Override
		public void run() {
			if (!isParsingItems() && !isReadingFile()) {
			
				setSensorReading(true);		// no dSM-metering in this time
				int consumption = digitalSTROM.getDeviceSensorValue(getToken(), this.device.getDSID(), null, this.sensorIndex);
				setSensorReading(false);
			
				switch (this.sensorIndex) {
			
				case ACTIVE_POWER:
								this.device.setPowerConsumption(consumption);
								break;
				case OUTPUT_CURRENT:
								this.device.setElectricMeterValue(consumption);
								break;
				default:
					break;
				}
			}
		
			this.cleanUp();
		}
	
		private void cleanUp() {
			this.device = null;
			this.sensorIndex = null;
		}
	
		public String getDsid() {
			return this.device.getDSID().getValue();
		}
	
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof DeviceConsumption) {
				DeviceConsumption other = (DeviceConsumption) obj;
				String device = this.device.getDSID().getValue()+this.sensorIndex.getIndex();
				return device.equals(other.getDsid()+other.sensorIndex.getIndex());
			}
			return false;
		}
	
	}

	/**
	 * Reads the configuration for a device for a specific sceneID (scene call)
	 * (how to react and not: if to react or not)
	 * 
	 * @author	Alexander Betker
	 * @since	1.3.0
	 *
	 */
	private class GetSceneOutputValue extends Thread {
	
		private Device device = null;
		private short sceneId = 0;
	
		public GetSceneOutputValue(Device device, short sceneId) {
			this.device = device;
			this.sceneId = sceneId;
		}
	
		@Override
		public void run() {
		
			if (!isReadingFile()) {
			
				setSensorReading(true);
				DeviceConfig config = digitalSTROM.getDeviceConfig(getToken(), this.device.getDSID(), null, DeviceParameterClassEnum.CLASS_128, this.sceneId);
				setSensorReading(false);
			
				if (config != null) {
					this.device.setSceneOutputValue(this.sceneId, (short) config.getValue());
					logger.info("UPDATED sceneOutputValue for dsid: "+this.device.getDSID()+", sceneID: "+sceneId+", value: "+config.getValue());
				}
			}
		
			this.cleanUp();
		}
	
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof GetSceneOutputValue) {
				GetSceneOutputValue other = (GetSceneOutputValue) obj;
				String str = other.device.getDSID().getValue()+"-"+other.sceneId;
				return (this.device.getDSID().getValue()+"-"+this.sceneId).equals(str);
			}
			return false;
		}
	
		private void cleanUp() {
			this.device = null;
		}
	
		public String getDsid() {
			return this.device.getDSID().getValue();
		}
	}

	/**
	 * Creates consumption jobs to get the consumption of
	 * all dSMs
	 * 
	 * @author	Alexander Betker
	 * @since	1.3.0
	 *
	 */
	private class ApartmentConsumptionThread extends Thread {
	
		private Item item = null;
	
		private int timeinterval = 0;
		private boolean shutdown = false;
	
		public ApartmentConsumptionThread(Item item, int timeinterval) {
			this.item = item;
			this.timeinterval = timeinterval;
		}
	
	@Override
	public void run() {
		
		while (!this.shutdown) {
			
			if (!isParsingItems() && !isReadingFile()) {
				addMeterConsumptionJob(new ApartmentConsumptionJob(this.item));
			}
			
			try {
				sleep(this.timeinterval);
			} catch (InterruptedException e) {
				logger.error("InterruptedException in apartment consumption thread, "+e.getLocalizedMessage());
				this.setShutdown(true);
			}
		}
		
		this.cleanUp();
	}
	
	private void cleanUp() {
		this.item = null;
	}
	
	public synchronized void setShutdown(boolean flag) {
		this.shutdown = flag;
	}
	
	public String getItemName() {
		return this.item.getName();
	}
	
}
	
	/**
	 * Apartment consumption job
	 * 
	 * @author	Alexander Betker
	 * @since	1.3.0
	 *
	 */
	private class ApartmentConsumptionJob extends Thread {
	
	private Item item = null;
	
	public ApartmentConsumptionJob(Item item) {
		this.item = item;
	}
	
	@Override
	public void run() {
		
		if (!systemIsBusy()) {
			
			int consumptionValue = -1;
			
			List<CachedMeteringValue> list = digitalSTROM.getLatest(getToken(), MeteringTypeEnum.consumption, ".meters(all)", null);
			if (list != null) {
				consumptionValue = 0;
				for (CachedMeteringValue value: list) {
					consumptionValue += value.getValue();
				}
			}
		
			org.openhab.core.types.State state = UnDefType.NULL; 
			
			if (consumptionValue != -1) {
				state = new DecimalType(consumptionValue);
			}
			
			if (this.item instanceof NumberItem) {
				((NumberItem) this.item).setState(state);
			}
			else if (this.item instanceof StringItem) {
				((StringItem) this.item).setState(state);
			}
		
		}
		
		this.cleanUp();
	}
	
		private void cleanUp() {
			this.item = null;
		}
	
		public String getItemName() {
			return this.item.getName();
		}
	
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ApartmentConsumptionJob) {
				ApartmentConsumptionJob other = (ApartmentConsumptionJob) obj;
				return item.getName().equals(other.getItemName());
			}
			return false;
		}
	}

	/**
	 * Creates consumption jobs for a specific dSM
	 * 
	 * @author	Alexander Betker
	 * @since	1.3.0
	 *
	 */
	private class TimedMeterConsumptionThread extends Thread {
	
		private Item item = null;
		private DSID dsid = null;
		private ConsumptionConfig consumptionType = null;
	
		private boolean shutdown = false;
	
		private int sleepTime = 0;
	
		public TimedMeterConsumptionThread(Item item, String dsid, int timeinterval) {
			this.item = item;
			this.dsid = new DSID(dsid);
			this.sleepTime = timeinterval;
		
			Map<String, BindingConfig> clonedConfigMap = getConfigMap();
			BindingConfig conf = clonedConfigMap.get(item.getName());
		
			if (conf instanceof DigitalSTROMBindingConfigItem) {
				DigitalSTROMBindingConfigItem confItem = (DigitalSTROMBindingConfigItem) conf;
			
				if (confItem.dsmid != null) {
					if (confItem.consumption != null) {
						this.consumptionType = confItem.consumption;
					}
					else {
						this.consumptionType = ConsumptionConfig.ACTIVE_POWER;
					}
				}
			}
		}
	
		@Override
		public void run() {
			while (!this.shutdown) {
			
				if (!isParsingItems() && !isReadingFile()) {
					addMeterConsumptionJob(new MeterConsumptionOnDemand(this.item, this.dsid, this.consumptionType));
				}
				
				try {
					sleep(this.sleepTime);
				} catch (InterruptedException e) {
					this.setShutdown(true);
					logger.error("InterruptedException in TimedMeterConsumptionThread with itemName: "+this.item.getName()+" and dsid: "+this.dsid.getValue());
				}
			}
		
			this.cleanUp();
		}
	
		public synchronized void setShutdown(boolean flag) {
			this.shutdown = flag;
		}
	
		private void cleanUp() {
			this.item = null;
			this.dsid = null;
			this.consumptionType = null;
		}
	
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TimedMeterConsumptionThread) {
				TimedMeterConsumptionThread other = (TimedMeterConsumptionThread) obj;
				String meter = this.dsid.getValue()+this.consumptionType.name();
				return meter.equals(other.dsid.getValue()+other.consumptionType.name());
			}
			return false;
		}
	
		public String getItemName() {
			return this.item.getName();
		}
	}

	/**
	 * dSM consumption job
	 * @author	Alexander Betker
	 * @since	1.3.0
	 *
	 */
	private class MeterConsumptionOnDemand extends Thread {
	
		private Item item = null;
		private DSID dsid = null;
		private ConsumptionConfig consumptionType = null;
	
		public MeterConsumptionOnDemand(Item item, DSID dsid, ConsumptionConfig consumptionType) {
			this.item = item;
			this.dsid = dsid;
			this.consumptionType = consumptionType;
		}
	
		@Override
		public void run() {
		
			if (!systemIsBusy()) {
			
				int consumptionValue = -1;
			
				switch (this.consumptionType) {
			
				case ACTIVE_POWER :
				
				List<CachedMeteringValue> consumptionList = digitalSTROM.getLatest(getToken(), MeteringTypeEnum.consumption, ".meters("+dsid.getValue()+")", null);
				if (consumptionList != null) {
					consumptionValue = 0;
					for (CachedMeteringValue value: consumptionList) {
						consumptionValue += value.getValue();
					}
				}
				
				break;
			case ELECTRIC_METER:
				
				List<CachedMeteringValue> energyList = digitalSTROM.getLatest(getToken(), MeteringTypeEnum.energy, ".meters("+dsid.getValue()+")",MeteringUnitsEnum.Wh);
				if (energyList != null) {
					consumptionValue = 0;
					for (CachedMeteringValue value: energyList) {
						consumptionValue += value.getValue();
					}
				}
				break;
			default:
				break;
				
			}
			
				org.openhab.core.types.State state = UnDefType.NULL; 
				
				if (consumptionValue != -1) {
					state = new DecimalType(consumptionValue);
				}
				
				if (this.item instanceof NumberItem) {
					((NumberItem) this.item).setState(state);
				}
				else if (this.item instanceof StringItem) {
					((StringItem) this.item).setState(state);
				}
			
			}
			this.cleanUp();
		}
	
		private void cleanUp() {
			this.item = null;
			this.dsid = null;
			this.consumptionType = null;
		}
	
		public String getItemName() {
			return this.item.getName();
		}
	
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof MeterConsumptionOnDemand) {
				MeterConsumptionOnDemand other = (MeterConsumptionOnDemand) obj;
				String meter = this.dsid.getValue()+this.consumptionType.name();
				return meter.equals(other.dsid.getValue()+other.consumptionType.name());
			}
			return false;
		}
	}


	/**
	 * Starts the mapping of the items with hardware and starts the
	 * metering
	 * 
	 * @author	Alexander Betker
	 * @since	1.3.0
	 *
	 */
	private class Countdown extends Thread {
	
		private final int DEFAULT_START = 10;
	
		private int value = DEFAULT_START;
	
		@Override
		public void run() {
			// set flag to avoid starting of any consumption jobs or sensor reading in this time
			setParsingItems(true);
		
			while (this.value > 0) {
				this.value -= 1;
			
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					this.value = -1;
					logger.error("InterruptedException in counter thread ... "+e.getLocalizedMessage());
				}
			}
		
			if (getToken() != null) {
				if (!shutdown) {
					setupItems();
					initItemsState();
					startDigitalstromStructureObserver();
					startApartmentConsumption();
					startMeterConsumption();
					startDeviceConsumption();
					registerEventListener(); 
					
				}
			}
		
			setParsingItems(false);
			
		}
	
		private void initItemsState() {
			Map<String, BindingConfig> confMap = getConfigMap();
			Collection<Item> items = getNameToItemMap().values();
			if (items != null) {
				for (Item item: items) {
					DigitalSTROMBindingConfigItem confItem = (DigitalSTROMBindingConfigItem)confMap.get(item.getName());
					
					if (confItem != null) {
						if (!confItem.isValidMeterItem() && !confItem.isValidSceneItem()) {
							updateItemState(item);
						}
					}
				}
			}
		}
		
		public void reset() {
			this.value = DEFAULT_START;
		}
	
	}

	@Override
	public void addItemListener(ItemListener itemListener) {
		if (itemListener != null) {
			synchronized(this.itemListenerList) {
				if (!this.itemListenerList.contains(itemListener)) {
					this.itemListenerList.add(itemListener);
				}
			}
		}
	}

	@Override
	public void removeItemListener(ItemListener itemListener) {
		if (itemListener != null) {
			synchronized(this.itemListenerList) {
				if (this.itemListenerList.contains(itemListener)) {
					this.itemListenerList.remove(itemListener);
				}
			}
		}
	}

	@Override
	public void notifyItemListener(String itemName, State state) {
		synchronized (itemListenerList) {
			for (ItemListener l: itemListenerList) {
				l.itemStateUpdated(itemName, state);
			}
		}
	}
	
	/**
	 * This thread is responsible for the connection session to the
	 * digitalSTROM-Server (dSS). In future perhaps making a simple test,
	 * in order to know for sure: there is still a valid session or not
	 * 
	 * @author	Alexander Betker
	 * @since	1.3.0
	 * 
	 */
	private class Heartbeat extends Thread {

		private boolean shutdown = false;

		private int sleepTime = 45000;

		@Override
		public void run() {
			while (!this.shutdown) {

				if (!serverIsFound()) {
					login();
				}
				else if (!systemIsBusy()){
					if (digitalSTROM.getTime(getToken()) == -1) {
						logger.warn("test method failed ... new login now");
						login();
					}
				}

				try {
					sleep(this.sleepTime);
				} catch (InterruptedException e) {
					this.setShutdown(true);
					logger.error("Heartbeat thread CRASHED ... "
							+ e.getLocalizedMessage());
				}
			}

		}

		public synchronized void setShutdown(boolean flag) {
			this.shutdown = flag;
			if (flag) {
				digitalSTROM.logout();
			}
		}
	}
	
	private synchronized boolean serverIsFound() {
		return serverIsFound;
	}
	
	private synchronized void setServerIsFound(boolean found) {
		serverIsFound = found;
	}
	
}
