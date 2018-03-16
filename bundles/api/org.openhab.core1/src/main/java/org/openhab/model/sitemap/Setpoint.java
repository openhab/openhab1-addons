/**
 * Copyright (c) 2015-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 */
package org.openhab.model.sitemap;

import java.math.BigDecimal;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Setpoint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.openhab.model.sitemap.Setpoint#getMinValue <em>Min Value</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Setpoint#getMaxValue <em>Max Value</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Setpoint#getStep <em>Step</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getSetpoint()
 * @model
 * @author Kai Kreuzer - Initial contribution
 */
public interface Setpoint extends NonLinkableWidget {
    /**
     * Returns the value of the '<em><b>Min Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Min Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Min Value</em>' attribute.
     * @see #setMinValue(BigDecimal)
     * @see org.openhab.model.sitemap.SitemapPackage#getSetpoint_MinValue()
     * @model
     */
    BigDecimal getMinValue();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Setpoint#getMinValue <em>Min Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Min Value</em>' attribute.
     * @see #getMinValue()
     */
    void setMinValue(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Max Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Max Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Max Value</em>' attribute.
     * @see #setMaxValue(BigDecimal)
     * @see org.openhab.model.sitemap.SitemapPackage#getSetpoint_MaxValue()
     * @model
     */
    BigDecimal getMaxValue();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Setpoint#getMaxValue <em>Max Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Max Value</em>' attribute.
     * @see #getMaxValue()
     */
    void setMaxValue(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Step</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Step</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Step</em>' attribute.
     * @see #setStep(BigDecimal)
     * @see org.openhab.model.sitemap.SitemapPackage#getSetpoint_Step()
     * @model
     */
    BigDecimal getStep();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Setpoint#getStep <em>Step</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Step</em>' attribute.
     * @see #getStep()
     */
    void setStep(BigDecimal value);

} // Setpoint
