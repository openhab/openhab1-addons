/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Voltage Current Device</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getVoltageCurrentDevice()
 * @model interface="true" abstract="true" superTypes="org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.MDecimalValue> org.openhab.binding.tinkerforge.internal.model.MSubDevice<org.openhab.binding.tinkerforge.internal.model.MBrickletVoltageCurrent> org.openhab.binding.tinkerforge.internal.model.CallbackListener org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration>"
 * @generated
 */
public interface VoltageCurrentDevice extends MSensor<DecimalValue>, MSubDevice<MBrickletVoltageCurrent>, CallbackListener, MTFConfigConsumer<TFBaseConfiguration>
{
} // VoltageCurrentDevice
