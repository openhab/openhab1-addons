/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.OnOffValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dual Button Button V2</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.DualButtonButtonV2#getDeviceType <em>Device Type</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.DualButtonButtonV2#getPosition <em>Position</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDualButtonButtonV2()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.DualButtonDeviceV2
 *        org.openhab.binding.tinkerforge.internal.model.MSensor&lt;org.openhab.binding.tinkerforge.internal.model.SwitchState&gt;
 *        org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer&lt;org.openhab.binding.tinkerforge.internal.model.ButtonConfiguration&gt;"
 * @generated
 */
public interface DualButtonButtonV2
        extends DualButtonDeviceV2, MSensor<OnOffValue>, MTFConfigConsumer<ButtonConfiguration> {
    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"dualbutton_button"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see #setDeviceType(String)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDualButtonButtonV2_DeviceType()
     * @model default="dualbutton_button" unique="false"
     * @generated
     */
    String getDeviceType();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DualButtonButtonV2#getDeviceType
     * <em>Device Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Device Type</em>' attribute.
     * @see #getDeviceType()
     * @generated
     */
    void setDeviceType(String value);

    /**
     * Returns the value of the '<em><b>Position</b></em>' attribute.
     * The literals are from the enumeration
     * {@link org.openhab.binding.tinkerforge.internal.model.DualButtonDevicePosition}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Position</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Position</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.DualButtonDevicePosition
     * @see #setPosition(DualButtonDevicePosition)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDualButtonButtonV2_Position()
     * @model unique="false"
     * @generated
     */
    DualButtonDevicePosition getPosition();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DualButtonButtonV2#getPosition
     * <em>Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Position</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.DualButtonDevicePosition
     * @see #getPosition()
     * @generated
     */
    void setPosition(DualButtonDevicePosition value);

} // DualButtonButtonV2
