/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openhab.binding.digitalstrom.DigitalSTROMBindingProvider;
import org.openhab.binding.digitalstrom.internal.client.DigitalSTROMAPI;
import org.openhab.binding.digitalstrom.internal.client.connection.JSONResponseHandler;
import org.openhab.binding.digitalstrom.internal.client.connection.transport.HttpTransport;
import org.openhab.binding.digitalstrom.internal.client.constants.ApartmentSceneEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.DeviceConstants;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONApiResponseKeysEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONRequestConstants;
import org.openhab.binding.digitalstrom.internal.client.constants.MeteringTypeEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.MeteringUnitsEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.OutputModeEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.SceneToStateMapper;
import org.openhab.binding.digitalstrom.internal.client.constants.SensorIndexEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.ZoneSceneEnum;
import org.openhab.binding.digitalstrom.internal.client.entity.Apartment;
import org.openhab.binding.digitalstrom.internal.client.entity.CachedMeteringValue;
import org.openhab.binding.digitalstrom.internal.client.entity.DSID;
import org.openhab.binding.digitalstrom.internal.client.entity.DetailedGroupInfo;
import org.openhab.binding.digitalstrom.internal.client.entity.Device;
import org.openhab.binding.digitalstrom.internal.client.entity.DeviceSceneSpec;
import org.openhab.binding.digitalstrom.internal.client.entity.Event;
import org.openhab.binding.digitalstrom.internal.client.entity.EventItem;
import org.openhab.binding.digitalstrom.internal.client.entity.Zone;
import org.openhab.binding.digitalstrom.internal.client.entity.impl.JSONEventImpl;
import org.openhab.binding.digitalstrom.internal.client.events.DeviceListener;
import org.openhab.binding.digitalstrom.internal.client.events.EventPropertyEnum;
import org.openhab.binding.digitalstrom.internal.client.impl.DigitalSTROMJSONImpl;
import org.openhab.binding.digitalstrom.internal.client.job.DeviceConsumptionSensorJob;
import org.openhab.binding.digitalstrom.internal.client.job.DeviceOutputValueSensorJob;
import org.openhab.binding.digitalstrom.internal.client.job.SceneOutputValueSensorJob;
import org.openhab.binding.digitalstrom.internal.client.job.SensorJob;
import org.openhab.binding.digitalstrom.internal.config.ConnectionConfig;
import org.openhab.binding.digitalstrom.internal.config.ConsumptionConfig;
import org.openhab.binding.digitalstrom.internal.config.ContextConfig;
import org.openhab.binding.digitalstrom.internal.config.DigitalSTROMBindingConfig;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
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
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Betker
 * @author Alex Maier
 * @since 1.3.0
 */
public class DigitalSTROMBinding extends
		AbstractActiveBinding<DigitalSTROMBindingProvider> implements
		ManagedService, DeviceListener {

	
	private static final Logger logger = LoggerFactory
			.getLogger(DigitalSTROMBinding.class);

	/**
	 * the interval to find new refresh candidates (defaults to 1000
	 * milliseconds)
	 */
	private int granularity = 1000;

	/** host name with port of digitalSTROM Server */
	private String uri;

	/** ApplicationToken to connect to digitalSTROM Server */
	private static String applicationToken;

	private int connectTimeout = ConnectionConfig.DEFAULT_CONNECT_TIMEOUT;

	private int readTimeout = ConnectionConfig.DEFAULT_READ_TIMEOUT;

	private String user = null;

	private String password = null;

	private String sessionToken = null;

	private boolean serverIsFound = false;

	private DigitalSTROMAPI digitalSTROM = null;

	private DigitalSTROMEventListener digitalSTROMEventListener = null;

	/** Mapping digitalSTROM-Scene to digitalSTROM-State */
	private SceneToStateMapper stateMapper = new SceneToStateMapper();

	// ######### MAPS ###########

	// openHABItemName - DigitalSTROMDevice
	private Map<String, Device> deviceMap = Collections
			.synchronizedMap(new HashMap<String, Device>());

	// dsid-String - DigitalSTROMDevice
	private Map<String, Device> dsidToDeviceMap = Collections
			.synchronizedMap(new HashMap<String, Device>());

	// all digitalSTROM Devices retrieved by digitalSTROM Server
	private Map<String, Device> rawDsidToDeviceMap = Collections
			.synchronizedMap(new HashMap<String, Device>());

	// zoneID - Map < groupID, List<dsid-String>>
	private Map<Integer, Map<Short, List<String>>> digitalSTROMZoneGroupMap = Collections
			.synchronizedMap(new HashMap<Integer, Map<Short, List<String>>>());

	// itemName - time stamp
	private Map<String, Long> lastUpdateMap = new HashMap<String, Long>();

	// ####### LISTs #########

	private List<String> echoBox = Collections
			.synchronizedList(new LinkedList<String>());

	private List<SensorJob> highPrioritySensorJobs = Collections
			.synchronizedList(new LinkedList<SensorJob>());
	private List<SensorJob> mediumPrioritySensorJobs = Collections
			.synchronizedList(new LinkedList<SensorJob>());
	private List<SensorJob> lowPrioritySensorJobs = Collections
			.synchronizedList(new LinkedList<SensorJob>());

	private SensorJobExecutor sensorJobExecutor = null;

	public DigitalSTROMBinding() {
	}

	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
		for (DigitalSTROMBindingProvider provider : providers) {
			provider.removeBindingChangeListener(this);
		}

		if (digitalSTROMEventListener != null) {
			digitalSTROMEventListener.shutdown();
			digitalSTROMEventListener = null;
		}

		if (sensorJobExecutor != null) {
			sensorJobExecutor.shutdown();
			sensorJobExecutor = null;
		}

		removeAllDeviceListener();
		deallocateResources();
		providers.clear();
		digitalSTROM.logout();
	}

	private void removeAllDeviceListener() {
		Map<String, Device> clonedMap = getDsidToDeviceMap();
		Collection<Device> collection = clonedMap.values();

		for (Device device : collection) {
			device.removeDeviceListener(this);
		}
	}

	private void deallocateResources() {
		deviceMap.clear();
		dsidToDeviceMap.clear();
		rawDsidToDeviceMap.clear();
		digitalSTROMZoneGroupMap.clear();
		lastUpdateMap.clear();
		echoBox.clear();
	}

	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return granularity;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "digitalstrom Refresh Service";
	}


	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		if (!serverIsFound()) {
			login();
		} else {
			if (digitalSTROM.getTime(getSessionToken()) == -1) {
				logger.warn("test method failed ... new login now");
				login();
			}
		}

		for (DigitalSTROMBindingProvider provider : providers) {
			for (DigitalSTROMBindingConfig itemConf : provider
					.getAllCircuitConsumptionItems()) {

				String itemName = itemConf.itemName;
				int refreshInterval = itemConf.timeinterval;

				Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
				if (lastUpdateTimeStamp == null) {
					lastUpdateTimeStamp = 0L;
				}

				long age = System.currentTimeMillis() - lastUpdateTimeStamp;
				boolean needsUpdate = age >= refreshInterval;

				if (needsUpdate) {

					logger.debug("item '{}' is about to be refreshed now",
							itemName);

					int consumptionValue = -1;

					if (itemConf.consumption == null
							|| itemConf.consumption
									.equals(ConsumptionConfig.OUTPUT_CURRENT))
						itemConf.consumption = ConsumptionConfig.ACTIVE_POWER;

					switch (itemConf.consumption) {

					case ACTIVE_POWER:
						List<CachedMeteringValue> consumptionList = digitalSTROM
								.getLatest(getSessionToken(),
										MeteringTypeEnum.consumption,
										".meters("
												+ itemConf.dsmid.getValue()
														.toLowerCase() + ")",
										null);
						if (consumptionList != null) {
							consumptionValue = 0;
							for (CachedMeteringValue value : consumptionList) {
								consumptionValue += value.getValue();
							}
						}
						break;
					case ELECTRIC_METER:
						List<CachedMeteringValue> energyList = digitalSTROM
								.getLatest(getSessionToken(),
										MeteringTypeEnum.energy, ".meters("
												+ itemConf.dsmid.getValue()
														.toLowerCase() + ")",
										MeteringUnitsEnum.Wh);
						if (energyList != null) {
							consumptionValue = 0;
							for (CachedMeteringValue value : energyList) {
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

					if (state != null) {
						eventPublisher.postUpdate(itemName, state);
					}
					lastUpdateMap.put(itemName, System.currentTimeMillis());
				}
			}

			for (DigitalSTROMBindingConfig itemConf : provider
					.getAllDeviceConsumptionItems()) {

				String itemName = itemConf.itemName;
				int refreshInterval = itemConf.timeinterval;

				Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
				if (lastUpdateTimeStamp == null) {
					lastUpdateTimeStamp = 0L;
				}

				long age = System.currentTimeMillis() - lastUpdateTimeStamp;
				boolean needsUpdate = age >= refreshInterval;

				if (needsUpdate) {
					logger.debug("item '{}' is about to be refreshed now",
							itemName);

					Device device = getDsidToDeviceMap().get(
							itemConf.dsid.getValue());
					if (device != null) {
						SensorIndexEnum sensorIndex = null;
						try {
							sensorIndex = SensorIndexEnum
									.valueOf(itemConf.consumption.name());
						} catch (Exception e) {
							sensorIndex = SensorIndexEnum.ACTIVE_POWER;
						}
						addLowPriorityJob(new DeviceConsumptionSensorJob(
								device, sensorIndex));
						lastUpdateMap.put(itemName, System.currentTimeMillis());
					}
				}
			}

		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand() is called for item: "+ itemName);
		deviceCall(itemName, command);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate() is called for item: "+ itemName);
		eventPublisher.postUpdate(itemName, newState);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {

			String refreshIntervalStr = (String) config.get("refreshinterval");
			if (StringUtils.isNotBlank(refreshIntervalStr)) {
				granularity = Integer.parseInt(refreshIntervalStr);
			}

			String uriStr = (String) config.get("uri");
			if (StringUtils.isNotBlank(uriStr)) {
				uri = uriStr;
			}

			String connectTimeoutStr = (String) config.get("connectTimeout");
			if (StringUtils.isNotBlank(connectTimeoutStr)) {
				connectTimeout = Integer.parseInt(connectTimeoutStr);
			}

			String readTimeoutStr = (String) config.get("readTimeout");
			if (StringUtils.isNotBlank(readTimeoutStr)) {
				readTimeout = Integer.parseInt(readTimeoutStr);
			}

			String applicationTokenStr = (String) config
					.get("loginToken");
			if (StringUtils.isNotBlank(applicationTokenStr)) {
				applicationToken = applicationTokenStr;
			}

			String userStr = (String) config.get("user");
			if (StringUtils.isNotBlank(userStr)) {
				user = userStr;
			}

			String passwordStr = (String) config.get("password");
			if (StringUtils.isNotBlank(passwordStr)) {
				password = passwordStr;
			}
			this.digitalSTROM = new DigitalSTROMJSONImpl(uri, connectTimeout,
					readTimeout);
			
			registerDigitalSTROMEventListener();
			startSensorJobExecutor();
			initializeDevices();
			
			setProperlyConfigured(true);
		}
	}

	private void initializeDevices() {
		for (DigitalSTROMBindingProvider provider : this.providers) {
			
			Collection<String> itemNames=provider.getItemNames();
		
			//initialize devices
			for (String itemName : itemNames){
				DigitalSTROMBindingConfig confItem = getConfigForItemName(itemName);
				if (confItem != null && confItem.dsid != null) {
					if (rawDsidToDeviceMap.size() == 0 && serverIsFound()) {
						rawDsidToDeviceMap = getAllDigitalSTROMDevicesMap();
					}
					Device device = rawDsidToDeviceMap.get(confItem.dsid.getValue());
					if (device != null) {
						addDevice(itemName, device);
						updateItemState(confItem.item);
						handleStructure(digitalSTROM
								.getApartmentStructure(getSessionToken()));
					}
				}
			}
		}
	}

	private Map<String, Device> getDsidToDeviceMap() {
		return new HashMap<String, Device>(dsidToDeviceMap);
	}

	private Map<Integer, Map<Short, List<String>>> getDigitalSTROMZoneGroupMap() {
		return new HashMap<Integer, Map<Short, List<String>>>(
				digitalSTROMZoneGroupMap);
	}

	private Map<String, Device> getAllDigitalSTROMDevicesMap() {
		Map<String, Device> deviceMap = new HashMap<String, Device>();

		for (Device device : digitalSTROM.getApartmentDevices(
				getSessionToken(), false)) {
			deviceMap.put(device.getDSID().getValue(), device);
		}
		return deviceMap;
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

	// Here we build up a new hashmap in order to replace it with the old one.
	// This hashmap is used to find the affected items after an event from
	// digitalSTROM.
	private void handleStructure(Apartment apartment) {
		if (apartment != null) {

			Map<Integer, Map<Short, List<String>>> newZoneGroupMap = Collections
					.synchronizedMap(new HashMap<Integer, Map<Short, List<String>>>());
			Map<String, Device> clonedDsidMap = getDsidToDeviceMap();

			for (Zone zone : apartment.getZoneMap().values()) {

				Map<Short, List<String>> groupMap = new HashMap<Short, List<String>>();

				for (DetailedGroupInfo g : zone.getGroups()) {

					List<String> devicesInGroup = new LinkedList<String>();
					for (String dsid : g.getDeviceList()) {
						if (clonedDsidMap.containsKey(dsid)) {
							devicesInGroup.add(dsid);
						}
					}
					groupMap.put(g.getGroupID(), devicesInGroup);
				}
				newZoneGroupMap.put(zone.getZoneId(), groupMap);
			}

			synchronized (digitalSTROMZoneGroupMap) {
				digitalSTROMZoneGroupMap = newZoneGroupMap;
			}
		}
	}

	private DigitalSTROMBindingConfig getConfigForItemName(String itemName) {
		for (DigitalSTROMBindingProvider provider : this.providers) {
			if (provider.getItemConfig(itemName) != null) {
				return provider.getItemConfig(itemName);
			}
		}
		return null;
	}

	private List<String> getItemNamesForDsid(String dsid) {
		for (DigitalSTROMBindingProvider provider : this.providers) {
			if (provider.getItemNamesByDsid(dsid) != null) {
				return provider.getItemNamesByDsid(dsid);
			}
		}
		return null;
	}

	private void deviceCall(String itemName, Command cm) {
		Device device = deviceMap.get(itemName);

		if (device != null) {
			if (cm instanceof org.openhab.core.library.types.OnOffType) {

				if (((org.openhab.core.library.types.OnOffType) cm)
						.equals(OnOffType.ON)) {

					boolean transmitted = digitalSTROM.turnDeviceOn(
							getSessionToken(), device.getDSID(), null);
					if (transmitted) {
						device.setOutputValue(device.getMaxOutPutValue());
						addEcho(device.getDSID().getValue(),
								(short) ZoneSceneEnum.MAXIMUM.getSceneNumber());
					}

				} else if (((org.openhab.core.library.types.OnOffType) cm)
						.equals(OnOffType.OFF)) {

					boolean transmitted = digitalSTROM.turnDeviceOff(
							getSessionToken(), device.getDSID(), null);
					if (transmitted) {
						device.setOutputValue(0);
						addEcho(device.getDSID().getValue(),
								(short) ZoneSceneEnum.MINIMUM.getSceneNumber());
					}

				}

			} else if (cm instanceof org.openhab.core.library.types.IncreaseDecreaseType) {

				if (!device.isDimmable()) {
					logger.warn("device is not in dimm mode: " + itemName
							+ " outputMode: "
							+ device.getOutputMode().getMode());
					return;
				}

				if (((org.openhab.core.library.types.IncreaseDecreaseType) cm)
						.equals(IncreaseDecreaseType.INCREASE)) {

					boolean transmitted = digitalSTROM.callDeviceScene(
							getSessionToken(), device.getDSID(), null,
							ZoneSceneEnum.INCREMENT, false);
					if (transmitted) {
						addEcho(device.getDSID().getValue(),
								(short) ZoneSceneEnum.INCREMENT
										.getSceneNumber());

						if (device.getOutputValue() == 0) {
							initDeviceOutputValue(device,
									DeviceConstants.DEVICE_SENSOR_OUTPUT);
						} else {
							device.increase();
						}
					} else {
						logger.error("transmitting increase command FAILED "
								+ itemName);
					}

				} else if (((org.openhab.core.library.types.IncreaseDecreaseType) cm)
						.equals(IncreaseDecreaseType.DECREASE)) {

					boolean transmitted = digitalSTROM.callDeviceScene(
							getSessionToken(), device.getDSID(), null,
							ZoneSceneEnum.DECREMENT, false);
					if (transmitted) {
						addEcho(device.getDSID().getValue(),
								(short) ZoneSceneEnum.DECREMENT
										.getSceneNumber());
						device.decrease();
					} else {
						logger.error("transmitting decrease command FAILED "
								+ itemName);
					}
				}

			} else if (cm instanceof org.openhab.core.library.types.PercentType) {
				int percent = -1;
				try {
					percent = (int) Float.parseFloat(cm.toString());
				} catch (java.lang.NumberFormatException e) {
					logger.error("NumberFormatException on a PercentType with command: "
							+ cm.toString());
				}
				if (percent != -1) {
					if (percent > -1 && percent < 101) {

						if (device.getOutputMode().equals(OutputModeEnum.SLAT)) {

							DigitalSTROMBindingConfig confItem = getConfigForItemName(itemName);
							if (confItem != null) {

								if (confItem.context != null
										&& confItem.context
												.equals(ContextConfig.slat)) {

									int old = device.getSlatPosition();
									device.setSlatPosition(fromPercentToValue(
											percent,
											device.getMaxSlatPosition()));

									boolean transmitted = digitalSTROM
											.setDeviceOutputValue(
													getSessionToken(),
													device.getDSID(),
													null,
													DeviceConstants.DEVICE_SENSOR_SLAT_OUTPUT,
													fromPercentToValue(
															percent,
															device.getMaxSlatPosition()));
									if (!transmitted) {
										device.setSlatPosition(old);
										logger.error("could NOT successfully set new value for slats ..."
												+ cm.toString());
									}
								} else {

									int old = device.getOutputValue();

									device.setOutputValue(fromPercentToValue(
											percent, device.getMaxOutPutValue()));

									boolean transmitted = digitalSTROM
											.setDeviceValue(
													getSessionToken(),
													device.getDSID(),
													null,
													fromPercentToValue(
															percent,
															device.getMaxOutPutValue()));
									if (!transmitted) {
										device.setOutputValue(old);
										logger.error("could NOT successfully set new value ..."
												+ cm.toString());
									}

								}
							}

						} else {

							int old = device.getOutputValue();

							device.setOutputValue(fromPercentToValue(percent,
									device.getMaxOutPutValue()));

							boolean transmitted = digitalSTROM.setDeviceValue(
									getSessionToken(),
									device.getDSID(),
									null,
									fromPercentToValue(percent,
											device.getMaxOutPutValue()));
							if (!transmitted) {
								device.setOutputValue(old);
								logger.error("could NOT successfully set new value ..."
										+ cm.toString());
							}

						}
					}
				}
			} else if (cm instanceof org.openhab.core.library.types.StopMoveType) {

				if (device.getOutputMode().equals(OutputModeEnum.SLAT)) {

					DigitalSTROMBindingConfig confItem = getConfigForItemName(itemName);
					if (confItem != null) {
						if (confItem.context != null
								&& confItem.context.equals(ContextConfig.slat)) {
							logger.warn("stop and move command NOT possible for slats, use PercentType command or up and down please");
						} else {
							handleStopMoveForRollershutter(device, cm);
						}
					}
				} else if (device.getOutputMode()
						.equals(OutputModeEnum.UP_DOWN)) {
					handleStopMoveForRollershutter(device, cm);
				}
			} else if (cm instanceof org.openhab.core.library.types.UpDownType) {

				if (device.getOutputMode().equals(OutputModeEnum.SLAT)) {

					// 255 is max open, 0 is closed
					DigitalSTROMBindingConfig confItem = getConfigForItemName(itemName);
					if (confItem != null) {

						if (confItem.context != null
								&& confItem.context.equals(ContextConfig.slat)) {

							if (((org.openhab.core.library.types.UpDownType) cm)
									.equals(UpDownType.UP)) {

								int slatPosition = device.getSlatPosition();
								int newPosition = slatPosition
										+ DeviceConstants.MOVE_STEP_SLAT;
								if (newPosition > device.getMaxSlatPosition()) {
									newPosition = device.getMaxSlatPosition();
								}

								boolean transmitted = digitalSTROM
										.setDeviceOutputValue(
												getSessionToken(),
												device.getDSID(),
												null,
												DeviceConstants.DEVICE_SENSOR_SLAT_OUTPUT,
												newPosition);
								if (transmitted) {
									device.setSlatPosition(newPosition);
								}

							} else if (((org.openhab.core.library.types.UpDownType) cm)
									.equals(UpDownType.DOWN)) {

								int slatPosition = device.getSlatPosition();
								int newPosition = slatPosition
										- DeviceConstants.MOVE_STEP_SLAT;
								if (newPosition < device.getMinSlatPosition()) {
									newPosition = device.getMinSlatPosition();
								}
								boolean transmitted = digitalSTROM
										.setDeviceOutputValue(
												getSessionToken(),
												device.getDSID(),
												null,
												DeviceConstants.DEVICE_SENSOR_SLAT_OUTPUT,
												newPosition);

								if (transmitted) {
									device.setSlatPosition(newPosition);
								}
							}
						} else {
							handleUpDownForRollershutter(device, cm);
						}
					}

				} else if (device.getOutputMode()
						.equals(OutputModeEnum.UP_DOWN)) {
					handleUpDownForRollershutter(device, cm);
				} else {
					logger.warn("Wrong item configuration ... this hardware is not a rollershutter: "
							+ itemName);
				}
			}

		} else {

			// this item is not mapped to real hardware, it is to make scene
			// calls (as they are known in digitalSTROM)

			if (cm instanceof DecimalType) {
				DigitalSTROMBindingConfig confItem = getConfigForItemName(itemName);
				if (confItem != null && confItem.context != null) {
					if (confItem.context.equals(ContextConfig.apartment)) {
						digitalSTROM.callApartmentScene(getSessionToken(),
								confItem.groupID, null, ApartmentSceneEnum
										.getApartmentScene(((DecimalType) cm)
												.intValue()), false);
					} else if (confItem.context.equals(ContextConfig.zone)) {
						digitalSTROM.callZoneScene(getSessionToken(),
								confItem.zoneID, null, confItem.groupID, null,
								ZoneSceneEnum.getZoneScene(((DecimalType) cm)
										.intValue()), false);
					}
				}
			} else if (cm instanceof StringType) {
				DigitalSTROMBindingConfig confItem = getConfigForItemName(itemName);
				if (confItem != null && confItem.context != null) {
					int scene = -1;
					try {
						scene = Integer.parseInt(cm.toString());
					} catch (java.lang.NumberFormatException e) {
						logger.error("NumberFormatException by parsing "
								+ cm.toString() + " for " + confItem.itemName);
					}
					if (scene != -1) {
						if (confItem.context.equals(ContextConfig.apartment)) {
							digitalSTROM
									.callApartmentScene(getSessionToken(),
											confItem.groupID, null,
											ApartmentSceneEnum
													.getApartmentScene(scene),
											false);
						} else if (confItem.context.equals(ContextConfig.zone)) {
							digitalSTROM.callZoneScene(getSessionToken(),
									confItem.zoneID, null, confItem.groupID,
									null, ZoneSceneEnum.getZoneScene(scene),
									false);
						}
					}
				}
			} else {
				logger.warn("couldn't find digitalstrom device for " + itemName);
			}
		}
	}

	private void handleUpDownForRollershutter(Device device, Command cm) {
		if (((org.openhab.core.library.types.UpDownType) cm)
				.equals(UpDownType.UP)) {

			boolean transmitted = digitalSTROM.callDeviceScene(
					getSessionToken(), device.getDSID(), null,
					ZoneSceneEnum.MAXIMUM, false);
			if (transmitted) {
				addEcho(device.getDSID().getValue(),
						(short) ZoneSceneEnum.MAXIMUM.getSceneNumber());
			} else {
				logger.error("could not transmit command UP for dsid: "
						+ device.getDSID().getValue());
			}

		} else if (((org.openhab.core.library.types.UpDownType) cm)
				.equals(UpDownType.DOWN)) {

			boolean transmitted = digitalSTROM.callDeviceScene(
					getSessionToken(), device.getDSID(), null,
					ZoneSceneEnum.MINIMUM, false);

			if (transmitted) {
				addEcho(device.getDSID().getValue(),
						(short) ZoneSceneEnum.MINIMUM.getSceneNumber());
			} else {
				logger.error("could not transmit command DOWN for dsid: "
						+ device.getDSID().getValue());
			}
		}
	}

	private void handleStopMoveForRollershutter(Device device, Command cm) {
		if (((org.openhab.core.library.types.StopMoveType) cm)
				.equals(StopMoveType.MOVE)) {

			boolean transmitted = digitalSTROM.callDeviceScene(
					getSessionToken(), device.getDSID(), null,
					ZoneSceneEnum.AREA_STEPPING_CONTINUE, false);

			if (transmitted) {
				addEcho(device.getDSID().getValue(),
						(short) ZoneSceneEnum.AREA_STEPPING_CONTINUE
								.getSceneNumber());
			} else {
				logger.error("could NOT transmit command MOVE for dsid: "
						+ device.getDSID().getValue());
			}

		} else if (((org.openhab.core.library.types.StopMoveType) cm)
				.equals(StopMoveType.STOP)) {

			boolean transmitted = digitalSTROM.callDeviceScene(
					getSessionToken(), device.getDSID(), null,
					ZoneSceneEnum.STOP, false);

			if (transmitted) {
				addEcho(device.getDSID().getValue(),
						(short) ZoneSceneEnum.STOP.getSceneNumber());

				initDeviceOutputValue(device,
						DeviceConstants.DEVICE_SENSOR_OUTPUT);
			} else {
				logger.warn("could NOT transmit command STOP for dsid: "
						+ device.getDSID().getValue());
			}
		}
	}

	private int fromPercentToValue(int percent, int max) {
		if (percent < 0 || percent == 0) {
			return 0;
		}
		if (max < 0 || max == 0) {
			return 0;
		}
		return (int) (max * (float) ((float) percent / 100));
	}

	// ... we want to ignore own 'command-echos'
	private void addEcho(String dsid, short sceneId) {
		synchronized (echoBox) {
			echoBox.add(dsid + "-" + sceneId);
		}
	}

	private boolean isEcho(String dsid, short sceneId) {
		String echo = dsid + "-" + sceneId;
		synchronized (echoBox) {
			if (echoBox.contains(echo)) {
				echoBox.remove(echo);
				return true;
			}
		}
		return false;
	}

	private boolean isApartmentScene(short sceneId) {
		return (sceneId > 63);
	}

	private boolean isDimmScene(short sceneId) {
		if (sceneId > 9 && sceneId < 13) {
			return true;
		}
		if (sceneId == 15) { // command to dim or for a roller shutter
			return true;
		}
		if (sceneId > 41 && sceneId < 50) {
			return true;
		}
		if (sceneId > 51 && sceneId < 56) { // command to dim or for a roller
											// shutter
			return true;
		}
		return false;
	}

	private void handleDimmScene(Device device, short sceneID, short groupID,
			boolean force) {

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

		} else if (device.isRollershutter()) {
			switch (sceneID) {

			case 15:

				initDeviceOutputValue(device,
						DeviceConstants.DEVICE_SENSOR_OUTPUT);
				if (device.getOutputMode().equals(OutputModeEnum.SLAT)) {
					initDeviceOutputValue(device,
							DeviceConstants.DEVICE_SENSOR_SLAT_OUTPUT);
				}
				break;
			case 52:
			case 53:
			case 54:
			case 55:

				initDeviceOutputValue(device,
						DeviceConstants.DEVICE_SENSOR_OUTPUT);
				if (device.getOutputMode().equals(OutputModeEnum.SLAT)) {
					initDeviceOutputValue(device,
							DeviceConstants.DEVICE_SENSOR_SLAT_OUTPUT);
				}
				break;
			default:

			}
		}

	}

	private void decrease(Device device) {
		initDeviceOutputValue(device, DeviceConstants.DEVICE_SENSOR_OUTPUT);
	}

	private void increase(Device device) {
		initDeviceOutputValue(device, DeviceConstants.DEVICE_SENSOR_OUTPUT);
	}

	private void handleApartmentScene(short sceneId, short groupId) {

		if (groupId == 0) {
			Map<String, Device> clonedDeviceMap = getDsidToDeviceMap();
			Set<String> dsidSet = clonedDeviceMap.keySet();

			for (String dsid : dsidSet) {
				Device device = clonedDeviceMap.get(dsid);

				if (device != null) {

					if (!device.containsSceneConfig(sceneId)) {
						getSceneSpec(device, sceneId);
					}

					if (!device.doIgnoreScene(sceneId)) {
						short output = device.getSceneOutputValue(sceneId);
						if (output != -1) {
							device.setOutputValue(output);
						} else {
							initDeviceOutputValue(device,
									DeviceConstants.DEVICE_SENSOR_OUTPUT);
							initSceneOutputValue(device, sceneId);
						}
					}
				}
			}
		} else if (groupId != -1) {

			Map<String, Device> clonedDeviceMap = getDsidToDeviceMap();
			Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap().get(0);
			List<String> dsidList = map.get(groupId);

			if (dsidList != null) {
				for (String dsid : dsidList) {
					Device device = clonedDeviceMap.get(dsid);

					if (device != null) {

						if (!device.containsSceneConfig(sceneId)) {
							getSceneSpec(device, sceneId);
						}

						if (!device.doIgnoreScene(sceneId)) {
							short output = device.getSceneOutputValue(sceneId);
							if (output != -1) {
								device.setOutputValue(output);
							} else {
								initDeviceOutputValue(device,
										DeviceConstants.DEVICE_SENSOR_OUTPUT);
								initSceneOutputValue(device, sceneId);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * only works on openhabEvent! please copy "openhab/openhab.js" to your dSS
	 * server (/usr/share/dss/add-ons/) and "openhab.xml" to
	 * /usr/share/dss/data/subscriptions.d/ than you need to restart your dSS
	 * 
	 * If you don't, you will not get detailed infos about, what exactly
	 * happened (for example: which device was turned on by a browser or handy
	 * app )
	 * 
	 * @param eventItem
	 */
	private void handleOpenhabEvent(EventItem eventItem) {
		if (eventItem != null) {
			int zoneId = -1;
			short groupId = -1;
			short sceneId = -1;

			boolean isDeviceCall = false;
			String dsidStr = null;

			String zoneIDStr = eventItem.getProperties().get(
					EventPropertyEnum.ZONEID);
			if (zoneIDStr != null) {
				try {
					zoneId = Integer.parseInt(zoneIDStr);
				} catch (java.lang.NumberFormatException e) {
					logger.error("NumberFormatException by handling event at parsing zoneId");
				}
			}

			String sceneStr = eventItem.getProperties().get(
					EventPropertyEnum.SCENEID);
			if (sceneStr != null) {
				try {
					sceneId = Short.parseShort(sceneStr);
				} catch (java.lang.NumberFormatException e) {
					logger.error("NumberFormatException by handling event at parsing sceneId: "
							+ sceneStr);
				}
			}

			String groupStr = eventItem.getProperties().get(
					EventPropertyEnum.GROUPID);
			if (groupStr != null) {
				try {
					groupId = Short.parseShort(groupStr);
				} catch (java.lang.NumberFormatException e) {
					logger.error("NumberFormatException by handling event at parsing groupId");
				}
			}

			dsidStr = eventItem.getProperties().get(EventPropertyEnum.DSID);

			String deviceCallStr = eventItem.getProperties().get(
					EventPropertyEnum.IS_DEVICE_CALL);
			if (deviceCallStr != null) {
				isDeviceCall = deviceCallStr.equals("true");
			}

			if (sceneId != -1) {

				if (!isEcho(dsidStr, sceneId)) {

					if (isDeviceCall) {

						if (dsidStr != null) {
							Device device = getDsidToDeviceMap().get(dsidStr);

							if (device != null) {
								if (!device.containsSceneConfig(sceneId)) {
									getSceneSpec(device, sceneId);
								}

								if (isDimmScene(sceneId)) {
									if (!device.doIgnoreScene(sceneId)) {
										handleDimmScene(device, sceneId,
												(short) -1, true);
									}
								} else if (stateMapper.isMappable(sceneId)) {
									boolean shouldBeOn = stateMapper
											.getMapping(sceneId);

									if (!device.doIgnoreScene(sceneId)) {
										if (shouldBeOn) {
											device.setOutputValue(device
													.getMaxOutPutValue());
										} else {
											device.setOutputValue(0);
										}
									}
								} else {
									if (!device.doIgnoreScene(sceneId)) {
										short value = device
												.getSceneOutputValue(sceneId);
										if (value != -1) {
											device.setOutputValue(value);
										} else {
											initDeviceOutputValue(
													device,
													DeviceConstants.DEVICE_SENSOR_OUTPUT);
											initSceneOutputValue(device,
													sceneId);
										}
									}
								}
							}
						}
					} else {

						if (isApartmentScene(sceneId)) {
							handleApartmentScene(sceneId, groupId);
						} else {

							if (zoneId == 0) {
								if (isDimmScene(sceneId)) {

									Map<String, Device> deviceMap = getDsidToDeviceMap();

									if (groupId == 0) {

										Set<String> dsidSet = deviceMap
												.keySet();

										if (dsidSet != null) {
											for (String dsid : dsidSet) {
												Device device = deviceMap
														.get(dsid);

												if (device != null) {

													if (!device
															.containsSceneConfig(sceneId)) {
														getSceneSpec(device,
																sceneId);
													}

													if (!device
															.doIgnoreScene(sceneId)) {
														handleDimmScene(
																deviceMap
																		.get(dsid),
																sceneId,
																groupId, false);
													}

												}
											}
										}
									} else if (groupId != -1) {

										Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap()
												.get(zoneId);

										if (map != null) {
											List<String> dsidList = map
													.get(groupId);
											if (dsidList != null) {
												for (String dsid : dsidList) {
													Device device = deviceMap
															.get(dsid);

													if (device != null) {

														if (!device
																.containsSceneConfig(sceneId)) {
															getSceneSpec(
																	device,
																	sceneId);
														}

														if (!device
																.doIgnoreScene(sceneId)) {
															handleDimmScene(
																	deviceMap
																			.get(dsid),
																	sceneId,
																	groupId,
																	false);
														}
													}
												}
											}
										}
									}
								} else if (stateMapper.isMappable(sceneId)) {

									boolean shouldBeOn = stateMapper
											.getMapping(sceneId);

									if (groupId == 0) {

										Map<String, Device> deviceMap = getDsidToDeviceMap();
										Set<String> dsidSet = deviceMap
												.keySet();
										if (dsidSet != null) {
											for (String dsid : dsidSet) {
												Device device = deviceMap
														.get(dsid);
												if (device != null) {

													if (!device
															.containsSceneConfig(sceneId)) {
														getSceneSpec(device,
																sceneId);
													}

													if (!device
															.doIgnoreScene(sceneId)) {

														if (shouldBeOn) {
															device.setOutputValue(device
																	.getMaxOutPutValue());
														} else {
															device.setOutputValue(0);
														}

													}
												}
											}
										}
									} else if (groupId != -1) {

										Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap()
												.get(zoneId);
										Map<String, Device> deviceMap = getDsidToDeviceMap();
										if (map != null) {
											List<String> dsidList = map
													.get(groupId);
											if (dsidList != null) {
												for (String dsid : dsidList) {
													Device device = deviceMap
															.get(dsid);
													if (device != null) {

														if (!device
																.containsSceneConfig(sceneId)) {
															getSceneSpec(
																	device,
																	sceneId);
														}

														if (!device
																.doIgnoreScene(sceneId)) {

															if (shouldBeOn) {
																device.setOutputValue(device
																		.getMaxOutPutValue());
															} else {
																device.setOutputValue(0);
															}

														}
													}
												}
											}
										}
									}
								} else {

									Map<String, Device> deviceMap = getDsidToDeviceMap();

									if (groupId != -1) {
										Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap()
												.get(zoneId);
										if (map != null) {
											List<String> dsidList = map
													.get(groupId);
											if (dsidList != null) {
												for (String dsid : dsidList) {
													Device device = deviceMap
															.get(dsid);

													if (device != null) {

														if (!device
																.containsSceneConfig(sceneId)) {
															getSceneSpec(
																	device,
																	sceneId);
														}

														if (!device
																.doIgnoreScene(sceneId)) {
															short sceneValue = device
																	.getSceneOutputValue(sceneId);
															if (sceneValue == -1) {
																initDeviceOutputValue(
																		device,
																		DeviceConstants.DEVICE_SENSOR_OUTPUT);
																initSceneOutputValue(
																		device,
																		sceneId);
															} else {
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

							else {

								if (isDimmScene(sceneId)) {

									if (groupId != -1) {
										Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap()
												.get(zoneId);
										if (map != null) {
											List<String> devicesInGroup = map
													.get(groupId);
											if (devicesInGroup != null) {
												Map<String, Device> deviceMap = getDsidToDeviceMap();

												for (String dsid : devicesInGroup) {
													Device device = deviceMap
															.get(dsid);

													if (device != null) {

														if (!device
																.containsSceneConfig(sceneId)) {
															getSceneSpec(
																	device,
																	sceneId);
														}

														if (!device
																.doIgnoreScene(sceneId)) {
															handleDimmScene(
																	deviceMap
																			.get(dsid),
																	sceneId,
																	groupId,
																	false);
														}

													}
												}
											}
										}
									}
								} else if (stateMapper.isMappable(sceneId)) {

									boolean shouldBeOn = stateMapper
											.getMapping(sceneId);
									Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap()
											.get(zoneId);
									Map<String, Device> deviceMap = getDsidToDeviceMap();

									if (map != null) {

										if (groupId != -1) {
											List<String> devicesInGroup = map
													.get(groupId);
											if (devicesInGroup != null) {
												for (String dsid : devicesInGroup) {
													Device device = deviceMap
															.get(dsid);

													if (device != null) {
														if (!device
																.containsSceneConfig(sceneId)) {
															getSceneSpec(
																	device,
																	sceneId);
														}

														if (!device
																.doIgnoreScene(sceneId)) {
															if (shouldBeOn) {
																device.setOutputValue(device
																		.getMaxOutPutValue());
															} else {
																device.setOutputValue(0);
															}
														}

													}
												}
											}
										}
									}
								} else {

									Map<Short, List<String>> map = getDigitalSTROMZoneGroupMap()
											.get(zoneId);
									Map<String, Device> deviceMap = getDsidToDeviceMap();
									if (map != null) {

										if (groupId != -1) {
											List<String> devicesInGroup = map
													.get(groupId);
											if (devicesInGroup != null) {
												for (String dsid : devicesInGroup) {
													Device device = deviceMap
															.get(dsid);
													if (device != null) {

														if (!device
																.containsSceneConfig(sceneId)) {
															getSceneSpec(
																	device,
																	sceneId);
														}

														if (!device
																.doIgnoreScene(sceneId)) {
															short outputValue = device
																	.getSceneOutputValue(sceneId);
															if (outputValue == -1) {
																initDeviceOutputValue(
																		device,
																		DeviceConstants.DEVICE_SENSOR_OUTPUT);
																initSceneOutputValue(
																		device,
																		sceneId);
															} else {
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
			} else {
				logger.error("an event without a sceneID; groupID:" + groupId
						+ ", zoneID:" + zoneId + ", isDeviceCall:"
						+ deviceCallStr + ", dsid:" + dsidStr);
			}
		}
	}

	// Here we read the configured and stored (in the chip) output value for a
	// specific scene
	// and we store this value in order to know next time what to do.
	// The first time a scene command is called, it takes some time for the
	// sensor reading,
	// but the next time we react very fast because we learned what to do on
	// this command.
	private void getSceneSpec(Device device, short sceneId) {

		// setSensorReading(true); // no metering in this time
		DeviceSceneSpec spec = digitalSTROM.getDeviceSceneMode(
				getSessionToken(), device.getDSID(), null, sceneId);
		// setSensorReading(false);

		if (spec != null) {
			device.addSceneConfig(sceneId, spec);
			logger.info("UPDATED ignoreList for dsid: " + device.getDSID()
					+ " sceneID: " + sceneId);
		}

	}

	private void initDeviceOutputValue(Device device, short index) {
		addHighPriorityJob(new DeviceOutputValueSensorJob(device, index));
	}

	private void initSceneOutputValue(Device device, short sceneId) {
		addMediumPriorityJob(new SceneOutputValueSensorJob(device, sceneId));
	}

	private void addHighPriorityJob(
			DeviceOutputValueSensorJob deviceOutputValueSensorJob) {
		synchronized (highPrioritySensorJobs) {
			if (!highPrioritySensorJobs.contains(deviceOutputValueSensorJob)) {
				highPrioritySensorJobs.add(deviceOutputValueSensorJob);
			}
		}
	}

	private void addMediumPriorityJob(
			SceneOutputValueSensorJob sceneOutputValueSensorJob) {
		synchronized (mediumPrioritySensorJobs) {
			if (!mediumPrioritySensorJobs.contains(sceneOutputValueSensorJob)) {
				mediumPrioritySensorJobs.add(sceneOutputValueSensorJob);
			}
		}
	}

	private void addLowPriorityJob(
			DeviceConsumptionSensorJob deviceConsumptionSensorJob) {
		synchronized (lowPrioritySensorJobs) {
			if (!lowPrioritySensorJobs.contains(deviceConsumptionSensorJob)) {
				lowPrioritySensorJobs.add(deviceConsumptionSensorJob);
			}
		}
	}

	private SensorJob getLowPriorityJob() {
		SensorJob job = null;
		synchronized (lowPrioritySensorJobs) {
			if (lowPrioritySensorJobs.size() > 0) {
				job = lowPrioritySensorJobs.get(0);
				lowPrioritySensorJobs.remove(job);
			}
		}
		return job;
	}

	private SensorJob getMediumPriorityJob() {
		SensorJob job = null;
		synchronized (mediumPrioritySensorJobs) {
			if (mediumPrioritySensorJobs.size() > 0) {
				job = mediumPrioritySensorJobs.get(0);
				mediumPrioritySensorJobs.remove(job);
			}
		}
		return job;
	}

	private SensorJob getHighPriorityJob() {
		SensorJob job = null;
		synchronized (highPrioritySensorJobs) {
			if (highPrioritySensorJobs.size() > 0) {
				job = highPrioritySensorJobs.get(0);
				highPrioritySensorJobs.remove(job);
			}
		}
		return job;
	}

	private void removeSensorJobs(DSID dsid) {
		synchronized (lowPrioritySensorJobs) {
			for (Iterator<SensorJob> iter = lowPrioritySensorJobs.iterator(); iter
					.hasNext();) {
				SensorJob job = iter.next();
				if (job.getDsid().equals(dsid))
					iter.remove();
			}
		}
		synchronized (mediumPrioritySensorJobs) {
			for (Iterator<SensorJob> iter = mediumPrioritySensorJobs.iterator(); iter
					.hasNext();) {
				SensorJob job = iter.next();
				if (job.getDsid().equals(dsid))
					iter.remove();
			}
		}
		synchronized (highPrioritySensorJobs) {
			for (Iterator<SensorJob> iter = highPrioritySensorJobs.iterator(); iter
					.hasNext();) {
				SensorJob job = iter.next();
				if (job.getDsid().equals(dsid))
					iter.remove();
			}
		}
	}

	private void login() {
		if (applicationToken != null) {
			String token = digitalSTROM.loginApplication(applicationToken);

			if (token == null && user != null && password != null) {
				token = digitalSTROM.login(user, password);
			}
			setSessionToken(token);
		} else if (this.user != null && this.password != null) {
			setSessionToken(digitalSTROM.login(user, password));
		}
		if (serverIsFound()) {
			handleStructure(digitalSTROM
					.getApartmentStructure(getSessionToken()));
		}
	}

	private void setSessionToken(String newToken) {
		sessionToken = newToken;
		if (newToken != null) {
			setServerIsFound(true);
			logger.info("SUCCESSFULLY got session-token");
		} else {
			setServerIsFound(false);
			logger.error("could NOT get session-token");
		}
	}

	private String getSessionToken() {
		return sessionToken;
	}

	private synchronized boolean serverIsFound() {
		return serverIsFound;
	}

	private synchronized void setServerIsFound(boolean found) {
		serverIsFound = found;
	}

	private void startSensorJobExecutor() {
		this.sensorJobExecutor = new SensorJobExecutor();
		this.sensorJobExecutor.start();
	}

	private void registerDigitalSTROMEventListener() {
		if (!serverIsFound()) {
			login();
		}
		this.digitalSTROMEventListener = new DigitalSTROMEventListener();
		this.digitalSTROMEventListener.start();
	}

	/**
	 * In order to avoid many sensor readings in a time, this thread starts the
	 * jobs, after the old one is finished
	 * 
	 * @author Alexander Betker
	 * @since 1.3.0
	 * 
	 */
	private class SensorJobExecutor extends Thread {

		private boolean shutdown = false;

		private final int sleepTime = readTimeout;

		@Override
		public void run() {

			while (!this.shutdown) {
				SensorJob job = getHighPriorityJob();

				if (job == null) {
					job = getMediumPriorityJob();
					if (job == null)
						job = getLowPriorityJob();
				}
				if (job != null) {
					job.execute(digitalSTROM, getSessionToken());
				}

				try {
					sleep(this.sleepTime);
				} catch (InterruptedException e) {
					this.shutdown();
					logger.error("InterruptedException in SensorJobExecutor Thread ... "
							+ e.getStackTrace());
				}
			}
		}

		public synchronized void shutdown() {
			this.shutdown = true;
		}
	}

	/**
	 * If someone turns a device or a zone etc. on, we will get a notification
	 * to update the state of the item
	 * 
	 * @author Alexander Betker
	 * @since 1.3.0
	 * 
	 */
	private class DigitalSTROMEventListener extends Thread {

		private boolean shutdown = false;
		private final String EVENT_NAME = "openhabEvent";
		private final int ID = 11;
		private int timeout = 1000;

		private final String INVALID_SESSION = "Invalid session!";// Invalid
																	// session!

		private HttpTransport transport = null;
		private JSONResponseHandler handler = null;

		public synchronized void shutdown() {
			this.shutdown = true;
			unsubscribe();
		}

		public DigitalSTROMEventListener() {
			this.handler = new JSONResponseHandler();
			this.transport = new HttpTransport(uri, connectTimeout, readTimeout);
			this.subscribe();
		}

		private void subscribe() {
			if (getSessionToken() != null) {

				boolean transmitted = digitalSTROM.subscribeEvent(
						getSessionToken(), EVENT_NAME, this.ID, connectTimeout,
						readTimeout);

				if (!transmitted) {
					this.shutdown = true;
					logger.error("Couldn't subscribe eventListener ... maybe timeout because system is to busy ...");
				}
			} else {
				logger.error("Couldn't subscribe eventListener because there is no token (no connection)");
			}
		}

		@Override
		public void run() {
			while (!this.shutdown) {

				String request = this.getEventAsRequest(this.ID, 500);

				if (request != null) {

					String response = this.transport.execute(request,
							2 * this.timeout, this.timeout);

					JSONObject responseObj = this.handler
							.toJSONObject(response);

					if (this.handler.checkResponse(responseObj)) {
						JSONObject obj = this.handler
								.getResultJSONObject(responseObj);

						if (obj != null
								&& obj.get(JSONApiResponseKeysEnum.EVENT_GET_EVENT
										.getKey()) instanceof JSONArray) {
							JSONArray array = (JSONArray) obj
									.get(JSONApiResponseKeysEnum.EVENT_GET_EVENT
											.getKey());
							try {
								handleEvent(array);
							} catch (Exception e) {
								logger.warn("EXCEPTION in eventListener thread : "
										+ e.getLocalizedMessage());
							}
						}
					} else {
						String errorStr = null;
						if (responseObj != null
								&& responseObj
										.get(JSONApiResponseKeysEnum.EVENT_GET_EVENT_ERROR
												.getKey()) != null) {
							errorStr = responseObj
									.get(JSONApiResponseKeysEnum.EVENT_GET_EVENT_ERROR
											.getKey()).toString();
						}

						if (errorStr != null
								&& errorStr.equals(this.INVALID_SESSION)) {
							this.subscribe();
						} else if (errorStr != null) {
							logger.error("Unknown error message in event response: "
									+ errorStr);
						}
					}
				}
			}
		}

		private String getEventAsRequest(int subscriptionID, int timeout) {
			if (getSessionToken() != null) {
				return JSONRequestConstants.JSON_EVENT_GET
						+ JSONRequestConstants.PARAMETER_TOKEN
						+ getSessionToken()
						+ JSONRequestConstants.INFIX_PARAMETER_SUBSCRIPTION_ID
						+ subscriptionID
						+ JSONRequestConstants.INFIX_PARAMETER_TIMEOUT
						+ timeout;
			}
			return null;
		}

		private boolean unsubscribeEvent(String name, int subscriptionID) {
			if (getSessionToken() != null) {
				return digitalSTROM.unsubscribeEvent(getSessionToken(),
						EVENT_NAME, this.ID, connectTimeout, readTimeout);
			}
			return false;
		}

		private boolean unsubscribe() {
			return this.unsubscribeEvent(this.EVENT_NAME, this.ID);
		}

		private void handleEvent(JSONArray array) {
			if (array.size() > 0) {
				Event event = new JSONEventImpl(array);

				for (EventItem item : event.getEventItems()) {
					if (item.getName() != null
							&& item.getName().equals(this.EVENT_NAME)) {
						handleOpenhabEvent(item);
					}
				}
			}
		}
	}

	@Override
	public void deviceUpdated(String dsid) {
		List<String> itemNames = getItemNamesForDsid(dsid);
		if (itemNames != null) {
			for (String itemName : itemNames) {
				updateItemState(getConfigForItemName(itemName).item);
			}
		}
	}

	private void updateItemState(Item item) {
		if (item != null) {
			Device device = deviceMap.get(item.getName());

			if (device != null) {

				State state = null;
				if (item instanceof DimmerItem) {
					state = new PercentType(
							getPercent(device.getMaxOutPutValue(),
									device.getOutputValue()));

				} else if (item instanceof SwitchItem
						&& !(item instanceof DimmerItem)) {

					state = device.getOutputValue() > 0 ? OnOffType.ON
							: OnOffType.OFF;
				} else if (item instanceof NumberItem) {

					DigitalSTROMBindingConfig confItem = getConfigForItemName(item
							.getName());
					if (confItem != null) {
						if (confItem.consumption != null) {

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
						}
					}
				} else if (item instanceof RollershutterItem) {

					DigitalSTROMBindingConfig confItem = getConfigForItemName(item
							.getName());
					if (confItem != null) {

						if (confItem.context != null
								&& confItem.context.equals(ContextConfig.slat)) {

							int output = getPercent(
									device.getMaxSlatPosition(),
									device.getSlatPosition());

							state = new PercentType(100 - output);
						} else if (confItem.context != null
								&& confItem.context
										.equals(ContextConfig.awning)) {

							int output = getPercent(device.getMaxOutPutValue(),
									device.getOutputValue());

							state = new PercentType(output);
						} else {
							int output = getPercent(device.getMaxOutPutValue(),
									device.getOutputValue());

							state = new PercentType(100 - output);
						}
					}
				} else if (item instanceof StringItem) {
					DigitalSTROMBindingConfig confItem = getConfigForItemName(item
							.getName());
					if (confItem != null) {

						if (confItem.consumption != null) {
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
						}
					}
				}
				eventPublisher.postUpdate(item.getName(), state);
			} else {
				logger.error("couldn't update item state, because device is null: "
						+ item.getName());
			}
		} else {
			logger.error("couldn't update item state, because item is null");
		}

	}

	private int getPercent(int max, int val) {
		if (withParameterMax(max) && withParameterValue(val)) {
			return (100 * val / max);
		}
		return -1;
	}

	private boolean withParameterMax(int max) {
		return (max > 0);

	}

	private boolean withParameterValue(int value) {
		return (value > -1);
	}

	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		if(provider instanceof DigitalSTROMBindingProvider){
			//remove device associated with the item
			Device device = deviceMap.get(itemName);
			if (device != null) {
				List<String> itemNamesForDsid = getItemNamesForDsid(device
						.getDSID().getValue());
				if (itemNamesForDsid.size() == 1) {
					device.removeDeviceListener(this);
					removeSensorJobs(device.getDSID());
				}
			}
			deviceMap.remove(itemName);
			
			//initialize the device
			DigitalSTROMBindingConfig confItem = getConfigForItemName(itemName);
			if (confItem != null && confItem.dsid != null) {
				if (rawDsidToDeviceMap.size() == 0 && serverIsFound()) {
					rawDsidToDeviceMap = getAllDigitalSTROMDevicesMap();
				}
				device = rawDsidToDeviceMap.get(confItem.dsid.getValue());
				if (device != null) {
					addDevice(itemName, device);
					updateItemState(confItem.item);
					handleStructure(digitalSTROM
							.getApartmentStructure(getSessionToken()));
				}
			}
		}
	}

}
