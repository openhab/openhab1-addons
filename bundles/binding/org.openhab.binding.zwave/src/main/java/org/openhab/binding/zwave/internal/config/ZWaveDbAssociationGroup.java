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
package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This implements the configuration group for the XML product database.
 *
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class ZWaveDbAssociationGroup {
    public Integer Index;
    public Integer Maximum;
    public boolean SetToController;
    @XStreamImplicit
    public List<ZWaveDbLabel> Label;
    @XStreamImplicit
    public List<ZWaveDbLabel> Help;

    ZWaveDbAssociationGroup() {
        SetToController = false;
    }
}
