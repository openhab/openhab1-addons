/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.sitemap;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.sitemap.Group#getItem <em>Item</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getGroup()
 * @model
 * @generated
 */
public interface Group extends Widget
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
   * @see org.openhab.model.sitemap.SitemapPackage#getGroup_Item()
   * @model
   * @generated
   */
  String getItem();

  /**
   * Sets the value of the '{@link org.openhab.model.sitemap.Group#getItem <em>Item</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Item</em>' attribute.
   * @see #getItem()
   * @generated
   */
  void setItem(String value);

} // Group
