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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sitemap</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.openhab.model.sitemap.Sitemap#getName <em>Name</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Sitemap#getLabel <em>Label</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Sitemap#getIcon <em>Icon</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Sitemap#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getSitemap()
 * @model
 * @author Kai Kreuzer - Initial contribution
 */
public interface Sitemap extends SitemapModel {
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.openhab.model.sitemap.SitemapPackage#getSitemap_Name()
     * @model
     */
    String getName();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Sitemap#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
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
     *
     * @return the value of the '<em>Label</em>' attribute.
     * @see #setLabel(String)
     * @see org.openhab.model.sitemap.SitemapPackage#getSitemap_Label()
     * @model
     */
    String getLabel();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Sitemap#getLabel <em>Label</em>}' attribute.
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
     * @see org.openhab.model.sitemap.SitemapPackage#getSitemap_Icon()
     * @model
     */
    String getIcon();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Sitemap#getIcon <em>Icon</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Icon</em>' attribute.
     * @see #getIcon()
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
     *
     * @return the value of the '<em>Children</em>' containment reference list.
     * @see org.openhab.model.sitemap.SitemapPackage#getSitemap_Children()
     * @model containment="true"
     */
    EList<Widget> getChildren();

} // Sitemap
