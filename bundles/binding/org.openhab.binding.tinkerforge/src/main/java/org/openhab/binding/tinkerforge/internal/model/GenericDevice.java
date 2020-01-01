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
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generic Device</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.4.0
 *        <!-- end-user-doc -->
 *
 *        <p>
 *        The following features are supported:
 *        </p>
 *        <ul>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.GenericDevice#getGenericDeviceId <em>Generic Device
 *        Id</em>}</li>
 *        </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getGenericDevice()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface GenericDevice extends EObject {
    /**
     * Returns the value of the '<em><b>Generic Device Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Generic Device Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Generic Device Id</em>' attribute.
     * @see #setGenericDeviceId(String)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getGenericDevice_GenericDeviceId()
     * @model unique="false"
     * @generated
     */
    String getGenericDeviceId();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.GenericDevice#getGenericDeviceId
     * <em>Generic Device Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Generic Device Id</em>' attribute.
     * @see #getGenericDeviceId()
     * @generated
     */
    void setGenericDeviceId(String value);

} // GenericDevice
