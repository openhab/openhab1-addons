/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.openhab.binding.tinkerforge.internal.model.Ecosystem;
import org.openhab.binding.tinkerforge.internal.model.GenericDevice;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.slf4j.Logger;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ecosystem</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.EcosystemImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.EcosystemImpl#getMbrickds <em>Mbrickds</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EcosystemImpl extends MinimalEObjectImpl.Container implements Ecosystem
{
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
   * The cached value of the '{@link #getMbrickds() <em>Mbrickds</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMbrickds()
   * @generated
   * @ordered
   */
  protected EList<MBrickd> mbrickds;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EcosystemImpl()
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
    return ModelPackage.Literals.ECOSYSTEM;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ECOSYSTEM__LOGGER, oldLogger, logger));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<MBrickd> getMbrickds()
  {
    if (mbrickds == null)
    {
      mbrickds = new EObjectContainmentWithInverseEList<MBrickd>(MBrickd.class, this, ModelPackage.ECOSYSTEM__MBRICKDS, ModelPackage.MBRICKD__ECOSYSTEM);
    }
    return mbrickds;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	public MBrickd getBrickd(String host, int port) {
		EList<MBrickd> _mbrickds = getMbrickds();
		for (final MBrickd mbrickd : _mbrickds) {
			if (mbrickd.getHost().equals(host) && mbrickd.getPort() == port)
				return mbrickd;
		}
		return null;
	}

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	public MBaseDevice getDevice(String uid, String subId) {
		EList<MBrickd> _mbrickds = getMbrickds();
		for (final MBrickd mbrickd : _mbrickds) {
			{
				final MBaseDevice mDevice = mbrickd.getDevice(uid);
				if (mDevice != null) {
					if (subId == null) {
						return mDevice;
					} else {
						if (mDevice instanceof MSubDeviceHolder) {
							final MSubDeviceHolder<?> mBrick = ((MSubDeviceHolder<?>) mDevice);
							EList<?> _msubdevices = mBrick.getMsubdevices();
							for (final Object ms : _msubdevices) // TODO: don't
																	// no why it
																	// is not a
																	// subdevice
							{
								if ((ms instanceof MSubDevice<?>)) {
									final MSubDevice<?> msubdevice = ((MSubDevice<?>) ms);
									String _subId = msubdevice.getSubId();
									if (_subId.equals(subId)) {
										return ((MBaseDevice) msubdevice);
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

  	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<MSubDevice<?>> getDevices4GenericId(String uid,
			String genericId) {
		EList<MSubDevice<?>> genericDevicesList = new BasicEList<MSubDevice<?>>();
		EList<MBrickd> _mbrickds = getMbrickds();
		for (final MBrickd mbrickd : _mbrickds) {
			{
				final MBaseDevice mDevice = mbrickd.getDevice(uid);
				if (mDevice != null) {
					if (mDevice instanceof MSubDeviceHolder) {
						final MSubDeviceHolder<?> mBrick = ((MSubDeviceHolder<?>) mDevice);
						EList<?> _msubdevices = mBrick.getMsubdevices();
						for (final Object mg : _msubdevices)
						{
							if (mg instanceof GenericDevice) {
								final GenericDevice mgenericdevice = ((GenericDevice) mg);
								String _genericId = mgenericdevice
										.getGenericDeviceId();
								if (_genericId.equals(genericId)) {
									genericDevicesList
											.add(((MSubDevice<?>) mgenericdevice));
								}
							}
						}
					}

				}
			}
		}
		return genericDevicesList;
	}

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void disconnect()
  {
    EList<MBrickd> _mbrickds = getMbrickds();
    for (final MBrickd mbrickd : _mbrickds)
    {
      mbrickd.disconnect();
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ModelPackage.ECOSYSTEM__MBRICKDS:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getMbrickds()).basicAdd(otherEnd, msgs);
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
      case ModelPackage.ECOSYSTEM__MBRICKDS:
        return ((InternalEList<?>)getMbrickds()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case ModelPackage.ECOSYSTEM__LOGGER:
        return getLogger();
      case ModelPackage.ECOSYSTEM__MBRICKDS:
        return getMbrickds();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ModelPackage.ECOSYSTEM__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.ECOSYSTEM__MBRICKDS:
        getMbrickds().clear();
        getMbrickds().addAll((Collection<? extends MBrickd>)newValue);
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
      case ModelPackage.ECOSYSTEM__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.ECOSYSTEM__MBRICKDS:
        getMbrickds().clear();
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
      case ModelPackage.ECOSYSTEM__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.ECOSYSTEM__MBRICKDS:
        return mbrickds != null && !mbrickds.isEmpty();
    }
    return super.eIsSet(featureID);
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
      case ModelPackage.ECOSYSTEM___GET_BRICKD__STRING_INT:
        return getBrickd((String)arguments.get(0), (Integer)arguments.get(1));
      case ModelPackage.ECOSYSTEM___GET_DEVICE__STRING_STRING:
        return getDevice((String)arguments.get(0), (String)arguments.get(1));
      case ModelPackage.ECOSYSTEM___GET_DEVICES4_GENERIC_ID__STRING_STRING:
        return getDevices4GenericId((String)arguments.get(0), (String)arguments.get(1));
      case ModelPackage.ECOSYSTEM___DISCONNECT:
        disconnect();
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
    result.append(')');
    return result.toString();
  }

} //EcosystemImpl
