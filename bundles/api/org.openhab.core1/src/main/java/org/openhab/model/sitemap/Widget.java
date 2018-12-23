/**
 * Copyright (c) 2015-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 */
package org.openhab.model.sitemap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Widget</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.openhab.model.sitemap.Widget#getItem <em>Item</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Widget#getLabel <em>Label</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Widget#getIcon <em>Icon</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Widget#getLabelColor <em>Label Color</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Widget#getValueColor <em>Value Color</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Widget#getVisibility <em>Visibility</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getWidget()
 * @model
 * @author Kai Kreuzer - Initial contribution
 */
public interface Widget extends EObject {
    /**
     * Returns the value of the '<em><b>Item</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Item</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Item</em>' attribute.
     * @see #setItem(String)
     * @see org.openhab.model.sitemap.SitemapPackage#getWidget_Item()
     * @model
     */
    String getItem();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Widget#getItem <em>Item</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Item</em>' attribute.
     * @see #getItem()
     */
    void setItem(String value);

    /**
     * Returns the value of the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Label</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Label</em>' attribute.
     * @see #setLabel(String)
     * @see org.openhab.model.sitemap.SitemapPackage#getWidget_Label()
     * @model
     */
    String getLabel();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Widget#getLabel <em>Label</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Label</em>' attribute.
     * @see #getLabel()
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
     *
     * @return the value of the '<em>Icon</em>' attribute.
     * @see #setIcon(String)
     * @see org.openhab.model.sitemap.SitemapPackage#getWidget_Icon()
     * @model
     */
    String getIcon();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Widget#getIcon <em>Icon</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Icon</em>' attribute.
     * @see #getIcon()
     */
    void setIcon(String value);

    /**
     * Returns the value of the '<em><b>Label Color</b></em>' containment reference list.
     * The list contents are of type {@link org.openhab.model.sitemap.ColorArray}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Label Color</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Label Color</em>' containment reference list.
     * @see org.openhab.model.sitemap.SitemapPackage#getWidget_LabelColor()
     * @model containment="true"
     */
    EList<ColorArray> getLabelColor();

    /**
     * Returns the value of the '<em><b>Value Color</b></em>' containment reference list.
     * The list contents are of type {@link org.openhab.model.sitemap.ColorArray}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value Color</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Value Color</em>' containment reference list.
     * @see org.openhab.model.sitemap.SitemapPackage#getWidget_ValueColor()
     * @model containment="true"
     */
    EList<ColorArray> getValueColor();

    /**
     * Returns the value of the '<em><b>Visibility</b></em>' containment reference list.
     * The list contents are of type {@link org.openhab.model.sitemap.VisibilityRule}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Visibility</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Visibility</em>' containment reference list.
     * @see org.openhab.model.sitemap.SitemapPackage#getWidget_Visibility()
     * @model containment="true"
     */
    EList<VisibilityRule> getVisibility();

} // Widget
