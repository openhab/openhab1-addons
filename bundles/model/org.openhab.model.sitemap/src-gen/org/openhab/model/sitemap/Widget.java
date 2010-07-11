/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.sitemap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Widget</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.sitemap.Widget#getLabel <em>Label</em>}</li>
 *   <li>{@link org.openhab.model.sitemap.Widget#getIcon <em>Icon</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getWidget()
 * @model
 * @generated
 */
public interface Widget extends EObject
{
  /**
   * Returns the value of the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Label</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Label</em>' attribute.
   * @see #setLabel(String)
   * @see org.openhab.model.sitemap.SitemapPackage#getWidget_Label()
   * @model
   * @generated
   */
  String getLabel();

  /**
   * Sets the value of the '{@link org.openhab.model.sitemap.Widget#getLabel <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Label</em>' attribute.
   * @see #getLabel()
   * @generated
   */
  void setLabel(String value);

  /**
   * Returns the value of the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Icon</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Icon</em>' attribute.
   * @see #setIcon(String)
   * @see org.openhab.model.sitemap.SitemapPackage#getWidget_Icon()
   * @model
   * @generated
   */
  String getIcon();

  /**
   * Sets the value of the '{@link org.openhab.model.sitemap.Widget#getIcon <em>Icon</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Icon</em>' attribute.
   * @see #getIcon()
   * @generated
   */
  void setIcon(String value);

} // Widget
