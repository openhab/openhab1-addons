/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.sitemap;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Linkable Widget</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.sitemap.LinkableWidget#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getLinkableWidget()
 * @model
 * @generated
 */
public interface LinkableWidget extends Widget
{
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
   * @see org.openhab.model.sitemap.SitemapPackage#getLinkableWidget_Children()
   * @model containment="true"
   * @generated
   */
  EList<Widget> getChildren();

} // LinkableWidget
