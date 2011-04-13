/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.sitemap;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sitemap</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.sitemap.Sitemap#getName <em>Name</em>}</li>
 *   <li>{@link org.openhab.model.sitemap.Sitemap#getLabel <em>Label</em>}</li>
 *   <li>{@link org.openhab.model.sitemap.Sitemap#getIcon <em>Icon</em>}</li>
 *   <li>{@link org.openhab.model.sitemap.Sitemap#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getSitemap()
 * @model
 * @generated
 */
public interface Sitemap extends SitemapModel
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.openhab.model.sitemap.SitemapPackage#getSitemap_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.openhab.model.sitemap.Sitemap#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

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
   * @see org.openhab.model.sitemap.SitemapPackage#getSitemap_Label()
   * @model
   * @generated
   */
  String getLabel();

  /**
   * Sets the value of the '{@link org.openhab.model.sitemap.Sitemap#getLabel <em>Label</em>}' attribute.
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
   * @see org.openhab.model.sitemap.SitemapPackage#getSitemap_Icon()
   * @model
   * @generated
   */
  String getIcon();

  /**
   * Sets the value of the '{@link org.openhab.model.sitemap.Sitemap#getIcon <em>Icon</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Icon</em>' attribute.
   * @see #getIcon()
   * @generated
   */
  void setIcon(String value);

  /**
   * Returns the value of the '<em><b>Children</b></em>' containment reference list.
   * The list contents are of type {@link org.openhab.model.sitemap.Widget}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Children</em>' containment reference list.
   * @see org.openhab.model.sitemap.SitemapPackage#getSitemap_Children()
   * @model containment="true"
   * @generated
   */
  EList<Widget> getChildren();

} // Sitemap
