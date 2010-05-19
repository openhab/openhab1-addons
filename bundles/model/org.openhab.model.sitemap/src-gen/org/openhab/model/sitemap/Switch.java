/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.sitemap;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Switch</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.sitemap.Switch#getItem <em>Item</em>}</li>
 *   <li>{@link org.openhab.model.sitemap.Switch#getButtonLabels <em>Button Labels</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getSwitch()
 * @model
 * @generated
 */
public interface Switch extends Widget
{
  /**
   * Returns the value of the '<em><b>Item</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Item</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Item</em>' attribute.
   * @see #setItem(String)
   * @see org.openhab.model.sitemap.SitemapPackage#getSwitch_Item()
   * @model
   * @generated
   */
  String getItem();

  /**
   * Sets the value of the '{@link org.openhab.model.sitemap.Switch#getItem <em>Item</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Item</em>' attribute.
   * @see #getItem()
   * @generated
   */
  void setItem(String value);

  /**
   * Returns the value of the '<em><b>Button Labels</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Button Labels</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Button Labels</em>' attribute list.
   * @see org.openhab.model.sitemap.SitemapPackage#getSwitch_ButtonLabels()
   * @model unique="false"
   * @generated
   */
  EList<String> getButtonLabels();

} // Switch
