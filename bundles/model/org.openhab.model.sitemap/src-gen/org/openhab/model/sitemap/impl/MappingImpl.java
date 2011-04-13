/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.sitemap.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.openhab.model.sitemap.Mapping;
import org.openhab.model.sitemap.SitemapPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mapping</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.model.sitemap.impl.MappingImpl#getCmd <em>Cmd</em>}</li>
 *   <li>{@link org.openhab.model.sitemap.impl.MappingImpl#getLabel <em>Label</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MappingImpl extends MinimalEObjectImpl.Container implements Mapping
{
  /**
   * The default value of the '{@link #getCmd() <em>Cmd</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCmd()
   * @generated
   * @ordered
   */
  protected static final String CMD_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getCmd() <em>Cmd</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCmd()
   * @generated
   * @ordered
   */
  protected String cmd = CMD_EDEFAULT;

  /**
   * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLabel()
   * @generated
   * @ordered
   */
  protected static final String LABEL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLabel()
   * @generated
   * @ordered
   */
  protected String label = LABEL_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MappingImpl()
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
    return SitemapPackage.Literals.MAPPING;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getCmd()
  {
    return cmd;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCmd(String newCmd)
  {
    String oldCmd = cmd;
    cmd = newCmd;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SitemapPackage.MAPPING__CMD, oldCmd, cmd));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLabel()
  {
    return label;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLabel(String newLabel)
  {
    String oldLabel = label;
    label = newLabel;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SitemapPackage.MAPPING__LABEL, oldLabel, label));
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
      case SitemapPackage.MAPPING__CMD:
        return getCmd();
      case SitemapPackage.MAPPING__LABEL:
        return getLabel();
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
      case SitemapPackage.MAPPING__CMD:
        setCmd((String)newValue);
        return;
      case SitemapPackage.MAPPING__LABEL:
        setLabel((String)newValue);
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
      case SitemapPackage.MAPPING__CMD:
        setCmd(CMD_EDEFAULT);
        return;
      case SitemapPackage.MAPPING__LABEL:
        setLabel(LABEL_EDEFAULT);
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
      case SitemapPackage.MAPPING__CMD:
        return CMD_EDEFAULT == null ? cmd != null : !CMD_EDEFAULT.equals(cmd);
      case SitemapPackage.MAPPING__LABEL:
        return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
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
    result.append(" (cmd: ");
    result.append(cmd);
    result.append(", label: ");
    result.append(label);
    result.append(')');
    return result.toString();
  }

} //MappingImpl
