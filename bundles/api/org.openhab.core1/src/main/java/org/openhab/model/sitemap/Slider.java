/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
/**
 */
package org.openhab.model.sitemap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slider</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.openhab.model.sitemap.Slider#getFrequency <em>Frequency</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Slider#isSwitchEnabled <em>Switch Enabled</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getSlider()
 * @model
 * @author Kai Kreuzer - Initial contribution
 */
public interface Slider extends NonLinkableWidget {
    /**
     * Returns the value of the '<em><b>Frequency</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Frequency</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Frequency</em>' attribute.
     * @see #setFrequency(int)
     * @see org.openhab.model.sitemap.SitemapPackage#getSlider_Frequency()
     * @model
     */
    int getFrequency();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Slider#getFrequency <em>Frequency</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Frequency</em>' attribute.
     * @see #getFrequency()
     */
    void setFrequency(int value);

    /**
     * Returns the value of the '<em><b>Switch Enabled</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Switch Enabled</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Switch Enabled</em>' attribute.
     * @see #setSwitchEnabled(boolean)
     * @see org.openhab.model.sitemap.SitemapPackage#getSlider_SwitchEnabled()
     * @model
     */
    boolean isSwitchEnabled();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Slider#isSwitchEnabled <em>Switch Enabled</em>}'
     * attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Switch Enabled</em>' attribute.
     * @see #isSwitchEnabled()
     */
    void setSwitchEnabled(boolean value);

} // Slider
