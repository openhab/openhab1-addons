/**
 * <copyright>
 * </copyright>
 *
 */
package org.openhab.model.sitemap;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.sitemap.List#getSeparator <em>Separator</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getList()
 * @model
 * @generated
 */
public interface List extends Widget
{
  /**
   * Returns the value of the '<em><b>Separator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Separator</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Separator</em>' attribute.
   * @see #setSeparator(String)
   * @see org.openhab.model.sitemap.SitemapPackage#getList_Separator()
   * @model
   * @generated
   */
  String getSeparator();

  /**
   * Sets the value of the '{@link org.openhab.model.sitemap.List#getSeparator <em>Separator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Separator</em>' attribute.
   * @see #getSeparator()
   * @generated
   */
  void setSeparator(String value);

} // List
