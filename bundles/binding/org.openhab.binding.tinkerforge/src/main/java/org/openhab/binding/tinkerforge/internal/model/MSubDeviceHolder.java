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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBrick</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.3.0
 *        <!-- end-user-doc -->
 *
 *        <p>
 *        The following features are supported:
 *        </p>
 *        <ul>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder#getMsubdevices
 *        <em>Msubdevices</em>}</li>
 *        </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSubDeviceHolder()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MSubDeviceHolder<S extends MSubDevice<?>> extends EObject {
    /**
     * Returns the value of the '<em><b>Msubdevices</b></em>' containment reference list.
     * It is bidirectional and its opposite is
     * '{@link org.openhab.binding.tinkerforge.internal.model.MSubDevice#getMbrick <em>Mbrick</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Msubdevices</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Msubdevices</em>' containment reference list.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSubDeviceHolder_Msubdevices()
     * @see org.openhab.binding.tinkerforge.internal.model.MSubDevice#getMbrick
     * @model opposite="mbrick" containment="true"
     * @generated
     */
    EList<S> getMsubdevices();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model
     * @generated
     */
    void initSubDevices();

} // MBrick
