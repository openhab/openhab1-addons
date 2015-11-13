/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.openhab.binding.tinkerforge.internal.TinkerforgeErrorHandler;
import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MBrickletPiezoSpeaker;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.ProgrammableSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.SwitchSensor;
import org.openhab.binding.tinkerforge.internal.tools.Tools;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletPiezoSpeaker;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MBricklet Piezo Speaker</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#isPoll <em>Poll</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#getTinkerforgeDevice <em>Tinkerforge Device</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#getIpConnection <em>Ip Connection</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#getConnectedUid <em>Connected Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#getPosition <em>Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#getDeviceIdentifier <em>Device Identifier</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#getBrickd <em>Brickd</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#getSwitchState <em>Switch State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletPiezoSpeakerImpl#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MBrickletPiezoSpeakerImpl extends MinimalEObjectImpl.Container implements MBrickletPiezoSpeaker
{
  private static final String MORSE = "morse";

  private static final String BEEP = "beep";

  private static final String MODE = "mode";

  /**
   * The default value of the '{@link #getLogger() <em>Logger</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLogger()
   * @generated
   * @ordered
   */
  protected static final Logger LOGGER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLogger() <em>Logger</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLogger()
   * @generated
   * @ordered
   */
  protected Logger logger = LOGGER_EDEFAULT;

  /**
   * The default value of the '{@link #getUid() <em>Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUid()
   * @generated
   * @ordered
   */
  protected static final String UID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUid() <em>Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUid()
   * @generated
   * @ordered
   */
  protected String uid = UID_EDEFAULT;

  /**
   * The default value of the '{@link #isPoll() <em>Poll</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isPoll()
   * @generated
   * @ordered
   */
  protected static final boolean POLL_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isPoll() <em>Poll</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isPoll()
   * @generated
   * @ordered
   */
  protected boolean poll = POLL_EDEFAULT;

  /**
   * The default value of the '{@link #getEnabledA() <em>Enabled A</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnabledA()
   * @generated
   * @ordered
   */
  protected static final AtomicBoolean ENABLED_A_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getEnabledA() <em>Enabled A</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnabledA()
   * @generated
   * @ordered
   */
  protected AtomicBoolean enabledA = ENABLED_A_EDEFAULT;

  /**
   * The cached value of the '{@link #getTinkerforgeDevice() <em>Tinkerforge Device</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTinkerforgeDevice()
   * @generated
   * @ordered
   */
  protected BrickletPiezoSpeaker tinkerforgeDevice;

  /**
   * The default value of the '{@link #getIpConnection() <em>Ip Connection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIpConnection()
   * @generated
   * @ordered
   */
  protected static final IPConnection IP_CONNECTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getIpConnection() <em>Ip Connection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIpConnection()
   * @generated
   * @ordered
   */
  protected IPConnection ipConnection = IP_CONNECTION_EDEFAULT;

  /**
   * The default value of the '{@link #getConnectedUid() <em>Connected Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectedUid()
   * @generated
   * @ordered
   */
  protected static final String CONNECTED_UID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getConnectedUid() <em>Connected Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectedUid()
   * @generated
   * @ordered
   */
  protected String connectedUid = CONNECTED_UID_EDEFAULT;

  /**
   * The default value of the '{@link #getPosition() <em>Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPosition()
   * @generated
   * @ordered
   */
  protected static final char POSITION_EDEFAULT = '\u0000';

  /**
   * The cached value of the '{@link #getPosition() <em>Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPosition()
   * @generated
   * @ordered
   */
  protected char position = POSITION_EDEFAULT;

  /**
   * The default value of the '{@link #getDeviceIdentifier() <em>Device Identifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceIdentifier()
   * @generated
   * @ordered
   */
  protected static final int DEVICE_IDENTIFIER_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getDeviceIdentifier() <em>Device Identifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceIdentifier()
   * @generated
   * @ordered
   */
  protected int deviceIdentifier = DEVICE_IDENTIFIER_EDEFAULT;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getSwitchState() <em>Switch State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSwitchState()
   * @generated
   * @ordered
   */
  protected static final OnOffValue SWITCH_STATE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSwitchState() <em>Switch State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSwitchState()
   * @generated
   * @ordered
   */
  protected OnOffValue switchState = SWITCH_STATE_EDEFAULT;

  /**
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "bricklet_piezo_speaker";

  private static final String DURATIONS = "durations";

  private static final String FREQUENCIES = "frequencies";

  private static final String REPEAT = "repeat";

  private static final String MORSECODES = "morsecodes";

  /**
   * The cached value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected String deviceType = DEVICE_TYPE_EDEFAULT;

  private BeepFinishedListener beepFinishedListener;

  private MorseFinishedListener morseFinishedListener;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MBrickletPiezoSpeakerImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return ModelPackage.Literals.MBRICKLET_PIEZO_SPEAKER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Logger getLogger()
  {
    return logger;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLogger(Logger newLogger)
  {
    Logger oldLogger = logger;
    logger = newLogger;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_PIEZO_SPEAKER__LOGGER, oldLogger, logger));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUid()
  {
    return uid;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUid(String newUid)
  {
    String oldUid = uid;
    uid = newUid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_PIEZO_SPEAKER__UID, oldUid, uid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isPoll()
  {
    return poll;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPoll(boolean newPoll)
  {
    boolean oldPoll = poll;
    poll = newPoll;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_PIEZO_SPEAKER__POLL, oldPoll, poll));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AtomicBoolean getEnabledA()
  {
    return enabledA;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEnabledA(AtomicBoolean newEnabledA)
  {
    AtomicBoolean oldEnabledA = enabledA;
    enabledA = newEnabledA;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_PIEZO_SPEAKER__ENABLED_A, oldEnabledA, enabledA));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletPiezoSpeaker getTinkerforgeDevice()
  {
    return tinkerforgeDevice;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTinkerforgeDevice(BrickletPiezoSpeaker newTinkerforgeDevice)
  {
    BrickletPiezoSpeaker oldTinkerforgeDevice = tinkerforgeDevice;
    tinkerforgeDevice = newTinkerforgeDevice;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_PIEZO_SPEAKER__TINKERFORGE_DEVICE, oldTinkerforgeDevice, tinkerforgeDevice));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IPConnection getIpConnection()
  {
    return ipConnection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIpConnection(IPConnection newIpConnection)
  {
    IPConnection oldIpConnection = ipConnection;
    ipConnection = newIpConnection;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_PIEZO_SPEAKER__IP_CONNECTION, oldIpConnection, ipConnection));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getConnectedUid()
  {
    return connectedUid;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setConnectedUid(String newConnectedUid)
  {
    String oldConnectedUid = connectedUid;
    connectedUid = newConnectedUid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_PIEZO_SPEAKER__CONNECTED_UID, oldConnectedUid, connectedUid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public char getPosition()
  {
    return position;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPosition(char newPosition)
  {
    char oldPosition = position;
    position = newPosition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_PIEZO_SPEAKER__POSITION, oldPosition, position));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getDeviceIdentifier()
  {
    return deviceIdentifier;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDeviceIdentifier(int newDeviceIdentifier)
  {
    int oldDeviceIdentifier = deviceIdentifier;
    deviceIdentifier = newDeviceIdentifier;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_PIEZO_SPEAKER__DEVICE_IDENTIFIER, oldDeviceIdentifier, deviceIdentifier));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_PIEZO_SPEAKER__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickd getBrickd()
  {
    if (eContainerFeatureID() != ModelPackage.MBRICKLET_PIEZO_SPEAKER__BRICKD) return null;
    return (MBrickd)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBrickd(MBrickd newBrickd, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newBrickd, ModelPackage.MBRICKLET_PIEZO_SPEAKER__BRICKD, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setBrickd(MBrickd newBrickd)
  {
    if (newBrickd != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MBRICKLET_PIEZO_SPEAKER__BRICKD && newBrickd != null))
    {
      if (EcoreUtil.isAncestor(this, newBrickd))
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
        msgs = eBasicRemoveFromContainer(msgs);
      if (newBrickd != null)
        msgs = ((InternalEObject)newBrickd).eInverseAdd(this, ModelPackage.MBRICKD__MDEVICES, MBrickd.class, msgs);
      msgs = basicSetBrickd(newBrickd, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_PIEZO_SPEAKER__BRICKD, newBrickd, newBrickd));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OnOffValue getSwitchState()
  {
    return switchState;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSwitchState(OnOffValue newSwitchState)
  {
    OnOffValue oldSwitchState = switchState;
    switchState = newSwitchState;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_PIEZO_SPEAKER__SWITCH_STATE, oldSwitchState, switchState));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDeviceType()
  {
    return deviceType;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void turnSwitch(OnOffValue state, DeviceOptions opts)
  {
    if (state == OnOffValue.ON) {
      beep(opts);
    } else {
      stopBeep();
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void fetchSwitchState()
  {
    // nothing to do
  }

  private enum Mode {
    BEEP, MORSE
  }

  private Long[] getDurations(String durationsopt) {
    if (durationsopt == null) {
      return null;
    }
    Long[] durations = null;
    logger.debug("durationsopt: {}", durationsopt);
    String[] numbers = durationsopt.split("\\|");
    for (int i = 0; i < numbers.length; i++) {
      logger.debug("duration number {}", numbers[i]);
    }
    durations = new Long[numbers.length];
    for (int i = 0; i < numbers.length; i++) {
      durations[i] = new Long(numbers[i]);
    }
    return durations;
  }

  private Integer[] getFrequencies(String frequenciesopt) {
    Integer[] frequencies = null;
    logger.debug("frequenciesopt {}", frequenciesopt);
    String[] numbers = frequenciesopt.split("\\|");
    frequencies = new Integer[numbers.length];
    for (int i = 0; i < numbers.length; i++) {
      frequencies[i] = new Integer(numbers[i]);
    }
    return frequencies;
  }

  private String[] getMorseCodes(String morseCodeOpt) {
    String[] morseCodes = null;
    logger.debug("morseCodeOpt {}", morseCodeOpt);
    morseCodes = morseCodeOpt.split("\\|");
    return morseCodes;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  private void beep(DeviceOptions opts) {
    try {
      Long[] durations = null;
      Integer[] frequencies = null;
      String[] morseCodes = null;
      Long repeats = null;
      Mode mode;
      if (beepFinishedListener != null) {
        logger.trace("removing beepFinishedListener");
        tinkerforgeDevice.removeBeepFinishedListener(beepFinishedListener);
        beepFinishedListener = null;
        tinkerforgeDevice.beep(0, 0);
      }
      if (morseFinishedListener != null) {
        logger.trace("removing morseFinishedListener");
        tinkerforgeDevice.removeMorseCodeFinishedListener(morseFinishedListener);
        morseFinishedListener = null;
        tinkerforgeDevice.beep(0, 0);
      }
      if (opts != null) {
        String modestr = Tools.getStringOpt(MODE, opts);
        if (modestr == null) {
          logger.error("mode is missing");
          return;
        }
        if (modestr.toLowerCase().equals(BEEP)) {
          mode = Mode.BEEP;
          durations = getDurations(Tools.getStringOpt(DURATIONS, opts));
          frequencies = getFrequencies(Tools.getStringOpt(FREQUENCIES, opts));
          repeats = Tools.getLongOpt(REPEAT, opts, 1L);
          logger.debug("repeats {}", repeats);
          if (durations == null || frequencies == null) {
            logger.error("durations and frequencies have values when mode is beep");
            return;
          } else if (durations.length != frequencies.length) {
            logger
                .error(
                    "every frequence needs a duration value and vice versa: frequencies count {}, durationscount {}",
                    frequencies.length, durations.length);
            return;
          }
        } else if (modestr.toLowerCase().equals(MORSE)) {
          mode = Mode.MORSE;
          frequencies = getFrequencies(Tools.getStringOpt(FREQUENCIES, opts));
          morseCodes = getMorseCodes(Tools.getStringOpt(MORSECODES, opts));
          repeats = Tools.getLongOpt(REPEAT, opts, 1L);
          logger.debug("repeats {}", repeats);
          if (morseCodes == null || frequencies == null) {
            logger.error("morseCodes and frequencies have values when mode is morse");
            return;
          } else if (morseCodes.length != frequencies.length) {
            logger
                .error(
                    "every morseCodes needs a frequency value and vice versa: morseCodes count {}, frequencies {}",
                    morseCodes.length, frequencies.length);
            return;
          }
        } else {
          logger.error("unknown mode string {}", modestr);
          return;
        }
      } else {
        logger.error("item configuration is missing");
        return;
      }

      if (mode == Mode.BEEP) {
        logger.debug("sending beeps");
        logger.debug("adding new BeepFinishedListener");
        beepFinishedListener = new BeepFinishedListener(this, durations, frequencies, repeats);
        tinkerforgeDevice.addBeepFinishedListener(beepFinishedListener);
        logger.debug("trigger beeping");
        tinkerforgeDevice.beep(durations[0], frequencies[0]);
      } else {
        logger.debug("sending morse codes");
        logger.debug("adding new MorseFinishedListener");
        morseFinishedListener = new MorseFinishedListener(this, morseCodes, frequencies, repeats);
        tinkerforgeDevice.addMorseCodeFinishedListener(morseFinishedListener);
        logger.debug("trigger beeping");
        // default morse code
        logger.debug(" morse code");
        tinkerforgeDevice.morseCode(morseCodes[0], frequencies[0]);
      }
    } catch (TimeoutException e) {
      TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
    } catch (NotConnectedException e) {
      TinkerforgeErrorHandler.handleError(this,
          TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
    }
  }

  private class BeepFinishedListener implements BrickletPiezoSpeaker.BeepFinishedListener {
    Long[] durations;
    Integer[] frequencies;
    Integer currentTone = 1;
    Integer sequenceLength;
    MBrickletPiezoSpeakerImpl mbricklet;
    private long repeats;
    private long round = 0;

    public BeepFinishedListener(MBrickletPiezoSpeakerImpl mbricklet, Long[] durations,
        Integer[] frequencies, long repeats) {
      this.durations = durations;
      this.frequencies = frequencies;
      this.mbricklet = mbricklet;
      this.repeats = repeats;
      this.sequenceLength = frequencies.length;
    }

    @Override
    public void beepFinished() {
      try {
        logger.debug("currentTone {}", currentTone);
        if (currentTone >= sequenceLength) {
          round++;
          logger.debug("round {} repeat {}", round, repeats);
          if (round < repeats) {
            currentTone = 0; // for repeats currentTone must be 0 because the initial Tone was send
                             // from outside of the listener
          } else {
            // we are done
            logger.debug("beep done");
            setSwitchState(OnOffValue.OFF);
            return;
          }
        } else {
          logger.debug("currenTone {}, sequenceLength {}", currentTone, sequenceLength);
        }
        logger.debug("beep duration {} frequency {}", durations[currentTone],
            frequencies[currentTone]);
        tinkerforgeDevice.beep(durations[currentTone], frequencies[currentTone]);
        currentTone++;
      } catch (TimeoutException e) {
        TinkerforgeErrorHandler.handleError(mbricklet,
            TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
      } catch (NotConnectedException e) {
        TinkerforgeErrorHandler.handleError(mbricklet,
            TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
      }
    }
  }

  private class MorseFinishedListener
      implements
        BrickletPiezoSpeaker.MorseCodeFinishedListener {
    Integer[] frequencies;
    String[] morseCodes;
    Integer currentSequence = 1;
    Integer sequenceLength;
    MBrickletPiezoSpeakerImpl mbricklet;
    private long repeats;
    private long round = 0;

    public MorseFinishedListener(MBrickletPiezoSpeakerImpl mbricklet, String[] morseCodes,
        Integer[] frequencies, long repeats) {
      this.frequencies = frequencies;
      this.morseCodes = morseCodes;
      this.mbricklet = mbricklet;
      this.repeats = repeats;
      this.sequenceLength = frequencies.length;
    }

    @Override
    public void morseCodeFinished() {
      try {
        logger.debug("currentSequence {}", currentSequence);
        if (currentSequence >= sequenceLength) {
          round++;
          logger.debug("round {} repeat {}", round, repeats);
          if (round < repeats) {
            currentSequence = 0; // for repeats currentSequence must be 0 because the initial Tone
                                 // was send
                             // from outside of the listener
          } else {
            // we are done
            logger.debug("beep done");
            setSwitchState(OnOffValue.OFF);
            return;
          }
        } else {
          logger.debug("currentSequence {}, sequenceLength {}", currentSequence, sequenceLength);
        }
        logger.debug("morse code {} frequency {}", morseCodes[currentSequence],
            frequencies[currentSequence]);
        tinkerforgeDevice.morseCode(morseCodes[currentSequence], frequencies[currentSequence]);
        currentSequence++;
      } catch (TimeoutException e) {
        TinkerforgeErrorHandler.handleError(mbricklet,
            TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
      } catch (NotConnectedException e) {
        TinkerforgeErrorHandler.handleError(mbricklet,
            TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
      }
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void init()
  {
    setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(MBrickletPiezoSpeakerImpl.class);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void enable()
  {
    tinkerforgeDevice = new BrickletPiezoSpeaker(getUid(), getIpConnection());
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void disable() {
    stopBeep();
    tinkerforgeDevice = null;
  }

  private void stopBeep() {
    if (beepFinishedListener != null) {
      tinkerforgeDevice.removeBeepFinishedListener(beepFinishedListener);
      beepFinishedListener = null;
    }
    if (morseFinishedListener != null) {
      tinkerforgeDevice.removeMorseCodeFinishedListener(morseFinishedListener);
      morseFinishedListener = null;
    }
    try {
      // stop beep
      tinkerforgeDevice.beep(0, 0);
    } catch (TimeoutException e) {
      TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
    } catch (NotConnectedException e) {
      TinkerforgeErrorHandler.handleError(this,
          TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__BRICKD:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetBrickd((MBrickd)otherEnd, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__BRICKD:
        return basicSetBrickd(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs)
  {
    switch (eContainerFeatureID())
    {
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__BRICKD:
        return eInternalContainer().eInverseRemove(this, ModelPackage.MBRICKD__MDEVICES, MBrickd.class, msgs);
    }
    return super.eBasicRemoveFromContainerFeature(msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__LOGGER:
        return getLogger();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__UID:
        return getUid();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__POLL:
        return isPoll();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__ENABLED_A:
        return getEnabledA();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__TINKERFORGE_DEVICE:
        return getTinkerforgeDevice();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__IP_CONNECTION:
        return getIpConnection();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__CONNECTED_UID:
        return getConnectedUid();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__POSITION:
        return getPosition();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__DEVICE_IDENTIFIER:
        return getDeviceIdentifier();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__NAME:
        return getName();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__BRICKD:
        return getBrickd();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__SWITCH_STATE:
        return getSwitchState();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__DEVICE_TYPE:
        return getDeviceType();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__POLL:
        setPoll((Boolean)newValue);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__TINKERFORGE_DEVICE:
        setTinkerforgeDevice((BrickletPiezoSpeaker)newValue);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__IP_CONNECTION:
        setIpConnection((IPConnection)newValue);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__CONNECTED_UID:
        setConnectedUid((String)newValue);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__POSITION:
        setPosition((Character)newValue);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__DEVICE_IDENTIFIER:
        setDeviceIdentifier((Integer)newValue);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__NAME:
        setName((String)newValue);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__BRICKD:
        setBrickd((MBrickd)newValue);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__SWITCH_STATE:
        setSwitchState((OnOffValue)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__POLL:
        setPoll(POLL_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__TINKERFORGE_DEVICE:
        setTinkerforgeDevice((BrickletPiezoSpeaker)null);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__IP_CONNECTION:
        setIpConnection(IP_CONNECTION_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__CONNECTED_UID:
        setConnectedUid(CONNECTED_UID_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__POSITION:
        setPosition(POSITION_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__DEVICE_IDENTIFIER:
        setDeviceIdentifier(DEVICE_IDENTIFIER_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__BRICKD:
        setBrickd((MBrickd)null);
        return;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__SWITCH_STATE:
        setSwitchState(SWITCH_STATE_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__POLL:
        return poll != POLL_EDEFAULT;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__TINKERFORGE_DEVICE:
        return tinkerforgeDevice != null;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__IP_CONNECTION:
        return IP_CONNECTION_EDEFAULT == null ? ipConnection != null : !IP_CONNECTION_EDEFAULT.equals(ipConnection);
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__CONNECTED_UID:
        return CONNECTED_UID_EDEFAULT == null ? connectedUid != null : !CONNECTED_UID_EDEFAULT.equals(connectedUid);
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__POSITION:
        return position != POSITION_EDEFAULT;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__DEVICE_IDENTIFIER:
        return deviceIdentifier != DEVICE_IDENTIFIER_EDEFAULT;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__BRICKD:
        return getBrickd() != null;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__SWITCH_STATE:
        return SWITCH_STATE_EDEFAULT == null ? switchState != null : !SWITCH_STATE_EDEFAULT.equals(switchState);
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass)
  {
    if (baseClass == SwitchSensor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICKLET_PIEZO_SPEAKER__SWITCH_STATE: return ModelPackage.SWITCH_SENSOR__SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == ProgrammableSwitchActor.class)
    {
      switch (derivedFeatureID)
      {
        default: return -1;
      }
    }
    return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass)
  {
    if (baseClass == SwitchSensor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.SWITCH_SENSOR__SWITCH_STATE: return ModelPackage.MBRICKLET_PIEZO_SPEAKER__SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == ProgrammableSwitchActor.class)
    {
      switch (baseFeatureID)
      {
        default: return -1;
      }
    }
    return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eDerivedOperationID(int baseOperationID, Class<?> baseClass)
  {
    if (baseClass == SwitchSensor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.SWITCH_SENSOR___FETCH_SWITCH_STATE: return ModelPackage.MBRICKLET_PIEZO_SPEAKER___FETCH_SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == ProgrammableSwitchActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.PROGRAMMABLE_SWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE_DEVICEOPTIONS: return ModelPackage.MBRICKLET_PIEZO_SPEAKER___TURN_SWITCH__ONOFFVALUE_DEVICEOPTIONS;
        default: return -1;
      }
    }
    return super.eDerivedOperationID(baseOperationID, baseClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER___TURN_SWITCH__ONOFFVALUE_DEVICEOPTIONS:
        turnSwitch((OnOffValue)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER___FETCH_SWITCH_STATE:
        fetchSwitchState();
        return null;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER___INIT:
        init();
        return null;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER___ENABLE:
        enable();
        return null;
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER___DISABLE:
        disable();
        return null;
    }
    return super.eInvoke(operationID, arguments);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (logger: ");
    result.append(logger);
    result.append(", uid: ");
    result.append(uid);
    result.append(", poll: ");
    result.append(poll);
    result.append(", enabledA: ");
    result.append(enabledA);
    result.append(", tinkerforgeDevice: ");
    result.append(tinkerforgeDevice);
    result.append(", ipConnection: ");
    result.append(ipConnection);
    result.append(", connectedUid: ");
    result.append(connectedUid);
    result.append(", position: ");
    result.append(position);
    result.append(", deviceIdentifier: ");
    result.append(deviceIdentifier);
    result.append(", name: ");
    result.append(name);
    result.append(", switchState: ");
    result.append(switchState);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(')');
    return result.toString();
  }

} //MBrickletPiezoSpeakerImpl
