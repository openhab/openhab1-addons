/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.sitemap.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.openhab.model.sitemap.List;
import org.openhab.model.sitemap.SitemapPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.model.sitemap.impl.ListImpl#getSeparator <em>Separator</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ListImpl extends WidgetImpl implements List
{
  /**
   * The default value of the '{@link #getSeparator() <em>Separator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSeparator()
   * @generated
   * @ordered
   */
  protected static final String SEPARATOR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSeparator() <em>Separator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSeparator()
   * @generated
   * @ordered
   */
  protected String separator = SEPARATOR_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ListImpl()
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
    return SitemapPackage.Literals.LIST;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSeparator()
  {
    return separator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSeparator(String newSeparator)
  {
    String oldSeparator = separator;
    separator = newSeparator;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SitemapPackage.LIST__SEPARATOR, oldSeparator, separator));
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
      case SitemapPackage.LIST__SEPARATOR:
        return getSeparator();
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
      case SitemapPackage.LIST__SEPARATOR:
        setSeparator((String)newValue);
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
      case SitemapPackage.LIST__SEPARATOR:
        setSeparator(SEPARATOR_EDEFAULT);
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
      case SitemapPackage.LIST__SEPARATOR:
        return SEPARATOR_EDEFAULT == null ? separator != null : !SEPARATOR_EDEFAULT.equals(separator);
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
    result.append(" (separator: ");
    result.append(separator);
    result.append(')');
    return result.toString();
  }

} //ListImpl
