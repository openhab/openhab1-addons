/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;

import org.openhab.binding.tinkerforge.internal.tools.NFCTagInfo;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MNFC Tag Info Listener</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMNFCTagInfoListener()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MNFCTagInfoListener extends EObject {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model tagInfoDataType="org.openhab.binding.tinkerforge.internal.model.NFCTagInfo" tagInfoUnique="false"
     * @generated
     */
    void handleTagInfo(NFCTagInfo tagInfo);

} // MNFCTagInfoListener
