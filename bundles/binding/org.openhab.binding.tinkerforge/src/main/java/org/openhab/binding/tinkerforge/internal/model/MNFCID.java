/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.StringValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MNFCID</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MNFCID#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMNFCID()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MNFCSubDevice
 *        org.openhab.binding.tinkerforge.internal.model.MSensor&lt;org.openhab.binding.tinkerforge.internal.model.StringValue&gt;
 *        org.openhab.binding.tinkerforge.internal.model.MNFCTagInfoListener"
 * @generated
 */
public interface MNFCID extends MNFCSubDevice, MSensor<StringValue>, MNFCTagInfoListener {

    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"id"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMNFCID_DeviceType()
     * @model default="id" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();
} // MNFCID
