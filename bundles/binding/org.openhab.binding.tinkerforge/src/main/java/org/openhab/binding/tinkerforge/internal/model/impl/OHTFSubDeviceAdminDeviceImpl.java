/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.OHTFDevice;
import org.openhab.binding.tinkerforge.internal.model.OHTFSubDeviceAdminDevice;
import org.openhab.binding.tinkerforge.internal.model.TFConfig;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>OHTF Sub Device Admin Device</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class OHTFSubDeviceAdminDeviceImpl<TFC extends TFConfig, IDS extends Enum> extends OHTFDeviceImpl<TFC, IDS> implements OHTFSubDeviceAdminDevice<TFC, IDS>
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OHTFSubDeviceAdminDeviceImpl()
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
    return ModelPackage.Literals.OHTF_SUB_DEVICE_ADMIN_DEVICE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isValidSubId(String subId)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eDerivedOperationID(int baseOperationID, Class<?> baseClass)
  {
    if (baseClass == OHTFDevice.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.OHTF_DEVICE___IS_VALID_SUB_ID__STRING: return ModelPackage.OHTF_SUB_DEVICE_ADMIN_DEVICE___IS_VALID_SUB_ID__STRING;
        default: return super.eDerivedOperationID(baseOperationID, baseClass);
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
      case ModelPackage.OHTF_SUB_DEVICE_ADMIN_DEVICE___IS_VALID_SUB_ID__STRING:
        return isValidSubId((String)arguments.get(0));
    }
    return super.eInvoke(operationID, arguments);
  }

} //OHTFSubDeviceAdminDeviceImpl
