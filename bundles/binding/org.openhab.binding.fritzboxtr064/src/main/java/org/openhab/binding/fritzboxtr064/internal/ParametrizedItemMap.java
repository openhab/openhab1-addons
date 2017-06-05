/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

/**
 * {@link ItemMap} for a SOAP service which takes an input parameter.
 *
 * @author Michael Koch <tensberg@gmx.net>
 * @since 1.11.0
 */
public interface ParametrizedItemMap extends ItemMap {

    /**
     * @return XML element name of the data in parameter of the SOAP service.
     */
    String getReadDataInName();

}
