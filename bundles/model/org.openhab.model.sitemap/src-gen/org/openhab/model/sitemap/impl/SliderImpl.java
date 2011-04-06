/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.model.sitemap.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.openhab.model.sitemap.SitemapPackage;
import org.openhab.model.sitemap.Slider;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Slider</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.model.sitemap.impl.SliderImpl#getFrequency <em>Frequency</em>}</li>
 *   <li>{@link org.openhab.model.sitemap.impl.SliderImpl#isSwitchEnabled <em>Switch Enabled</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SliderImpl extends WidgetImpl implements Slider
{
  /**
   * The default value of the '{@link #getFrequency() <em>Frequency</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFrequency()
   * @generated
   * @ordered
   */
  protected static final String FREQUENCY_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFrequency() <em>Frequency</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFrequency()
   * @generated
   * @ordered
   */
  protected String frequency = FREQUENCY_EDEFAULT;

  /**
   * The default value of the '{@link #isSwitchEnabled() <em>Switch Enabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSwitchEnabled()
   * @generated
   * @ordered
   */
  protected static final boolean SWITCH_ENABLED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isSwitchEnabled() <em>Switch Enabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSwitchEnabled()
   * @generated
   * @ordered
   */
  protected boolean switchEnabled = SWITCH_ENABLED_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SliderImpl()
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
    return SitemapPackage.Literals.SLIDER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getFrequency()
  {
    return frequency;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFrequency(String newFrequency)
  {
    String oldFrequency = frequency;
    frequency = newFrequency;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SitemapPackage.SLIDER__FREQUENCY, oldFrequency, frequency));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isSwitchEnabled()
  {
    return switchEnabled;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSwitchEnabled(boolean newSwitchEnabled)
  {
    boolean oldSwitchEnabled = switchEnabled;
    switchEnabled = newSwitchEnabled;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SitemapPackage.SLIDER__SWITCH_ENABLED, oldSwitchEnabled, switchEnabled));
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
      case SitemapPackage.SLIDER__FREQUENCY:
        return getFrequency();
      case SitemapPackage.SLIDER__SWITCH_ENABLED:
        return isSwitchEnabled();
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
      case SitemapPackage.SLIDER__FREQUENCY:
        setFrequency((String)newValue);
        return;
      case SitemapPackage.SLIDER__SWITCH_ENABLED:
        setSwitchEnabled((Boolean)newValue);
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
      case SitemapPackage.SLIDER__FREQUENCY:
        setFrequency(FREQUENCY_EDEFAULT);
        return;
      case SitemapPackage.SLIDER__SWITCH_ENABLED:
        setSwitchEnabled(SWITCH_ENABLED_EDEFAULT);
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
      case SitemapPackage.SLIDER__FREQUENCY:
        return FREQUENCY_EDEFAULT == null ? frequency != null : !FREQUENCY_EDEFAULT.equals(frequency);
      case SitemapPackage.SLIDER__SWITCH_ENABLED:
        return switchEnabled != SWITCH_ENABLED_EDEFAULT;
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
    result.append(" (frequency: ");
    result.append(frequency);
    result.append(", switchEnabled: ");
    result.append(switchEnabled);
    result.append(')');
    return result.toString();
  }

} //SliderImpl
