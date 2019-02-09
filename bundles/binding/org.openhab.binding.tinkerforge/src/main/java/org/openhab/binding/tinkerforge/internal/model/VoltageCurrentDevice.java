/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Voltage Current Device</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.5.0
 *        <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getVoltageCurrentDevice()
 * @model interface="true" abstract="true"
 *        superTypes="org.openhab.binding.tinkerforge.internal.model.MSensor&lt;org.openhab.binding.tinkerforge.internal.model.MDecimalValue&gt;
 *        org.openhab.binding.tinkerforge.internal.model.MSubDevice&lt;org.openhab.binding.tinkerforge.internal.model.MBrickletVoltageCurrent&gt;
 *        org.openhab.binding.tinkerforge.internal.model.CallbackListener
 *        org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer&lt;org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration&gt;"
 * @generated
 */
public interface VoltageCurrentDevice extends MSensor<DecimalValue>, MSubDevice<MBrickletVoltageCurrent>,
        CallbackListener, MTFConfigConsumer<TFBaseConfiguration> {
} // VoltageCurrentDevice
