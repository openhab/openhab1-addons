/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.weather.internal.model.common;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple class with the JAXB mapping for a list of common id mappings.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
@XmlRootElement(name = "common-id-mappings")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonIdList {

    @XmlElement(name = "common-id-mapping")
    private List<CommonId> commonIds = new ArrayList<CommonId>();

    public List<CommonId> getCommonIds() {
        return commonIds;
    }
}
