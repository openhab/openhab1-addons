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
 * A representation of the model object '<em><b>Linkable Widget</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.openhab.model.sitemap.LinkableWidget#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getLinkableWidget()
 * @model
 * @author Kai Kreuzer - Initial contribution
 */
public interface LinkableWidget extends Widget {
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
     * @see org.openhab.model.sitemap.SitemapPackage#getLinkableWidget_Children()
     * @model containment="true"
     */
    EList<Widget> getChildren();

} // LinkableWidget
