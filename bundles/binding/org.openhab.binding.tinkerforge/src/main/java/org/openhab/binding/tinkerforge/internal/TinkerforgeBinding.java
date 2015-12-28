/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal;

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.openhab.binding.tinkerforge.TinkerforgeBindingProvider;
import org.openhab.binding.tinkerforge.ecosystem.TinkerforgeContextImpl;
import org.openhab.binding.tinkerforge.internal.config.ConfigurationHandler;
import org.openhab.binding.tinkerforge.internal.model.ColorActor;
import org.openhab.binding.tinkerforge.internal.model.DigitalActor;
import org.openhab.binding.tinkerforge.internal.model.DimmableActor;
import org.openhab.binding.tinkerforge.internal.model.Ecosystem;
import org.openhab.binding.tinkerforge.internal.model.GenericDevice;
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
import org.openhab.binding.tinkerforge.internal.model.MoveActor;
import org.openhab.binding.tinkerforge.internal.model.NumberActor;
import org.openhab.binding.tinkerforge.internal.model.OHConfig;
import org.openhab.binding.tinkerforge.internal.model.OHTFDevice;
import org.openhab.binding.tinkerforge.internal.model.PercentTypeActor;
import org.openhab.binding.tinkerforge.internal.model.ProgrammableColorActor;
import org.openhab.binding.tinkerforge.internal.model.ProgrammableSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.SetPointActor;
import org.openhab.binding.tinkerforge.internal.model.SimpleColorActor;
import org.openhab.binding.tinkerforge.internal.model.SwitchSensor;
import org.openhab.binding.tinkerforge.internal.model.TFConfig;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;
import org.openhab.binding.tinkerforge.internal.types.DirectionValue;
import org.openhab.binding.tinkerforge.internal.types.HSBValue;
import org.openhab.binding.tinkerforge.internal.types.HighLowValue;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.openhab.binding.tinkerforge.internal.types.PercentValue;
import org.openhab.binding.tinkerforge.internal.types.TinkerforgeValue;
import org.openhab.binding.tinkerforge.internal.types.UnDefValue;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
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
 * This binding connects the TinkerForge devices to the openhab eventbus.
 * 
 * This class uses an EMF model with a TinkerforgeEcosystem object as root. The TinkerforgeEcosystem
 * has Brickd child objects. The Brickd object has an IpConnection to the TinkerForge brickd daemon,
 * identified by an ip address or host name and a port. The Brickd object has child objects for
 * every connected TinkerForge device. The TinkerForge device object holds leaf objects for
 * subdevices if available. All the device objects are sharing the IpConnection of the Brickd
 * object. The EMF device objects can be interpreted as a facade for the TinkerForge api device
 * objects. If available, the EMF device objects implement TinkerForge CallbackListeners for sensor
 * value updates and are updating the EMF device object sensor properties accordingly.
 * 
 * The binding adds a listener to the TinkerforgeEcosystem. On the one hand this listener handles
 * updated sensor values and propagates them to the openhab eventbus. On the other hand the listener
 * is informed about new devices in the TinkerforgeEcosystem and thus can pass configuration
 * settings from openhab.cfg to the devices. The callback period of the CallbackListener and a
 * threshold value are configurable through openhab.cfg.
 * 
 * All device values are additionally polled by the execute method mainly to get values from
 * subdevices which don't have TinkerForge CallbackListeners for getting the sensor values.
 * 
 * Tinkerforge devices which work as actors like relays can be controlled with this binding.
 * 
 * For now only a subset of the TinkerForge devices are supported and not all features of the
 * devices are implemented. More devices and features will be added soon. The following devices are
 * supported for now:
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
public class TinkerforgeBinding extends AbstractActiveBinding<TinkerforgeBindingProvider>
    implements
      ManagedService {

  private static final String CONFIG_KEY_HOSTS = "hosts";

  private static final Logger logger = LoggerFactory.getLogger(TinkerforgeBinding.class);

  private static final int BRICKD_DEFAULT_PORT = 4223;

  /**
   * the refresh interval which is used to poll values from the Tinkerforge server (optional,
   * defaults to 60000ms)
   */
  private long refreshInterval = 60000;

  private Ecosystem tinkerforgeEcosystem;

  private ModelFactory modelFactory;
  private OHConfig ohConfig;
  private boolean isConnected;
  private TinkerforgeContextImpl context = (TinkerforgeContextImpl) TinkerforgeContextImpl
      .getInstance();

  public TinkerforgeBinding() {
    modelFactory = ModelFactory.eINSTANCE;
  }

  @Override
  public void activate() {}

  @Override
  public void deactivate() {
    disconnectModel();
  }

  /**
   * Disconnects the IpConnections to all TinkerForge brickds and destroys the TinkerforgeEcosystem.
   */
  private void disconnectModel() {
    if (isConnected) {
      logger.debug("disconnect called");
      tinkerforgeEcosystem.disconnect();
      tinkerforgeEcosystem = null;
      context.setEcosystem(null);
      isConnected = false;
    }
  }

  /**
   * Creates a Tinkerforge Ecosystem object and adds a listener to it.
   */
  private void connectModel() {
    tinkerforgeEcosystem = modelFactory.createEcosystem();
    context.setEcosystem(tinkerforgeEcosystem);
    listen2Model(tinkerforgeEcosystem);
    logger.debug("{} connectModel called", LoggerConstants.TFINIT);
    isConnected = true;
  }

  /**
   * Searches for a brickd with the given {@code host} and {@code port} in the Ecosystem. If there
   * is no brickd found a new Brickd object is created, added to the Ecosystem an the IpConnection
   * to the Tinkerforge brickd is established and a device enumeration is triggert.
   * 
   * @param host The host name or ip address of the TinkerForge brickd as String.
   * @param port The port of the TinkerForge brickd as int.
   * @param authkey
   */
  private void connectBrickd(String host, int port, String authkey) {
    MBrickd brickd = tinkerforgeEcosystem.getBrickd(host, port);
    if (brickd == null) {
      brickd = modelFactory.createMBrickd();
      brickd.setHost(host);
      brickd.setPort(port);
      brickd.setAuthkey(authkey);
      brickd.setEcosystem(tinkerforgeEcosystem);
      tinkerforgeEcosystem.getMbrickds().add(brickd);
      brickd.init();
      brickd.connect();
      logger.debug("{} Tinkerforge new brickd for host: {}", LoggerConstants.TFINIT, host);
    } else {
      logger.debug("{} Tinkerforge found existing brickd for host: {}", LoggerConstants.TFINIT,
          host);
    }
  }

  /**
   * Adds a listener {@link EContentAdapter} to the {@link Ecosystem}. The listener handles updated
   * sensor values and posts them to the openhab eventbus by
   * {@link #processTFDeviceValues(Notification) processTFDeviceValues}. Furthermore the addition
   * and removal of devices is handled by {@link #initializeTFDevices(Notification)
   * initializeTFDevices}.
   * 
   * @param tinkerforgeEcosystem The EMF Ecosystem object.
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

  private boolean checkDuplicateGenericDevice(GenericDevice device, String uid, String subId) {
    boolean isDuplicate = false;
    final String genericDeviceId = device.getGenericDeviceId();
    final EList<MSubDevice<?>> genericDevicesList =
        tinkerforgeEcosystem.getDevices4GenericId(uid, genericDeviceId);
    if (genericDevicesList.size() != 0) {
      for (MSubDevice<?> gd : genericDevicesList) {
        if (!gd.getSubId().equals(subId) && gd.getEnabledA().get()) {
          isDuplicate = true;
          logger.error("{} existing device is uid {} subId {}", LoggerConstants.CONFIG,
              gd.getUid(), gd.getSubId());
        }
      }
    }

    return isDuplicate;
  }

  /**
   * Configures and enables newly found devices. For sub devices the master device is also enabled.
   * Configuration is only added if there is a configuration from openhab.cfg available and the
   * device is configurable which is the case for {@link MTFConfigConsumer}. Devices of type
   * {@link IODevice} are only enabled if they are configured in openhab.cfg, all other devices are
   * always enabled.
   * 
   * @param device A device object as {@link MBaseDevice}.
   * @param uid The device uid as String.
   * @param subId The device subid as String or <code>null</code> if the device is not a sub device.
   */
  @SuppressWarnings("unchecked")
  private synchronized void addMDevice(MBaseDevice device, String uid, String subId) {
    String logId = subId == null ? uid : uid + " " + subId;
    OHTFDevice<?, ?> deviceConfig = ohConfig.getConfigByTFId(uid, subId);
    if (deviceConfig == null) {
      logger.debug("{} found no device configuration for uid \"{}\" subid \"{}\"",
          LoggerConstants.TFINITSUB, uid, subId);
    }
    if (device.getEnabledA().compareAndSet(false, true)) {
      if (subId != null) {
        MDevice<?> masterDevice = (MDevice<?>) device.eContainer();
        // recursion for adding the master device
        if (!masterDevice.getEnabledA().get()) {
          logger.debug("{} enabling masterDevice {}", LoggerConstants.TFINITSUB,
              masterDevice.getUid());
          addMDevice(masterDevice, uid, null);
        }
      }
      if (device instanceof MTFConfigConsumer<?> && deviceConfig != null) {
        logger.debug("{} found MTFConfigConsumer id {}", LoggerConstants.TFINIT, logId);
        if (device instanceof GenericDevice
            && checkDuplicateGenericDevice((GenericDevice) device, uid, subId)) {
          logger
              .error(
                  "{} ignoring duplicate device uid: {}, subId {}, genericId {}. Fix your openhab.cfg!",
                  LoggerConstants.CONFIG, uid, subId);
          device.getEnabledA().compareAndSet(true, false);
        } else {
          TFConfig deviceTfConfig = EcoreUtil.copy(deviceConfig.getTfConfig());
          logger.debug("{} setting tfConfig for {}", LoggerConstants.TFINIT, logId);
          logger.debug("{} adding/enabling device {} with config: {}", LoggerConstants.TFINIT,
              logId, deviceTfConfig);
          ((MTFConfigConsumer<EObject>) device).setTfConfig(deviceTfConfig);
          device.enable();
        }
      } else if (device instanceof IODevice) {
        logger.debug("{} ignoring unconfigured  IODevice: {}", LoggerConstants.TFINIT, logId);
        // set the device disabled, this is needed for not getting
        // states
        // through execute method
        device.getEnabledA().compareAndSet(true, false);
      } else {
        device.enable();
        logger.debug("{} adding/enabling device: {}", LoggerConstants.TFINIT, logId);
      }
    }
  }

  /**
   * Adds or removes a device to / from the Ecosystem. Notifications from {@link MBrickd} are used
   * for adding devices (not sub devices) and removing of devices and their corresponding sub
   * devices.
   * 
   * Notifications from {@link MSubDeviceHolder} for adding sub devices.
   * 
   * @param notification The {@link Notification} for add and remove events to the {@link Ecosystem}
   *        .
   */
  private void initializeTFDevices(Notification notification) {
    logger.trace("{} notifier {}", LoggerConstants.TFINIT, notification.getNotifier());
    if (notification.getNotifier() instanceof MBrickd) {
      logger.debug("{} notifier is Brickd", LoggerConstants.TFINIT);
      int featureID = notification.getFeatureID(MBrickd.class);
      if (featureID == ModelPackage.MBRICKD__MDEVICES) {
        if (notification.getEventType() == Notification.ADD) {
          MDevice<?> mDevice = (MDevice<?>) notification.getNewValue();
          addMDevice(mDevice, mDevice.getUid(), null);
        } else if (notification.getEventType() == Notification.ADD_MANY) {
          logger.debug("{} Notifier: add many called: ", LoggerConstants.TFINIT);
        } else if (notification.getEventType() == Notification.REMOVE) {
          logger.debug("{} Notifier: remove called: ", LoggerConstants.TFINIT);
          if (notification.getOldValue() instanceof MBaseDevice) {
            logger.debug("{} Notifier: remove called for MBaseDevice", LoggerConstants.TFINIT);
            MBaseDevice mDevice = (MBaseDevice) notification.getOldValue();
            String uid = mDevice.getUid();
            String subId = null;
            if (searchConfiguredItemName(uid, subId) != null) {
              logger.debug("{} Notifier: removing device: uid {} subid {}", LoggerConstants.TFINIT,
                  uid, subId);
              postUpdate(uid, subId, UnDefValue.UNDEF);
            }
          } else {
            logger.debug("{} unknown notification from mdevices {}", LoggerConstants.TFINIT,
                notification);
          }
        }
      } else {
        logger.debug("{} Notifier: unknown feature {}", LoggerConstants.TFINIT,
            notification.getFeature());
      }
    } else if (notification.getNotifier() instanceof MSubDeviceHolder<?>) {
      int featureID = notification.getFeatureID(MSubDeviceHolder.class);
      if (featureID == ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES) {
        logger.debug("{} MSubdevices Notifier called", LoggerConstants.TFINITSUB);
        if (notification.getEventType() == Notification.ADD) {
          MSubDevice<?> mSubDevice = (MSubDevice<?>) notification.getNewValue();
          addMDevice(mSubDevice, mSubDevice.getUid(), mSubDevice.getSubId());

        }
        if (notification.getEventType() == Notification.REMOVE) {
          logger.debug("{} remove notification from subdeviceholder", LoggerConstants.TFINIT);
          logger.debug("{} Notifier: remove called for MSubDevice", LoggerConstants.TFINIT);
          MSubDevice<?> mDevice = (MSubDevice<?>) notification.getOldValue();
          String uid = mDevice.getUid();
          String subId = mDevice.getSubId();
          if (searchConfiguredItemName(uid, subId) != null) {
            logger.debug("{} Notifier: removing device: uid {} subid {}", LoggerConstants.TFINIT,
                uid, subId);
            postUpdate(uid, subId, UnDefValue.UNDEF);
          }
        }
      }
    } else {
      logger.debug("{} unhandled notifier {}", LoggerConstants.TFINIT, notification.getNotifier());
    }
  }

  /**
   * Processes change events from the {@link Ecosystem}. Sensor values from {@link MSensor} are
   * handled by {@link #processSensorValue(MSensor, Notification) processSensorValue}, actor values
   * from {@link MSwitchActore} are handled by
   * {@link #processSwitchActorValue(MSwitchActor, Notification) processSwitchActorValue}. (no add
   * or remove events, these are handled in {@link #initializeTFDevices(Notification)
   * initializeTFDevices}).
   * 
   * 
   * @param notification The {@link Notification} about changes to the {@link Ecosystem}.
   */
  private void processTFDeviceValues(Notification notification) {
    if (notification.getNotifier() instanceof MSensor) {
      MSensor<?> sensor = (MSensor<?>) notification.getNotifier();
      int featureID = notification.getFeatureID(MSensor.class);
      if (featureID == ModelPackage.MSENSOR__SENSOR_VALUE) {
        processValue((MBaseDevice) sensor, notification);
      }
    }
 else if (notification.getNotifier() instanceof SetPointActor<?>) {
      SetPointActor<?> actor = (SetPointActor<?>) notification.getNotifier();
      int setpointFeatureID = notification.getFeatureID(SetPointActor.class);
      if (setpointFeatureID == ModelPackage.SET_POINT_ACTOR__PERCENT_VALUE) {
        processValue((MBaseDevice) actor, notification);
      }
    }
 else if (notification.getNotifier() instanceof MoveActor) {
      MoveActor actor = (MoveActor) notification.getNotifier();
      int moveFeatureID = notification.getFeatureID(MoveActor.class);
      if (moveFeatureID == ModelPackage.MOVE_ACTOR__DIRECTION) {
        processValue((MBaseDevice) actor, notification);
      }
    }
 else if (notification.getNotifier() instanceof MSwitchActor) {
      MSwitchActor switchActor = (MSwitchActor) notification.getNotifier();
      int featureID = notification.getFeatureID(MSwitchActor.class);
      if (featureID == ModelPackage.MSWITCH_ACTOR__SWITCH_STATE) {
        processValue((MBaseDevice) switchActor, notification);
      }
    }
 else if (notification.getNotifier() instanceof ProgrammableSwitchActor) {
      logger.trace("notification {}", notification);
      logger.trace("notifier {}", notification.getNotifier());
      ProgrammableSwitchActor switchActor = (ProgrammableSwitchActor) notification.getNotifier();
      // use the super type class for getting the featureID. Should not be necessary according to
      // the docs or I misunderstand it. But this approach works.
      int featureID = notification.getFeatureID(SwitchSensor.class);
      logger.trace("notification ProgrammableSwitchActor id {}", featureID);
      if (featureID == ModelPackage.PROGRAMMABLE_SWITCH_ACTOR__SWITCH_STATE) {
        logger.trace("ProgrammableSwitchActor switch state changed sending notification");
        processValue((MBaseDevice) switchActor, notification);
      }
    }
 else if (notification.getNotifier() instanceof DigitalActor) {
      DigitalActor actor = (DigitalActor) notification.getNotifier();
      int featureID = notification.getFeatureID(DigitalActor.class);
      if (featureID == ModelPackage.DIGITAL_ACTOR__DIGITAL_STATE) {
        processValue((MBaseDevice) actor, notification);
      }
    }
 else if (notification.getNotifier() instanceof ColorActor) {
      ColorActor actor = (ColorActor) notification.getNotifier();
      int featureID = notification.getFeatureID(ColorActor.class);
      if (featureID == ModelPackage.COLOR_ACTOR__COLOR) {
        processValue((MBaseDevice) actor, notification);
      }
    }
 else if (notification.getNotifier() instanceof MBrickd) {
      MBrickd brickd = (MBrickd) notification.getNotifier();
      int featureID = notification.getFeatureID(MBrickd.class);
      if (featureID == ModelPackage.MBRICKD__CONNECTED_COUNTER) {
        String subId = "connected_counter";
        processValue(brickd, notification, subId);
      } else if (featureID == ModelPackage.MBRICKD__IS_CONNECTED) {
        String subId = "isconnected";
        processValue(brickd, notification, subId);
      }
    }
    // TODO hier muss noch was fuer die dimmer und rollershutter rein
    else {
      logger.trace("{} ignored notifier {}", LoggerConstants.TFMODELUPDATE,
          notification.getNotifier());
    }
  }

  private void processValue(MBrickd brickd, Notification notification, String subId) {
    TinkerforgeValue newValue = (TinkerforgeValue) notification.getNewValue();
    String uid = brickd.getHost() + ":" + ((Integer) brickd.getPort()).toString();
    logger.trace("{} Notifier found brickd value uid {} subid {}", LoggerConstants.TFMODELUPDATE,
        uid, subId);
    postUpdate(uid, subId, newValue);
  }
  /**
   * Processes changed device values to post them to the openHAB event bus.
   * 
   * @param device The {@link MBaseDevice} device, which has a changed value.
   * @param notification The {@link Notification} about changes to the {@link Ecosystem}.
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
      logger.trace("{} Notifier found mDevice sensor value for: {}", LoggerConstants.TFMODELUPDATE,
          uid);
    }
    postUpdate(uid, subId, newValue);
  }


  /**
   * Searches the name of an item which is bound to the device with the given uid and subid.
   * 
   * @param uid The device uid as {@code String}.
   * @param subId The device subid as {@code String} or {@code null} if it is not a sub device.
   * @return The name of the item which is bound to the device as {@code String} or {@code null} if
   *         no item was found.
   */
  private String searchConfiguredItemName(String uid, String subId) {
    for (TinkerforgeBindingProvider provider : providers) {
      for (String itemName : provider.getItemNames()) {
        String deviceUid = provider.getUid(itemName);
        String subDeviceId = provider.getSubId(itemName);
        String deviceName = provider.getName(itemName);
        if (deviceName != null) {
          logger.trace("found item for command: name {}", deviceName);
          OHTFDevice<?, ?> ohtfDevice = ohConfig.getConfigByOHId(deviceName);
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
   * Searches the provider which is bound to the device with the given uid and subid.
   * 
   * @param uid The device uid as {@code String}.
   * @param subId The device subid as {@code String} or {@code null} if it is not a sub device.
   * @return The {@code TinkerforgeBindingProvider} which is bound to the device as {@code Item} or
   *         {@code null} if no item was found.
   */
  private Map<String, TinkerforgeBindingProvider> getBindingProviders(String uid, String subId) {
    Map<String, TinkerforgeBindingProvider> providerMap =
        new HashMap<String, TinkerforgeBindingProvider>();
    for (TinkerforgeBindingProvider provider : providers) {
      for (String itemName : provider.getItemNames()) {
        String deviceUid = provider.getUid(itemName);
        String subDeviceId = provider.getSubId(itemName);
        String deviceName = provider.getName(itemName);
        if (deviceName != null) {
          OHTFDevice<?, ?> ohtfDevice = ohConfig.getConfigByOHId(deviceName);
          deviceUid = ohtfDevice.getUid();
          subDeviceId = ohtfDevice.getSubid();
          logger.trace("found deviceName {}, uid={}, subId {}", deviceName, deviceUid, subDeviceId);
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
   * Triggers an update of state values for all devices. The update is propagated through the
   * {@link Ecosystem} listeners. All OutActors are ignored, they may only send updates if the
   * hardware device has updates (think of a pressed switch).
   * 
   */
  @Override
  protected void execute() {
    for (TinkerforgeBindingProvider provider : providers) {
      for (String itemName : provider.getItemNames()) {
        updateItemValues(provider, itemName, true);
      }
    }
  }

  /**
   * Triggers an update of state values for all devices.
   * 
   * @param provider The {@code TinkerforgeBindingProvider} which is bound to the device as
   *        {@code Item}
   * @param itemName The name of the {@code Item} as String
   * @param only_poll_enabled Fetch only the values of devices which do not support callback
   *        listeners. These devices are marked with poll "true" flag.
   */
  protected void updateItemValues(TinkerforgeBindingProvider provider, String itemName,
      boolean only_poll_enabled) {
    if ( tinkerforgeEcosystem == null){
      logger.warn("tinkerforge ecosystem not yet ready");
      return;
    }
    String deviceUid = provider.getUid(itemName);
    Item item = provider.getItem(itemName);
    String deviceSubId = provider.getSubId(itemName);
    String deviceName = provider.getName(itemName);
    if (deviceName != null) {
      String[] ids = getDeviceIdsForDeviceName(deviceName);
      deviceUid = ids[0];
      deviceSubId = ids[1];
    }
    MBaseDevice mDevice = tinkerforgeEcosystem.getDevice(deviceUid, deviceSubId);
    if (mDevice != null && mDevice.getEnabledA().get()) {
      if (only_poll_enabled && !mDevice.isPoll()) {
        // do nothing
        logger.debug("{} omitting fetch value for no poll{}:{}", LoggerConstants.ITEMUPDATE,
            deviceUid, deviceSubId);
      } else {
        if (mDevice instanceof MSensor) {
          ((MSensor<?>) mDevice).fetchSensorValue();
        } else if (mDevice instanceof SwitchSensor && item instanceof SwitchItem) {
          ((SwitchSensor) mDevice).fetchSwitchState();
        } else if (mDevice instanceof DigitalActor) {
          ((DigitalActor) mDevice).fetchDigitalValue();
        }
      }
    }
  }

  @Override
  public void bindingChanged(BindingProvider provider, String itemName) {
    logger.debug("{} bindingChanged item {}", LoggerConstants.ITEMUPDATE, itemName);
    updateItemValues((TinkerforgeBindingProvider) provider, itemName, false);
  }

  private void postUpdate(String uid, String subId, TinkerforgeValue sensorValue) {
    // TODO undef handling
    logger.trace("postUpdate called for uid {} subid {}", uid, subId);
    Map<String, TinkerforgeBindingProvider> providerMap = getBindingProviders(uid, subId);
    if (providerMap.size() == 0) {
      logger.debug("{} found no item for uid {}, subid {}", LoggerConstants.TFMODELUPDATE, uid,
          subId);
    }
    for (Entry<String, TinkerforgeBindingProvider> entry : providerMap.entrySet()) {
      String itemName = entry.getKey();
      TinkerforgeBindingProvider provider = entry.getValue();
      Class<? extends Item> itemType = provider.getItemType(itemName);
      State value = UnDefType.UNDEF;
      if (sensorValue instanceof DecimalValue) {
        if (itemType.isAssignableFrom(NumberItem.class)
            || itemType.isAssignableFrom(StringItem.class)) {
          value = DecimalType.valueOf(String.valueOf(sensorValue));
          logger.trace("found item to update for DecimalValue {}", itemName);
        } else if (itemType.isAssignableFrom(ContactItem.class)) {
          value =
              sensorValue.equals(DecimalValue.ZERO) ? OpenClosedType.CLOSED : OpenClosedType.OPEN;
        } else if (itemType.isAssignableFrom(SwitchItem.class)) {
          value = sensorValue.equals(DecimalValue.ZERO) ? OnOffType.OFF : OnOffType.ON;
        } else {
          logger.trace("no update for DecimalValue for item {}", itemName);
          continue;
        }
      } else if (sensorValue instanceof HighLowValue) {
        if (itemType.isAssignableFrom(NumberItem.class)
            || itemType.isAssignableFrom(StringItem.class)) {
          value =
              sensorValue == HighLowValue.HIGH ? DecimalType.valueOf("1") : DecimalType
                  .valueOf("0");
        } else if (itemType.isAssignableFrom(ContactItem.class)) {
          value = sensorValue == HighLowValue.HIGH ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
        } else if (itemType.isAssignableFrom(SwitchItem.class)) {
          value = sensorValue == HighLowValue.HIGH ? OnOffType.ON : OnOffType.OFF;
        } else {
          continue;
        }
      } else if (sensorValue instanceof OnOffValue) {
        if (itemType.isAssignableFrom(NumberItem.class)
            || itemType.isAssignableFrom(StringItem.class)) {
          value =
              sensorValue == OnOffValue.ON ? DecimalType.valueOf("1") : DecimalType.valueOf("0");
        } else if (itemType.isAssignableFrom(ContactItem.class)) {
          value = sensorValue == OnOffValue.ON ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
        } else if (itemType.isAssignableFrom(SwitchItem.class)) {
          value = sensorValue == OnOffValue.ON ? OnOffType.ON : OnOffType.OFF;
        } else {
          continue;
        }
      } else if (sensorValue instanceof PercentValue) {
         if (itemType.isAssignableFrom(SwitchItem.class)) {
          value =
              ((PercentValue) sensorValue).toBigDecimal().compareTo(BigDecimal.ZERO) == 1
                  ? OnOffType.ON
                  : OnOffType.OFF;
          logger.debug("switch found {}", itemName);
        } 
         else if (itemType.isAssignableFrom(RollershutterItem.class)
            || itemType.isAssignableFrom(DimmerItem.class)) {
          value = new PercentType(((PercentValue) sensorValue).toBigDecimal());
          logger.debug("Rollershutter or dimmer found {} {}", itemName);
        } 
        else if (itemType.isAssignableFrom(ContactItem.class)) {
          value =
              ((PercentValue) sensorValue).toBigDecimal().compareTo(BigDecimal.ZERO) == -1
                  ? OpenClosedType.OPEN
                  : OpenClosedType.CLOSED;
          logger.debug("contact found {}", itemName);
        } else {
          continue;
        }
      } else if (sensorValue instanceof DirectionValue) {
        if (itemType.isAssignableFrom(RollershutterItem.class)) {
          value = sensorValue == DirectionValue.RIGHT ? UpDownType.UP : UpDownType.DOWN;
          logger.trace("found item to update for UpDownValue {}", itemName);
        } else {
          continue;
        }
      } else if (sensorValue instanceof HSBValue) {
        if (itemType.isAssignableFrom(ColorItem.class)) {
          logger.trace("found item to update for HSBValue {}", itemName);
          value = ((HSBValue) sensorValue).getHsbValue();
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
   * Gets the uid and the subid of a device from the openhab.cfg, using the device name as input.
   * 
   * @param deviceName The symbolic device name as {@code String}.
   * @return A String array with the device uid as first element as {@code String} and the device
   *         subid as second element as {@code String} or {@code null}.
   */
  private String[] getDeviceIdsForDeviceName(String deviceName) {
    logger.trace("searching ids for name {}", deviceName);
    OHTFDevice<?, ?> ohtfDevice = ohConfig.getConfigByOHId(deviceName);
    String[] ids = {ohtfDevice.getUid(), ohtfDevice.getSubid()};
    return ids;
  }

  /**
   * {@inheritDoc}
   * 
   * Searches the item with the given {@code itemName} in the {@link TinkerforgeBindingProvider}
   * collection and gets the uid and subid of the device. The appropriate device is searched in the
   * ecosystem and the command is executed on the device.
   * 
   * {@code OnOffType} commands are executed on {@link MInSwitchActor} objects. {@code StringType}
   * commands are executed on {@link MTextActor} objects.
   * 
   */
  @Override
  protected void internalReceiveCommand(String itemName, Command command) {
    logger.debug("received command {} for item {}", command, itemName);
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
          logger.trace("{} found item for command: uid: {}, subid: {}", LoggerConstants.COMMAND,
              deviceUid, deviceSubId);
          MBaseDevice mDevice = tinkerforgeEcosystem.getDevice(deviceUid, deviceSubId);
          if (mDevice != null && mDevice.getEnabledA().get()) {
            if (command instanceof OnOffType) {
              logger.trace("{} found onoff command", LoggerConstants.COMMAND);
              OnOffType cmd = (OnOffType) command;
              if (mDevice instanceof MSwitchActor) {
                OnOffValue state = cmd == OnOffType.OFF ? OnOffValue.OFF : OnOffValue.ON;
                ((MSwitchActor) mDevice).turnSwitch(state);
              } else if (mDevice instanceof DigitalActor) {
                HighLowValue state = cmd == OnOffType.OFF ? HighLowValue.LOW : HighLowValue.HIGH;
                ((DigitalActor) mDevice).turnDigital(state);
              } else if (mDevice instanceof ProgrammableSwitchActor) {
                OnOffValue state = cmd == OnOffType.OFF ? OnOffValue.OFF : OnOffValue.ON;
                ((ProgrammableSwitchActor) mDevice).turnSwitch(state,
                    provider.getDeviceOptions(itemName));
              } else {
                logger.error("{} received OnOff command for non-SwitchActor",
                    LoggerConstants.COMMAND);
              }
            } else if (command instanceof StringType) {
              logger.trace("{} found string command", LoggerConstants.COMMAND);
              if (mDevice instanceof MTextActor) {
                ((MTextActor) mDevice).setText(command.toString());
              }
            } else if (command instanceof DecimalType) {
              logger.debug("{} found number command", LoggerConstants.COMMAND);
              if (command instanceof HSBType) {
                logger.debug("{} found HSBType command", LoggerConstants.COMMAND);
                if (mDevice instanceof ProgrammableColorActor) {
                  logger.debug("{} found ProgrammableColorActor {}", itemName);
                  ((ProgrammableColorActor) mDevice).setSelectedColor((HSBType) command,
                      provider.getDeviceOptions(itemName));
                } else if (mDevice instanceof SimpleColorActor) {
                  logger.debug("{} found SimpleColorActor {}", itemName);
                  ((SimpleColorActor) mDevice).setSelectedColor((HSBType) command);
                }
              } else if (command instanceof PercentType) {
                if (mDevice instanceof SetPointActor) {
                  ((SetPointActor<?>) mDevice).setValue(((PercentType) command),
                      provider.getDeviceOptions(itemName));
                  logger.debug("found SetpointActor");
                } else if (mDevice instanceof PercentTypeActor) {
                  ((PercentTypeActor) mDevice).setValue(((PercentType) command),
                      provider.getDeviceOptions(itemName));
                  logger.debug("found PercentType actor");
                } else {
                  logger.error("found no percenttype actor");
                }
              } else {
                if (mDevice instanceof NumberActor) {
                  ((NumberActor) mDevice).setNumber(((DecimalType) command).toBigDecimal());
                } else if (mDevice instanceof SetPointActor) {
                  ((SetPointActor<?>) mDevice).setValue(((DecimalType) command).toBigDecimal(),
                      provider.getDeviceOptions(itemName));
                } else {
                  logger.error("found no number actor");
                }
              }
            } else if (command instanceof UpDownType) {
              UpDownType cmd = (UpDownType) command;
              logger.debug("{} UpDownType command {}", itemName, cmd);
              if (mDevice instanceof MoveActor) {
                ((MoveActor) mDevice).move((UpDownType) command,
                    provider.getDeviceOptions(itemName));
              }
            } else if (command instanceof StopMoveType) {
              StopMoveType cmd = (StopMoveType) command;
              if (mDevice instanceof MoveActor) {
                if (cmd == StopMoveType.STOP) {
                  ((MoveActor) mDevice).stop();
                } else {
                  ((MoveActor) mDevice).moveon(provider.getDeviceOptions(itemName));
                }
              }
              logger.debug("{} StopMoveType command {}", itemName, cmd);
            } else if (command instanceof IncreaseDecreaseType) {
              IncreaseDecreaseType cmd = (IncreaseDecreaseType) command;
              if (mDevice instanceof DimmableActor) {
                ((DimmableActor<?>) mDevice).dimm((IncreaseDecreaseType) command,
                    provider.getDeviceOptions(itemName));
              }
              logger.debug("{} IncreaseDecreaseType command {}", itemName, cmd);
            }

            else {
              logger.error("{} got unknown command type: {}", LoggerConstants.COMMAND,
                  command.toString());
            }
          } else
            logger.error("{} no tinkerforge device found for command for item uid: {} subId: {}",
                LoggerConstants.COMMAND, deviceUid, deviceSubId);
        }
      }
    }
  }

  /**
   * Updates the configuration of the managed service.
   * 
   * Extracts the host and port configuration and connects the appropriate brickds.
   * 
   * The device configuration from openhab.cfg is parsed into a {@code Map} based (temporary)
   * structure. This structure is used to generate the {@link OHConfig} EMF model configuration
   * store.
   */
  @Override
  public void updated(Dictionary<String, ?> config) throws ConfigurationException {
    if (config != null) {
      if (isConnected) {
        disconnectModel();
      }
      connectModel();
      String refreshIntervalString = (String) config.get("refresh");
      if (StringUtils.isNotBlank(refreshIntervalString)) {
        refreshInterval = Long.parseLong(refreshIntervalString);
      }

      ConfigurationHandler configurationHandler = new ConfigurationHandler();
      ohConfig = configurationHandler.createConfig(config);

      // read further config parameters here ...
      logger.debug("{} updated called", LoggerConstants.CONFIG);
      // must be done after all other config has been processed
      String cfgHostsLine = (String) config.get(CONFIG_KEY_HOSTS);
      parseCfgHostsAndConnect(cfgHostsLine);
      setProperlyConfigured(true);
    }
  }

  /**
   * Parses the the hosts line from openhab.cfg into hosts and port parts and connects the
   * appropriate brickds by calling {@link #connectBrickd(String, int) connectBrickd}.
   * 
   * @param cfgHostsLine The hosts line found in the openhab.cfg as {@code String}.
   */
  private void parseCfgHostsAndConnect(String cfgHostsLine) {
    String[] cfgHostsEntries = cfgHostsLine.split("\\s");
    for (int i = 0; i < cfgHostsEntries.length; i++) {
      String cfgHostEntry = cfgHostsEntries[i];
      String[] cfgHostAndPort = cfgHostEntry.split(":", 3);
      String host = cfgHostAndPort[0];
      int port;
      String authkey = null;
      if (cfgHostAndPort.length > 1) {
        if (!cfgHostAndPort[1].equals("")) {
          port = Integer.parseInt(cfgHostAndPort[1]);
        } else {
          port = BRICKD_DEFAULT_PORT;
        }
      } else {
        port = BRICKD_DEFAULT_PORT;
      }
      if (cfgHostAndPort.length == 3) {
        authkey = cfgHostAndPort[2];
      }
      logger.debug("parse brickd config: host {}, port {}, authkey is set {}", host, port,
          authkey != null
          ? true
          : false);
      connectBrickd(host, port, authkey);
    }
  }

}
