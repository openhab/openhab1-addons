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
 * A representation of the model object '<em><b>Image</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.openhab.model.sitemap.Image#getUrl <em>Url</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Image#getRefresh <em>Refresh</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Image#getIconColor <em>Icon Color</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getImage()
 * @model
 * @author Kai Kreuzer - Initial contribution
 */
public interface Image extends LinkableWidget {
    /**
     * Returns the value of the '<em><b>Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Url</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Url</em>' attribute.
     * @see #setUrl(String)
     * @see org.openhab.model.sitemap.SitemapPackage#getImage_Url()
     * @model
     */
    String getUrl();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Image#getUrl <em>Url</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Url</em>' attribute.
     * @see #getUrl()
     */
    void setUrl(String value);

    /**
     * Returns the value of the '<em><b>Refresh</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Refresh</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Refresh</em>' attribute.
     * @see #setRefresh(int)
     * @see org.openhab.model.sitemap.SitemapPackage#getImage_Refresh()
     * @model
     */
    int getRefresh();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Image#getRefresh <em>Refresh</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Refresh</em>' attribute.
     * @see #getRefresh()
     */
    void setRefresh(int value);

    /**
     * Returns the value of the '<em><b>Icon Color</b></em>' containment reference list.
     * The list contents are of type {@link org.openhab.model.sitemap.ColorArray}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Icon Color</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Icon Color</em>' containment reference list.
     * @see org.openhab.model.sitemap.SitemapPackage#getImage_IconColor()
     * @model containment="true"
     */
    EList<ColorArray> getIconColor();

} // Image
