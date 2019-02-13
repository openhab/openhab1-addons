/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.StringValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MNFC Text</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MNFCText#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMNFCText()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MNFCSubDevice
 *        org.openhab.binding.tinkerforge.internal.model.MSensor&lt;org.openhab.binding.tinkerforge.internal.model.StringValue&gt;
 *        org.openhab.binding.tinkerforge.internal.model.MNFCNDEFRecordListener"
 * @generated
 */
public interface MNFCText extends MNFCSubDevice, MSensor<StringValue>, MNFCNDEFRecordListener {

    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"text"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMNFCText_DeviceType()
     * @model default="text" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();
} // MNFCText
