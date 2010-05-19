/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.sitemap.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

import org.openhab.model.sitemap.SitemapPackage;
import org.openhab.model.sitemap.Switch;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Switch</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.model.sitemap.impl.SwitchImpl#getItem <em>Item</em>}</li>
 *   <li>{@link org.openhab.model.sitemap.impl.SwitchImpl#getButtonLabels <em>Button Labels</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SwitchImpl extends WidgetImpl implements Switch
{
  /**
   * The default value of the '{@link #getItem() <em>Item</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getItem()
   * @generated
   * @ordered
   */
  protected static final String ITEM_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getItem() <em>Item</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getItem()
   * @generated
   * @ordered
   */
  protected String item = ITEM_EDEFAULT;

  /**
   * The cached value of the '{@link #getButtonLabels() <em>Button Labels</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getButtonLabels()
   * @generated
   * @ordered
   */
  protected EList<String> buttonLabels;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SwitchImpl()
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
    return SitemapPackage.Literals.SWITCH;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getItem()
  {
    return item;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setItem(String newItem)
  {
    String oldItem = item;
    item = newItem;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SitemapPackage.SWITCH__ITEM, oldItem, item));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getButtonLabels()
  {
    if (buttonLabels == null)
    {
      buttonLabels = new EDataTypeEList<String>(String.class, this, SitemapPackage.SWITCH__BUTTON_LABELS);
    }
    return buttonLabels;
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
      case SitemapPackage.SWITCH__ITEM:
        return getItem();
      case SitemapPackage.SWITCH__BUTTON_LABELS:
        return getButtonLabels();
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
      case SitemapPackage.SWITCH__ITEM:
        setItem((String)newValue);
        return;
      case SitemapPackage.SWITCH__BUTTON_LABELS:
        getButtonLabels().clear();
        getButtonLabels().addAll((Collection<? extends String>)newValue);
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
      case SitemapPackage.SWITCH__ITEM:
        setItem(ITEM_EDEFAULT);
        return;
      case SitemapPackage.SWITCH__BUTTON_LABELS:
        getButtonLabels().clear();
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
      case SitemapPackage.SWITCH__ITEM:
        return ITEM_EDEFAULT == null ? item != null : !ITEM_EDEFAULT.equals(item);
      case SitemapPackage.SWITCH__BUTTON_LABELS:
        return buttonLabels != null && !buttonLabels.isEmpty();
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
    result.append(" (item: ");
    result.append(item);
    result.append(", buttonLabels: ");
    result.append(buttonLabels);
    result.append(')');
    return result.toString();
  }

} //SwitchImpl
