/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletNFC;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet NFC</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletNFC()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice&lt;org.openhab.binding.tinkerforge.internal.model.TinkerBrickletNFC&gt;
 *        org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder&lt;org.openhab.binding.tinkerforge.internal.model.MNFCSubDevice&gt;"
 * @generated
 */
public interface MBrickletNFC extends MDevice<BrickletNFC>, MSubDeviceHolder<MNFCSubDevice> {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model listenerUnique="false"
     * @generated
     */
    void addNDEFRecordListener(MNFCNDEFRecordListener listener);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model listenerUnique="false"
     * @generated
     */
    void removeNDEFRecordListener(MNFCNDEFRecordListener listener);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model listenerUnique="false"
     * @generated
     */
    void addTagInfoListener(MNFCTagInfoListener listener);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model listenerUnique="false"
     * @generated
     */
    void removeTagInfoListener(MNFCTagInfoListener listener);
} // MBrickletNFC
