/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.openhab.binding.tinkerforge.TinkerforgeBindingProvider;
import org.openhab.binding.tinkerforge.internal.model.BarometerSubIDs;
import org.openhab.binding.tinkerforge.internal.model.DigitalActor;
import org.openhab.binding.tinkerforge.internal.model.Ecosystem;
import org.openhab.binding.tinkerforge.internal.model.GenericDevice;
import org.openhab.binding.tinkerforge.internal.model.IO16SubIds;
import org.openhab.binding.tinkerforge.internal.model.IODevice;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MDevice;
import org.openhab.binding.tinkerforge.internal.model.MInSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MSensor;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.MTextActor;
import org.openhab.binding.tinkerforge.internal.model.ModelFactory;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.NoSubIds;
import org.openhab.binding.tinkerforge.internal.model.OHConfig;
import org.openhab.binding.tinkerforge.internal.model.OHTFDevice;
import org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFConfig;
import org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;
import org.openhab.binding.tinkerforge.internal.types.HighLowValue;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.openhab.binding.tinkerforge.internal.types.TinkerforgeValue;
import org.openhab.binding.tinkerforge.internal.types.UnDefValue;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This binding connects the TinkerForge devices to the openhab eventbus.
 * 
 * This class uses an EMF model with a TinkerforgeEcosystem object as root. The
 * TinkerforgeEcosystem has Brickd child objects. The Brickd object has an
 * IpConnection to the TinkerForge brickd daemon, identified by an ip address or
 * host name and a port. The Brickd object has child objects for every connected
 * TinkerForge device. The TinkerForge device object holds leaf objects for
 * subdevices if available. All the device objects are sharing the IpConnection
 * of the Brickd object. The EMF device objects can be interpreted as a facade
 * for the TinkerForge api device objects. If available, the EMF device objects
 * implement TinkerForge CallbackListeners for sensor value updates and are
 * updating the EMF device object sensor properties accordingly.
 * 
 * The binding adds a listener to the TinkerforgeEcosystem. On the one hand this
 * listener handles updated sensor values and propagates them to the openhab
 * eventbus. On the other hand the listener is informed about new devices in the
 * TinkerforgeEcosystem and thus can pass configuration settings from
 * openhab.cfg to the devices. The callback period of the CallbackListener and a
 * threshold value are configurable through openhab.cfg.
 * 
 * All device values are additionally polled by the execute method mainly to get
 * values from subdevices which don't have TinkerForge CallbackListeners for
 * getting the sensor values.
 * 
 * Tinkerforge devices which work as actors like relays can be controlled with
 * this binding.
 * 
 * For now only a subset of the TinkerForge devices are supported and not all
 * features of the devices are implemented. More devices and features will be
 * added soon. The following devices are supported for now:
 * <ul>
 * <li>Servo Brick</li>
 * <li>DC Brick</li>
 * <li>Dual Relay Bricklet</li>
 * <li>Humidity Bricklet</li>
 * <li>Distance IR Bricklet</li>
 * <li>Temperature Bricklet</li>
 * <li>Barometer Bricklet</li>
 * <ul>
 * <li>Barometer</li>
 * <li>Temperature Device</li>
 * </ul>
 * <li>Ambient Light Bricklet</li> <li>LCD</li>
 * <ul>
 * <li>LCD 20×4 Bricklet</li>
 * <li>4 Buttons</li>
 * </ul>
 * </ul>
 * 
 * @author Theo Weiss
 * @since 1.3.0
 */
public class TinkerforgeBinding extends
		AbstractActiveBinding<TinkerforgeBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(TinkerforgeBinding.class);

	private static final int BRICKD_DEFAULT_PORT = 4223;

	private static final Pattern UID_PATTERN = 
		Pattern.compile(String.format("^(.*?)\\.(%s)$", ConfigKey.uid.name()));

	/**
	 * the refresh interval which is used to poll values from the Tinkerforge
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	private Ecosystem tinkerforgeEcosystem;

	private ModelFactory modelFactory;
	private OHConfig ohConfig;
	private boolean isConnected;
	
	
	private enum ConfigKey {
		subid, uid, type, hosts
	}

	private enum ConfigKeyAdmin {
		subid, uid, type, ohId;
	}

	private enum TypeKey {
		servo, bricklet_distance_ir, brick_dc, bricklet_humidity, 
		bricklet_temperature, bricklet_barometer, bricklet_ambient_light,
		io_actuator, iosensor, bricklet_io16, bricklet_industrial_digital_4in
	}
	
	public TinkerforgeBinding() {
		modelFactory = ModelFactory.eINSTANCE;
	}

	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
		disconnectModel();
	}

	/**
	 * Disconnects the IpConnections to all TinkerForge brickds and destroys the
	 * TinkerforgeEcosystem.
	 */
	private void disconnectModel() {
		if (isConnected) {
			logger.debug("disconnect called");
			tinkerforgeEcosystem.disconnect();
			tinkerforgeEcosystem = null;
			isConnected = false;
		}
	}

	/**
	 * Creates a Tinkerforge Ecosystem object and adds a listener to it.
	 */
	private void connectModel() {
		tinkerforgeEcosystem = modelFactory.createEcosystem();
		listen2Model(tinkerforgeEcosystem);
		logger.debug("{} connectModel called", LoggerConstants.TFINIT);
		isConnected = true;
	}

	/**
	 * Searches for a brickd with the given {@code host} and {@code port} in the
	 * Ecosystem. If there is no brickd found a new Brickd object is created,
	 * added to the Ecosystem an the IpConnection to the Tinkerforge brickd is
	 * established and a device enumeration is triggert.
	 * 
	 * @param host
	 *            The host name or ip address of the TinkerForge brickd as
	 *            String.
	 * @param port
	 *            The port of the TinkerForge brickd as int.
	 */
	private void connectBrickd(String host, int port) {
		MBrickd brickd = tinkerforgeEcosystem.getBrickd(host, port);
		if (brickd == null) {
			brickd = modelFactory.createMBrickd();
			brickd.setHost(host);
			brickd.setPort(port);
			brickd.setEcosystem(tinkerforgeEcosystem);
			tinkerforgeEcosystem.getMbrickds().add(brickd);
			brickd.init();
			brickd.connect();
			logger.debug("{} Tinkerforge new brickd for host: {}",
					LoggerConstants.TFINIT, host);
		} else {
			logger.debug("{} Tinkerforge found existing brickd for host: {}",
					LoggerConstants.TFINIT, host);
		}
	}

	/**
	 * Adds a listener {@link EContentAdapter} to the {@link Ecosystem}. The
	 * listener handles updated sensor values and posts them to the openhab
	 * eventbus by {@link #processTFDeviceValues(Notification)
	 * processTFDeviceValues}. Furthermore the addition and removal of devices
	 * is handled by {@link #initializeTFDevices(Notification)
	 * initializeTFDevices}.
	 * 
	 * @param tinkerforgeEcosystem
	 *            The EMF Ecosystem object.
	 */
	private void listen2Model(Ecosystem tinkerforgeEcosystem) {
		EContentAdapter modelAdapter = new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				logger.debug("TinkerforgeNotifier was notified");
				if (notification.getEventType() == Notification.ADD
						|| notification.getEventType() == Notification.ADD_MANY
						|| notification.getEventType() == Notification.REMOVE
						|| notification.getEventType() == Notification.REMOVE_MANY) {
					initializeTFDevices(notification);
				} else {
					processTFDeviceValues(notification);
				}
			}

		};
		tinkerforgeEcosystem.eAdapters().add(modelAdapter);
	}

	private boolean checkDuplicateGenericDevice(GenericDevice device,
			String uid, String subId) {
		boolean isDuplicate = false;
		final String genericDeviceId = device.getGenericDeviceId();
		final EList<MSubDevice<?>> genericDevicesList = tinkerforgeEcosystem
				.getDevices4GenericId(uid, genericDeviceId);
		if (genericDevicesList.size() != 0) {
			for (MSubDevice<?> gd : genericDevicesList) {
				if (!gd.getSubId().equals(subId) && gd.getEnabledA().get()) {
					isDuplicate = true;
					logger.error("{} existing device is uid {} subId {}",
							LoggerConstants.CONFIG, gd.getUid(), gd.getSubId());
				}
			}
		}

		return isDuplicate;
	}

	/**
	 * Configures and enables newly found devices. For sub devices the master
	 * device is also enabled. Configuration is only added if there is a
	 * configuration from openhab.cfg available and the device is configurable
	 * which is the case for {@link MTFConfigConsumer}. Devices of type
	 * {@link IODevice} are only enabled if they are configured in openhab.cfg,
	 * all other devices are always enabled.
	 * 
	 * @param device
	 *            A device object as {@link MBaseDevice}.
	 * @param uid
	 *            The device uid as String.
	 * @param subId
	 *            The device subid as String or <code>null</code> if the device
	 *            is not a sub device.
	 */
	@SuppressWarnings("unchecked")
	private void addMDevice(MBaseDevice device, String uid, String subId) {
		String logId = subId == null ? uid : uid + " " + subId;
		OHTFDevice<?, ?> deviceConfig = ohConfig.getConfigByTFId(uid, subId);
		if (device.getEnabledA().compareAndSet(false, true)) {
			if (subId != null) {
				MDevice<?> masterDevice = (MDevice<?>) device.eContainer();
				// recursion for adding the master device
				if (!masterDevice.getEnabledA().get()) {
					logger.debug("{} enabling masterDevice {}",
							LoggerConstants.TFINITSUB, masterDevice.getUid());
					addMDevice(masterDevice, uid, null);
				}
			}
			if (device instanceof MTFConfigConsumer<?> && deviceConfig != null) {
				logger.debug("{} found MTFConfigConsumer id {}",
						LoggerConstants.TFINIT, logId);
				if (device instanceof GenericDevice
						&& checkDuplicateGenericDevice((GenericDevice) device,
								uid, subId)) {
					logger.error(
							"{} ignoring duplicate device uid: {}, subId {}, genericId {}. Fix your openhab.cfg!",
							LoggerConstants.CONFIG, uid, subId);
					device.getEnabledA().compareAndSet(true, false);
				} else {
					TFConfig deviceTfConfig = EcoreUtil.copy(deviceConfig.getTfConfig());
					logger.debug("{} setting tfConfig for {}",
							LoggerConstants.TFINIT, logId);
					((MTFConfigConsumer<EObject>) device)
							.setTfConfig(deviceTfConfig);
					device.enable();
					logger.debug("{} adding/enabling device {} with config: {}",
							LoggerConstants.TFINIT, logId, deviceTfConfig);
				}
			} else if (device instanceof IODevice) {
				logger.debug("{} ignoring unconfigured  IODevice: {}",
						LoggerConstants.TFINIT, logId);
				// set the device disabled, this is needed for not getting
				// states
				// through execute method
				device.getEnabledA().compareAndSet(true, false);
			} else {
				device.enable();
				logger.debug("{} adding/enabling device: {}",
						LoggerConstants.TFINIT, logId);
			}
		}
	}

	/**
	 * Adds or removes a device to / from the Ecosystem. Notifications from
	 * {@link MBrickd} are used for adding devices (not sub devices) and
	 * removing of devices and their corresponding sub devices.
	 * 
	 * Notifications from {@link MSubDeviceHolder} for adding sub devices.
	 * 
	 * @param notification
	 *            The {@link Notification} for add and remove events to the
	 *            {@link Ecosystem}.
	 */
	private void initializeTFDevices(Notification notification) {
		logger.debug("{} notifier {}", LoggerConstants.TFINIT, notification.getNotifier());
		if (notification.getNotifier() instanceof MBrickd) {
			logger.debug("{} notifier is Brickd", LoggerConstants.TFINIT);
			int featureID = notification.getFeatureID(MBrickd.class);
			if (featureID == ModelPackage.MBRICKD__MDEVICES) {
				 if (notification.getEventType() == Notification.ADD) {
					MDevice<?> mDevice = (MDevice<?>) notification.getNewValue();
					addMDevice(mDevice, mDevice.getUid(), null);
				} else if (notification.getEventType() == Notification.ADD_MANY) {
					logger.debug("{} Notifier: add many called: ",
							LoggerConstants.TFINIT);
				} else if (notification.getEventType() == Notification.REMOVE) {
					logger.debug("{} Notifier: remove called: ",LoggerConstants.TFINIT);
					if (notification.getOldValue() instanceof MBaseDevice) {
						logger.debug("{} Notifier: remove called for MBaseDevice",LoggerConstants.TFINIT);
						MBaseDevice mDevice = (MBaseDevice) notification.getOldValue();
						String uid = mDevice.getUid();
						String subId = null;
						if (searchConfiguredItemName(uid, subId) != null) {
							logger.debug("{} Notifier: removing device: uid {} subid {}", LoggerConstants.TFINIT, uid, subId);
							postUpdate(uid, subId, UnDefValue.UNDEF);
						}
					}
					else {
						logger.debug("{} unknown notification from mdevices {}", LoggerConstants.TFINIT, notification);
					}
				}
			}
			else {
				logger.debug("{} Notifier: unknown feature {}", LoggerConstants.TFINIT, notification.getFeature());
			}
		} else if (notification.getNotifier() instanceof MSubDeviceHolder<?>) {
			int featureID = notification.getFeatureID(MSubDeviceHolder.class);
			if (featureID == ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES) {
				logger.debug("{} MSubdevices Notifier called",
						LoggerConstants.TFINITSUB);
				if (notification.getEventType() == Notification.ADD) {
					MSubDevice<?> mSubDevice = (MSubDevice<?>) notification.getNewValue();
					addMDevice(mSubDevice, mSubDevice.getUid(), mSubDevice.getSubId());

				}
				if (notification.getEventType() == Notification.REMOVE){
					logger.debug("{} remove notification from subdeviceholder", LoggerConstants.TFINIT);
					logger.debug("{} Notifier: remove called for MSubDevice",LoggerConstants.TFINIT);
					MSubDevice<?> mDevice = (MSubDevice<?>) notification.getOldValue();
					String uid = mDevice.getUid();
					String subId = mDevice.getSubId();
					if (searchConfiguredItemName(uid, subId) != null) {
						logger.debug("{} Notifier: removing device: uid {} subid {}", LoggerConstants.TFINIT, uid, subId);
						postUpdate(uid, subId, UnDefValue.UNDEF);
					}
				}
			}
		}
		else {
			logger.debug("{} unhandled notifier {}", LoggerConstants.TFINIT, notification.getNotifier());
		}
	}

	/**
	 * Processes change events from the {@link Ecosystem}. Sensor values from
	 * {@link MSensor} are handled by
	 * {@link #processSensorValue(MSensor, Notification) processSensorValue},
	 * actor values from {@link MSwitchActore} are handled by
	 * {@link #processSwitchActorValue(MSwitchActor, Notification)
	 * processSwitchActorValue}. (no add or remove events, these are handled in
	 * {@link #initializeTFDevices(Notification) initializeTFDevices}).
	 * 
	 * 
	 * @param notification
	 *            The {@link Notification} about changes to the
	 *            {@link Ecosystem}.
	 */
	private void processTFDeviceValues(Notification notification) {
		if (notification.getNotifier() instanceof MSensor) {
			MSensor<?> sensor = (MSensor<?>) notification.getNotifier();
			int featureID = notification.getFeatureID(MSensor.class);
			if (featureID == ModelPackage.MSENSOR__SENSOR_VALUE) {
				processValue((MBaseDevice) sensor, notification);
			}
		} else if (notification.getNotifier() instanceof MSwitchActor) {
			MSwitchActor switchActor = (MSwitchActor) notification
					.getNotifier();
			int featureID = notification.getFeatureID(MSwitchActor.class);
			if (featureID == ModelPackage.MSWITCH_ACTOR__SWITCH_STATE) {
				processValue((MBaseDevice) switchActor, notification);
			}
		} else {
			logger.trace("{} ignored notifier {}",
					LoggerConstants.TFMODELUPDATE, notification.getNotifier());
		}
	}

	/**
	 * Processes changed device values to post them to the openHAB event bus.
	 * 
	 * @param device
	 *            The {@link MBaseDevice} device, which has a changed value.
	 * @param notification
	 *            The {@link Notification} about changes to the
	 *            {@link Ecosystem}.
	 */
	private void processValue(MBaseDevice device, Notification notification) {
		TinkerforgeValue newValue = (TinkerforgeValue) notification.getNewValue();
		String uid = device.getUid();
		String subId = null;
		if (device instanceof MSubDevice<?>) {
			subId = ((MSubDevice<?>) device).getSubId();
			logger.trace("{} Notifier found MSubDevice sensor value for: {}",
					LoggerConstants.TFMODELUPDATE, subId);
		} else {
			logger.trace("{} Notifier found mDevice sensor value for: {}",
					LoggerConstants.TFMODELUPDATE, uid);
		}
		postUpdate(uid, subId, newValue);
	}


	/**
	 * Searches the name of an item which is bound to the device with the given
	 * uid and subid.
	 * 
	 * @param uid
	 *            The device uid as {@code String}.
	 * @param subId
	 *            The device subid as {@code String} or {@code null} if it is
	 *            not a sub device.
	 * @return The name of the item which is bound to the device as
	 *         {@code String} or {@code null} if no item was found.
	 */
	private String searchConfiguredItemName(String uid, String subId) {
		for (TinkerforgeBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				String deviceUid = provider.getUid(itemName);
				String subDeviceId = provider.getSubId(itemName);
				String deviceName = provider.getName(itemName);
				if (deviceName != null) {
					logger.trace("found item for command: name {}", deviceName);
					OHTFDevice<?, ?> ohtfDevice = ohConfig
							.getConfigByOHId(deviceName);
					deviceUid = ohtfDevice.getUid();
					deviceName = ohtfDevice.getSubid();
				}
				if (uid.equals(deviceUid)) {
					if (subId == null && subDeviceId == null) {
						return itemName;
					} else if (subId != null && subId.equals(subDeviceId)) {
						return itemName;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Searches the provider which is bound to the device with the given
	 * uid and subid.
	 * 
	 * @param uid
	 *            The device uid as {@code String}.
	 * @param subId
	 *            The device subid as {@code String} or {@code null} if it is
	 *            not a sub device.
	 * @return The {@code TinkerforgeBindingProvider} which is bound to the device as
	 *         {@code Item} or {@code null} if no item was found.
	 */
	private Map<String, TinkerforgeBindingProvider> getBindingProviders(String uid, String subId) {
		Map<String, TinkerforgeBindingProvider> providerMap = new HashMap<String, TinkerforgeBindingProvider>();
		for (TinkerforgeBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				String deviceUid = provider.getUid(itemName);
				String subDeviceId = provider.getSubId(itemName);
				String deviceName = provider.getName(itemName);
				if (deviceName != null) {
					logger.trace("found item for command: name {}", deviceName);
					OHTFDevice<?, ?> ohtfDevice = ohConfig
							.getConfigByOHId(deviceName);
					deviceUid = ohtfDevice.getUid();
					deviceName = ohtfDevice.getSubid();
				}
				if (uid.equals(deviceUid)) {
					if (subId == null && subDeviceId == null) {
						 providerMap.put(itemName, provider);
					} else if (subId != null && subId.equals(subDeviceId)) {
						 providerMap.put(itemName, provider);
					}
				}
			}
		}
		return providerMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Tinkerforge Refresh Service";
	}

	/**
	 * The working method which is called by the refresh thread.
	 * 
	 * Sensor or state values for all devices are fetched from the
	 * {@link Ecosystem} and posted to the event bus. All OutActors are
	 * ignored, they may only send updates if the hardware device has updates
	 * (think of a pressed switch).
	 * 
	 */
	@Override
	protected void execute() {
		for (TinkerforgeBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				updateItemValues(provider, itemName);
			}
		}
	}

	/**
	 * Get the current values for an {@code Item}.
	 * 
	 * @param provider
	 *            The {@code TinkerforgeBindingProvider} which is bound to the
	 *            device as {@code Item}
	 * @param itemName
	 *            The name of the {@code Item} as String
	 */
	protected void updateItemValues(TinkerforgeBindingProvider provider,
			String itemName) {
		String deviceUid = provider.getUid(itemName);
		Item item = provider.getItem(itemName);
		String deviceSubId = provider.getSubId(itemName);
		String deviceName = provider.getName(itemName);
		if (deviceName != null) {
			String[] ids = getDeviceIdsForDeviceName(deviceName);
			deviceUid = ids[0];
			deviceSubId = ids[1];
		}
		MBaseDevice mDevice = tinkerforgeEcosystem.getDevice(deviceUid,
				deviceSubId);
		if (mDevice != null && mDevice.getEnabledA().get()) {
			if (mDevice instanceof MSensor) {
				postUpdate(deviceUid, deviceSubId,
						((MSensor<?>) mDevice).fetchSensorValue());
			} else if (mDevice instanceof MInSwitchActor
					&& item instanceof SwitchItem) {
				OnOffValue switchState = ((MInSwitchActor) mDevice)
						.fetchSwitchState();
				postUpdate(deviceUid, deviceSubId, switchState);
				logger.debug("execute called: found MInSwitchActor state: {}",
						switchState);
			} else if (mDevice instanceof DigitalActor) {
				HighLowValue highLowValue = ((DigitalActor) mDevice)
						.fetchDigitalValue();
				postUpdate(deviceUid, deviceSubId, highLowValue);
				logger.debug("{} execute called: found DigitalActor state: {}",
						LoggerConstants.TFCOMMAND, highLowValue);
			}
		}
	}

	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		logger.debug("{} bindingChanged item {}", LoggerConstants.ITEMUPDATE,
				itemName);
		updateItemValues((TinkerforgeBindingProvider) provider, itemName);
	}

	private void postUpdate(String uid, String subId,
			TinkerforgeValue sensorValue) {
		// TODO undef handling
		Map<String, TinkerforgeBindingProvider> providerMap = getBindingProviders(
				uid, subId);
		if (providerMap.size() == 0) {
			logger.debug("{} found no item for uid {}, subid {}",
					LoggerConstants.TFMODELUPDATE, uid, subId);
		}
		for (Entry<String, TinkerforgeBindingProvider> entry : providerMap
				.entrySet()) {
			String itemName = entry.getKey();
			TinkerforgeBindingProvider provider = entry.getValue();
			Class<? extends Item> itemType = provider.getItemType(itemName);
			State value = UnDefType.UNDEF;
			if (sensorValue instanceof DecimalValue) {
				value = DecimalType.valueOf(String.valueOf(sensorValue));
			} else if (sensorValue instanceof HighLowValue) {
				if (itemType.isAssignableFrom(NumberItem.class)
						|| itemType.isAssignableFrom(StringItem.class)) {
					value = sensorValue == HighLowValue.HIGH ? DecimalType
							.valueOf("1") : DecimalType.valueOf("0");
				} else if (itemType.isAssignableFrom(ContactItem.class)) {
					value = sensorValue == HighLowValue.HIGH ? OpenClosedType.OPEN
							: OpenClosedType.CLOSED;
				} else if (itemType.isAssignableFrom(SwitchItem.class)) {
					value = sensorValue == HighLowValue.HIGH ? OnOffType.ON
							: OnOffType.OFF;
				} else {
					logger.error("{} unsupported item type {} for item {}",
							LoggerConstants.TFMODELUPDATE,
							provider.getItem(itemName), itemName);
				}
			} else if (sensorValue instanceof OnOffValue) {
				if (itemType.isAssignableFrom(NumberItem.class)
						|| itemType.isAssignableFrom(StringItem.class)) {
					value = sensorValue == OnOffValue.ON ? DecimalType
							.valueOf("1") : DecimalType.valueOf("0");
				} else if (itemType.isAssignableFrom(ContactItem.class)) {
					value = sensorValue == OnOffValue.ON ? OpenClosedType.OPEN
							: OpenClosedType.CLOSED;
				} else if (itemType.isAssignableFrom(SwitchItem.class)) {
					value = sensorValue == OnOffValue.ON ? OnOffType.ON
							: OnOffType.OFF;
				} else {
					logger.error("{} unsupported item type {} for item {}",
							LoggerConstants.TFMODELUPDATE,
							provider.getItem(itemName), itemName);
				}
			} else if (sensorValue == UnDefValue.UNDEF || sensorValue == null) {
				value = UnDefType.UNDEF;
			}
			eventPublisher.postUpdate(itemName, value);
			logger.debug("{} postupdate: found sensorValue: {} for item {}",
					LoggerConstants.TFMODELUPDATE, sensorValue, itemName);
		}
	}
	
	/**
	 * Gets the uid and the subid of a device from the openhab.cfg, using the
	 * device name as input.
	 * 
	 * @param deviceName
	 *            The symbolic device name as {@code String}.
	 * @return A String array with the device uid as first element as
	 *         {@code String} and the device subid as second element as
	 *         {@code String} or {@code null}.
	 */
	private String[] getDeviceIdsForDeviceName(String deviceName) {
		logger.trace("found item for command: name {}", deviceName);
		OHTFDevice<?, ?> ohtfDevice = ohConfig.getConfigByOHId(deviceName);
		String[] ids = { ohtfDevice.getUid(), ohtfDevice.getSubid() };
		return ids;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Searches the item with the given {@code itemName} in the
	 * {@link TinkerforgeBindingProvider} collection and gets the uid and subid
	 * of the device. The appropriate device is searched in the ecosystem and
	 * the command is executed on the device.
	 * 
	 * {@code OnOffType} commands are executed on {@link MInSwitchActor}
	 * objects. {@code StringType} commands are executed on {@link MTextActor}
	 * objects.
	 * 
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		for (TinkerforgeBindingProvider provider : providers) {
			for (String itemNameP : provider.getItemNames()) {
				if (itemNameP.equals(itemName)) {
					String deviceUid = provider.getUid(itemName);
					String deviceSubId = provider.getSubId(itemName);
					String deviceName = provider.getName(itemName);
					if (deviceName != null) {
						String[] ids = getDeviceIdsForDeviceName(deviceName);
						deviceUid = ids[0];
						deviceSubId = ids[1];
					}
					logger.trace(
							"{} found item for command: uid: {}, subid: {}",
							LoggerConstants.COMMAND, deviceUid, deviceSubId);
					MBaseDevice mDevice = tinkerforgeEcosystem.getDevice(
							deviceUid, deviceSubId);
					if (mDevice != null && mDevice.getEnabledA().get()) {
						if (command instanceof OnOffType) {
							logger.trace("{} found onoff command",
									LoggerConstants.COMMAND);
							if (mDevice instanceof MInSwitchActor) {
								OnOffType cmd = (OnOffType) command;
								OnOffValue state = cmd == OnOffType.OFF ? OnOffValue.OFF
										: OnOffValue.ON;
								((MSwitchActor) mDevice).turnSwitch(state);
							}
							else if (mDevice instanceof DigitalActor){
								OnOffType cmd = (OnOffType) command;
								HighLowValue state = cmd == OnOffType.OFF ? HighLowValue.LOW
										: HighLowValue.HIGH;
								((DigitalActor) mDevice).turnDigital(state);
							} else {
								logger.error(
										"{} received OnOff command for non-SwitchActor",
										LoggerConstants.COMMAND);
							}
						} else if (command instanceof StringType) {
							logger.trace("{} found string command",
									LoggerConstants.COMMAND);
							if (mDevice instanceof MTextActor) {
								((MTextActor) mDevice).setText(command.toString());
							}
						} else {
							logger.error("{} got unknown command type: {}",
									LoggerConstants.COMMAND,
									command.toString());
						}
					} else
						logger.error(
								"{} no tinkerforge device found for command",
								LoggerConstants.COMMAND);
				}
			}
		}
	}

	/**
	 * Parses the configuration received from configManagement service and
	 * caches it in a map. This map is added to another map with the openhab
	 * symbolic device name as key. The symbolic name is the first part of
	 * configuration key of the openhab.cfg entry. The configuration entries
	 * look like this: tinkerforge:<openhab symbolic device
	 * name>.<property>=<value> e.g. "tinkerforge:dc_garage.uid=62Zduj"
	 * 
	 * @param config
	 *            The configuration received from the configManagement service.
	 * @return The parsed configuration for each device as Map with the
	 *         configuration key as String and the value as String. These maps
	 *         are hold in an outer Map with the symbolic device name as key.
	 * @throws ConfigurationException
	 */
	private Map<String, Map<String, String>> createConfigContainer(
			Dictionary<String, ?> config) throws ConfigurationException {
		Map<String, Map<String, String>> configContainer = new HashMap<String, Map<String, String>>();
		Enumeration<String> keys = config.keys();
		ArrayList<String> keys2ignore = new ArrayList<String>(3);
		keys2ignore.add(ConfigKey.uid.name());
		keys2ignore.add(ConfigKey.subid.name());

		while (keys.hasMoreElements()) {
			// first search all uids
			String key = keys.nextElement();
			logger.debug("{} key:value {} : {}", LoggerConstants.CONFIG, key,
					config.get(key));
			// the config-key enumeration contains additional keys that
			// we
			// don't want to process here ...
			if ("service.pid".equals(key)) {
				continue;
			}
			Matcher matcher = UID_PATTERN.matcher(key);
			if (matcher.matches()) {
				matcher.reset();
				matcher.find();
				HashMap<String, String> configMap = new HashMap<String, String>();
				String ohId = matcher.group(1);
				logger.trace("{} found symbolic name: {}",
						LoggerConstants.CONFIG, ohId);
				configMap.put(ConfigKeyAdmin.ohId.name(), ohId);
				configMap.put(ConfigKey.uid.name(), (String) config.get(key));
				configMap.put(ConfigKey.subid.name(), (String) config.get(ohId + "." + ConfigKey.subid.name()));
				String deviceType = (String) config.get(ohId + "." + ConfigKey.type.name());
				configMap.put(ConfigKey.type.name(), deviceType);
				if (deviceType == null) {
					throw new ConfigurationException(ohId, "type is missing");
				}
				checkTfType(ohId, deviceType);
				if (configContainer.containsKey(ohId)) {
					throw new ConfigurationException(ohId, String.format(
							"{} found duplicate entry for symbolic name {}",
							LoggerConstants.CONFIG, ohId));
				}
				// second iteration to get the remaining, not common,
				// configuration keys and their values
				Enumeration<String> keys2 = config.keys();
				Pattern ohIdPattern = Pattern.compile(String.format("^%s\\.(.*?)$", ohId));
				while (keys2.hasMoreElements()) {
					String key2 = keys2.nextElement();
					Matcher matcherOhId = ohIdPattern.matcher(key2);
					if (matcherOhId.matches()) {
						matcherOhId.reset();
						matcherOhId.find();
						String matchedKey = matcherOhId.group(1);
						if (keys2ignore.contains(matchedKey)) {
							continue;
						} else {
							String value2 = (String) config.get(key2);
							logger.trace("TFOPENHABCONFIG {} ohConfigKey {}", matchedKey, value2);
							configMap.put(matchedKey, value2);
						}
					}
				}
				configContainer.put(ohId, configMap);
			}
		}
		return configContainer;
	}

	/**
	 * Updates the configuration of the managed service.
	 * 
	 * Extracts the host and port configuration and connects the appropriate
	 * brickds.
	 * 
	 * The device configuration from openhab.cfg is parsed into a {@code Map}
	 * based (temporary) structure. This structure is used to generate the
	 * {@link OHConfig} EMF model configuration store.
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			if (isConnected) {
				disconnectModel();
			}
			connectModel();
			ohConfig = modelFactory.createOHConfig();

			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			// read further config parameters here ...
			logger.debug("{} updated called", LoggerConstants.CONFIG);
			Map<String, Map<String, String>> configContainer = createConfigContainer(config);

			for (Map<String, String> deviceConfig : configContainer.values()) {
				createOHTFDeviceConfig(deviceConfig);
			}
			// must be done after all other config has been processed
			String cfgHostsLine = (String) config.get(ConfigKey.hosts.name());
			parseCfgHostsAndConnect(cfgHostsLine);
			setProperlyConfigured(true);
		}
	}

	/**
	 * Generates the {@link OHConfig} EMF model configuration store for the
	 * device configuration from openhab.cfg.
	 * 
	 * Creates the device specific configuration object {@link OHTFDevice} and
	 * calls {@link #fillupConfig(OHTFDevice, Map) fillupConfig} to fill in the
	 * configuration into the configuration object.
	 * 
	 * @param deviceConfig
	 *            The device configuration as {@code Map} of {@code Strings}.
	 * @throws ConfigurationException
	 */
	private void createOHTFDeviceConfig(Map<String, String> deviceConfig)
			throws ConfigurationException {
		String deviceType = deviceConfig.get(ConfigKey.type.name());
		if (deviceType.equals(TypeKey.servo.name())) {
			logger.debug("{} setting servo config",
					LoggerConstants.CONFIG);
			TFServoConfiguration servoConfiguration = modelFactory
					.createTFServoConfiguration();
			OHTFDevice<TFServoConfiguration, IO16SubIds> ohtfDevice = modelFactory
					.createOHTFDevice();
			ohtfDevice.setTfConfig(servoConfiguration);
			fillupConfig(ohtfDevice, deviceConfig);
		} else if (deviceType.equals(TypeKey.bricklet_distance_ir.name())
				|| deviceType.equals(TypeKey.bricklet_humidity.name())
				|| deviceType.equals(TypeKey.bricklet_temperature.name())
				|| deviceType.equals(TypeKey.bricklet_barometer.name())
				|| deviceType.equals(TypeKey.bricklet_ambient_light.name())) {
			logger.debug("{} setting base config",
					LoggerConstants.CONFIG);
			TFBaseConfiguration tfBaseConfiguration = modelFactory
					.createTFBaseConfiguration();
			if (deviceType.equals(TypeKey.bricklet_barometer)) {
				OHTFDevice<TFBaseConfiguration, BarometerSubIDs> ohtfDevice = modelFactory
						.createOHTFDevice();
				ohtfDevice.setTfConfig(tfBaseConfiguration);
				fillupConfig(ohtfDevice, deviceConfig);

			} else {
				OHTFDevice<TFBaseConfiguration, NoSubIds> ohtfDevice = modelFactory
						.createOHTFDevice();
				ohtfDevice.setTfConfig(tfBaseConfiguration);
				fillupConfig(ohtfDevice, deviceConfig);
			}
		} else if (deviceType.equals(TypeKey.brick_dc.name())) {
			logger.debug("{} setting dc config",
					LoggerConstants.CONFIG);
			TFBrickDCConfiguration tfBrickDCConfiguration = modelFactory
					.createTFBrickDCConfiguration();
			OHTFDevice<TFBrickDCConfiguration, NoSubIds> ohtfDevice = modelFactory
					.createOHTFDevice();
			ohtfDevice.getSubDeviceIds().addAll(
					Arrays.asList(NoSubIds.values()));
			ohtfDevice.setTfConfig(tfBrickDCConfiguration);
			fillupConfig(ohtfDevice, deviceConfig);
		} else if (deviceType.equals(TypeKey.io_actuator.name())) {
			logger.debug("{} setting io_actuator config",
					LoggerConstants.CONFIG);
			TFIOActorConfiguration tfioActorConfiguration = modelFactory
					.createTFIOActorConfiguration();
			OHTFDevice<TFIOActorConfiguration, IO16SubIds> ohtfDevice = modelFactory
					.createOHTFDevice();
			ohtfDevice.getSubDeviceIds().addAll(
					Arrays.asList(IO16SubIds.values()));
			ohtfDevice.setTfConfig(tfioActorConfiguration);
			fillupConfig(ohtfDevice, deviceConfig);
		} else if (deviceType.equals(TypeKey.iosensor.name())) {
			logger.debug("{} setting iosensor config",
					LoggerConstants.CONFIG);
			TFIOSensorConfiguration tfioSensorConfiguration = modelFactory
					.createTFIOSensorConfiguration();
			OHTFDevice<TFIOSensorConfiguration, IO16SubIds> ohtfDevice = modelFactory
					.createOHTFDevice();
			ohtfDevice.getSubDeviceIds().addAll(
					Arrays.asList(IO16SubIds.values()));
			ohtfDevice.setTfConfig(tfioSensorConfiguration);
			fillupConfig(ohtfDevice, deviceConfig);
		} else if (deviceType.equals(TypeKey.bricklet_industrial_digital_4in
				.name()) || deviceType.equals(TypeKey.bricklet_io16.name())) {
			logger.debug("{} setting no tfConfig device_type {}",
					LoggerConstants.CONFIG, deviceType);
			TFInterruptListenerConfiguration tfInterruptListenerConfiguration = modelFactory
					.createTFInterruptListenerConfiguration();
			OHTFDevice<TFInterruptListenerConfiguration, NoSubIds> ohtfDevice = modelFactory
					.createOHTFDevice();
			ohtfDevice.getSubDeviceIds().addAll(
					Arrays.asList(NoSubIds.values()));
			ohtfDevice.setTfConfig(tfInterruptListenerConfiguration);
			fillupConfig(ohtfDevice, deviceConfig);
		} else {
			logger.debug("{} setting no tfConfig device_type {}",
					LoggerConstants.CONFIG, deviceType);
			logger.trace("{} deviceType {}", LoggerConstants.CONFIG, deviceType);
			OHTFDevice<?, NoSubIds> ohtfDevice = modelFactory
					.createOHTFDevice();
			ohtfDevice.getSubDeviceIds().addAll(
					Arrays.asList(NoSubIds.values()));
			fillupConfig(ohtfDevice, deviceConfig);
		}
	}

	/**
	 * Fills in the configuration into the configuration object and adds it to
	 * the {@link OHConfig}.
	 * 
	 * @param ohtfDevice
	 *            The device specific configuration object {@link OHTFDevice}.
	 * @param deviceConfig
	 *            The device configuration as {@code Map} of {@code Strings}.
	 * @throws ConfigurationException
	 */
	private void fillupConfig(OHTFDevice<?, ?> ohtfDevice,
			Map<String, String> deviceConfig) throws ConfigurationException {
		String uid = deviceConfig.get(ConfigKey.uid.name());
		ohtfDevice.setUid(uid);
		String subid = deviceConfig.get(ConfigKey.subid.name());
		if (subid != null) {
			if (!ohtfDevice.isValidSubId(subid)) {
				throw new ConfigurationException(subid, String.format(
								"\"%s\" is an invalid subId: openhab.cfg has to be fixed!",
								subid));
			}
			ohtfDevice.setSubid(subid);
		}
		if (ohConfig.getConfigByTFId(uid, subid) != null) {
			throw new ConfigurationException(String.format("uid: %s subId: %s",
					uid, subid),
					String.format(
							"%s: duplicate device config for uid \"%s\" and subId \"%s\": fix openhab.cfg",
							LoggerConstants.CONFIG, uid, subid));
		}
		String symbolicName = deviceConfig.get(ConfigKeyAdmin.ohId.name());
		if (ohConfig.getConfigByOHId(symbolicName) != null) {
			throw new ConfigurationException(
					String.format("symbolic name: %s", symbolicName),
					String.format(
							"%s: duplicate device config for symbolic name \"%s\": fix openhab.cfg",
							LoggerConstants.CONFIG, symbolicName));
		}
		ohtfDevice.setOhid(symbolicName);

		EObject tfConfig = ohtfDevice.getTfConfig();
		EList<EStructuralFeature> features = null;
		if (tfConfig != null) {
			features = tfConfig.eClass().getEAllStructuralFeatures();
		}
		ArrayList<String> configKeyList = new ArrayList<String>();
		for (ConfigKeyAdmin configKey : ConfigKeyAdmin.values()) {
			configKeyList.add(configKey.toString());
		}
		for (String property : deviceConfig.keySet()) {
			if (configKeyList.contains(property)) {
				continue;
			}
			else {
				logger.trace("{} found  property {}",
						LoggerConstants.CONFIG, property);
			}

			if (features != null) {
				for (EStructuralFeature feature : features) {
					logger.trace("found feature: {}", feature.getName());
					if (feature.getName().equals(property)) {
						logger.trace("{} feature type {}",
								LoggerConstants.CONFIG, feature.getEType()
										.getInstanceClassName());
						logger.debug("configuring feature: {} for uid {}", feature.getName(), uid);
						if (feature.getEType().getInstanceClassName().equals("int")) {
							tfConfig.eSet(feature, Integer.parseInt(deviceConfig.get(property)));
						} else if (feature.getEType().getInstanceClassName().equals("short")) {
							tfConfig.eSet(feature, Short.parseShort(deviceConfig.get(property)));
						} else if (feature.getEType().getInstanceClassName().equals("long")) {
							tfConfig.eSet(feature, Long.parseLong(deviceConfig.get(property)));
						} else if (feature.getEType().getInstanceClassName().equals("boolean")) {
							logger.debug("{} found boolean value",
									LoggerConstants.CONFIG);
							tfConfig.eSet(feature, Boolean.parseBoolean(deviceConfig.get(property)));
						} else {
							throw new ConfigurationException(feature.getName(),
									"unsupported configuration type needed");
						}
						break;
					}
				}
			}
		}

		ohConfig.getOhTfDevices().add(ohtfDevice);
	}

	/**
	 * Checks if the {@code deviceType} is known by the {@link Ecosystem}.
	 * 
	 * @param ohId
	 *            The name of the device found in openhab.cfg as {@code String}.
	 * @param deviceType
	 *            The device type found in openhab.cfg as {@code String}.
	 * @throws ConfigurationException
	 */
	private void checkTfType(String ohId, String deviceType) throws ConfigurationException {
		ModelPackage modelPackage = ModelPackage.eINSTANCE;
		boolean deviceFound = false;
		for (EClassifier eClassifier : modelPackage.getEClassifiers()) {
			if (eClassifier instanceof EClass) {
				EList<EAttribute> attributes = ((EClass) eClassifier).getEAllAttributes();
				for (EAttribute attribute : attributes) {
					if (attribute.getName().equals("deviceType")) {
						if (attribute.getDefaultValueLiteral().equals(deviceType)) {
							deviceFound = true;
							break;
						}
					}
				}
			}
		}
		if (!deviceFound) {
			throw new ConfigurationException(ohId, "unknown device type: " + deviceType);
		}
	}

	/**
	 * Parses the the hosts line from openhab.cfg into hosts and port parts and
	 * connects the appropriate brickds by calling
	 * {@link #connectBrickd(String, int) connectBrickd}.
	 * 
	 * @param cfgHostsLine
	 *            The hosts line found in the openhab.cfg as {@code String}.
	 */
	private void parseCfgHostsAndConnect(String cfgHostsLine) {
		String[] cfgHostsEntries = cfgHostsLine.split("\\s");
		for (int i = 0; i < cfgHostsEntries.length; i++) {
			String cfgHostEntry = cfgHostsEntries[i];
			String[] cfgHostAndPort = cfgHostEntry.split(":", 2);
			String host = cfgHostAndPort[0];
			int port;
			if (cfgHostAndPort.length == 2) {
				port = Integer.parseInt(cfgHostAndPort[1]);
			} else {
				port = BRICKD_DEFAULT_PORT;
			}
			connectBrickd(host, port);
		}
	}

}
