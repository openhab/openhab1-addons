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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.openhab.model.sitemap.Mapping#getCmd <em>Cmd</em>}</li>
 * <li>{@link org.openhab.model.sitemap.Mapping#getLabel <em>Label</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getMapping()
 * @model
 * @author Kai Kreuzer - Initial contribution
 */
public interface Mapping extends EObject {
    /**
     * Returns the value of the '<em><b>Cmd</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cmd</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Cmd</em>' attribute.
     * @see #setCmd(String)
     * @see org.openhab.model.sitemap.SitemapPackage#getMapping_Cmd()
     * @model
     */
    String getCmd();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Mapping#getCmd <em>Cmd</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Cmd</em>' attribute.
     * @see #getCmd()
     */
    void setCmd(String value);

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
     * @see org.openhab.model.sitemap.SitemapPackage#getMapping_Label()
     * @model
     */
    String getLabel();

    /**
     * Sets the value of the '{@link org.openhab.model.sitemap.Mapping#getLabel <em>Label</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Label</em>' attribute.
     * @see #getLabel()
     */
    void setLabel(String value);

} // Mapping
