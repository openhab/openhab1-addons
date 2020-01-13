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
package org.openhab.action.openwebif.internal.impl.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openhab.action.openwebif.internal.impl.model.adapter.BooleanTypeAdapter;

/**
 * Object that represents a powerstate result.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
@XmlRootElement(name = "e2powerstate")
@XmlAccessorType(XmlAccessType.FIELD)
public class PowerState {

    @XmlElement(name = "e2instandby")
    @XmlJavaTypeAdapter(value = BooleanTypeAdapter.class)
    private Boolean standby;

    /**
     * Returns true, if the receiver is in standby.
     */
    public boolean isStandby() {
        return standby != null && standby == true;
    }
}
