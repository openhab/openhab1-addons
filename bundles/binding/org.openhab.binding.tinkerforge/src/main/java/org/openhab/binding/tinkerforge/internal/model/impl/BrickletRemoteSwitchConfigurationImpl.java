/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.openhab.binding.tinkerforge.internal.model.BrickletRemoteSwitchConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bricklet Remote Switch Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.BrickletRemoteSwitchConfigurationImpl#getTypeADevices <em>Type ADevices</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.BrickletRemoteSwitchConfigurationImpl#getTypeBDevices <em>Type BDevices</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.BrickletRemoteSwitchConfigurationImpl#getTypeCDevices <em>Type CDevices</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BrickletRemoteSwitchConfigurationImpl extends MinimalEObjectImpl.Container implements BrickletRemoteSwitchConfiguration
{
  /**
   * The default value of the '{@link #getTypeADevices() <em>Type ADevices</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTypeADevices()
   * @generated
   * @ordered
   */
  protected static final String TYPE_ADEVICES_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTypeADevices() <em>Type ADevices</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTypeADevices()
   * @generated
   * @ordered
   */
  protected String typeADevices = TYPE_ADEVICES_EDEFAULT;

  /**
   * The default value of the '{@link #getTypeBDevices() <em>Type BDevices</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTypeBDevices()
   * @generated
   * @ordered
   */
  protected static final String TYPE_BDEVICES_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTypeBDevices() <em>Type BDevices</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTypeBDevices()
   * @generated
   * @ordered
   */
  protected String typeBDevices = TYPE_BDEVICES_EDEFAULT;

  /**
   * The default value of the '{@link #getTypeCDevices() <em>Type CDevices</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTypeCDevices()
   * @generated
   * @ordered
   */
  protected static final String TYPE_CDEVICES_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTypeCDevices() <em>Type CDevices</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTypeCDevices()
   * @generated
   * @ordered
   */
  protected String typeCDevices = TYPE_CDEVICES_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BrickletRemoteSwitchConfigurationImpl()
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
    return ModelPackage.Literals.BRICKLET_REMOTE_SWITCH_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getTypeADevices()
  {
    return typeADevices;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTypeADevices(String newTypeADevices)
  {
    String oldTypeADevices = typeADevices;
    typeADevices = newTypeADevices;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_ADEVICES, oldTypeADevices, typeADevices));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getTypeBDevices()
  {
    return typeBDevices;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTypeBDevices(String newTypeBDevices)
  {
    String oldTypeBDevices = typeBDevices;
    typeBDevices = newTypeBDevices;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_BDEVICES, oldTypeBDevices, typeBDevices));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getTypeCDevices()
  {
    return typeCDevices;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTypeCDevices(String newTypeCDevices)
  {
    String oldTypeCDevices = typeCDevices;
    typeCDevices = newTypeCDevices;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_CDEVICES, oldTypeCDevices, typeCDevices));
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
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_ADEVICES:
        return getTypeADevices();
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_BDEVICES:
        return getTypeBDevices();
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_CDEVICES:
        return getTypeCDevices();
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
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_ADEVICES:
        setTypeADevices((String)newValue);
        return;
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_BDEVICES:
        setTypeBDevices((String)newValue);
        return;
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_CDEVICES:
        setTypeCDevices((String)newValue);
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
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_ADEVICES:
        setTypeADevices(TYPE_ADEVICES_EDEFAULT);
        return;
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_BDEVICES:
        setTypeBDevices(TYPE_BDEVICES_EDEFAULT);
        return;
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_CDEVICES:
        setTypeCDevices(TYPE_CDEVICES_EDEFAULT);
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
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_ADEVICES:
        return TYPE_ADEVICES_EDEFAULT == null ? typeADevices != null : !TYPE_ADEVICES_EDEFAULT.equals(typeADevices);
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_BDEVICES:
        return TYPE_BDEVICES_EDEFAULT == null ? typeBDevices != null : !TYPE_BDEVICES_EDEFAULT.equals(typeBDevices);
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_CDEVICES:
        return TYPE_CDEVICES_EDEFAULT == null ? typeCDevices != null : !TYPE_CDEVICES_EDEFAULT.equals(typeCDevices);
    }
    return super.eIsSet(featureID);
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
    result.append(" (typeADevices: ");
    result.append(typeADevices);
    result.append(", typeBDevices: ");
    result.append(typeBDevices);
    result.append(", typeCDevices: ");
    result.append(typeCDevices);
    result.append(')');
    return result.toString();
  }

} //BrickletRemoteSwitchConfigurationImpl
